package com.keqi.gress.plugin.appstore.admin.service;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件依赖验证服务
 * 
 * 负责验证插件的依赖是否已上架
 * 
 * @author Gress Team
 * @since 1.0.0
 */
@Service
public class PluginDependencyValidationService {
    
    private static final Log log = LogFactory.get(PluginDependencyValidationService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 依赖检查模式
     */
    public enum CheckMode {
        STRICT,  // 严格模式：依赖缺失时拒绝上架
        WARN,    // 警告模式：依赖缺失时允许上架但记录警告
        NONE     // 不检查
    }
    
    /**
     * 检查直接依赖是否已上架
     * 
     * @param dependencies 依赖列表（JSON字符串）
     * @param checkMode 检查模式
     * @return 验证结果
     */
    public DependencyValidationResult validateDirectDependencies(String dependencies, CheckMode checkMode) {
        DependencyValidationResult result = new DependencyValidationResult();
        
        if (checkMode == CheckMode.NONE) {
            result.setValid(true);
            return result;
        }
        
        if (dependencies == null || dependencies.trim().isEmpty()) {
            result.setValid(true);
            return result;
        }
        
        try {
            // 解析依赖信息
            List<DependencyInfo> dependencyList = parseDependencies(dependencies);
            
            if (dependencyList.isEmpty()) {
                result.setValid(true);
                return result;
            }
            
            // 检查每个直接依赖
            List<String> missingDependencies = new ArrayList<>();
            List<String> warnings = new ArrayList<>();
            
            for (DependencyInfo dep : dependencyList) {
                boolean exists = checkDependencyExists(dep);
                
                if (!exists) {
                    String message = String.format("依赖 %s@%s 尚未上架", 
                        dep.getPluginId(), 
                        dep.getVersion() != null ? dep.getVersion() : "latest");
                    
                    if (checkMode == CheckMode.STRICT) {
                        missingDependencies.add(message);
                    } else {
                        warnings.add(message);
                    }
                } else {
                    // 检查版本是否匹配（如果有版本要求）
                    if (dep.getVersion() != null && !dep.getVersion().isEmpty()) {
                        String warning = checkVersionCompatibility(dep);
                        if (warning != null) {
                            warnings.add(warning);
                        }
                    }
                }
            }
            
            result.setMissingDependencies(missingDependencies);
            result.setWarnings(warnings);
            
            // 严格模式下，如果有缺失依赖，验证失败
            if (checkMode == CheckMode.STRICT && !missingDependencies.isEmpty()) {
                result.setValid(false);
                result.setErrorMessage("依赖检查失败：" + String.join("; ", missingDependencies));
            } else {
                result.setValid(true);
                if (!warnings.isEmpty()) {
                    result.setWarningMessage("依赖警告：" + String.join("; ", warnings));
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("验证依赖失败", e);
            if (checkMode == CheckMode.STRICT) {
                result.setValid(false);
                result.setErrorMessage("依赖验证失败：" + e.getMessage());
            } else {
                result.setValid(true);
                result.setWarningMessage("依赖验证异常：" + e.getMessage());
            }
            return result;
        }
    }
    
    /**
     * 检查依赖是否存在且已上架
     */
    private boolean checkDependencyExists(DependencyInfo dependency) {
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, dependency.getPluginId())
            .eq(PluginManager::getStatus, "ONLINE") // 必须是已上架状态
            .one();
        
        return plugin != null;
    }
    
    /**
     * 检查版本兼容性
     */
    private String checkVersionCompatibility(DependencyInfo dependency) {
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, dependency.getPluginId())
            .eq(PluginManager::getStatus, "ONLINE")
            .one();
        
        if (plugin == null) {
            return null;
        }
        
        String currentVersion = plugin.getCurrentVersion();
        if (currentVersion == null || currentVersion.isEmpty()) {
            return String.format("依赖 %s 的版本信息缺失", dependency.getPluginId());
        }
        
        // 如果指定了精确版本，检查版本是否匹配
        if (dependency.getVersion() != null && !dependency.getVersion().isEmpty()) {
            if (!currentVersion.equals(dependency.getVersion())) {
                return String.format("依赖 %s 的版本不匹配：需要 %s，当前为 %s", 
                    dependency.getPluginId(), dependency.getVersion(), currentVersion);
            }
        }
        
        // 如果指定了版本范围，检查版本是否在范围内
        // TODO: 实现版本范围匹配（>=, <=, >, <, ~, ^ 等）
        // 目前只记录警告，不阻止上架
        if (dependency.getVersionRange() != null && !dependency.getVersionRange().isEmpty()) {
            // 简单的版本范围检查（未来可以扩展为完整的语义化版本范围匹配）
            if (dependency.getVersionRange().startsWith(">=")) {
                String minVersion = dependency.getVersionRange().substring(2).trim();
                // 使用版本比较工具检查
                try {
                    if (!isVersionGreaterOrEqual(currentVersion, minVersion)) {
                        return String.format("依赖 %s 的版本不满足要求：需要 %s，当前为 %s", 
                            dependency.getPluginId(), dependency.getVersionRange(), currentVersion);
                    }
                } catch (Exception e) {
                    log.warn("版本比较失败: {} vs {}", currentVersion, minVersion, e);
                    // 版本比较失败时不阻止，只记录警告
                }
            }
            // 其他版本范围格式（<=, >, <, ~, ^）可以类似处理
        }
        
        return null;
    }
    
    /**
     * 检查版本是否大于等于指定版本
     * 简单的版本比较实现（x.y.z 格式）
     */
    private boolean isVersionGreaterOrEqual(String version1, String version2) {
        try {
            String[] v1Parts = version1.split("\\.");
            String[] v2Parts = version2.split("\\.");
            
            int maxLength = Math.max(v1Parts.length, v2Parts.length);
            for (int i = 0; i < maxLength; i++) {
                int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
                int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
                
                if (v1Part > v2Part) {
                    return true;
                } else if (v1Part < v2Part) {
                    return false;
                }
            }
            return true; // 相等
        } catch (Exception e) {
            log.warn("版本比较失败: {} vs {}", version1, version2, e);
            return false;
        }
    }
    
    /**
     * 解析依赖信息（JSON字符串）
     */
    private List<DependencyInfo> parseDependencies(String dependenciesJson) {
        try {
            if (dependenciesJson == null || dependenciesJson.trim().isEmpty()) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(dependenciesJson, 
                new TypeReference<List<DependencyInfo>>() {});
        } catch (Exception e) {
            log.error("解析依赖信息失败: {}", dependenciesJson, e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 依赖信息
     */
    @Data
    public static class DependencyInfo {
        private String pluginId;
        private String version;
        private Boolean optional;
        private String versionRange;
    }
    
    /**
     * 依赖验证结果
     */
    @Data
    public static class DependencyValidationResult {
        /** 是否有效 */
        private boolean valid;
        
        /** 错误信息 */
        private String errorMessage;
        
        /** 警告信息 */
        private String warningMessage;
        
        /** 缺失的依赖列表 */
        private List<String> missingDependencies = new ArrayList<>();
        
        /** 警告列表 */
        private List<String> warnings = new ArrayList<>();
    }
}










