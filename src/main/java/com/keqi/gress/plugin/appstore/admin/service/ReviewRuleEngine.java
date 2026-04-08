package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.RuleEvaluationResult;
import com.keqi.gress.plugin.appstore.admin.dto.ValidationResult;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import com.keqi.gress.plugin.appstore.admin.entity.ReviewRule;
import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.plugin.appstore.admin.enums.RuleType;
import com.keqi.gress.plugin.appstore.admin.enums.ScanStatus;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Review Rule Engine
 * Evaluates and applies automated review rules to plugin submissions
 */
@Service
@Slf4j
public class ReviewRuleEngine {
    

    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    /**
     * Evaluate a single rule against a submission
     */
    public RuleEvaluationResult evaluate(PluginSubmission submission, ReviewRule rule) {

            // Parse conditions
            JSONObject conditions = JSON.parseObject(rule.getConditions());
            
            // Evaluate conditions
            boolean matched = evaluateConditions(submission, conditions);
            
            // Parse actions
            List<String> actions = new ArrayList<>();
            if (matched && rule.getActions() != null) {
                actions = JSON.parseArray(rule.getActions(), String.class);
            }
            
            String reason = matched ? "All conditions matched" : "Conditions not met";
            
            return RuleEvaluationResult.builder()
                    .ruleId(rule.getId())
                    .ruleName(rule.getRuleName())
                    .matched(matched)
                    .reason(reason)
                    .actions(actions)
                    .build();

    }
    
    /**
     * Evaluate conditions against a submission
     */
    private boolean evaluateConditions(PluginSubmission submission, JSONObject conditions) {
        // Check plugin type condition
        if (conditions.containsKey("pluginType")) {
            String expectedType = conditions.getString("pluginType");
            if (!submission.getPluginType().equals(expectedType)) {
                return false;
            }
        }
        
        // Check developer condition
        if (conditions.containsKey("developerId")) {
            String expectedDeveloperId = conditions.getString("developerId");
            if (!submission.getDeveloperId().equals(expectedDeveloperId)) {
                return false;
            }
        }
        
        // Check file size condition
        if (conditions.containsKey("maxFileSize")) {
            Long maxFileSize = conditions.getLong("maxFileSize");
            if (submission.getFileSize() != null && submission.getFileSize() > maxFileSize) {
                return false;
            }
        }
        
        if (conditions.containsKey("minFileSize")) {
            Long minFileSize = conditions.getLong("minFileSize");
            if (submission.getFileSize() != null && submission.getFileSize() < minFileSize) {
                return false;
            }
        }
        
        // Check scan status condition
        if (conditions.containsKey("scanStatus")) {
            String expectedScanStatus = conditions.getString("scanStatus");
            if (submission.getScanStatus() == null || 
                !submission.getScanStatus().equals(expectedScanStatus)) {
                return false;
            }
        }
        
        // Check category condition
        if (conditions.containsKey("category")) {
            String expectedCategory = conditions.getString("category");
            if (submission.getCategory() == null || 
                !submission.getCategory().equals(expectedCategory)) {
                return false;
            }
        }
        
        // Check tags condition (submission must have all specified tags)
        if (conditions.containsKey("tags")) {
            List<String> requiredTags = conditions.getJSONArray("tags").toJavaList(String.class);
            if (submission.getTags() == null || 
                !submission.getTags().containsAll(requiredTags)) {
                return false;
            }
        }
        
        // All conditions passed
        return true;
    }
    
    /**
     * Execute rule action on a submission
     */
    public void executeAction(PluginSubmission submission, String action, Long ruleId) {

            switch (action.toUpperCase()) {
                case "AUTO_APPROVE":
                    autoApproveSubmission(submission, ruleId);
                    break;
                    
                case "AUTO_REJECT":
                    autoRejectSubmission(submission, ruleId);
                    break;
                    
                case "FLAG_FOR_REVIEW":
                    flagSubmissionForReview(submission, ruleId);
                    break;
                    
                case "REQUIRE_MANUAL_REVIEW":
                    // Just log, no status change
                    log.info("Submission {} flagged for manual review by rule {}", 
                            submission.getId(), ruleId);
                    break;
                    
                default:
                    log.warn("Unknown action: {}", action);
            }
            
            // Record action in audit log
            auditLogService.logSuccess(
                    "RULE_ACTION_EXECUTED",
                    "Execute Rule Action",
                    "PLUGIN_SUBMISSION",
                    submission.getId().toString(),
                    submission.getPluginName(),
                    "system",
                    "Review Rule Engine",
                    null,
                    Map.of(
                            "ruleId", ruleId,
                            "action", action
                    )
            );
            

    }
    
