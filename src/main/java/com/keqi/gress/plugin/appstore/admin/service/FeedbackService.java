package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.Feedback;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackStatus;
import com.keqi.gress.plugin.appstore.admin.enums.FeedbackType;
import com.keqi.gress.plugin.appstore.admin.enums.Severity;
import com.keqi.gress.plugin.appstore.admin.support.RequestActorContextBinder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Feedback Service
 * Business logic for user feedback management
 */
@Service
public class FeedbackService {
    
    private static final Log log = LogFactory.get(FeedbackService.class);
    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private AuditLogService auditLogService;
    
    /**
     * Get feedback list with filtering and pagination
     *
     * @param request Query request
     * @return Paginated feedback list
     */
    public PageResult<FeedbackDTO> getFeedbacks(FeedbackQueryRequest request) {
        log.info("Querying feedbacks with request: {}", request);
        
        // Build query using Lambda
        IPage<Feedback> pageResult = dataSource.lambdaQuery(Feedback.class)
            .func(wrapper -> {
                // Apply plugin ID filter
                if (request.getPluginId() != null && !request.getPluginId().trim().isEmpty()) {
                    wrapper.eq(Feedback::getPluginId, request.getPluginId());
                }
                
                // Apply feedback type filter
                if (request.getFeedbackType() != null) {
                    wrapper.eq(Feedback::getFeedbackType, request.getFeedbackType().name());
                }
                
                // Apply status filter
                if (request.getStatus() != null) {
                    wrapper.eq(Feedback::getStatus, request.getStatus().name());
                }
                
                // Apply severity filter
                if (request.getSeverity() != null) {
                    wrapper.eq(Feedback::getSeverity, request.getSeverity().name());
                }
                
                // Apply user ID filter
                if (request.getUserId() != null && !request.getUserId().trim().isEmpty()) {
                    wrapper.eq(Feedback::getUserId, request.getUserId());
                }
                
                // Apply keyword search
                if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                    String keywordPattern = "%" + request.getKeyword().trim() + "%";
                    wrapper.and()
                        .like(Feedback::getTitle, keywordPattern)
                        .or()
                        .like(Feedback::getContent, keywordPattern);
                }
                
                // Order by submit time desc
                wrapper.orderByDesc(Feedback::getSubmitTime);
            })
            .page(request.getPage(), request.getSize());
        
        // Convert to DTOs
        List<FeedbackDTO> feedbacks = pageResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        log.info("Found {} feedbacks out of {} total", feedbacks.size(), pageResult.getTotal());
        
