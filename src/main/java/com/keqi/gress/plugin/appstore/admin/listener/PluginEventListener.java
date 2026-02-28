package com.keqi.gress.plugin.appstore.admin.listener;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.service.PluginStatisticsService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.context.event.EventListener;

import java.util.Map;

/**
 * Plugin Event Listener
 * Listens to plugin-related events and updates statistics
 */
@Service
public class PluginEventListener {
    
    private static final Log log = LogFactory.get(PluginEventListener.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginStatisticsService pluginStatisticsService;
    
    /**
     * Listen to plugin installation events
     * Updates install count when a plugin is installed
     *
     * @param event Plugin event
     */
    @EventListener
    public void onPluginInstalled(Object event) {
        try {
            // Check if this is a plugin.installed event
            if (event == null) {
                return;
            }
            
            // Extract event type and data
            String eventType = getEventType(event);
            if (!"plugin.installed".equals(eventType)) {
                return;
            }
            
            Map<String, Object> data = getEventData(event);
            if (data == null) {
                return;
            }
            
            String pluginId = (String) data.get("pluginId");
            if (pluginId != null) {
                pluginStatisticsService.updateInstallCount(pluginId);
                log.info("Updated install statistics for plugin: {}", pluginId);
            }
            
        } catch (Exception e) {
            log.error("Failed to handle plugin installed event", e);
        }
    }
    
    /**
     * Listen to plugin uninstallation events
     * Updates uninstall count when a plugin is uninstalled
     *
     * @param event Plugin event
     */
    @EventListener
    public void onPluginUninstalled(Object event) {
        try {
            // Check if this is a plugin.uninstalled event
            if (event == null) {
                return;
            }
            
            // Extract event type and data
            String eventType = getEventType(event);
            if (!"plugin.uninstalled".equals(eventType)) {
                return;
            }
            
            Map<String, Object> data = getEventData(event);
            if (data == null) {
                return;
            }
            
            String pluginId = (String) data.get("pluginId");
            if (pluginId != null) {
                pluginStatisticsService.updateUninstallCount(pluginId);
                log.info("Updated uninstall statistics for plugin: {}", pluginId);
            }
            
        } catch (Exception e) {
            log.error("Failed to handle plugin uninstalled event", e);
        }
    }
    
    /**
     * Listen to workflow execution completed events
     * Tracks plugin usage when workflows are executed
     *
     * @param event Workflow execution event
     */
    @EventListener
    public void onWorkflowExecuted(Object event) {
        try {
            // Check if this is a workflow.execution.completed event
            if (event == null) {
                return;
            }
            
            // Extract event type
            String eventType = getEventType(event);
            if (!"workflow.execution.completed".equals(eventType)) {
                return;
            }
            
            // Extract metadata which contains execution context
            Map<String, Object> metadata = getEventMetadata(event);
            if (metadata == null) {
                return;
            }
            
            // Get the execution context from metadata
            Object contextObj = metadata.get("context");
            if (contextObj == null) {
                return;
            }
            
            // Extract workflow definition from context to find plugin nodes
            Object workflowObj = extractWorkflowFromContext(contextObj);
            if (workflowObj == null) {
                return;
            }
            
            // Extract plugin IDs from workflow nodes
            java.util.Set<String> pluginIds = extractPluginIdsFromWorkflow(workflowObj);
            
            // Update usage statistics for each plugin
            for (String pluginId : pluginIds) {
                updatePluginUsageStatistics(pluginId);
            }
            
            if (!pluginIds.isEmpty()) {
                log.info("Updated usage statistics for {} plugins in workflow execution", pluginIds.size());
            }
            
        } catch (Exception e) {
            log.error("Failed to handle workflow execution event", e);
        }
    }
    
    /**
     * Update plugin usage statistics
     * This tracks active usage of plugins in workflow executions
     *
     * @param pluginId Plugin ID
     */
    private void updatePluginUsageStatistics(String pluginId) {
        try {
            // Update active users count for the plugin
            // This is a simplified implementation - in production you might want to track
            // unique users per day, execution counts, etc.
            log.debug("Tracking usage for plugin: {}", pluginId);
            
            // For now, we just log the usage. In a full implementation, you would:
            // 1. Update a plugin_usage table with execution counts
            // 2. Track unique users per plugin per day
            // 3. Update active_users count in statistics table
            
        } catch (Exception e) {
            log.error("Failed to update usage statistics for plugin: {}", pluginId, e);
        }
    }
    
    /**
     * Extract workflow definition from execution context
     */
    private Object extractWorkflowFromContext(Object contextObj) {
        try {
            java.lang.reflect.Method getWorkflowMethod = contextObj.getClass().getMethod("getWorkflow");
            return getWorkflowMethod.invoke(contextObj);
        } catch (Exception e) {
            log.debug("Failed to extract workflow from context", e);
            return null;
        }
    }
    
    /**
     * Extract plugin IDs from workflow definition
     * Looks for nodes with type "plugin" and extracts their plugin IDs
     */
    @SuppressWarnings("unchecked")
    private java.util.Set<String> extractPluginIdsFromWorkflow(Object workflowObj) {
        java.util.Set<String> pluginIds = new java.util.HashSet<>();
        
        try {
            // Get nodes from workflow
            java.lang.reflect.Method getNodesMethod = workflowObj.getClass().getMethod("getNodes");
            Object nodesObj = getNodesMethod.invoke(workflowObj);
            
            if (nodesObj instanceof java.util.List) {
                java.util.List<?> nodes = (java.util.List<?>) nodesObj;
                
                for (Object node : nodes) {
                    // Check if node type is "plugin"
                    try {
                        java.lang.reflect.Method getNodeTypeMethod = node.getClass().getMethod("getNodeType");
                        String nodeType = (String) getNodeTypeMethod.invoke(node);
                        
                        if ("plugin".equals(nodeType)) {
                            // Extract plugin ID from node config
                            java.lang.reflect.Method getConfigMethod = node.getClass().getMethod("getConfig");
                            Object configObj = getConfigMethod.invoke(node);
                            
                            if (configObj instanceof Map) {
                                Map<String, Object> config = (Map<String, Object>) configObj;
                                String pluginId = (String) config.get("pluginId");
                                
                                if (pluginId != null && !pluginId.isEmpty()) {
                                    pluginIds.add(pluginId);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.debug("Failed to extract plugin ID from node", e);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Failed to extract plugin IDs from workflow", e);
        }
        
        return pluginIds;
    }
    
    /**
     * Extract event type from event object using reflection
     */
    private String getEventType(Object event) {
        try {
            java.lang.reflect.Method getTypeMethod = event.getClass().getMethod("getType");
            return (String) getTypeMethod.invoke(event);
        } catch (Exception e) {
            log.debug("Failed to extract event type", e);
            return null;
        }
    }
    
    /**
     * Extract event data from event object using reflection
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getEventData(Object event) {
        try {
            java.lang.reflect.Method getDataMethod = event.getClass().getMethod("getData");
            Object data = getDataMethod.invoke(event);
            if (data instanceof Map) {
                return (Map<String, Object>) data;
            }
        } catch (Exception e) {
            log.debug("Failed to extract event data", e);
        }
        return null;
    }
    
    /**
     * Extract event metadata from event object using reflection
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getEventMetadata(Object event) {
        try {
            java.lang.reflect.Method getMetadataMethod = event.getClass().getMethod("getMetadata");
            Object metadata = getMetadataMethod.invoke(event);
            if (metadata instanceof Map) {
                return (Map<String, Object>) metadata;
            }
        } catch (Exception e) {
            log.debug("Failed to extract event metadata", e);
        }
        return null;
    }
}
