package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Developer Detail DTO
 * Detailed information about a developer including their plugins
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDetailDTO {
    
    /**
     * Developer ID
     */
    private Long id;
    
    /**
     * User ID
     */
    private String userId;
    
    /**
     * Username
     */
    private String username;
    
    /**
     * Email
     */
    private String email;
    
    /**
     * Display name
     */
    private String displayName;
    
    /**
     * Company
     */
    private String company;
    
    /**
     * Website
     */
    private String website;
    
    /**
     * Bio
     */
    private String bio;
    
    /**
     * Developer status (PENDING/ACTIVE/SUSPENDED)
     */
    private String status;
    
    /**
     * Is verified
     */
    private Boolean verified;
    
    /**
     * Number of plugins
     */
    private Integer pluginCount;
    
    /**
     * Total downloads
     */
    private Integer totalDownloads;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Review comment
     */
    private String reviewComment;
    
    /**
     * Application time
     */
    private LocalDateTime applyTime;
    
    /**
     * Create time
     */
    private LocalDateTime createTime;
    
    /**
     * Update time
     */
    private LocalDateTime updateTime;
    
    /**
     * List of plugins submitted by this developer
     */
    private List<PluginSubmissionDTO> plugins;
}
