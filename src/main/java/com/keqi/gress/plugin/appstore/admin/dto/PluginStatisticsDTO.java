package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Plugin Statistics DTO
 * Data transfer object for plugin statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginStatisticsDTO {
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Statistics date
     */
    private LocalDate statDate;
    
    /**
     * Install count
     */
    private Integer installCount;
    
    /**
     * Uninstall count
     */
    private Integer uninstallCount;
    
    /**
     * Active installs
     */
    private Integer activeInstalls;
    
    /**
     * Active users
     */
    private Integer activeUsers;
    
    /**
     * New users
     */
    private Integer newUsers;
    
    /**
     * Average rating
     */
    private BigDecimal ratingAverage;
    
    /**
     * Rating count
     */
    private Integer ratingCount;
    
    /**
     * Review count
     */
    private Integer reviewCount;
    
    /**
     * Feedback count
     */
    private Integer feedbackCount;
    
    /**
     * Report count
     */
    private Integer reportCount;
}
