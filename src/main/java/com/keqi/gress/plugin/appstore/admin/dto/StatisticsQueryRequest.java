package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Statistics Query Request
 * Request parameters for querying statistics data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsQueryRequest {
    
    /**
     * Plugin ID (optional, null for all plugins)
     */
    private String pluginId;
    
    /**
     * Start date for the query range
     */
    private LocalDate startDate;
    
    /**
     * End date for the query range
     */
    private LocalDate endDate;
    
    /**
     * Aggregation type (DAILY, WEEKLY, MONTHLY)
     */
    private String aggregationType;
}
