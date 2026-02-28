package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Process Feedback Request
 * Request to process a feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessFeedbackRequest {
    
    /**
     * Handler ID
     */
    private String handlerId;
    
    /**
     * Handler name
     */
    private String handlerName;
    
    /**
     * Processing comment
     */
    private String comment;
}
