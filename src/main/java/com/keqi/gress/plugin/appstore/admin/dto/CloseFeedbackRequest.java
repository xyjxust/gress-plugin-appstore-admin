package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.HandlerAwareRequest;

/**
 * Close Feedback Request
 * Request to close a feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseFeedbackRequest implements HandlerAwareRequest {
    
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
