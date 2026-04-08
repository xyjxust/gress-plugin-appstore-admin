package com.keqi.gress.plugin.appstore.admin;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.plugin.ApplicationPlugin;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.PluginSpec;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.MappingInterface;

import com.keqi.gress.plugin.appstore.admin.config.AppStoreAdminConfig;
import com.keqi.gress.plugin.appstore.admin.controller.*;
import org.pf4j.Extension;
import org.pf4j.Plugin;

/**
 * 插件商店管理系统插件
 * 
 * 提供完整的插件生命周期管理能力，包括：
 * - 插件提交审核
 * - 插件上架/下架
 * - 版本管理
 * - 统计分析
 * - 开发者管理
 * - 审计日志
 * - 审核规则引擎
 * - 用户反馈管理
 * 
 * 注意：@Service 类的扫描和初始化由 ApplicationServiceScanner 自动完成
 * - Controller 会在插件启动时自动扫描、实例化并调用 @PostConstruct 方法
 * - Service 会在插件启动时自动扫描、实例化并调用 @PostConstruct 方法
 * - EventListener 会在插件启动时自动扫描和注册
 * 
 * @author Gress Team
 * @version 1.0.0
 */
@Extension
@PluginSpec(
        id = "appstore-admin",
        name = "插件商店管理",
        description = "插件商店管理后台，提供插件审核、上架、下架、版本管理等管理功能",
        version = "1.0.0",
        author = "Gress Team",
        tags = {"admin", "management", "plugin-store"},
        icon = "icons/appstore-admin.svg",
        jsPath = "js/appstore-admin-frontend.js",
        inputClass = AppStoreAdminConfig.class
)
@Service
public class AppStoreAdminPlugin extends Plugin implements ApplicationPlugin {
    
    private static final Log log = LogFactory.get(AppStoreAdminPlugin.class);
    
    @Inject
    private MappingInterface mapping;
    
    // 注入所有 Controller
    @Inject
    private AuditLogController auditLogController;
    
    @Inject
    private BatchOperationController batchOperationController;
    
    @Inject
    private CategoryController categoryController;
    
    @Inject
    private DeveloperManagementController developerManagementController;
    
    @Inject
    private DisplayConfigController displayConfigController;
    
    @Inject
    private FeedbackController feedbackController;
    
    @Inject
    private PluginManagementController pluginManagementController;
    
    @Inject
    private PluginStatisticsController pluginStatisticsController;
    
    @Inject
    private PluginSubmissionController pluginSubmissionController;
    
    @Inject
    private PluginVersionController pluginVersionController;
    
    @Inject
    private ReviewRuleController reviewRuleController;
    
    @Inject
    private TagController tagController;

    @Inject
    private AppStoreApiController appStoreApiController;
    
    /**
     * 服务初始化完成后自动调用
     * 注册所有 Controller 的路由
     */
//    @PostConstruct
    public void registerRoutes() {
        log.info("注册 AppStore Admin 插件 API 路由");
        
        try {
            // 使用 registerForPlugin 方法强制使用 plugins/appstore-admin/ 前缀
            String prefix = mapping.registerForPlugin("appstore-admin", auditLogController);
            log.debug("注册 AuditLogController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", batchOperationController);
            log.debug("注册 BatchOperationController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", categoryController);
            log.debug("注册 CategoryController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", developerManagementController);
            log.debug("注册 DeveloperManagementController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", displayConfigController);
            log.debug("注册 DisplayConfigController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", feedbackController);
            log.debug("注册 FeedbackController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", pluginManagementController);
            log.debug("注册 PluginManagementController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", pluginStatisticsController);
            log.debug("注册 PluginStatisticsController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", pluginSubmissionController);
            log.debug("注册 PluginSubmissionController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", pluginVersionController);
            log.debug("注册 PluginVersionController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", reviewRuleController);
            log.debug("注册 ReviewRuleController，前缀: {}", prefix);
            
            prefix = mapping.registerForPlugin("appstore-admin", tagController);
            log.debug("注册 TagController，前缀: {}", prefix);
            mapping.registerForPlugin("appstore-admin",appStoreApiController);
            log.info("AppStore Admin 插件 API 路由注册完成，所有路由前缀: /plugins/appstore-admin");
        } catch (Exception e) {
            log.error("注册 AppStore Admin 插件路由失败", e);
            throw new RuntimeException("注册插件路由失败", e);
        }
    }
    
