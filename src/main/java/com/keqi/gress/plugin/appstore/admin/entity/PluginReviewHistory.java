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
 * Plugin Review History Entity
 * Records the review history of plugin submissions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_plugin_review_history")
public class PluginReviewHistory {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * Submission ID
     */
    private Long submissionId;
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Version
     */
    private String version;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Decision (APPROVED/REJECTED)
     */
    private String decision;
    
    /**
     * Review comment
     */
    private String comment;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
}
