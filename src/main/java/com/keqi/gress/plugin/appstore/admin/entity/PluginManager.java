package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Plugin Manager Entity
 * Represents a listed plugin in the app store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_manager")
public class PluginManager {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
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
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
    
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
}
