package com.keqi.gress.plugin.appstore.admin.dto;

import com.keqi.gress.plugin.appstore.admin.enums.FeedbackStatus;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackType;
import com.keqi.gress.plugin.appstore.admin.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Feedback DTO
 * Data transfer object for user feedback
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    
    /**
     * Feedback ID
     */
    private Long id;
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Plugin version
     */
    private String pluginVersion;
    
    /**
     * Feedback type
     */
    private FeedbackType feedbackType;
    
    /**
     * Title
     */
    private String title;
    
    /**
     * Content
     */
    private String content;
    
    /**
     * Severity
     */
    private Severity severity;
    
    /**
     * User ID
     */
    private String userId;
    
    /**
     * Username
     */
    private String username;
    
    /**
     * Contact
     */
    private String contact;
    
    /**
     * Feedback status
     */
    private FeedbackStatus status;
    
    /**
     * Handler name
     */
    private String handlerName;
    
    /**
     * Handle time
     */
    private LocalDateTime handleTime;
    
    /**
     * Attachments
     */
    private List<String> attachments;
    
    /**
     * Submission time
     */
    private LocalDateTime submitTime;
}
