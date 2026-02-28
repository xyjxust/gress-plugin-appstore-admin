package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginReviewHistory;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.plugin.appstore.admin.enums.ScanStatus;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Plugin Review Service
 * Handles plugin submission review operations
 */
@Service
public class PluginReviewService {
    
    private static final Log log = LogFactory.get(PluginReviewService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource pluginDataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private ReviewRuleEngine reviewRuleEngine;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private SecurityScanService securityScanService;
    
    /**
     * Trigger security scan for a submission
     * This should be called when a new submission is created
     *
     * @param submissionId Submission ID
     */
    public void triggerSecurityScan(Long submissionId) {
        log.info("Triggering security scan for submission: {}", submissionId);
        
        try {
            // Get submission details using Lambda
            PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .one();
            
            if (submission == null) {
                log.warn("Submission not found: {}", submissionId);
                return;
            }
            
            String filePath = submission.getFilePath();
            
            if (filePath == null || filePath.trim().isEmpty()) {
                log.warn("File path is empty for submission: {}", submissionId);
                return;
            }
            
            // Perform security scan asynchronously (in a real implementation, this would be done in a separate thread)
            securityScanService.scanPlugin(submissionId, filePath);
            
            // Check if should auto-reject based on scan results
            if (securityScanService.shouldAutoReject(submissionId)) {
                log.warn("Submission {} has critical vulnerabilities, auto-rejecting", submissionId);
                
                // Auto-reject the submission
                RejectionRequest autoRejectRequest = RejectionRequest.builder()
                    .reviewerId("SYSTEM")
                    .reviewerName("Security Scanner")
                    .reason("插件包含严重安全漏洞，已自动拒绝。请修复漏洞后重新提交。")
                    .build();
                
                rejectPlugin(submissionId, autoRejectRequest);
            }
            
        } catch (Exception e) {
            log.error("Error triggering security scan for submission {}: {}", submissionId, e.getMessage(), e);
        }
    }
    
    /**
     * Apply review rules to a submission
     * This should be called when a new submission is created or updated
     *
     * @param submissionId Submission ID
     */
    public void applyReviewRules(Long submissionId) {
        log.info("Applying review rules to submission: {}", submissionId);
        
        try {
            // Get submission details using Lambda
            PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .one();
            
            if (submission == null) {
                log.warn("Submission not found: {}", submissionId);
                return;
            }
            
            // Apply rules
            List<RuleEvaluationResult> results = reviewRuleEngine.applyRules(submission);
            
            log.info("Applied {} rules to submission {}, {} matched", 
                    results.size(), submissionId, 
                    results.stream().filter(RuleEvaluationResult::getMatched).count());
            
        } catch (Exception e) {
            log.error("Error applying review rules to submission {}: {}", submissionId, e.getMessage(), e);
        }
    }
    
    /**
     * Get plugin submissions with filtering and pagination
     *
     * @param request Query request with filters
     * @return Paginated list of plugin submissions
     */
    public PageResult<PluginSubmissionDTO> getSubmissions(SubmissionQueryRequest request) {
        log.info("Querying plugin submissions with request: {}", request);
        
        // Build query using Lambda
        com.keqi.gress.plugin.api.database.page.IPage<PluginSubmission> pageResult = pluginDataSource.lambdaQuery(PluginSubmission.class)
            .func(wrapper -> {
                // Apply status filter
                if (request.getStatus() != null) {
                    wrapper.eq(PluginSubmission::getStatus, request.getStatus().name());
                }
                
                // Apply plugin type filter
                if (request.getPluginType() != null) {
                    wrapper.eq(PluginSubmission::getPluginType, request.getPluginType().name());
                }
                
                // Apply keyword search
                if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                    String keywordPattern = "%" + request.getKeyword().trim() + "%";
                    wrapper.and()
                        .like(PluginSubmission::getPluginName, keywordPattern)
                        .or()
                        .like(PluginSubmission::getDeveloperName, keywordPattern)
                        .or()
                        .like(PluginSubmission::getDescription, keywordPattern);
                }
                
                // Apply time range filter
                if (request.getStartTime() != null) {
                    wrapper.ge(PluginSubmission::getSubmitTime, request.getStartTime());
                }
                
                if (request.getEndTime() != null) {
                    wrapper.le(PluginSubmission::getSubmitTime, request.getEndTime());
                }
                
                // Order by submit time desc
                wrapper.orderByDesc(PluginSubmission::getSubmitTime);
            })
            .page(request.getPage(), request.getSize());
        
        // Convert to DTOs
        List<PluginSubmissionDTO> submissions = pageResult.getRecords().stream()
                .map(this::convertSubmissionToDTO)
                .collect(Collectors.toList());
        
        long total = pageResult.getTotal();
        
        log.info("Found {} submissions out of {} total", submissions.size(), total);
        
        return PageResult.of(submissions, total, request.getPage(), request.getSize());
    }
    
