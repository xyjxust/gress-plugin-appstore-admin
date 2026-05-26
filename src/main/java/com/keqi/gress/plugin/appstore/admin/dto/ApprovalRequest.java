package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.ReviewerAwareRequest;

/**
 * Plugin Approval Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest implements ReviewerAwareRequest {
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review comment (optional)
     */
    private String comment;
}
