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
 * Developer Entity
 * Represents a plugin developer account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_developer")
public class Developer {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
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
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
}
