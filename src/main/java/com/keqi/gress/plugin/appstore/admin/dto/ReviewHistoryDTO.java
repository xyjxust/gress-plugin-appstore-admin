package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Review History DTO
 * Data transfer object for review history records
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewHistoryDTO {
    
    /**
     * History record ID
     */
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
     * Review decision (APPROVED/REJECTED)
     */
    private String decision;
    
    /**
     * Review comment
     */
    private String comment;
    
    /**
     * Applied rules
     */
    private Object appliedRules;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
}