    /**
     * Get plugin submission detail by ID
     *
     * @param id Submission ID
     * @return Plugin submission detail
     */
    public PluginSubmissionDetailDTO getSubmissionDetail(Long id) {
        log.info("Getting submission detail for ID: {}", id);
        
        // Query submission using Lambda
        PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
            .eq(PluginSubmission::getId, id)
            .one();
        
        if (submission == null) {
            log.warn("Submission not found: {}", id);
            return null;
        }
        
        // Query review history using Lambda
        List<PluginReviewHistory> historyList = pluginDataSource.lambdaQuery(PluginReviewHistory.class)
            .eq(PluginReviewHistory::getSubmissionId, id)
            .orderByDesc(PluginReviewHistory::getReviewTime)
            .list();
        
        List<ReviewHistoryDTO> reviewHistory = historyList.stream()
                .map(this::convertHistoryToDTO)
                .collect(Collectors.toList());
        
        // Convert to detail DTO
        PluginSubmissionDetailDTO detail = convertSubmissionToDetailDTO(submission);
        detail.setReviewHistory(reviewHistory);
        
        log.info("Retrieved submission detail for: {}", detail.getPluginName());
        
        return detail;
    }
    
    /**
     * Convert PluginSubmission entity to PluginSubmissionDTO
     */
    private PluginSubmissionDTO convertSubmissionToDTO(PluginSubmission entity) {
        return PluginSubmissionDTO.builder()
                .id(entity.getId())
                .pluginId(entity.getPluginId())
                .pluginName(entity.getPluginName())
                .pluginType(entity.getPluginType() != null ? PluginType.valueOf(entity.getPluginType()) : null)
                .version(entity.getVersion())
                .developerId(entity.getDeveloperId())
                .developerName(entity.getDeveloperName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .tags(entity.getTags())
                .category(entity.getCategory())
                .status(entity.getStatus() != null ? SubmissionStatus.valueOf(entity.getStatus()) : null)
                .scanStatus(entity.getScanStatus() != null ? ScanStatus.valueOf(entity.getScanStatus()) : null)
                .reviewerName(entity.getReviewerName())
                .reviewTime(entity.getReviewTime())
                .submitTime(entity.getSubmitTime())
                .build();
    }
    
    /**
     * Convert PluginSubmission entity to PluginSubmissionDetailDTO
     */
    private PluginSubmissionDetailDTO convertSubmissionToDetailDTO(PluginSubmission entity) {
        return PluginSubmissionDetailDTO.builder()
                .id(entity.getId())
                .pluginId(entity.getPluginId())
                .pluginName(entity.getPluginName())
                .pluginType(entity.getPluginType() != null ? PluginType.valueOf(entity.getPluginType()) : null)
                .version(entity.getVersion())
                .developerId(entity.getDeveloperId())
                .developerName(entity.getDeveloperName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .tags(entity.getTags())
                .category(entity.getCategory())
                .filePath(entity.getFilePath())
                .fileSize(entity.getFileSize())
                .fileHash(entity.getFileHash())
                .status(entity.getStatus() != null ? SubmissionStatus.valueOf(entity.getStatus()) : null)
                .reviewerId(entity.getReviewerId())
                .reviewerName(entity.getReviewerName())
                .reviewTime(entity.getReviewTime())
                .reviewComment(entity.getReviewComment())
                .scanStatus(entity.getScanStatus() != null ? ScanStatus.valueOf(entity.getScanStatus()) : null)
                .scanResult(entity.getScanResult() != null ? parseJson(entity.getScanResult()) : null)
                .submitTime(entity.getSubmitTime())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
    
    /**
     * Convert PluginReviewHistory entity to ReviewHistoryDTO
     */
    private ReviewHistoryDTO convertHistoryToDTO(PluginReviewHistory entity) {
        return ReviewHistoryDTO.builder()
                .id(entity.getId())
                .submissionId(entity.getSubmissionId())
                .pluginId(entity.getPluginId())
                .version(entity.getVersion())
                .reviewerId(entity.getReviewerId())
                .reviewerName(entity.getReviewerName())
                .decision(entity.getDecision())
                .comment(entity.getComment())
                .reviewTime(entity.getReviewTime())
                .build();
    }
    
    // Helper methods for type conversion
    
    
    private Object parseJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return JSON.parse(json);
        } catch (Exception e) {
            log.warn("Failed to parse JSON: {}", json, e);
            return null;
        }
    }
    
    /**
     * Approve a plugin submission
     *
     * @param submissionId Submission ID
     * @param request Approval request
     */
    public void approvePlugin(Long submissionId, ApprovalRequest request) {
        log.info("Approving plugin submission: {}", submissionId);
        
        // Validate request
        if (request.getReviewerId() == null || request.getReviewerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer ID is required");
        }
        if (request.getReviewerName() == null || request.getReviewerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer name is required");
        }
        
        // Query current submission using Lambda
        PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
            .eq(PluginSubmission::getId, submissionId)
            .one();
        
        if (submission == null) {
            throw new IllegalArgumentException("Plugin submission not found: " + submissionId);
        }
        
        // Check if already reviewed
        String currentStatus = submission.getStatus();
        if (!"PENDING".equals(currentStatus)) {
            throw new IllegalStateException("Plugin submission is not in PENDING status: " + currentStatus);
        }
        
        // Check security scan status
        String scanStatusStr = submission.getScanStatus();
        if (scanStatusStr != null) {
            ScanStatus scanStatus = ScanStatus.valueOf(scanStatusStr);
            
            if (scanStatus == ScanStatus.PENDING || scanStatus == ScanStatus.SCANNING) {
                throw new IllegalStateException("Security scan is not completed yet. Please wait for scan to complete.");
            }
            
            if (scanStatus == ScanStatus.COMPLETED) {
                // Check if has critical vulnerabilities
                if (securityScanService.shouldAutoReject(submissionId)) {
                    throw new IllegalStateException("Plugin has critical security vulnerabilities. Cannot approve.");
                }
            }
        }
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "id", submissionId,
            "status", currentStatus
        );
        
        // Execute approval in transaction
        pluginDataSource.executeTransaction(() -> {
            LocalDateTime now = LocalDateTime.now();
            
            // 1. Update submission status using Lambda
            pluginDataSource.lambdaUpdate(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .set(PluginSubmission::getStatus, "APPROVED")
                .set(PluginSubmission::getReviewerId, request.getReviewerId())
                .set(PluginSubmission::getReviewerName, request.getReviewerName())
                .set(PluginSubmission::getReviewTime, now)
                .set(PluginSubmission::getReviewComment, request.getComment())
                .update();
            
            // 2. Insert review history using Lambda
            PluginReviewHistory history = PluginReviewHistory.builder()
                .submissionId(submissionId)
                .pluginId(submission.getPluginId())
                .version(submission.getVersion())
                .reviewerId(request.getReviewerId())
                .reviewerName(request.getReviewerName())
                .decision("APPROVED")
                .comment(request.getComment())
                .reviewTime(now)
                .createTime(now)
                .build();
            
            pluginDataSource.insert(history);
            
            // 3. Update or insert plugin manager record
            String pluginId = submission.getPluginId();
            long count = pluginDataSource.lambdaQuery(PluginManager.class)
                .eq(PluginManager::getPluginId, pluginId)
                .count();
            
            if (count == 0) {
                // Insert new manager record using Lambda
                PluginManager manager = PluginManager.builder()
                    .pluginId(pluginId)
                    .pluginName(submission.getPluginName())
                    .pluginType(submission.getPluginType())
                    .currentVersion(submission.getVersion())
                    .status("ONLINE")
                    .developerId(submission.getDeveloperId())
                    .developerName(submission.getDeveloperName())
                    .createTime(now)
                    .updateTime(now)
                    .build();
                
                pluginDataSource.insert(manager);
            } else {
                // Update existing manager record using Lambda
                pluginDataSource.lambdaUpdate(PluginManager.class)
                    .eq(PluginManager::getPluginId, pluginId)
                    .set(PluginManager::getStatus, "ONLINE")
                    .set(PluginManager::getCurrentVersion, submission.getVersion())
                    .set(PluginManager::getUpdateTime, now)
                    .update();
            }
        });
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "id", submissionId,
            "status", "APPROVED",
            "reviewerId", request.getReviewerId(),
            "reviewerName", request.getReviewerName()
        );
        
        // 4. Record audit log
        auditLogService.logSuccess(
            "APPROVE_PLUGIN",
            "Approve Plugin",
            "PLUGIN_SUBMISSION",
            submissionId.toString(),
            submission.getPluginName(),
            request.getReviewerId(),
            request.getReviewerName(),
            beforeData,
            afterData
        );
        
        log.info("Plugin submission approved successfully: {}", submissionId);
    }
    
