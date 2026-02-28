package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.PluginReviewService;
import org.springframework.web.bind.annotation.*;

/**
 * Plugin Submission Controller
 * REST API for plugin submission management
 */

@Service
@RestController
@RequestMapping("/plugins/submissions")
public class PluginSubmissionController {

    private final static Log log = LogFactory.get(PluginSubmissionController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginReviewService pluginReviewService;
    
    /**
     * Get plugin submissions with filtering and pagination
     *
     * @param page Page number (1-based), default 1
     * @param size Page size, default 20
     * @param status Filter by submission status
     * @param type Filter by plugin type
     * @param keyword Search keyword
     * @param startTime Filter by start time (timestamp in milliseconds)
     * @param endTime Filter by end time (timestamp in milliseconds)
     * @return Paginated list of plugin submissions
     */
    @GetMapping
    public Result<PageResult<PluginSubmissionDTO>> getSubmissions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        
        log.info("GET /plugins/submissions - page: {}, size: {}, status: {}, type: {}, keyword: {}", 
                 page, size, status, type, keyword);
        
        try {
            // Build query request
            SubmissionQueryRequest request = SubmissionQueryRequest.builder()
                    .page(page)
                    .size(size)
                    .keyword(keyword)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
            
            // Parse enum values
            if (status != null && !status.trim().isEmpty()) {
                try {
                    request.setStatus(com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Result.error("Invalid status value: " + status);
                }
            }
            
            if (type != null && !type.trim().isEmpty()) {
                try {
                    request.setPluginType(com.keqi.gress.common.plugin.PluginType.valueOf(type.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Result.error("Invalid plugin type: " + type);
                }
            }
            
            // Query submissions
            PageResult<PluginSubmissionDTO> result = pluginReviewService.getSubmissions(request);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("Failed to get plugin submissions", e);
            return Result.error("Failed to query plugin submissions: " + e.getMessage());
        }
    }
    
    /**
     * Get plugin submission detail by ID
     *
     * @param id Submission ID
     * @return Plugin submission detail
     */
    @GetMapping("/{id}")
    public Result<PluginSubmissionDetailDTO> getSubmissionDetail(@PathVariable Long id) {
        log.info("GET /plugins/submissions/{}", id);
        
        try {
            PluginSubmissionDetailDTO detail = pluginReviewService.getSubmissionDetail(id);
            
            if (detail == null) {
                return Result.error("Plugin submission not found: " + id);
            }
            
            return Result.success(detail);
            
        } catch (Exception e) {
            log.error("Failed to get plugin submission detail: {}", id, e);
            return Result.error("Failed to get plugin submission detail: " + e.getMessage());
        }
    }
    
    /**
     * Approve a plugin submission
     *
     * @param id Submission ID
     * @param request Approval request
     * @return Success result
     */
    @PostMapping("/{id}/approve")
    public Result<Void> approvePlugin(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        log.info("POST /plugins/submissions/{}/approve - reviewer: {}", 
                 id, request.getReviewerName());
        
        try {
            pluginReviewService.approvePlugin(id, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid approval request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for approval: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to approve plugin: {}", id, e);
            return Result.error("Failed to approve plugin: " + e.getMessage());
        }
    }
    
    /**
     * Reject a plugin submission
     *
     * @param id Submission ID
     * @param request Rejection request
     * @return Success result
     */
    @PostMapping("/{id}/reject")
    public Result<Void> rejectPlugin(@PathVariable Long id, @RequestBody RejectionRequest request) {
        log.info("POST /plugins/submissions/{}/reject - reviewer: {}, reason: {}", 
                 id, request.getReviewerName(), request.getReason());
        
        try {
            pluginReviewService.rejectPlugin(id, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid rejection request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for rejection: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to reject plugin: {}", id, e);
            return Result.error("Failed to reject plugin: " + e.getMessage());
        }
    }
    
    /**
     * Batch review plugin submissions
     *
     * @param request Batch review request
     * @return Number of successfully reviewed submissions
     */
    @PostMapping("/batch-review")
    public Result<Integer> batchReview(@RequestBody BatchReviewRequest request) {
        log.info("POST /plugins/submissions/batch-review - count: {}, decision: {}", 
                 request.getSubmissionIds().size(), request.getDecision());
        
        try {
            int successCount = pluginReviewService.batchReview(request);
            return Result.success(successCount);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid batch review request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to batch review plugins", e);
            return Result.error("Failed to batch review plugins: " + e.getMessage());
        }
    }
}