    /**
     * Auto-approve a submission
     */
    private void autoApproveSubmission(PluginSubmission submission, Long ruleId) {
        dataSource.lambdaUpdate(PluginSubmission.class)
            .set(PluginSubmission::getStatus, SubmissionStatus.APPROVED.name())
            .set(PluginSubmission::getReviewComment, "Auto-approved by rule #" + ruleId)
            .set(PluginSubmission::getReviewTime, LocalDateTime.now())
            .eq(PluginSubmission::getId, submission.getId())
            .update();
        
        log.info("Submission {} auto-approved by rule {}", submission.getId(), ruleId);
    }
    
    /**
     * Auto-reject a submission
     */
    private void autoRejectSubmission(PluginSubmission submission, Long ruleId) {
        dataSource.lambdaUpdate(PluginSubmission.class)
            .set(PluginSubmission::getStatus, SubmissionStatus.REJECTED.name())
            .set(PluginSubmission::getReviewComment, "Auto-rejected by rule #" + ruleId)
            .set(PluginSubmission::getReviewTime, LocalDateTime.now())
            .eq(PluginSubmission::getId, submission.getId())
            .update();
        
        log.info("Submission {} auto-rejected by rule {}", submission.getId(), ruleId);
    }
    
    /**
     * Flag a submission for manual review
     */
    private void flagSubmissionForReview(PluginSubmission submission, Long ruleId) {
        // Add a flag in the review comment
        String currentComment = submission.getReviewComment() != null ? 
                submission.getReviewComment() : "";
        String flagComment = currentComment + "\n[FLAGGED by rule #" + ruleId + "]";
        
        dataSource.lambdaUpdate(PluginSubmission.class)
            .set(PluginSubmission::getReviewComment, flagComment)
            .eq(PluginSubmission::getId, submission.getId())
            .update();
        
        log.info("Submission {} flagged for review by rule {}", submission.getId(), ruleId);
    }
    
    /**
     * Get all matching rules for a submission (ordered by priority)
     */
    public List<ReviewRule> getMatchingRules(PluginSubmission submission) {
        // Get all enabled rules ordered by priority (descending)
        List<ReviewRule> allRules = dataSource.lambdaQuery(ReviewRule.class)
            .eq(ReviewRule::getEnabled, true)
            .orderByDesc(ReviewRule::getPriority)
            .orderByAsc(ReviewRule::getId)
            .list();
        
        // Filter to only matching rules
        List<ReviewRule> matchingRules = new ArrayList<>();
        for (ReviewRule rule : allRules) {
            RuleEvaluationResult result = evaluate(submission, rule);
            if (result.getMatched()) {
                matchingRules.add(rule);
            }
        }
        
        return matchingRules;
    }
    
    /**
     * Apply all matching rules to a submission
     */
    public List<RuleEvaluationResult> applyRules(PluginSubmission submission) {
        log.info("Applying rules to submission {}", submission.getId());
        
        List<ReviewRule> matchingRules = getMatchingRules(submission);
        List<RuleEvaluationResult> results = new ArrayList<>();
        
        for (ReviewRule rule : matchingRules) {
            RuleEvaluationResult result = evaluate(submission, rule);
            results.add(result);
            
            if (result.getMatched()) {
                // Update rule match statistics
                updateRuleStatistics(rule.getId());
                
                // Execute actions
                for (String action : result.getActions()) {
                    executeAction(submission, action, rule.getId());
                }
                
                // If rule is AUTO_APPROVE or AUTO_REJECT, stop processing further rules
                if (rule.getRuleType().equals(RuleType.AUTO_APPROVE.name()) ||
                        rule.getRuleType().equals(RuleType.AUTO_REJECT.name())) {
                    log.info("Stopping rule evaluation after {} rule", rule.getRuleType());
                    break;
                }
            }
        }
        
        return results;
    }
    
