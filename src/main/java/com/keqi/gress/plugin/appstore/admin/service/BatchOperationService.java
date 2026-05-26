package com.keqi.gress.plugin.appstore.admin.service;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSON;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginReviewHistory;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 批量操作服务
 * 处理插件的批量审核、批量下架、批量分类调整等操作
 */
@Service
public class BatchOperationService {
    
    private static final Log log = LogFactory.get(BatchOperationService.class);
    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private AuditLogService auditLogService;
    
    /**
     * 批量审核插件提交
     * 使用事务确保原子性
     *
     * @param request 批量审核请求
     * @return 批量操作结果
     */
    public BatchOperationResult batchReview(BatchReviewRequest request) {
        log.info("开始批量审核，提交ID数量: {}, 决策: {}", 
                request.getSubmissionIds().size(), request.getDecision());
        
        List<String> successIds = new ArrayList<>();
        Map<String, String> failures = new HashMap<>();
        
        // 使用事务确保原子性
        try {
            dataSource.executeTransaction(() -> {
                for (Long submissionId : request.getSubmissionIds()) {
                    try {
                        // 执行单个审核操作
                        reviewSingleSubmission(submissionId, request);
                        successIds.add(String.valueOf(submissionId));
                        
                    } catch (Exception e) {
                        log.error("审核提交失败: submissionId={}", submissionId, e);
                        failures.put(String.valueOf(submissionId), e.getMessage());
                        // 如果需要全部成功才提交事务，可以在这里抛出异常
                        // throw new RuntimeException("批量审核失败", e);
                    }
                }
            });
            
        } catch (Exception e) {
            log.error("批量审核事务失败", e);
            return BatchOperationResult.builder()
                    .operationType("BATCH_REVIEW")
                    .total(request.getSubmissionIds().size())
                    .successCount(0)
                    .failureCount(request.getSubmissionIds().size())
                    .successIds(new ArrayList<>())
                    .failures(Map.of("transaction", "事务执行失败: " + e.getMessage()))
                    .summary("批量审核失败，事务已回滚")
                    .build();
        }
        
        // 记录审计日志
        auditLogService.log("BATCH_REVIEW", "批量审核", "SUBMISSION", 
                JSON.toJSONString(request.getSubmissionIds()), 
                "批量提交", 
                request.getReviewerId(), 
                request.getReviewerName(), 
                "SUCCESS",
                null, 
                request);
        
        // 构建结果
        String summary = String.format("批量审核完成：总数 %d，成功 %d，失败 %d",
                request.getSubmissionIds().size(), successIds.size(), failures.size());
        
        return BatchOperationResult.builder()
                .operationType("BATCH_REVIEW")
                .total(request.getSubmissionIds().size())
                .successCount(successIds.size())
                .failureCount(failures.size())
                .successIds(successIds)
                .failures(failures)
                .summary(summary)
                .build();
    }
    
    /**
     * 在事务中执行单个审核操作
     */
    private void reviewSingleSubmission(Long submissionId, BatchReviewRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. 查询提交信息（单表查询使用 Lambda）
        PluginSubmission submission = dataSource.lambdaQuery(PluginSubmission.class)
            .eq(PluginSubmission::getId, submissionId)
            .one();
        
        if (submission == null) {
            throw new RuntimeException("提交记录不存在: " + submissionId);
        }
        
        // 2. 更新提交状态（单表更新使用 Lambda）
        SubmissionStatus status = "APPROVE".equalsIgnoreCase(request.getDecision()) 
            ? SubmissionStatus.APPROVED 
            : SubmissionStatus.REJECTED;
        
        dataSource.lambdaUpdate(PluginSubmission.class)
            .set(PluginSubmission::getStatus, status)
            .set(PluginSubmission::getReviewerId, request.getReviewerId())
            .set(PluginSubmission::getReviewerName, request.getReviewerName())
            .set(PluginSubmission::getReviewTime, now)
            .set(PluginSubmission::getReviewComment, request.getComment())
            .eq(PluginSubmission::getId, submissionId)
            .update();
        
        // 3. 插入审核历史（单表插入使用 Lambda）
        PluginReviewHistory history = PluginReviewHistory.builder()
            .submissionId(submissionId)
            .pluginId(submission.getPluginId())
            .version(submission.getVersion())
            .reviewerId(request.getReviewerId())
            .reviewerName(request.getReviewerName())
            .decision(status.name())
            .comment(request.getComment())
            .reviewTime(now)
            .build();
        history.setCreateTime(now);
        
        dataSource.insert(history);
        
        // 4. 如果是批准，更新插件管理表
        if ("APPROVE".equalsIgnoreCase(request.getDecision())) {
            updatePluginManagerOnApproval(submission.getPluginId());
        }
    }
    
