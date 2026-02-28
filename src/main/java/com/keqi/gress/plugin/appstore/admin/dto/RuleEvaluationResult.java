package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Rule Evaluation Result
 * Contains the result of evaluating a rule against a submission
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleEvaluationResult {
    
    /**
     * Rule ID
     */
    private Long ruleId;
    
    /**
     * Rule name
     */
    private String ruleName;
    
    /**
     * Whether the rule matched
     */
    private Boolean matched;
    
    /**
     * Evaluation details/reason
     */
    private String reason;
    
    /**
     * Actions to execute if matched
     */
    private List<String> actions;
}
