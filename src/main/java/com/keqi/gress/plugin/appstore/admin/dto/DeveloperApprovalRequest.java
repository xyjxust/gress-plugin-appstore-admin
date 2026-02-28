package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Developer Approval Request
 * Request to approve a developer application
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperApprovalRequest {
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review comment
     */
    private String comment;
}
