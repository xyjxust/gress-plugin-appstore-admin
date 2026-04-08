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

            List<ReviewRuleDTO> rules = reviewRuleService.getAllRules();
            return Result.success(rules);

    }
    
    /**
     * Get review rule by ID
     * GET /review-rules/{id}
     */
    @GetMapping("/{id}")
    public Result<ReviewRuleDTO> getRuleById(@PathVariable Long id) {

            ReviewRuleDTO rule = reviewRuleService.getRuleById(id);
            return Result.success(rule);

    }
    
    /**
     * Create a new review rule
     * POST /review-rules
     */
    @PostMapping
    public Result<Long> createRule(@RequestBody CreateReviewRuleRequest request) {

            // TODO: Get current user from security context
            String createdBy = "admin";
            
            Long ruleId = reviewRuleService.createRule(request, createdBy);
            return Result.success(ruleId);

    }
    
    /**
     * Update an existing review rule
     * PUT /review-rules/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> updateRule(@PathVariable Long id, 
                                    @RequestBody UpdateReviewRuleRequest request) {

            reviewRuleService.updateRule(id, request);
            return Result.success(null);

    }
    
    /**
     * Delete a review rule
     * DELETE /review-rules/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {

            reviewRuleService.deleteRule(id);
            return Result.success(null);

    }
    
    /**
     * Enable a review rule
     * POST /review-rules/{id}/enable
     */
    @PostMapping("/{id}/enable")
    public Result<Void> enableRule(@PathVariable Long id) {

            reviewRuleService.enableRule(id);
            return Result.success(null);

    }
    
    /**
     * Disable a review rule
     * POST /review-rules/{id}/disable
     */
    @PostMapping("/{id}/disable")
    public Result<Void> disableRule(@PathVariable Long id) {

            reviewRuleService.disableRule(id);
            return Result.success(null);

    }
    
    /**
     * Get enabled rules only
     * GET /review-rules/enabled
     */
    @GetMapping("/enabled")
    public Result<List<ReviewRuleDTO>> getEnabledRules() {

            List<ReviewRuleDTO> rules = reviewRuleService.getEnabledRules();
            return Result.success(rules);

    }
}