    /**
     * Reject a plugin submission
     *
     * @param submissionId Submission ID
     * @param request Rejection request
     */
    public void rejectPlugin(Long submissionId, RejectionRequest request) {
        log.info("Rejecting plugin submission: {}", submissionId);
        
        // Validate request
        if (request.getReviewerId() == null || request.getReviewerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer ID is required");
        }
        if (request.getReviewerName() == null || request.getReviewerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer name is required");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }
        
        // Query current submission using Lambda
        PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
            .eq(PluginSubmission::getId, submissionId)
            .one();
        
        if (submission == null) {
            throw new IllegalArgumentException("Plugin submission not found: " + submissionId);
        }
        
        // Check if already reviewed
        String currentStatus = submission.getStatus();
        if (!"PENDING".equals(currentStatus)) {
            throw new IllegalStateException("Plugin submission is not in PENDING status: " + currentStatus);
        }
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "id", submissionId,
            "status", currentStatus
        );
        
        // Execute rejection in transaction
        pluginDataSource.executeTransaction(() -> {
            LocalDateTime now = LocalDateTime.now();
            
            // 1. Update submission status using Lambda
            pluginDataSource.lambdaUpdate(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .set(PluginSubmission::getStatus, "REJECTED")
                .set(PluginSubmission::getReviewerId, request.getReviewerId())
                .set(PluginSubmission::getReviewerName, request.getReviewerName())
                .set(PluginSubmission::getReviewTime, now)
                .set(PluginSubmission::getReviewComment, request.getReason())
                .update();
            
            // 2. Insert review history using Lambda
            PluginReviewHistory history = PluginReviewHistory.builder()
                .submissionId(submissionId)
                .pluginId(submission.getPluginId())
                .version(submission.getVersion())
                .reviewerId(request.getReviewerId())
                .reviewerName(request.getReviewerName())
                .decision("REJECTED")
                .comment(request.getReason())
                .reviewTime(now)
                .createTime(now)
                .build();
            
            pluginDataSource.insert(history);
        });
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "id", submissionId,
            "status", "REJECTED",
            "reviewerId", request.getReviewerId(),
            "reviewerName", request.getReviewerName(),
            "reason", request.getReason()
        );
        
        // 3. Record audit log
        auditLogService.logSuccess(
            "REJECT_PLUGIN",
            "Reject Plugin",
            "PLUGIN_SUBMISSION",
            submissionId.toString(),
            submission.getPluginName(),
            request.getReviewerId(),
            request.getReviewerName(),
            beforeData,
            afterData
        );
        
        log.info("Plugin submission rejected successfully: {}", submissionId);
    }
    
