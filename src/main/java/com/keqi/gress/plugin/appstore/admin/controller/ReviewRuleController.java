package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.CreateReviewRuleRequest;
import com.keqi.gress.plugin.appstore.admin.dto.ReviewRuleDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateReviewRuleRequest;
import com.keqi.gress.plugin.appstore.admin.service.ReviewRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Review Rule Controller
 * REST API for managing review rules
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/review-rules")
public class ReviewRuleController {

    private final static Log log = LogFactory.get(ReviewRuleController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private ReviewRuleService reviewRuleService;
    
    /**
     * Get all review rules
     * GET /review-rules
     */
    @GetMapping
    public Result<List<ReviewRuleDTO>> getAllRules() {
        try {
            List<ReviewRuleDTO> rules = reviewRuleService.getAllRules();
            return Result.success(rules);
        } catch (Exception e) {
            log.error("Error getting review rules", e);
            return Result.error("Failed to get review rules: " + e.getMessage());
        }
    }
    
    /**
     * Get review rule by ID
     * GET /review-rules/{id}
     */
    @GetMapping("/{id}")
    public Result<ReviewRuleDTO> getRuleById(@PathVariable Long id) {
        try {
            ReviewRuleDTO rule = reviewRuleService.getRuleById(id);
            return Result.success(rule);
        } catch (Exception e) {
            log.error("Error getting review rule: {}", id, e);
            return Result.error("Failed to get review rule: " + e.getMessage());
        }
    }
    
    /**
     * Create a new review rule
     * POST /review-rules
     */
    @PostMapping
    public Result<Long> createRule(@RequestBody CreateReviewRuleRequest request) {
        try {
            // TODO: Get current user from security context
            String createdBy = "admin";
            
            Long ruleId = reviewRuleService.createRule(request, createdBy);
            return Result.success(ruleId);
        } catch (Exception e) {
            log.error("Error creating review rule", e);
            return Result.error("Failed to create review rule: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing review rule
     * PUT /review-rules/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> updateRule(@PathVariable Long id, 
                                    @RequestBody UpdateReviewRuleRequest request) {
        try {
            reviewRuleService.updateRule(id, request);
            return Result.success(null);
        } catch (Exception e) {
            log.error("Error updating review rule: {}", id, e);
            return Result.error("Failed to update review rule: " + e.getMessage());
        }
    }
    
    /**
     * Delete a review rule
     * DELETE /review-rules/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {
        try {
            reviewRuleService.deleteRule(id);
            return Result.success(null);
        } catch (Exception e) {
            log.error("Error deleting review rule: {}", id, e);
            return Result.error("Failed to delete review rule: " + e.getMessage());
        }
    }
    
    /**
     * Enable a review rule
     * POST /review-rules/{id}/enable
     */
    @PostMapping("/{id}/enable")
    public Result<Void> enableRule(@PathVariable Long id) {
        try {
            reviewRuleService.enableRule(id);
            return Result.success(null);
        } catch (Exception e) {
            log.error("Error enabling review rule: {}", id, e);
            return Result.error("Failed to enable review rule: " + e.getMessage());
        }
    }
    
    /**
     * Disable a review rule
     * POST /review-rules/{id}/disable
     */
    @PostMapping("/{id}/disable")
    public Result<Void> disableRule(@PathVariable Long id) {
        try {
            reviewRuleService.disableRule(id);
            return Result.success(null);
        } catch (Exception e) {
            log.error("Error disabling review rule: {}", id, e);
            return Result.error("Failed to disable review rule: " + e.getMessage());
        }
    }
    
    /**
     * Get enabled rules only
     * GET /review-rules/enabled
     */
    @GetMapping("/enabled")
    public Result<List<ReviewRuleDTO>> getEnabledRules() {
        try {
            List<ReviewRuleDTO> rules = reviewRuleService.getEnabledRules();
            return Result.success(rules);
        } catch (Exception e) {
            log.error("Error getting enabled review rules", e);
            return Result.error("Failed to get enabled review rules: " + e.getMessage());
        }
    }
}