    /**
     * Update rule match statistics
     */
    private void updateRuleStatistics(Long ruleId) {
        // Get current rule to update match count
        ReviewRule rule = dataSource.lambdaQuery(ReviewRule.class)
            .eq(ReviewRule::getId, ruleId)
            .one();
        
        if (rule != null) {
            // Update using Lambda
            dataSource.lambdaUpdate(ReviewRule.class)
                .eq(ReviewRule::getId, ruleId)
                .set(ReviewRule::getMatchCount, (rule.getMatchCount() != null ? rule.getMatchCount() : 0) + 1)
                .set(ReviewRule::getLastMatchTime, LocalDateTime.now())
                .update();
        }
    }
    
    /**
     * Validate rule syntax and conditions
     */
    public ValidationResult validateRule(ReviewRule rule) {
        List<String> errors = new ArrayList<>();
        
        // Validate rule name
        if (rule.getRuleName() == null || rule.getRuleName().trim().isEmpty()) {
            errors.add("Rule name is required");
        }
        
        // Validate rule type
        if (rule.getRuleType() == null) {
            errors.add("Rule type is required");
        }
        
        // Validate conditions
        if (rule.getConditions() == null || rule.getConditions().trim().isEmpty()) {
            errors.add("Rule conditions are required");
        } else {
            try {
                JSONObject conditions = JSON.parseObject(rule.getConditions());
                
                // Check if it's a nested structure (with logic and conditions array)
                if (conditions.containsKey("logic") && conditions.containsKey("conditions")) {
                    // Validate logic value
                    String logic = conditions.getString("logic");
                    if (!logic.equals("AND") && !logic.equals("OR")) {
                        errors.add("Invalid logic value: " + logic + ", must be AND or OR");
                    }
                    
                    // Validate conditions array
                    com.alibaba.fastjson2.JSONArray conditionsArray = conditions.getJSONArray("conditions");
                    if (conditionsArray == null || conditionsArray.isEmpty()) {
                        errors.add("Conditions array cannot be empty");
                    } else {
                        for (int i = 0; i < conditionsArray.size(); i++) {
                            Object conditionObj = conditionsArray.get(i);
                            if (conditionObj instanceof JSONObject) {
                                JSONObject condition = (JSONObject) conditionObj;
                                String field = condition.getString("field");
                                String operator = condition.getString("operator");
                                
                                if (field == null || field.trim().isEmpty()) {
                                    errors.add("Condition " + (i + 1) + ": field is required");
                                } else if (!isValidConditionField(field)) {
                                    errors.add("Condition " + (i + 1) + ": invalid field: " + field);
                                }
                                
                                if (operator == null || operator.trim().isEmpty()) {
                                    errors.add("Condition " + (i + 1) + ": operator is required");
                                } else if (!isValidConditionOperator(operator)) {
                                    errors.add("Condition " + (i + 1) + ": invalid operator: " + operator);
                                }
                            } else {
                                errors.add("Condition " + (i + 1) + ": must be an object");
                            }
                        }
                    }
                } else {
                    // Legacy format: simple key-value pairs
                    // Validate condition keys
                    for (String key : conditions.keySet()) {
                        if (!isValidConditionKey(key)) {
                            errors.add("Invalid condition key: " + key);
                        }
                    }
                    
                    // Validate plugin type if present
                    if (conditions.containsKey("pluginType")) {
                        String pluginType = conditions.getString("pluginType");
                        try {
                            PluginType.valueOf(pluginType);
                        } catch (IllegalArgumentException e) {
                            errors.add("Invalid plugin type: " + pluginType);
                        }
                    }
                    
                    // Validate scan status if present
                    if (conditions.containsKey("scanStatus")) {
                        String scanStatus = conditions.getString("scanStatus");
                        try {
                            ScanStatus.valueOf(scanStatus);
                        } catch (IllegalArgumentException e) {
                            errors.add("Invalid scan status: " + scanStatus);
                        }
                    }
                }
                
            } catch (Exception e) {
                errors.add("Invalid JSON format for conditions: " + e.getMessage());
            }
        }
        
        // Validate actions
        if (rule.getActions() == null || rule.getActions().trim().isEmpty()) {
            errors.add("Rule actions are required");
        } else {
            try {
                com.alibaba.fastjson2.JSONArray actionsArray = JSON.parseArray(rule.getActions());
                if (actionsArray == null || actionsArray.isEmpty()) {
                    errors.add("Actions array cannot be empty");
                } else {
                    for (int i = 0; i < actionsArray.size(); i++) {
                        Object actionObj = actionsArray.get(i);
                        if (actionObj instanceof String) {
                            // Legacy format: string array
                            String action = (String) actionObj;
                            if (!isValidAction(action)) {
                                errors.add("Invalid action: " + action);
                            }
                        } else if (actionObj instanceof JSONObject) {
                            // New format: object array with type and params
                            JSONObject action = (JSONObject) actionObj;
                            String actionType = action.getString("type");
                            if (actionType == null || actionType.trim().isEmpty()) {
                                errors.add("Action " + (i + 1) + ": type is required");
                            } else {
                                // Map frontend action types to backend action types
                                String mappedAction = mapActionType(actionType);
                                if (mappedAction == null) {
                                    errors.add("Action " + (i + 1) + ": invalid type: " + actionType);
                                }
                            }
                        } else {
                            errors.add("Action " + (i + 1) + ": must be a string or object");
                        }
                    }
                }
            } catch (Exception e) {
                errors.add("Invalid JSON format for actions: " + e.getMessage());
            }
        }
        
        // Validate priority
        if (rule.getPriority() == null || rule.getPriority() < 0) {
            errors.add("Priority must be a non-negative number");
        }
        
        return ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();
    }
    
