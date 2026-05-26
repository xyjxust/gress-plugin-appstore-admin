package com.keqi.gress.plugin.appstore.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * Plugin Statistics Entity
 * Stores daily statistics for each plugin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_plugin_statistics")
public class PluginStatistics extends BaseEntity {
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
    
}
