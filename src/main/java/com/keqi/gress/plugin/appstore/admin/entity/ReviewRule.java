package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Review Rule Entity
 * Represents an automated review rule
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_review_rule")
public class ReviewRule {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * Rule name
     */
    @TableField("rule_name")
    private String ruleName;
    
    /**
     * Rule description
     */
    @TableField("description")
    private String description;
    
    // Rule Configuration
    
    /**
     * Rule type (stored as String to avoid ClassNotFoundException during deserialization)
     */
    @TableField("rule_type")
    private String ruleType;
    
    /**
     * Rule conditions (stored as JSON in database)
     */
    @TableField("conditions")
    private String conditions;
    
    /**
     * Rule actions (stored as JSON in database)
     */
    @TableField("actions")
    private String actions;
    
    // Priority and Status
    
    /**
     * Priority (higher number = higher priority)
     */
    @TableField("priority")
    private Integer priority;
    
    /**
     * Is enabled
     */
    @TableField("enabled")
    private Boolean enabled;
    
    // Statistics Information
    
    /**
     * Number of times this rule has matched
     */
    @TableField("match_count")
    private Integer matchCount;
    
    /**
     * Last time this rule matched
     */
    @TableField("last_match_time")
    private LocalDateTime lastMatchTime;
    
    // Creation Information
    
    /**
     * Created by user ID
     */
    @TableField("created_by")
    private String createdBy;
    
    /**
     * Creation time
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
