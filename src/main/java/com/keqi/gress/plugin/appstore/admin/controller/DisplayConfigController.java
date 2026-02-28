package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.DisplayConfigService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示配置控制器
 * 提供展示规则配置的REST API
 */
@Service
@RestController
@RequestMapping("/display-config")
public class DisplayConfigController {
    
    private static final Log log = LogFactory.get(DisplayConfigController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private DisplayConfigService displayConfigService;
    
    /**
     * 获取所有展示配置
     * GET /display-config
     */
    @GetMapping
    public Map<String, Object> getAllConfigs() {
        try {
            List<DisplayConfigDTO> configs = displayConfigService.getAllConfigs();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", configs);
            result.put("total", configs.size());
            
            return result;
            
        } catch (Exception e) {
            log.error("获取展示配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取展示配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 根据配置键获取配置
     * GET /display-config/{configKey}
     */
    @GetMapping("/{configKey}")
    public Map<String, Object> getConfigByKey(@PathVariable String configKey) {
        try {
            DisplayConfigDTO config = displayConfigService.getConfigByKey(configKey);
            
            if (config == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "配置不存在: " + configKey);
                return error;
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", config);
            
            return result;
            
        } catch (Exception e) {
            log.error("获取展示配置失败: configKey={}", configKey, e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取展示配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 更新展示配置
     * PUT /display-config
     */
    @PutMapping
    public Map<String, Object> updateConfig(@RequestBody UpdateDisplayConfigRequest request) {
        try {
            DisplayConfigDTO config = displayConfigService.updateConfig(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", config);
            result.put("message", "展示配置更新成功");
            
            return result;
            
        } catch (Exception e) {
            log.error("更新展示配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "更新展示配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 获取推荐插件配置
     * GET /display-config/recommended-plugins
     */
    @GetMapping("/recommended-plugins")
    public Map<String, Object> getRecommendedPlugins() {
        try {
            RecommendedPluginsConfig config = displayConfigService.getRecommendedPluginsConfig();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", config);
            
            return result;
            
        } catch (Exception e) {
            log.error("获取推荐插件配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取推荐插件配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 更新推荐插件配置
     * PUT /display-config/recommended-plugins
     */
    @PutMapping("/recommended-plugins")
    public Map<String, Object> updateRecommendedPlugins(@RequestBody RecommendedPluginsConfig config) {
        try {
            DisplayConfigDTO result = displayConfigService.updateRecommendedPlugins(config);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "推荐插件配置更新成功");
            
            return response;
            
        } catch (Exception e) {
            log.error("更新推荐插件配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "更新推荐插件配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 获取热门插件规则配置
     * GET /display-config/hot-plugins-rule
     */
    @GetMapping("/hot-plugins-rule")
    public Map<String, Object> getHotPluginsRule() {
        try {
            HotPluginsRuleConfig config = displayConfigService.getHotPluginsRuleConfig();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", config);
            
            return result;
            
        } catch (Exception e) {
            log.error("获取热门插件规则配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取热门插件规则配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 更新热门插件规则配置
     * PUT /display-config/hot-plugins-rule
     */
    @PutMapping("/hot-plugins-rule")
    public Map<String, Object> updateHotPluginsRule(@RequestBody HotPluginsRuleConfig config) {
        try {
            DisplayConfigDTO result = displayConfigService.updateHotPluginsRule(config);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "热门插件规则配置更新成功");
            
            return response;
            
        } catch (Exception e) {
            log.error("更新热门插件规则配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "更新热门插件规则配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 获取新插件展示天数配置
     * GET /display-config/new-plugin-days
     */
    @GetMapping("/new-plugin-days")
    public Map<String, Object> getNewPluginDays() {
        try {
            Integer days = displayConfigService.getNewPluginDays();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", days);
            
            return result;
            
        } catch (Exception e) {
            log.error("获取新插件展示天数配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "获取新插件展示天数配置失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 更新新插件展示天数配置
     * PUT /display-config/new-plugin-days
     */
    @PutMapping("/new-plugin-days")
    public Map<String, Object> updateNewPluginDays(@RequestBody Map<String, Integer> request) {
        try {
            Integer days = request.get("days");
            if (days == null || days <= 0) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "天数必须大于0");
                return error;
            }
            
            DisplayConfigDTO result = displayConfigService.updateNewPluginDays(days);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", result);
            response.put("message", "新插件展示天数配置更新成功");
            
            return response;
            
        } catch (Exception e) {
            log.error("更新新插件展示天数配置失败", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "更新新插件展示天数配置失败: " + e.getMessage());
            return error;
        }
    }
}
