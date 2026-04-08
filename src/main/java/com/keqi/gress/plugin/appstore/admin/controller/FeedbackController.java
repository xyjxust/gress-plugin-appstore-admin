package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackStatus;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackType;
import com.keqi.gress.plugin.appstore.admin.enums.Severity;
import com.keqi.gress.plugin.appstore.admin.dto.*;

import com.keqi.gress.plugin.appstore.admin.service.FeedbackService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Feedback Controller
 * REST API for user feedback management
 */
@Service
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    
    private static final Log log = LogFactory.get(FeedbackController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private FeedbackService feedbackService;
    
    /**
     * Get feedbacks with filtering and pagination
     *
     * @param page Page number (1-based), default 1
     * @param size Page size, default 20
     * @param pluginId Filter by plugin ID
     * @param feedbackType Filter by feedback type
     * @param status Filter by feedback status
     * @param severity Filter by severity
     * @param keyword Search keyword
     * @param userId Filter by user ID
     * @return Paginated list of feedbacks
     */
    @GetMapping
    public Result<PageResult<FeedbackDTO>> getFeedbacks(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String pluginId,
            @RequestParam(required = false) String feedbackType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String userId) {
        

            // Build query request
            FeedbackQueryRequest request = FeedbackQueryRequest.builder()
                    .page(page)
                    .size(size)
                    .pluginId(pluginId)
                    .keyword(keyword)
                    .userId(userId)
                    .build();
            
            // Parse feedback type enum
            if (feedbackType != null && !feedbackType.trim().isEmpty()) {
                try {
                    request.setFeedbackType(FeedbackType.valueOf(feedbackType.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Result.error("Invalid feedback type: " + feedbackType);
                }
            }
            
            // Parse status enum
            if (status != null && !status.trim().isEmpty()) {
                try {
                    request.setStatus(FeedbackStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Result.error("Invalid feedback status: " + status);
                }
            }
            
            // Parse severity enum
            if (severity != null && !severity.trim().isEmpty()) {
                try {
                    request.setSeverity(Severity.valueOf(severity.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Result.error("Invalid severity: " + severity);
                }
            }
            
            // Query feedbacks
            PageResult<FeedbackDTO> result = feedbackService.getFeedbacks(request);
            
            return Result.success(result);

    }
    
    /**
     * Get feedback detail by ID
     *
     * @param id Feedback ID
     * @return Feedback detail
     */
    @GetMapping("/{id}")
    public Result<FeedbackDetailDTO> getFeedbackDetail(@PathVariable Long id) {

            FeedbackDetailDTO detail = feedbackService.getFeedbackDetail(id);
            
            if (detail == null) {
                return Result.error("Feedback not found: " + id);
            }
            
            return Result.success(detail);

    }
    
    /**
     * Process a feedback (change status to IN_PROGRESS)
     *
     * @param id Feedback ID
     * @param request Process request
     * @return Success result
     */
    @PostMapping("/{id}/process")
    public Result<Void> processFeedback(@PathVariable Long id, @RequestBody ProcessFeedbackRequest request) {

            feedbackService.processFeedback(id, request);
            return Result.success();
            

    }
    
    /**
     * Close a feedback (change status to CLOSED)
     *
     * @param id Feedback ID
     * @param request Close request
     * @return Success result
     */
    @PostMapping("/{id}/close")
    public Result<Void> closeFeedback(@PathVariable Long id, @RequestBody CloseFeedbackRequest request) {

            feedbackService.closeFeedback(id, request);
            return Result.success();

    }
}
