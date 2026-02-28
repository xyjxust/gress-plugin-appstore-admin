package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Plugin Update Request
 * Request to update plugin basic information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginUpdateRequest {
    
    /**
     * Plugin ID (set from path parameter)
     */
    private String pluginId;
    
    /**
     * Plugin name
     */
    private String pluginName;
    
    /**
     * Plugin description
     */
    private String description;
    
    /**
     * Category ID
     */
    private Long categoryId;
    
    /**
     * Category name (for display)
     */
    private String category;
    
    /**
     * Plugin tags
     */
    private List<String> tags;
    
    /**
     * Plugin icon URL
     */
    private String iconUrl;
    
    /**
     * Plugin homepage URL
     */
    private String homepage;
    
    /**
     * Documentation URL
     */
    private String documentationUrl;
    
    /**
     * Support email
     */
    private String supportEmail;
    
    /**
     * License type
     */
    private String license;
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Update reason/notes
     */
    private String updateReason;
}
