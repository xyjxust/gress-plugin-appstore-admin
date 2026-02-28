package com.keqi.gress.plugin.appstore.admin.dto;

import com.keqi.gress.plugin.appstore.admin.enums.RuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Review Rule Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRuleRequest {
    
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
     * Rule conditions (JSON object)
     */
    private Object conditions;
    
    /**
     * Rule actions (JSON array)
     */
    private Object actions;
    
    /**
     * Priority (higher number = higher priority)
     */
    private Integer priority;
    
    /**
     * Is enabled
     */
    private Boolean enabled;
}