    /**
     * Check if a condition key is valid (for legacy format)
     */
    private boolean isValidConditionKey(String key) {
        return key.equals("pluginType") ||
               key.equals("developerId") ||
               key.equals("maxFileSize") ||
               key.equals("minFileSize") ||
               key.equals("scanStatus") ||
               key.equals("category") ||
               key.equals("tags");
    }
    
    /**
     * Check if a condition field is valid (for new nested format)
     */
    private boolean isValidConditionField(String field) {
        return field.equals("pluginType") ||
               field.equals("developerId") ||
               field.equals("fileSize") ||
               field.equals("scanStatus") ||
               field.equals("category") ||
               field.equals("tags") ||
               field.equals("pluginName") ||
               field.equals("version");
    }
    
    /**
     * Check if a condition operator is valid
     */
    private boolean isValidConditionOperator(String operator) {
        return operator.equals("equals") ||
               operator.equals("notEquals") ||
               operator.equals("contains") ||
               operator.equals("notContains") ||
               operator.equals("greaterThan") ||
               operator.equals("lessThan") ||
               operator.equals("greaterThanOrEqual") ||
               operator.equals("lessThanOrEqual") ||
               operator.equals("in") ||
               operator.equals("notIn");
    }
    
    /**
     * Check if an action is valid (for legacy string format)
     */
    private boolean isValidAction(String action) {
        return action.equalsIgnoreCase("AUTO_APPROVE") ||
               action.equalsIgnoreCase("AUTO_REJECT") ||
               action.equalsIgnoreCase("FLAG_FOR_REVIEW") ||
               action.equalsIgnoreCase("REQUIRE_MANUAL_REVIEW");
    }
    
    /**
     * Map frontend action type to backend action type
     */
    private String mapActionType(String frontendType) {
        if (frontendType == null) {
            return null;
        }
        switch (frontendType.toLowerCase()) {
            case "approve":
                return "AUTO_APPROVE";
            case "reject":
                return "AUTO_REJECT";
            case "flag":
            case "flag_for_review":
                return "FLAG_FOR_REVIEW";
            case "notify":
            case "require_manual_review":
                return "REQUIRE_MANUAL_REVIEW";
            default:
                return null;
        }
    }
}
