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
 * Plugin Submission Detail DTO
 * Data transfer object for detailed plugin submission view
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginSubmissionDetailDTO {
    
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
     * Developer email
     */
    private String developerEmail;
    
    // Plugin Metadata
    
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
    
    // File Information
    
    /**
     * Plugin file path
     */
    private String filePath;
    
    /**
     * File size in bytes
     */
    private Long fileSize;
    
    /**
     * File hash
     */
    private String fileHash;
    
    // Review Information
    
    /**
     * Submission status
     */
    private SubmissionStatus status;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Review comment
     */
    private String reviewComment;
    
    // Security Scan
    
    /**
     * Security scan status
     */
    private ScanStatus scanStatus;
    
    /**
     * Security scan result
     */
    private Object scanResult;
    
    // Timestamps
    
    /**
     * Submission time
     */
    private LocalDateTime submitTime;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
    
    // Additional Information
    
    /**
     * Review history
     */
    private List<ReviewHistoryDTO> reviewHistory;
}