    /**
     * 插件启动时调用
     * 
     * @Service 类已由 ApplicationServiceScanner 自动扫描和初始化
     * @PostConstruct 方法已自动调用
     * 数据库 Schema 由 Flyway 自动初始化
     */
    @Override
    public void start() {
        log.info("插件商店管理插件启动");
        log.info("插件 ID: appstore-admin");
        log.info("插件版本: 1.0.0");
        log.info("前端资源: js/appstore-admin-frontend.js");
    }
    
    /**
     * 服务销毁前自动调用
     * 注销所有 Controller 的路由
     */
//    @PreDestroy
    public void unregisterRoutes() {
        log.info("注销 AppStore Admin 插件 API 路由");
        
        try {
            // 注销所有 Controller 的路由
            if (auditLogController != null) {
                mapping.unregisterForPlugin("appstore-admin", auditLogController);
                log.debug("注销 AuditLogController 路由");
            }
            
            if (batchOperationController != null) {
                mapping.unregisterForPlugin("appstore-admin", batchOperationController);
                log.debug("注销 BatchOperationController 路由");
            }
            
            if (categoryController != null) {
                mapping.unregisterForPlugin("appstore-admin", categoryController);
                log.debug("注销 CategoryController 路由");
            }
            
            if (developerManagementController != null) {
                mapping.unregisterForPlugin("appstore-admin", developerManagementController);
                log.debug("注销 DeveloperManagementController 路由");
            }
            
            if (displayConfigController != null) {
                mapping.unregisterForPlugin("appstore-admin", displayConfigController);
                log.debug("注销 DisplayConfigController 路由");
            }
            
            if (feedbackController != null) {
                mapping.unregisterForPlugin("appstore-admin", feedbackController);
                log.debug("注销 FeedbackController 路由");
            }
            
            if (pluginManagementController != null) {
                mapping.unregisterForPlugin("appstore-admin", pluginManagementController);
                log.debug("注销 PluginManagementController 路由");
            }
            
            if (pluginStatisticsController != null) {
                mapping.unregisterForPlugin("appstore-admin", pluginStatisticsController);
                log.debug("注销 PluginStatisticsController 路由");
            }
            
            if (pluginSubmissionController != null) {
                mapping.unregisterForPlugin("appstore-admin", pluginSubmissionController);
                log.debug("注销 PluginSubmissionController 路由");
            }
            
            if (pluginVersionController != null) {
                mapping.unregisterForPlugin("appstore-admin", pluginVersionController);
                log.debug("注销 PluginVersionController 路由");
            }
            
            if (reviewRuleController != null) {
                mapping.unregisterForPlugin("appstore-admin", reviewRuleController);
                log.debug("注销 ReviewRuleController 路由");
            }
            
            if (tagController != null) {
                mapping.unregisterForPlugin("appstore-admin", tagController);
                log.debug("注销 TagController 路由");
            }
            
            if (appStoreApiController != null) {
                mapping.unregisterForPlugin("appstore-admin", appStoreApiController);
                log.debug("注销 AppStoreApiController 路由");
            }
            
            log.info("AppStore Admin 插件 API 路由注销完成");
        } catch (Exception e) {
            log.error("注销 AppStore Admin 插件路由失败", e);
            // 不抛出异常，避免影响插件卸载流程
        }
    }
    
    /**
     * 插件停止时调用
     * 
     * @PreDestroy 方法会由 ApplicationServiceScanner 自动调用
     */
    @Override
    public void stop() {
        log.info("插件商店管理插件停止");
    }
}
