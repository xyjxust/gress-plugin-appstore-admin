package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plugin Rejection Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectionRequest {
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Rejection reason (required)
     */
    private String reason;
}
