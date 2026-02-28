package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Close Feedback Request
 * Request to close a feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseFeedbackRequest {
    
    /**
     * Handler ID
     */
    private String handlerId;
    
    /**
     * Handler name
     */
    private String handlerName;
    
    /**
     * Closing comment
     */
    private String comment;
}