    /**
     * 批准时更新插件管理表（单表操作使用 Lambda）
     */
    private void updatePluginManagerOnApproval(String pluginId) {
        LocalDateTime now = LocalDateTime.now();
        
        // 检查插件是否已存在（单表查询使用 Lambda）
        long count = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .count();
        
        if (count > 0) {
            // 更新状态为上架（单表更新使用 Lambda）
            dataSource.lambdaUpdate(PluginManager.class)
                .set(PluginManager::getStatus, "ONLINE")
                .set(PluginManager::getUpdateTime, now)
                .eq(PluginManager::getPluginId, pluginId)
                .update();
        } else {
            // 插入新记录（单表插入使用 Lambda）
            PluginManager manager = PluginManager.builder()
                .pluginId(pluginId)
                .status("ONLINE")
                .build();
            manager.setCreateTime(now);
            manager.setUpdateTime(now);
            
            dataSource.insert(manager);
        }
    }
    
    /**
     * 批量下架插件
     * 使用事务确保原子性
     *
     * @param request 批量下架请求
     * @return 批量操作结果
     */
    public BatchOperationResult batchDelist(BatchDelistRequest request) {
        log.info("开始批量下架，插件数量: {}", request.getPluginIds().size());
        
        List<String> successIds = new ArrayList<>();
        Map<String, String> failures = new HashMap<>();
        
        // 使用事务确保原子性
        try {
            dataSource.executeTransaction(() -> {
                for (String pluginId : request.getPluginIds()) {
                    try {
                        // 执行单个下架操作
                        delistSinglePlugin(pluginId, request);
                        successIds.add(pluginId);
                        
                    } catch (Exception e) {
                        log.error("下架插件失败: pluginId={}", pluginId, e);
                        failures.put(pluginId, e.getMessage());
                    }
                }
            });
            
        } catch (Exception e) {
            log.error("批量下架事务失败", e);
            return BatchOperationResult.builder()
                    .operationType("BATCH_DELIST")
                    .total(request.getPluginIds().size())
                    .successCount(0)
                    .failureCount(request.getPluginIds().size())
                    .successIds(new ArrayList<>())
                    .failures(Map.of("transaction", "事务执行失败: " + e.getMessage()))
                    .summary("批量下架失败，事务已回滚")
                    .build();
        }
        
        // 记录审计日志
        auditLogService.log("BATCH_DELIST", "批量下架", "PLUGIN", 
                JSON.toJSONString(request.getPluginIds()), 
                "批量插件", 
                request.getOperatorId(), 
                request.getOperatorName(), 
                "SUCCESS",
                null, 
                request);
        
        // 构建结果
        String summary = String.format("批量下架完成：总数 %d，成功 %d，失败 %d",
                request.getPluginIds().size(), successIds.size(), failures.size());
        
        return BatchOperationResult.builder()
                .operationType("BATCH_DELIST")
                .total(request.getPluginIds().size())
                .successCount(successIds.size())
                .failureCount(failures.size())
                .successIds(successIds)
                .failures(failures)
                .summary(summary)
                .build();
    }
    
