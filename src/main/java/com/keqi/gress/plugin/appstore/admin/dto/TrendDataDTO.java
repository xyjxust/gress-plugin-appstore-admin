package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Trend Data DTO
 * Provides time-series data for trend analysis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataDTO {
    
    /**
     * Plugin ID (null for overall trends)
     */
    private String pluginId;
    
    /**
     * Start date of the trend period
     */
    private LocalDate startDate;
    
    /**
     * End date of the trend period
     */
    private LocalDate endDate;
    
    /**
     * List of data points
     */
    private List<TrendDataPoint> dataPoints;
    
    /**
     * Individual data point in the trend
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendDataPoint {
        
        /**
         * Date of this data point
         */
        private LocalDate date;
        
        /**
         * Install count on this date
         */
        private Integer installCount;
        
        /**
         * Uninstall count on this date
         */
        private Integer uninstallCount;
        
        /**
         * Active installs on this date
         */
        private Integer activeInstalls;
        
        /**
         * Active users on this date
         */
        private Integer activeUsers;
        
        /**
         * New users on this date
         */
        private Integer newUsers;
    }
}
