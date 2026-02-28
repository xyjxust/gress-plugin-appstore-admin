package com.keqi.gress.plugin.appstore.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plugin Statistics Entity
 * Stores daily statistics for each plugin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_plugin_statistics")
public class PluginStatistics {
    
    /**
     * Primary key
     */
    private Long id;
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Statistics date
     */
    private LocalDate statDate;
    
    // Installation Statistics
    
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
    
    // User Statistics
    
    /**
     * Active users
     */
    private Integer activeUsers;
    
    /**
     * New users
     */
    private Integer newUsers;
    
    // Rating Statistics
    
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
    
    // Feedback Statistics
    
    /**
     * Feedback count
     */
    private Integer feedbackCount;
    
    /**
     * Report count
     */
    private Integer reportCount;
    
    // Timestamps
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
}
