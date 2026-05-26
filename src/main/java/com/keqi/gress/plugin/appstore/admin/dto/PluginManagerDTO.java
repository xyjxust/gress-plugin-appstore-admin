package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Plugin Manager DTO
 * Represents a listed plugin in the public store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginManagerDTO {
    
    /**
     * Primary key
     */
    private Long id;
    
    /**
     * Plugin ID
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
     * Status (ONLINE/OFFLINE/DELISTED)
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
     * Description
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
     * Price type (free/paid)
     */
    private String priceType;
    
    /**
     * Install count
     */
    private Integer installCount;
    
    /**
     * Rating average
     */
    private Double ratingAverage;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
}
