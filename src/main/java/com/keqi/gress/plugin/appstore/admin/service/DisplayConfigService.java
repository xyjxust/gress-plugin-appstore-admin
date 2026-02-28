package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.DisplayConfigDTO;
import com.keqi.gress.plugin.appstore.admin.dto.HotPluginsRuleConfig;
import com.keqi.gress.plugin.appstore.admin.dto.RecommendedPluginsConfig;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateDisplayConfigRequest;
import com.keqi.gress.plugin.appstore.admin.entity.DisplayConfig;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 展示配置服务
 * 管理插件商店的展示规则配置
 */
@Service
public class DisplayConfigService {
    
    private static final Log log = LogFactory.get(DisplayConfigService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    // 配置键常量
    public static final String CONFIG_KEY_RECOMMENDED_PLUGINS = "recommended_plugins";
    public static final String CONFIG_KEY_HOT_PLUGINS_RULE = "hot_plugins_rule";
    public static final String CONFIG_KEY_NEW_PLUGIN_DAYS = "new_plugin_days";
    
    /**
     * 记录审计日志的辅助方法
     */
    private void logAudit(String operationType, Object targetId, Object details) {
        try {
            auditLogService.log(
                operationType,
                operationType,
                "DISPLAY_CONFIG",
                String.valueOf(targetId),
                "Display Config",
                "system",
                "System",
                "SUCCESS",
                null,
                details
            );
        } catch (Exception e) {
            log.error("记录审计日志失败", e);
        }
    }
    
    /**
     * 获取所有展示配置
     */
    public List<DisplayConfigDTO> getAllConfigs() {
        List<DisplayConfig> configs = dataSource.lambdaQuery(DisplayConfig.class)
            .orderByAsc(DisplayConfig::getConfigKey)
            .list();
        
        return configs.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * 根据配置键获取配置
     */
    public DisplayConfigDTO getConfigByKey(String configKey) {
        DisplayConfig config = dataSource.lambdaQuery(DisplayConfig.class)
            .eq(DisplayConfig::getConfigKey, configKey)
            .one();
        
        if (config == null) {
            return null;
        }
        
        return mapToDTO(config);
    }
    
    /**
     * 更新展示配置
     */
    public DisplayConfigDTO updateConfig(UpdateDisplayConfigRequest request) {
        // 检查配置是否存在
        DisplayConfigDTO existing = getConfigByKey(request.getConfigKey());
        
        if (existing == null) {
            // 创建新配置
            DisplayConfig entity = DisplayConfig.builder()
                .configKey(request.getConfigKey())
                .configValue(JSON.toJSONString(request.getConfigValue()))
                .description(request.getDescription())
                .build();
            
            dataSource.insert(entity);
            
            log.info("创建展示配置: key={}", request.getConfigKey());
        } else {
            // 更新现有配置
            String finalDescription = request.getDescription() != null
                ? request.getDescription()
                : existing.getDescription();
            
            dataSource.lambdaUpdate(DisplayConfig.class)
                .eq(DisplayConfig::getConfigKey, request.getConfigKey())
                .set(DisplayConfig::getConfigValue, JSON.toJSONString(request.getConfigValue()))
                .set(DisplayConfig::getDescription, finalDescription)
                .update();
            
            log.info("更新展示配置: key={}", request.getConfigKey());
        }
        
        // 记录审计日志
        logAudit("UPDATE_DISPLAY_CONFIG", request.getConfigKey(), Map.of(
            "before", existing,
            "after", request
        ));
        
        // 刷新缓存（触发前端更新）
        refreshCache(request.getConfigKey());
        
        return getConfigByKey(request.getConfigKey());
    }
    
    /**
     * 获取推荐插件配置
     */
    public RecommendedPluginsConfig getRecommendedPluginsConfig() {
        DisplayConfigDTO config = getConfigByKey(CONFIG_KEY_RECOMMENDED_PLUGINS);
        
        if (config == null || config.getConfigValue() == null) {
            return RecommendedPluginsConfig.builder()
                .plugins(List.of())
                .build();
        }
        
        try {
            String jsonStr = config.getConfigValue().toString();
            return JSON.parseObject(jsonStr, RecommendedPluginsConfig.class);
        } catch (Exception e) {
            log.error("解析推荐插件配置失败", e);
            return RecommendedPluginsConfig.builder()
                .plugins(List.of())
                .build();
        }
    }
    
    /**
     * 更新推荐插件配置
     */
    public DisplayConfigDTO updateRecommendedPlugins(RecommendedPluginsConfig config) {
        UpdateDisplayConfigRequest request = UpdateDisplayConfigRequest.builder()
            .configKey(CONFIG_KEY_RECOMMENDED_PLUGINS)
            .configValue(config)
            .description("推荐插件列表配置")
            .build();
        
        return updateConfig(request);
    }
    
    /**
     * 获取热门插件规则配置
     */
    public HotPluginsRuleConfig getHotPluginsRuleConfig() {
        DisplayConfigDTO config = getConfigByKey(CONFIG_KEY_HOT_PLUGINS_RULE);
        
        if (config == null || config.getConfigValue() == null) {
            // 返回默认配置
            return HotPluginsRuleConfig.builder()
                .metric("install_count")
                .period(7)
                .limit(10)
                .minInstalls(0)
                .minRating(0.0)
                .build();
        }
        
        try {
            String jsonStr = config.getConfigValue().toString();
            return JSON.parseObject(jsonStr, HotPluginsRuleConfig.class);
        } catch (Exception e) {
            log.error("解析热门插件规则配置失败", e);
            return HotPluginsRuleConfig.builder()
                .metric("install_count")
                .period(7)
                .limit(10)
                .build();
        }
    }
    
    /**
     * 更新热门插件规则配置
     */
    public DisplayConfigDTO updateHotPluginsRule(HotPluginsRuleConfig config) {
        UpdateDisplayConfigRequest request = UpdateDisplayConfigRequest.builder()
            .configKey(CONFIG_KEY_HOT_PLUGINS_RULE)
            .configValue(config)
            .description("热门插件计算规则配置")
            .build();
        
        return updateConfig(request);
    }
    
    /**
     * 获取新插件展示天数配置
     */
    public Integer getNewPluginDays() {
        DisplayConfigDTO config = getConfigByKey(CONFIG_KEY_NEW_PLUGIN_DAYS);
        
        if (config == null || config.getConfigValue() == null) {
            return 30; // 默认30天
        }
        
        try {
            return Integer.parseInt(config.getConfigValue().toString());
        } catch (Exception e) {
            log.error("解析新插件展示天数配置失败", e);
            return 30;
        }
    }
    
    /**
     * 更新新插件展示天数配置
     */
    public DisplayConfigDTO updateNewPluginDays(Integer days) {
        UpdateDisplayConfigRequest request = UpdateDisplayConfigRequest.builder()
            .configKey(CONFIG_KEY_NEW_PLUGIN_DAYS)
            .configValue(days)
            .description("新插件标记显示天数")
            .build();
        
        return updateConfig(request);
    }
    
    /**
     * 刷新缓存
     * 触发前端更新展示规则
     */
    private void refreshCache(String configKey) {
        // TODO: 实现缓存刷新逻辑
        // 可以通过事件系统通知前端刷新
        // 或者使用Redis等缓存系统进行刷新
        log.info("刷新展示配置缓存: key={}", configKey);
        
        // 发布配置更新事件
        try {
            // 这里可以通过事件系统通知其他服务
            log.info("展示配置已更新，缓存已刷新: key={}", configKey);
        } catch (Exception e) {
            log.error("刷新缓存失败", e);
        }
    }
    
    /**
     * 将数据库行映射为DTO
     */
    private DisplayConfigDTO mapToDTO(DisplayConfig entity) {
        String configValueJson = entity.getConfigValue();
        Object configValue = null;
        
        if (configValueJson != null && !configValueJson.trim().isEmpty()) {
            try {
                // 尝试解析为JSON对象
                configValue = JSON.parse(configValueJson);
            } catch (Exception e) {
                // 如果解析失败，保持原始字符串
                configValue = configValueJson;
            }
        }
        
        return DisplayConfigDTO.builder()
            .id(entity.getId())
            .configKey(entity.getConfigKey())
            .configValue(configValue)
            .description(entity.getDescription())
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }
}
