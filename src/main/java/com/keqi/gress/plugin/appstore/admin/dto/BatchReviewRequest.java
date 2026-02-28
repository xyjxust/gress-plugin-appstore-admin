package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Batch Review Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchReviewRequest {
    
    /**
     * List of submission IDs to review
     */
    private List<Long> submissionIds;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review decision (APPROVE or REJECT)
     */
    private String decision;
    
    /**
     * Review comment/reason
     */
    private String comment;
}
