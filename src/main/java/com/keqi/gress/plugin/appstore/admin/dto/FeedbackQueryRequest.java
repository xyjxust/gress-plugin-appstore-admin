package com.keqi.gress.plugin.appstore.admin.dto;


import com.keqi.gress.plugin.appstore.admin.enums.FeedbackStatus;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackType;
import com.keqi.gress.plugin.appstore.admin.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Feedback Query Request
 * Request parameters for querying feedback list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackQueryRequest {
    
    /**
     * Page number (1-based)
     */
    private Integer page;
    
    /**
     * Page size
     */
    private Integer size;
    
    /**
     * Filter by plugin ID
     */
    private String pluginId;
    
    /**
     * Filter by feedback type
     */
    private FeedbackType feedbackType;
    
    /**
     * Filter by feedback status
     */
    private FeedbackStatus status;
    
    /**
     * Filter by severity
     */
    private Severity severity;
    
    /**
     * Search keyword (search in title and content)
     */
    private String keyword;
    
    /**
     * Filter by user ID
     */
    private String userId;
}
