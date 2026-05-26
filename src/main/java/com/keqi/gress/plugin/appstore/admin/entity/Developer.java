package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Developer Entity
 * Represents a plugin developer account
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_developer")
public class Developer extends BaseEntity {
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
    
    // Developer Information
    
    /**
     * Display name
     */
    private String displayName;
    
    /**
     * Company or organization
     */
    private String company;
    
    /**
     * Website URL
     */
    private String website;
    
    /**
     * Bio/description
     */
    private String bio;
    
    // Status Information
    
    /**
     * Developer status (PENDING/ACTIVE/SUSPENDED)
     */
    private String status;
    
    /**
     * Is verified
     */
    private Boolean verified;
    
    // Statistics Information
    
    /**
     * Number of plugins
     */
    private Integer pluginCount;
    
    /**
     * Total downloads across all plugins
     */
    private Integer totalDownloads;
    
    // Review Information
    
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
    
    // Timestamps
    
    /**
     * Application time
     */
    private LocalDateTime applyTime;
    
}
