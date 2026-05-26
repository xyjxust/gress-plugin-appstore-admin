package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.CreateReviewRuleRequest;
import com.keqi.gress.plugin.appstore.admin.dto.ReviewRuleDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateReviewRuleRequest;
import com.keqi.gress.plugin.appstore.admin.service.ReviewRuleService;
import com.keqi.gress.plugin.appstore.admin.support.OperatorContextHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Review Rule Controller
 * REST API for managing review rules
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/review-rules")
@PluginMenu(id = "review-rules", name = "审核规则", managementEnabled = true)
public class ReviewRuleController {

    private final static Log log = LogFactory.get(ReviewRuleController.class);
    
    @Autowired
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
    @PluginAction(id = "create", name = "新建规则")
    public Result<Long> createRule(@RequestBody CreateReviewRuleRequest request) {
            Long ruleId = reviewRuleService.createRule(request);
            return Result.success(ruleId);

    }
    
    /**
     * Update an existing review rule
     * PUT /review-rules/{id}
     */
    @PutMapping("/{id}")
    @PluginAction(id = "update", name = "编辑规则", managementEnabled = true, actionCode = "UPDATE")
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
    @PluginAction(id = "delete", name = "删除规则", managementEnabled = true, actionCode = "DELETE")
    public Result<Void> deleteRule(@PathVariable Long id) {

            reviewRuleService.deleteRule(id);
            return Result.success(null);

    }
    
    /**
     * Enable a review rule
     * POST /review-rules/{id}/enable
     */
    @PostMapping("/{id}/enable")
    @PluginAction(id = "enable", name = "启用规则", managementEnabled = true, actionCode = "ENABLE")
    public Result<Void> enableRule(@PathVariable Long id) {

            reviewRuleService.enableRule(id);
            return Result.success(null);

    }
    
    /**
     * Disable a review rule
     * POST /review-rules/{id}/disable
     */
    @PostMapping("/{id}/disable")
    @PluginAction(id = "disable", name = "禁用规则", managementEnabled = true, actionCode = "DISABLE")
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
