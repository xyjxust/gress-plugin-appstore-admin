package com.keqi.gress.plugin.appstore.admin.dto;

import com.keqi.gress.plugin.appstore.admin.enums.RuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Review Rule DTO
 * Data transfer object for review rules
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRuleDTO {
    
    /**
     * Rule ID
     */
    private Long id;
    
    /**
     * Rule name
     */
    private String ruleName;
    
    /**
     * Rule description
     */
    private String description;
    
    /**
     * Rule type
     */
    private RuleType ruleType;
    
    /**
     * Rule conditions
     */
    private Object conditions;
    
    /**
     * Rule actions
     */
    private Object actions;
    
    /**
     * Priority
     */
    private Integer priority;
    
    /**
     * Is enabled
     */
    private Boolean enabled;
    
    /**
     * Match count
     */
    private Integer matchCount;
    
    /**
     * Last match time
     */
    private LocalDateTime lastMatchTime;
    
    /**
     * Created by
     */
    private String createdBy;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
}
