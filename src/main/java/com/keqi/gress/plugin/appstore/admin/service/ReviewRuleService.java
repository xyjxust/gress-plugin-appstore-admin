package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.ReviewRule;
import com.keqi.gress.plugin.appstore.admin.enums.RuleType;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Review Rule Service
 * Manages CRUD operations for review rules
 */
@Service
public class ReviewRuleService {
    
    private static final Log log = LogFactory.get(ReviewRuleService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private ReviewRuleEngine reviewRuleEngine;
    
    /**
     * Get all review rules
     */
    public List<ReviewRuleDTO> getAllRules() {
        List<ReviewRule> rules = dataSource.lambdaQuery(ReviewRule.class)
            .orderByDesc(ReviewRule::getPriority)
            .orderByAsc(ReviewRule::getId)
            .list();
        
        return rules.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get review rule by ID
     */
    public ReviewRuleDTO getRuleById(Long id) {
        ReviewRule rule = dataSource.lambdaQuery(ReviewRule.class)
            .eq(ReviewRule::getId, id)
            .one();
        
        if (rule == null) {
            throw new RuntimeException("Review rule not found: " + id);
        }
        
        return mapToDTO(rule);
    }
    
    /**
     * Create a new review rule
     */
    public Long createRule(CreateReviewRuleRequest request, String createdBy) {
        log.info("Creating review rule: {}", request.getRuleName());
        
        // Build rule entity for validation
        ReviewRule rule = ReviewRule.builder()
                .ruleName(request.getRuleName())
                .description(request.getDescription())
                .ruleType(request.getRuleType().name())
                .conditions(JSON.toJSONString(request.getConditions()))
                .actions(JSON.toJSONString(request.getActions()))
                .priority(request.getPriority() != null ? request.getPriority() : 0)
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();
        
        // Validate rule
        ValidationResult validation = reviewRuleEngine.validateRule(rule);
        if (!validation.getValid()) {
            throw new RuntimeException("Invalid rule: " + String.join(", ", validation.getErrors()));
        }
        
        // Insert rule
        rule.setMatchCount(0);
        rule.setCreatedBy(createdBy);
        rule.setCreateTime(LocalDateTime.now());
        rule.setUpdateTime(LocalDateTime.now());
        
        dataSource.insert(rule);
        
        Long ruleId = rule.getId();
        
        // Record audit log
        auditLogService.logSuccess(
                "CREATE_REVIEW_RULE",
                "Create Review Rule",
                "REVIEW_RULE",
                ruleId.toString(),
                request.getRuleName(),
                createdBy,
                createdBy,
                null,
                Map.of(
                        "ruleName", request.getRuleName(),
                        "ruleType", request.getRuleType()
                )
        );
        
        log.info("Review rule created with ID: {}", ruleId);
        return ruleId;
    }
    
    /**
     * Update an existing review rule
     */
    public void updateRule(Long id, UpdateReviewRuleRequest request) {
        log.info("Updating review rule: {}", id);
        
        // Check if rule exists
        ReviewRuleDTO existing = getRuleById(id);
        
        // Build updated rule for validation
        ReviewRule rule = ReviewRule.builder()
                .id(id)
                .ruleName(request.getRuleName() != null ? request.getRuleName() : existing.getRuleName())
                .description(request.getDescription())
                .ruleType(request.getRuleType() != null ? request.getRuleType().name() : existing.getRuleType().name())
                .conditions(request.getConditions() != null ? 
                        JSON.toJSONString(request.getConditions()) : 
                        JSON.toJSONString(existing.getConditions()))
                .actions(request.getActions() != null ? 
                        JSON.toJSONString(request.getActions()) : 
                        JSON.toJSONString(existing.getActions()))
                .priority(request.getPriority() != null ? request.getPriority() : existing.getPriority())
                .build();
        
        // Validate rule
        ValidationResult validation = reviewRuleEngine.validateRule(rule);
        if (!validation.getValid()) {
            throw new RuntimeException("Invalid rule: " + String.join(", ", validation.getErrors()));
        }
        
        // Execute update
        dataSource.lambdaUpdate(ReviewRule.class)
            .func(chain -> {
                if (request.getRuleName() != null) {
                    chain.set(ReviewRule::getRuleName, request.getRuleName());
                }
                if (request.getDescription() != null) {
                    chain.set(ReviewRule::getDescription, request.getDescription());
                }
                if (request.getRuleType() != null) {
                    chain.set(ReviewRule::getRuleType, request.getRuleType().name());
                }
                if (request.getConditions() != null) {
                    chain.set(ReviewRule::getConditions, JSON.toJSONString(request.getConditions()));
                }
                if (request.getActions() != null) {
                    chain.set(ReviewRule::getActions, JSON.toJSONString(request.getActions()));
                }
                if (request.getPriority() != null) {
                    chain.set(ReviewRule::getPriority, request.getPriority());
                }
                chain.set(ReviewRule::getUpdateTime, LocalDateTime.now());
            })
            .eq(ReviewRule::getId, id)
            .update();
        
        // Record audit log
        auditLogService.logSuccess(
                "UPDATE_REVIEW_RULE",
                "Update Review Rule",
                "REVIEW_RULE",
                id.toString(),
                existing.getRuleName(),
                "admin", // TODO: Get from security context
                "admin",
                existing,
                request
        );
        
        log.info("Review rule updated: {}", id);
    }
    
    /**
     * Delete a review rule
     */
    public void deleteRule(Long id) {
        log.info("Deleting review rule: {}", id);
        
        // Get rule details before deletion
        ReviewRuleDTO rule = getRuleById(id);
        
        // Delete rule
        dataSource.lambdaUpdate(ReviewRule.class)
            .eq(ReviewRule::getId, id)
            .delete();
        
        // Record audit log
        auditLogService.logSuccess(
                "DELETE_REVIEW_RULE",
                "Delete Review Rule",
                "REVIEW_RULE",
                id.toString(),
                rule.getRuleName(),
                "admin", // TODO: Get from security context
                "admin",
                rule,
                null
        );
        
        log.info("Review rule deleted: {}", id);
    }
    
    /**
     * Enable a review rule
     */
    public void enableRule(Long id) {
        log.info("Enabling review rule: {}", id);
        
        // Get rule details
        ReviewRuleDTO rule = getRuleById(id);
        
        // Enable rule
        dataSource.lambdaUpdate(ReviewRule.class)
            .set(ReviewRule::getEnabled, true)
            .set(ReviewRule::getUpdateTime, LocalDateTime.now())
            .eq(ReviewRule::getId, id)
            .update();
        
        // Record audit log
        auditLogService.logSuccess(
                "ENABLE_REVIEW_RULE",
                "Enable Review Rule",
                "REVIEW_RULE",
                id.toString(),
                rule.getRuleName(),
                "admin", // TODO: Get from security context
                "admin",
                Map.of("enabled", false),
                Map.of("enabled", true)
        );
        
        log.info("Review rule enabled: {}", id);
    }
    
    /**
     * Disable a review rule
     */
    public void disableRule(Long id) {
        log.info("Disabling review rule: {}", id);
        
        // Get rule details
        ReviewRuleDTO rule = getRuleById(id);
        
        // Disable rule
        dataSource.lambdaUpdate(ReviewRule.class)
            .set(ReviewRule::getEnabled, false)
            .set(ReviewRule::getUpdateTime, LocalDateTime.now())
            .eq(ReviewRule::getId, id)
            .update();
        
        // Record audit log
        auditLogService.logSuccess(
                "DISABLE_REVIEW_RULE",
                "Disable Review Rule",
                "REVIEW_RULE",
                id.toString(),
                rule.getRuleName(),
                "admin", // TODO: Get from security context
                "admin",
                Map.of("enabled", true),
                Map.of("enabled", false)
        );
        
        log.info("Review rule disabled: {}", id);
    }
    
    /**
     * Get enabled rules only
     */
    public List<ReviewRuleDTO> getEnabledRules() {
        List<ReviewRule> rules = dataSource.lambdaQuery(ReviewRule.class)
            .eq(ReviewRule::getEnabled, true)
            .orderByDesc(ReviewRule::getPriority)
            .orderByAsc(ReviewRule::getId)
            .list();
        
        return rules.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Map entity to DTO
     */
    private ReviewRuleDTO mapToDTO(ReviewRule entity) {
        Object conditionsObj = null;
        Object actionsObj = null;
        
        try {
            if (entity.getConditions() != null) {
                conditionsObj = JSON.parse(entity.getConditions());
            }
            
            if (entity.getActions() != null) {
                actionsObj = JSON.parse(entity.getActions());
            }
        } catch (Exception e) {
            log.warn("Error parsing JSON for rule {}: {}", entity.getId(), e.getMessage());
        }
        
        return ReviewRuleDTO.builder()
                .id(entity.getId())
                .ruleName(entity.getRuleName())
                .description(entity.getDescription())
                .ruleType(RuleType.valueOf(entity.getRuleType()))
                .conditions(conditionsObj)
                .actions(actionsObj)
                .priority(entity.getPriority())
                .enabled(entity.getEnabled())
                .matchCount(entity.getMatchCount())
                .lastMatchTime(entity.getLastMatchTime())
                .createdBy(entity.getCreatedBy())
                .createTime(entity.getCreateTime())
                .build();
    }
}
