package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.database.mapping.FastJsonListTypeHandler;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Feedback Entity
 * Represents user feedback or report
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_feedback")
public class Feedback extends BaseEntity {
    /**
     * Plugin ID
     */
    @TableField("plugin_id")
    private String pluginId;
    
    /**
     * Plugin version
     */
    @TableField("plugin_version")
    private String pluginVersion;
    
    // Feedback Information
    
    /**
     * Feedback type (stored as String to avoid ClassNotFoundException during deserialization)
     */
    @TableField("feedback_type")
    private String feedbackType;
    
    /**
     * Title
     */
    @TableField("title")
    private String title;
    
    /**
     * Content
     */
    @TableField("content")
    private String content;
    
    /**
     * Severity level (stored as String to avoid ClassNotFoundException during deserialization)
     */
    @TableField("severity")
    private String severity;
    
    // User Information
    
    /**
     * User ID
     */
    @TableField("user_id")
    private String userId;
    
    /**
     * Username
     */
    @TableField("username")
    private String username;
    
    /**
     * Contact information
     */
    @TableField("contact")
    private String contact;
    
    // Processing Information
    
    /**
     * Feedback status (stored as String to avoid ClassNotFoundException during deserialization)
     */
    @TableField("status")
    private String status;
    
    /**
     * Handler ID
     */
    @TableField("handler_id")
    private String handlerId;
    
    /**
     * Handler name
     */
    @TableField("handler_name")
    private String handlerName;
    
    /**
     * Handle time
     */
    @TableField("handle_time")
    private LocalDateTime handleTime;
    
    /**
     * Handle comment
     */
    @TableField("handle_comment")
    private String handleComment;
    
    /**
     * Attachments (stored as JSON array in database)
     */
    @TableField(value = "attachments",typeHandler = FastJsonListTypeHandler.class)
    private List<String> attachments;
    
    // Timestamps
    
    /**
     * Submission time
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;
    
}