    /**
     * 在事务中执行单个下架操作
     */
    private void delistSinglePlugin(String pluginId, BatchDelistRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. 检查插件是否存在且已上架（单表查询使用 Lambda）
        PluginManager manager = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .one();
        
        if (manager == null) {
            throw new RuntimeException("插件不存在: " + pluginId);
        }
        
        if (!"ONLINE".equals(manager.getStatus())) {
            throw new RuntimeException("插件未上架，无法下架: " + pluginId);
        }
        
        // 2. 更新插件状态为下架（单表更新使用 Lambda）
        dataSource.lambdaUpdate(PluginManager.class)
            .set(PluginManager::getStatus, "OFFLINE")
            .set(PluginManager::getDelistReason, request.getReason())
            .set(PluginManager::getDelistTime, now)
            .set(PluginManager::getUpdateTime, now)
            .eq(PluginManager::getPluginId, pluginId)
            .update();
        
        // 3. 记录下架历史（使用动态 SQL）
        String insertHistorySql = """
                INSERT INTO as_admin_delist_history 
                (plugin_id, operator_id, operator_name, reason, delist_time) 
                VALUES (#{pluginId}, #{operatorId}, #{operatorName}, #{reason}, #{delistTime})
                """;
        
        dataSource.dynamicSql(insertHistorySql)
                .param("pluginId", pluginId)
                .param("operatorId", request.getOperatorId())
                .param("operatorName", request.getOperatorName())
                .param("reason", request.getReason())
                .param("delistTime", now)
                .execute();
    }
    
    /**
     * 批量更新插件分类
     * 使用事务确保原子性
     *
     * @param request 批量分类更新请求
     * @return 批量操作结果
     */
    public BatchOperationResult batchUpdateCategory(BatchCategoryUpdateRequest request) {
        log.info("开始批量更新分类，插件数量: {}", request.getPluginIds().size());
        
        List<String> successIds = new ArrayList<>();
        Map<String, String> failures = new HashMap<>();
        
        // 使用事务确保原子性
        try {
            dataSource.executeTransaction(() -> {
                for (String pluginId : request.getPluginIds()) {
                    try {
                        // 执行单个分类更新操作
                        updateSinglePluginCategory(pluginId, request);
                        successIds.add(pluginId);
                        
                    } catch (Exception e) {
                        log.error("更新插件分类失败: pluginId={}", pluginId, e);
                        failures.put(pluginId, e.getMessage());
                    }
                }
            });
            
        } catch (Exception e) {
            log.error("批量更新分类事务失败", e);
            return BatchOperationResult.builder()
                    .operationType("BATCH_UPDATE_CATEGORY")
                    .total(request.getPluginIds().size())
                    .successCount(0)
                    .failureCount(request.getPluginIds().size())
                    .successIds(new ArrayList<>())
                    .failures(Map.of("transaction", "事务执行失败: " + e.getMessage()))
                    .summary("批量更新分类失败，事务已回滚")
                    .build();
        }
        
        // 记录审计日志
        auditLogService.log("BATCH_UPDATE_CATEGORY", "批量更新分类", "PLUGIN", 
                JSON.toJSONString(request.getPluginIds()), 
                "批量插件", 
                request.getOperatorId(), 
                request.getOperatorName(), 
                "SUCCESS",
                null, 
                request);
        
        // 构建结果
        String summary = String.format("批量更新分类完成：总数 %d，成功 %d，失败 %d",
                request.getPluginIds().size(), successIds.size(), failures.size());
        
        return BatchOperationResult.builder()
                .operationType("BATCH_UPDATE_CATEGORY")
                .total(request.getPluginIds().size())
                .successCount(successIds.size())
                .failureCount(failures.size())
                .successIds(successIds)
                .failures(failures)
                .summary(summary)
                .build();
    }
    
    /**
     * 在事务中执行单个分类更新操作
     */
    private void updateSinglePluginCategory(String pluginId, BatchCategoryUpdateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. 检查插件是否存在（单表查询使用 Lambda）
        long count = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .count();
        
        if (count == 0) {
            throw new RuntimeException("插件不存在: " + pluginId);
        }
        
        // 2. 更新分类和标签（单表更新使用 Lambda）
        String tagsJson = request.getTags() != null ? JSON.toJSONString(request.getTags()) : null;
        
        dataSource.lambdaUpdate(PluginManager.class)
            .set(PluginManager::getCategory, request.getCategory())
            .set(PluginManager::getTags, tagsJson)
            .set(PluginManager::getUpdateTime, now)
            .eq(PluginManager::getPluginId, pluginId)
            .update();
    }
}
