package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Plugin Manager Entity
 * Represents a listed plugin in the app store
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_manager")
public class PluginManager extends BaseEntity {
    /**
     * Plugin ID (unique identifier)
     */
    private String pluginId;
    
    /**
     * Plugin name
     */
    private String pluginName;
    
    /**
     * Plugin type (TASK/TRIGGER/APPLICATION)
     */
    private String pluginType;
    
    /**
     * Current version
     */
    private String currentVersion;
    
    /**
     * Plugin status (ONLINE/OFFLINE/DELISTED)
     */
    private String status;
    
    /**
     * Developer ID
     */
    private String developerId;
    
    /**
     * Developer name
     */
    private String developerName;
    
    /**
     * Plugin description
     */
    private String description;
    
    /**
     * Icon path
     */
    private String icon;
    
    /**
     * Category
     */
    private String category;
    
    /**
     * Install count
     */
    private Integer installCount;
    
    /**
     * Rating average
     */
    private Double ratingAverage;
    
    /**
     * File path
     */
    // private String filePath;
    
    /**
     * Delist reason
     */
    private String delistReason;
    
    /**
     * Delist time
     */
    private LocalDateTime delistTime;
    
    /**
     * Tags (JSON array)
     */
    private String tags;

    /**
     * Price type: free/paid
     */
    @TableField("price_type")
    private String priceType;
}
