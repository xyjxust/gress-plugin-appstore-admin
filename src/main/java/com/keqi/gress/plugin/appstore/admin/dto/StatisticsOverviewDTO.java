package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Statistics Overview DTO
 * Provides overall statistics summary across all plugins
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsOverviewDTO {
    
    /**
     * Total number of plugins
     */
    private Integer totalPlugins;
    
    /**
     * Total number of active plugins
     */
    private Integer activePlugins;
    
    /**
     * Total number of pending submissions
     */
    private Integer pendingSubmissions;
    
    /**
     * Total install count across all plugins
     */
    private Long totalInstalls;
    
    /**
     * Total active users across all plugins
     */
    private Long totalActiveUsers;
    
    /**
     * Average rating across all plugins
     */
    private BigDecimal averageRating;
    
    /**
     * Total feedback count
     */
    private Integer totalFeedback;
    
    /**
     * Total report count
     */
    private Integer totalReports;
}
