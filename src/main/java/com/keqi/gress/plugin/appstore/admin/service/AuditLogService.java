package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogDTO;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogExportRequest;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogQueryRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.entity.AuditLog;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Audit Log Service
 * Records all administrative operations for audit trail
 */
@Service
public class AuditLogService {
    
    private static final Log log = LogFactory.get(AuditLogService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    /**
     * Log an operation
     *
     * @param operationType Operation type (e.g., "APPROVE_PLUGIN", "REJECT_PLUGIN")
     * @param operationName Operation name
     * @param targetType Target type (e.g., "PLUGIN", "DEVELOPER")
     * @param targetId Target ID
     * @param targetName Target name
     * @param operatorId Operator ID
     * @param operatorName Operator name
     * @param result Operation result ("SUCCESS" or "FAILURE")
     * @param beforeData Data before operation (optional)
     * @param afterData Data after operation (optional)
     */
    public void log(String operationType, String operationName, String targetType, String targetId, 
                    String targetName, String operatorId, String operatorName, String result,
                    Object beforeData, Object afterData) {
        
        log.info("Recording audit log: type={}, target={}:{}, operator={}, result={}", 
                 operationType, targetType, targetId, operatorName, result);
        
        try {
            String beforeDataJson = beforeData != null ? JSON.toJSONString(beforeData) : null;
            String afterDataJson = afterData != null ? JSON.toJSONString(afterData) : null;
            String operationDesc = String.format("%s performed %s on %s %s", 
                                                operatorName, operationName, targetType, targetName);
            
            // 使用 Lambda 方式插入
            AuditLog auditLog = AuditLog.builder()
                    .operationType(operationType)
                    .operationName(operationName)
                    .operationDesc(operationDesc)
                    .targetType(targetType)
                    .targetId(targetId)
                    .targetName(targetName)
                    .operatorId(operatorId)
                    .operatorName(operatorName)
                    .result(result)
                    .beforeData(beforeDataJson)
                    .afterData(afterDataJson)
                    .operationTime(LocalDateTime.now())
                    .build();
            
            dataSource.insert(auditLog);
            
            log.info("Audit log recorded successfully");
            
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
            // Don't throw exception - audit log failure should not break the main operation
        }
    }
    
    /**
     * Log a successful operation
     */
    public void logSuccess(String operationType, String operationName, String targetType, String targetId,
                          String targetName, String operatorId, String operatorName,
                          Object beforeData, Object afterData) {
        log(operationType, operationName, targetType, targetId, targetName, 
            operatorId, operatorName, "SUCCESS", beforeData, afterData);
    }
    
    /**
     * Log a failed operation
     */
    public void logFailure(String operationType, String operationName, String targetType, String targetId,
                          String targetName, String operatorId, String operatorName, String errorMessage) {
        log(operationType, operationName, targetType, targetId, targetName,
            operatorId, operatorName, "FAILURE", null, errorMessage);
    }
    
    /**
     * Simplified log method for operations
     *
     * @param operationType Operation type
     * @param targetType Target type
     * @param targetId Target ID
     * @param targetName Target name
     * @param operatorId Operator ID
     * @param operatorName Operator name
     * @param result Operation result
     * @param beforeData Data before operation
     * @param afterData Data after operation
     */
    public void log(String operationType, String targetType, String targetId, String targetName,
                    String operatorId, String operatorName, String result,
                    Object beforeData, Object afterData) {
        log(operationType, operationType, targetType, targetId, targetName,
            operatorId, operatorName, result, beforeData, afterData);
    }
    
    /**
     * Query audit logs with filtering and pagination
     *
     * @param request Query request with filters
     * @return Paginated audit log results
     */
    public PageResult<AuditLogDTO> queryLogs(AuditLogQueryRequest request) {
        log.info("Querying audit logs: page={}, size={}, operationType={}, targetType={}, operatorId={}", 
                 request.getPage(), request.getSize(), request.getOperationType(), 
                 request.getTargetType(), request.getOperatorId());
        
        try {
            // 使用 Lambda Query Chain 进行分页查询
            var query = dataSource.lambdaQuery(AuditLog.class);
            
            // Apply filters
            applyQueryFilters(query, request);
            
            // Execute with pagination
            IPage<AuditLog> page = query.orderByDesc(AuditLog::getOperationTime)
                                        .page(request.getPage(), request.getSize());
            
            // Map to DTOs
            List<AuditLogDTO> logs = page.getRecords().stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            log.info("Found {} audit logs (total: {})", logs.size(), page.getTotal());
            
            return PageResult.of(logs, page.getTotal(), request.getPage(), request.getSize());
            
        } catch (Exception e) {
            log.error("Failed to query audit logs", e);
            throw new RuntimeException("Failed to query audit logs: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get audit log detail by ID
     *
     * @param id Audit log ID
     * @return Audit log detail
     */
    public AuditLogDTO getLogDetail(Long id) {
        log.info("Getting audit log detail: id={}", id);
        
        try {
            // 使用 Lambda Query Chain
            AuditLog auditLog = dataSource.lambdaQuery(AuditLog.class)
                .eq(AuditLog::getId, id)
                .one();
            
            if (auditLog == null) {
                log.warn("Audit log not found: id={}", id);
                return null;
            }
            
            return mapToDTO(auditLog);
            
        } catch (Exception e) {
            log.error("Failed to get audit log detail: id={}", id, e);
            throw new RuntimeException("Failed to get audit log detail: " + e.getMessage(), e);
        }
    }
    
    /**
     * Export audit logs to specified format
     *
     * @param request Export request with filters and format
     * @return Exported data as string (CSV, JSON, or Excel format)
     */
    public String exportLogs(AuditLogExportRequest request) {
        log.info("Exporting audit logs: format={}, operationType={}, startTime={}, endTime={}", 
                 request.getFormat(), request.getOperationType(), request.getStartTime(), request.getEndTime());
        
        try {
            // 使用 Lambda Query Chain（不分页）
            var query = dataSource.lambdaQuery(AuditLog.class);
            
            // Apply filters
            applyExportFilters(query, request);
            
            // Execute query
            List<AuditLog> auditLogs = query.orderByDesc(AuditLog::getOperationTime)
                                            .list();
            
            // Map to DTOs
            List<AuditLogDTO> logs = auditLogs.stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            
            log.info("Exporting {} audit logs in {} format", logs.size(), request.getFormat());
            
            // Export based on format
            String format = request.getFormat() != null ? request.getFormat().toUpperCase() : "JSON";
            switch (format) {
                case "CSV":
                    return exportToCSV(logs);
                case "JSON":
                    return exportToJSON(logs);
                case "EXCEL":
                    // For Excel, we'll return CSV format (can be enhanced later)
                    return exportToCSV(logs);
                default:
                    throw new IllegalArgumentException("Unsupported export format: " + request.getFormat());
            }
            
        } catch (Exception e) {
            log.error("Failed to export audit logs", e);
            throw new RuntimeException("Failed to export audit logs: " + e.getMessage(), e);
        }
    }
    
    /**
     * Archive old audit logs
     * Moves logs older than the specified days to archive table or deletes them
     *
     * @param daysToKeep Number of days to keep logs (logs older than this will be archived)
     * @return Number of logs archived
     */
    public int archiveLogs(int daysToKeep) {
        log.info("Archiving audit logs older than {} days", daysToKeep);
        
        try {
            // Calculate cutoff date
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
            
            // Count logs to be archived
            long count = dataSource.lambdaQuery(AuditLog.class)
                .lt(AuditLog::getOperationTime, cutoffDate)
                .count();
            
            if (count == 0) {
                log.info("No audit logs to archive");
                return 0;
            }
            
            // For now, we'll just delete old logs
            // In production, you might want to move them to an archive table first
            int deleted = dataSource.lambdaUpdate(AuditLog.class)
                .lt(AuditLog::getOperationTime, cutoffDate)
                .delete();
            
            log.info("Archived {} audit logs", deleted);
            
            return deleted;
            
        } catch (Exception e) {
            log.error("Failed to archive audit logs", e);
            throw new RuntimeException("Failed to archive audit logs: " + e.getMessage(), e);
        }
    }
    
    /**
     * 应用查询过滤器（用于 queryLogs）
     */
    private void applyQueryFilters(com.keqi.gress.plugin.api.database.chain.QueryChain<AuditLog> query, AuditLogQueryRequest request) {
        if (request.getOperationType() != null && !request.getOperationType().trim().isEmpty()) {
            query.eq(AuditLog::getOperationType, request.getOperationType());
        }
        
        if (request.getTargetType() != null && !request.getTargetType().trim().isEmpty()) {
            query.eq(AuditLog::getTargetType, request.getTargetType());
        }
        
        if (request.getTargetId() != null && !request.getTargetId().trim().isEmpty()) {
            query.eq(AuditLog::getTargetId, request.getTargetId());
        }
        
        if (request.getOperatorId() != null && !request.getOperatorId().trim().isEmpty()) {
            query.eq(AuditLog::getOperatorId, request.getOperatorId());
        }
        
        if (request.getResult() != null && !request.getResult().trim().isEmpty()) {
            query.eq(AuditLog::getResult, request.getResult());
        }
        
        if (request.getStartTime() != null) {
            LocalDateTime startTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getStartTime()), ZoneId.systemDefault());
            query.ge(AuditLog::getOperationTime, startTime);
        }
        
        if (request.getEndTime() != null) {
            LocalDateTime endTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getEndTime()), ZoneId.systemDefault());
            query.le(AuditLog::getOperationTime, endTime);
        }
        
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            query.and().func(q -> q
                .like(AuditLog::getOperationName, keyword)
                .or()
                .like(AuditLog::getOperationDesc, keyword)
                .or()
                .like(AuditLog::getTargetName, keyword)
            );
        }
    }
    
    /**
     * 应用导出过滤器（用于 exportLogs）
     */
    private void applyExportFilters(com.keqi.gress.plugin.api.database.chain.QueryChain<AuditLog> query, AuditLogExportRequest request) {
        if (request.getOperationType() != null && !request.getOperationType().trim().isEmpty()) {
            query.eq(AuditLog::getOperationType, request.getOperationType());
        }
        
        if (request.getTargetType() != null && !request.getTargetType().trim().isEmpty()) {
            query.eq(AuditLog::getTargetType, request.getTargetType());
        }
        
        if (request.getOperatorId() != null && !request.getOperatorId().trim().isEmpty()) {
            query.eq(AuditLog::getOperatorId, request.getOperatorId());
        }
        
        if (request.getResult() != null && !request.getResult().trim().isEmpty()) {
            query.eq(AuditLog::getResult, request.getResult());
        }
        
        if (request.getStartTime() != null) {
            LocalDateTime startTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getStartTime()), ZoneId.systemDefault());
            query.ge(AuditLog::getOperationTime, startTime);
        }
        
        if (request.getEndTime() != null) {
            LocalDateTime endTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(request.getEndTime()), ZoneId.systemDefault());
            query.le(AuditLog::getOperationTime, endTime);
        }
        
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            query.and().func(q -> q
                .like(AuditLog::getOperationName, keyword)
                .or()
                .like(AuditLog::getOperationDesc, keyword)
                .or()
                .like(AuditLog::getTargetName, keyword)
            );
        }
    }
    
    /**
     * Map AuditLog entity to AuditLogDTO
     */
    private AuditLogDTO mapToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setOperationType(auditLog.getOperationType());
        dto.setOperationName(auditLog.getOperationName());
        dto.setOperationDesc(auditLog.getOperationDesc());
        dto.setTargetType(auditLog.getTargetType());
        dto.setTargetId(auditLog.getTargetId());
        dto.setTargetName(auditLog.getTargetName());
        dto.setOperatorId(auditLog.getOperatorId());
        dto.setOperatorName(auditLog.getOperatorName());
        dto.setOperatorIp(auditLog.getOperatorIp());
        dto.setResult(auditLog.getResult());
        dto.setErrorMessage(auditLog.getErrorMessage());
        
        // Parse JSON fields
        if (auditLog.getBeforeData() != null && !auditLog.getBeforeData().trim().isEmpty()) {
            dto.setBeforeData(JSON.parse(auditLog.getBeforeData()));
        }
        
        if (auditLog.getAfterData() != null && !auditLog.getAfterData().trim().isEmpty()) {
            dto.setAfterData(JSON.parse(auditLog.getAfterData()));
        }
        
        dto.setOperationTime(auditLog.getOperationTime());
        
        return dto;
    }
    
    /**
     * Export logs to CSV format
     */
    private String exportToCSV(List<AuditLogDTO> logs) {
        StringBuilder csv = new StringBuilder();
        
        // CSV header
        csv.append("ID,Operation Type,Operation Name,Target Type,Target ID,Target Name,")
           .append("Operator ID,Operator Name,Result,Operation Time\n");
        
        // CSV rows
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (AuditLogDTO log : logs) {
            csv.append(log.getId()).append(",")
               .append(escapeCsv(log.getOperationType())).append(",")
               .append(escapeCsv(log.getOperationName())).append(",")
               .append(escapeCsv(log.getTargetType())).append(",")
               .append(escapeCsv(log.getTargetId())).append(",")
               .append(escapeCsv(log.getTargetName())).append(",")
               .append(escapeCsv(log.getOperatorId())).append(",")
               .append(escapeCsv(log.getOperatorName())).append(",")
               .append(escapeCsv(log.getResult())).append(",")
               .append(log.getOperationTime() != null ? log.getOperationTime().format(formatter) : "")
               .append("\n");
        }
        
        return csv.toString();
    }
    
    /**
     * Export logs to JSON format
     */
    private String exportToJSON(List<AuditLogDTO> logs) {
        return JSON.toJSONString(logs);
    }
    
    /**
     * Escape CSV field value
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // If value contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
    
}
