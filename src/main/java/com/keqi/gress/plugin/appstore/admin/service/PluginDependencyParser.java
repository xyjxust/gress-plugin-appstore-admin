package com.keqi.gress.plugin.appstore.admin.service;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 插件依赖解析器
 * 
 * 从 JAR 文件的 plugin.properties 中解析依赖信息（PF4J 标准格式）
 * 
 * @author Gress Team
 * @since 1.0.0
 */
public class PluginDependencyParser {
    
    private static final Log log = LogFactory.get(PluginDependencyParser.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 从 JAR 文件中解析依赖信息
     * 
     * 只支持从 plugin.properties 文件中解析 plugin.dependencies 配置
     * 格式：plugin.dependencies=plugin-id1@versionRange1, plugin-id2@versionRange2
     * 
     * @param jarFilePath JAR 文件路径
     * @return 依赖信息列表（JSON字符串）
     */
    public static String parseDependenciesFromJar(String jarFilePath) {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            // 从 plugin.properties 中解析依赖（PF4J 标准格式）
            String dependencies = parseFromPluginProperties(jarFile);
            if (dependencies != null && !dependencies.isEmpty() && !"[]".equals(dependencies)) {
                return dependencies;
            }
            
            log.debug("未找到依赖信息: {}", jarFilePath);
            return "[]"; // 返回空数组 JSON
            
        } catch (Exception e) {
            log.warn("解析依赖信息失败: {}", jarFilePath, e);
            return "[]"; // 返回空数组 JSON
        }
    }
    
    /**
     * 从 plugin.properties 中解析依赖（PF4J 标准格式）
     * 
     * 格式：plugin.dependencies=plugin-id1@versionRange1, plugin-id2@versionRange2
     * 示例：plugin.dependencies=ai-provider@>=1.0.1, another-plugin@2.0.0
     */
    private static String parseFromPluginProperties(JarFile jarFile) {
        try {
            JarEntry entry = jarFile.getJarEntry("plugin.properties");
            if (entry == null) {
                return null;
            }
            
            try (InputStream is = jarFile.getInputStream(entry)) {
                java.util.Properties props = new java.util.Properties();
                props.load(is);
                
                String dependenciesStr = props.getProperty("plugin.dependencies");
                if (dependenciesStr == null || dependenciesStr.trim().isEmpty()) {
                    return null;
                }
                
                // 解析格式：plugin-id1@versionRange1,plugin-id2@versionRange2
                // 例如：ai-provider@>=1.0.1, another-plugin@2.0.0
                List<DependencyInfo> dependencies = new ArrayList<>();
                String[] deps = dependenciesStr.split(",");
                for (String dep : deps) {
                    String depStr = dep.trim();
                    if (depStr.isEmpty()) {
                        continue;
                    }
                    
                    DependencyInfo info = new DependencyInfo();
                    
                    // 使用 @ 作为分隔符
                    int atIndex = depStr.indexOf('@');
                    if (atIndex > 0) {
                        // 有版本范围：plugin-id@versionRange
                        info.setPluginId(depStr.substring(0, atIndex).trim());
                        String versionRange = depStr.substring(atIndex + 1).trim();
                        info.setVersionRange(versionRange);
                        
                        // 如果版本范围是精确版本（不包含 >=, <=, >, <, ~, ^ 等符号），也设置 version
                        if (!versionRange.matches(".*[><=~^].*")) {
                            info.setVersion(versionRange);
                        }
                    } else {
                        // 无版本范围：plugin-id
                        info.setPluginId(depStr);
                    }
                    
                    info.setOptional(false);
                    dependencies.add(info);
                }
                
                return objectMapper.writeValueAsString(dependencies);
            }
            
        } catch (Exception e) {
            log.debug("从 plugin.properties 解析依赖失败", e);
            return null;
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
}










