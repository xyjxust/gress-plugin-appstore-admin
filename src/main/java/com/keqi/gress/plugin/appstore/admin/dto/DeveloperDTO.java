package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Developer DTO
 * Data transfer object for developer information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDTO {
    
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
     * Application time
     */
    private LocalDateTime applyTime;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
}
