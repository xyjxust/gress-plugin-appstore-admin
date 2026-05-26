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
 * Plugin Review History Entity
 * Records the review history of plugin submissions
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_plugin_review_history")
public class PluginReviewHistory extends BaseEntity {
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
    
}
