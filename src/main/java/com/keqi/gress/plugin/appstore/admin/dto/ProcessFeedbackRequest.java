package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.HandlerAwareRequest;

/**
 * Process Feedback Request
 * Request to process a feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessFeedbackRequest implements HandlerAwareRequest {
    
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
