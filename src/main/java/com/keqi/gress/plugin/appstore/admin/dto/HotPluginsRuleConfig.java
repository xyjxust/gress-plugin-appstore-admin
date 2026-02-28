package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门插件规则配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotPluginsRuleConfig {
    
    /**
     * 计算指标（install_count: 安装数, active_users: 活跃用户数, rating: 评分）
     */
    private String metric;
    
    /**
     * 统计周期（天数）
     */
    private Integer period;
    
    /**
     * 显示数量限制
     */
    private Integer limit;
    
    /**
     * 最小安装数阈值
     */
    private Integer minInstalls;
    
    /**
     * 最小评分阈值
     */
    private Double minRating;
}