        return PageResult.of(feedbacks, pageResult.getTotal(), request.getPage(), request.getSize());
    }
    
    /**
     * Get feedback detail by ID
     *
     * @param id Feedback ID
     * @return Feedback detail
     */
    public FeedbackDetailDTO getFeedbackDetail(Long id) {
        log.info("Getting feedback detail: {}", id);
        
        Feedback feedback = dataSource.lambdaQuery(Feedback.class)
            .eq(Feedback::getId, id)
            .one();
        
        if (feedback == null) {
            log.warn("Feedback not found: {}", id);
            return null;
        }
        
        FeedbackDetailDTO dto = convertToDetailDTO(feedback);
        
        log.info("Retrieved feedback detail: {}", dto.getTitle());
        
        return dto;
    }
    
    /**
     * Process a feedback (change status to IN_PROGRESS)
     *
     * @param id Feedback ID
     * @param request Process request
     */
    public void processFeedback(Long id, ProcessFeedbackRequest request) {
        log.info("Processing feedback: {}, handler: {}", id, request.getHandlerName());
        RequestActorContextBinder.bindHandler(request);
        
        // Validate request
        if (request.getHandlerId() == null || request.getHandlerId().trim().isEmpty()) {
            throw new IllegalArgumentException("处理人ID不能为空");
        }
        
        if (request.getHandlerName() == null || request.getHandlerName().trim().isEmpty()) {
            throw new IllegalArgumentException("处理人名称不能为空");
        }
        
        // Get current feedback
        Feedback feedback = getFeedbackById(id);
        if (feedback == null) {
            throw new IllegalArgumentException("反馈不存在：" + id);
        }
        
        // Validate state transition
        if (!Objects.equals(feedback.getStatus(), FeedbackStatus.OPEN.name())) {
            throw new IllegalStateException("只能处理待处理状态的反馈，当前状态：" + feedback.getStatus());
        }
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "id", id,
            "status", feedback.getStatus()
        );
        
        // Update feedback status
        LocalDateTime now = LocalDateTime.now();
        int updated = dataSource.lambdaUpdate(Feedback.class)
            .eq(Feedback::getId, id)
            .func(wrapper -> {
                wrapper.set(Feedback::getStatus, FeedbackStatus.IN_PROGRESS.name())
                    .set(Feedback::getHandlerId, request.getHandlerId())
                    .set(Feedback::getHandlerName, request.getHandlerName())
                    .set(Feedback::getHandleTime, now)
                    .set(Feedback::getUpdateTime, now);
                
                if (request.getComment() != null && !request.getComment().trim().isEmpty()) {
                    wrapper.set(Feedback::getHandleComment, request.getComment());
                }
            })
            .update();
        
        if (updated == 0) {
            throw new IllegalStateException("更新反馈状态失败");
        }
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "id", id,
            "status", FeedbackStatus.IN_PROGRESS.name(),
            "comment", request.getComment() != null ? request.getComment() : ""
        );
        
        // Record audit log
        auditLogService.logSuccess(
                "PROCESS_FEEDBACK",
                "处理反馈",
                "FEEDBACK",
                id.toString(),
                feedback.getTitle(),
                request.getHandlerId(),
                request.getHandlerName(),
                beforeData,
                afterData
        );
        
        log.info("Feedback processed successfully: {}", id);
    }
    
    /**
     * Close a feedback (change status to CLOSED)
     *
     * @param id Feedback ID
     * @param request Close request
     */
    public void closeFeedback(Long id, CloseFeedbackRequest request) {
        log.info("Closing feedback: {}, handler: {}", id, request.getHandlerName());
        RequestActorContextBinder.bindHandler(request);
        
        // Validate request
        if (request.getHandlerId() == null || request.getHandlerId().trim().isEmpty()) {
            throw new IllegalArgumentException("处理人ID不能为空");
        }
        
        if (request.getHandlerName() == null || request.getHandlerName().trim().isEmpty()) {
            throw new IllegalArgumentException("处理人名称不能为空");
        }
        
        // Get current feedback
        Feedback feedback = getFeedbackById(id);
        if (feedback == null) {
            throw new IllegalArgumentException("反馈不存在：" + id);
        }
        
        // Validate state transition - can close from any status except CLOSED
        if (FeedbackStatus.CLOSED.name().equals(feedback.getStatus())) {
            throw new IllegalStateException("反馈已关闭");
        }
        
        String previousStatus = feedback.getStatus();
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "id", id,
            "status", previousStatus
        );
        
        // Update feedback status
        LocalDateTime now = LocalDateTime.now();
        int updated = dataSource.lambdaUpdate(Feedback.class)
            .eq(Feedback::getId, id)
            .func(wrapper -> {
                wrapper.set(Feedback::getStatus, FeedbackStatus.CLOSED.name())
                    .set(Feedback::getHandlerId, request.getHandlerId())
                    .set(Feedback::getHandlerName, request.getHandlerName())
                    .set(Feedback::getHandleTime, now)
                    .set(Feedback::getUpdateTime, now);
                
                if (request.getComment() != null && !request.getComment().trim().isEmpty()) {
                    wrapper.set(Feedback::getHandleComment, request.getComment());
                }
            })
            .update();
        
        if (updated == 0) {
            throw new IllegalStateException("更新反馈状态失败");
        }
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "id", id,
            "status", FeedbackStatus.CLOSED.name(),
            "comment", request.getComment() != null ? request.getComment() : ""
        );
        
        // Record audit log
        auditLogService.logSuccess(
                "CLOSE_FEEDBACK",
                "关闭反馈",
                "FEEDBACK",
                id.toString(),
                feedback.getTitle(),
                request.getHandlerId(),
                request.getHandlerName(),
                beforeData,
                afterData
        );
        
        log.info("Feedback closed successfully: {}", id);
    }

    /**
     * Get feedback by ID
     *
     * @param id Feedback ID
     * @return Feedback entity
     */
    private Feedback getFeedbackById(Long id) {
        return dataSource.lambdaQuery(Feedback.class)
            .eq(Feedback::getId, id)
            .one();
    }
    
    /**
     * Convert Feedback entity to FeedbackDTO
     */
    private FeedbackDTO convertToDTO(Feedback entity) {
        return FeedbackDTO.builder()
                .id(entity.getId())
                .pluginId(entity.getPluginId())
                .pluginVersion(entity.getPluginVersion())
                .feedbackType(entity.getFeedbackType() != null ? FeedbackType.valueOf(entity.getFeedbackType()) : null)
                .title(entity.getTitle())
                .content(entity.getContent())
                .severity(entity.getSeverity() != null ? Severity.valueOf(entity.getSeverity()) : null)
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .contact(entity.getContact())
                .status(entity.getStatus() != null ? FeedbackStatus.valueOf(entity.getStatus()) : null)
                .handlerName(entity.getHandlerName())
                .handleTime(entity.getHandleTime())
                .attachments(entity.getAttachments())
                .submitTime(entity.getSubmitTime())
                .build();
    }
    
    /**
     * Convert Feedback entity to FeedbackDetailDTO
     */
    private FeedbackDetailDTO convertToDetailDTO(Feedback entity) {
        return FeedbackDetailDTO.builder()
                .id(entity.getId())
                .pluginId(entity.getPluginId())
                .pluginName(null) // TODO: Query from plugin table if needed
                .pluginVersion(entity.getPluginVersion())
                .feedbackType(entity.getFeedbackType() != null ? FeedbackType.valueOf(entity.getFeedbackType()) : null)
                .title(entity.getTitle())
                .content(entity.getContent())
                .severity(entity.getSeverity() != null ? Severity.valueOf(entity.getSeverity()) : null)
                .userId(entity.getUserId())
                .username(entity.getUsername())
                .contact(entity.getContact())
                .status(entity.getStatus() != null ? FeedbackStatus.valueOf(entity.getStatus()) : null)
                .handlerId(entity.getHandlerId())
                .handlerName(entity.getHandlerName())
                .handleTime(entity.getHandleTime())
                .handleComment(entity.getHandleComment())
                .attachments(entity.getAttachments())
                .submitTime(entity.getSubmitTime())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
    
}
