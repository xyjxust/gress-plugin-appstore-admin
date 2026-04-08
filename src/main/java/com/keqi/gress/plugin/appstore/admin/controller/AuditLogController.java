package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogDTO;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogExportRequest;
import com.keqi.gress.plugin.appstore.admin.dto.AuditLogQueryRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.service.AuditLogService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Audit Log Controller
 * REST API for audit log management
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private static  final Log log = LogFactory.get(AuditLogController.class);
    
    @Inject
    private AuditLogService auditLogService;
    
    /**
     * Query audit logs with filtering and pagination
     *
     * @param page Page number (1-based), default 1
     * @param size Page size, default 20
     * @param operationType Filter by operation type
     * @param targetType Filter by target type
     * @param targetId Filter by target ID
     * @param operatorId Filter by operator ID
     * @param result Filter by operation result (SUCCESS/FAILURE)
     * @param startTime Filter by start time (timestamp in milliseconds)
     * @param endTime Filter by end time (timestamp in milliseconds)
     * @param keyword Search keyword
     * @return Paginated list of audit logs
     */
    @GetMapping
    public Result<PageResult<AuditLogDTO>> queryLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String targetId,
            @RequestParam(required = false) String operatorId,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam(required = false) String keyword) {
        

        

            // Validate page and size
            if (page < 1) {
                return Result.error("Page number must be greater than 0");
            }
            if (size < 1 || size > 100) {
                return Result.error("Page size must be between 1 and 100");
            }
            
            // Build query request
            AuditLogQueryRequest request = AuditLogQueryRequest.builder()
                    .page(page)
                    .size(size)
                    .operationType(operationType)
                    .targetType(targetType)
                    .targetId(targetId)
                    .operatorId(operatorId)
                    .result(result)
                    .startTime(startTime)
                    .endTime(endTime)
                    .keyword(keyword)
                    .build();
            
            // Query logs
            PageResult<AuditLogDTO> logs = auditLogService.queryLogs(request);
            
            return Result.success(logs);
    }
    
    /**
     * Get audit log detail by ID
     *
     * @param id Audit log ID
     * @return Audit log detail
     */
    @GetMapping("/{id}")
    public Result<AuditLogDTO> getLogDetail(@PathVariable Long id) {

            AuditLogDTO logDetail = auditLogService.getLogDetail(id);
            
            if (logDetail == null) {
                return Result.error("Audit log not found: " + id);
            }
            
            return Result.success(logDetail);

    }
    
    /**
     * Export audit logs
     *
     * @param format Export format (CSV, JSON, EXCEL)
     * @param operationType Filter by operation type
     * @param targetType Filter by target type
     * @param operatorId Filter by operator ID
     * @param result Filter by operation result
     * @param startTime Filter by start time (timestamp in milliseconds)
     * @param endTime Filter by end time (timestamp in milliseconds)
     * @param keyword Search keyword
     * @return Exported data file
     */
    @GetMapping("/export")
    public ResponseEntity<?> exportLogs(
            @RequestParam(defaultValue = "JSON") String format,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) String operatorId,
            @RequestParam(required = false) String result,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime,
            @RequestParam(required = false) String keyword) {
        

            // Validate format
            String exportFormat = format.toUpperCase();
            if (!exportFormat.equals("CSV") && !exportFormat.equals("JSON") && !exportFormat.equals("EXCEL")) {
                return ResponseEntity.badRequest()
                        .body(Result.error("Invalid export format. Supported: CSV, JSON, EXCEL"));
            }
            
            // Build export request
            AuditLogExportRequest request = AuditLogExportRequest.builder()
                    .format(exportFormat)
                    .operationType(operationType)
                    .targetType(targetType)
                    .operatorId(operatorId)
                    .result(result)
                    .startTime(startTime)
                    .endTime(endTime)
                    .keyword(keyword)
                    .build();
            
            // Export logs
            String exportedData = auditLogService.exportLogs(request);
            
            // Determine content type and filename
            String contentType;
            String filename;
            switch (exportFormat) {
                case "CSV":
                    contentType = "text/csv";
                    filename = "audit_logs_" + System.currentTimeMillis() + ".csv";
                    break;
                case "JSON":
                    contentType = "application/json";
                    filename = "audit_logs_" + System.currentTimeMillis() + ".json";
                    break;
                case "EXCEL":
                    contentType = "application/vnd.ms-excel";
                    filename = "audit_logs_" + System.currentTimeMillis() + ".csv";
                    break;
                default:
                    contentType = "text/plain";
                    filename = "audit_logs_" + System.currentTimeMillis() + ".txt";
            }
            
            // Return file response
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(exportedData);
    }
    
    /**
     * Archive old audit logs
     *
     * @param daysToKeep Number of days to keep logs (default 90)
     * @return Number of logs archived
     */
    @PostMapping("/archive")
    public Result<Integer> archiveLogs(@RequestParam(defaultValue = "90") Integer daysToKeep) {

            // Validate daysToKeep
            if (daysToKeep < 1) {
                return Result.error("Days to keep must be greater than 0");
            }
            
            if (daysToKeep < 30) {
                return Result.error("Days to keep must be at least 30 for safety");
            }
            
            // Archive logs
            int archivedCount = auditLogService.archiveLogs(daysToKeep);
            
            log.info("Archived {} audit logs", archivedCount);
            
            return Result.success(archivedCount);
            

    }
}