    /**
     * Batch review plugin submissions
     *
     * @param request Batch review request
     * @return Number of successfully reviewed submissions
     */
    public int batchReview(BatchReviewRequest request) {
        log.info("Batch reviewing {} submissions with decision: {}", 
                 request.getSubmissionIds().size(), request.getDecision());
        
        // Validate request
        if (request.getSubmissionIds() == null || request.getSubmissionIds().isEmpty()) {
            throw new IllegalArgumentException("Submission IDs are required");
        }
        if (request.getReviewerId() == null || request.getReviewerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer ID is required");
        }
        if (request.getReviewerName() == null || request.getReviewerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reviewer name is required");
        }
        if (!"APPROVE".equals(request.getDecision()) && !"REJECT".equals(request.getDecision())) {
            throw new IllegalArgumentException("Invalid decision: " + request.getDecision());
        }
        if ("REJECT".equals(request.getDecision()) && 
            (request.getComment() == null || request.getComment().trim().isEmpty())) {
            throw new IllegalArgumentException("Rejection reason is required");
        }
        
        int successCount = 0;
        
        // Process each submission
        for (Long submissionId : request.getSubmissionIds()) {
            try {
                if ("APPROVE".equals(request.getDecision())) {
                    ApprovalRequest approvalRequest = ApprovalRequest.builder()
                        .reviewerId(request.getReviewerId())
                        .reviewerName(request.getReviewerName())
                        .comment(request.getComment())
                        .build();
                    approvePlugin(submissionId, approvalRequest);
                } else {
                    RejectionRequest rejectionRequest = RejectionRequest.builder()
                        .reviewerId(request.getReviewerId())
                        .reviewerName(request.getReviewerName())
                        .reason(request.getComment())
                        .build();
                    rejectPlugin(submissionId, rejectionRequest);
                }
                successCount++;
            } catch (Exception e) {
                log.error("Failed to review submission: {}", submissionId, e);
                // Continue with next submission
            }
        }
        
        log.info("Batch review completed: {}/{} successful", 
                 successCount, request.getSubmissionIds().size());
        
        return successCount;
    }
}
