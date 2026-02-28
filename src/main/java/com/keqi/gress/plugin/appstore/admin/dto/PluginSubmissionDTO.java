package com.keqi.gress.plugin.appstore.admin.dto;

import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.plugin.appstore.admin.enums.ScanStatus;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Plugin Submission DTO
 * Data transfer object for plugin submission list view
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginSubmissionDTO {
    
    /**
     * Submission ID
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
     * Plugin type
     */
    private PluginType pluginType;
    
    /**
     * Version number
     */
    private String version;
    
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
     * Tags
     */
    private List<String> tags;
    
    /**
     * Category
     */
    private String category;
    
    /**
     * Submission status
     */
    private SubmissionStatus status;
    
    /**
     * Security scan status
     */
    private ScanStatus scanStatus;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Submission time
     */
    private LocalDateTime submitTime;
}
