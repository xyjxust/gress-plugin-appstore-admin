package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 推荐插件配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedPluginsConfig {
    
    /**
     * 推荐插件列表（按顺序）
     */
    private List<RecommendedPlugin> plugins;
    
    /**
     * 推荐插件项
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedPlugin {
        /**
         * 插件ID
         */
        private String pluginId;
        
        /**
         * 插件名称
         */
        private String pluginName;
        
        /**
         * 显示顺序
         */
        private Integer displayOrder;
        
        /**
         * 推荐原因
         */
        private String reason;
    }
}
