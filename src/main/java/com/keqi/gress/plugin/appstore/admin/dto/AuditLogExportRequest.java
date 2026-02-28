package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Audit Log Export Request
 * Request parameters for exporting audit logs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogExportRequest {
    
    /**
     * Export format (CSV, JSON, EXCEL)
     */
    private String format;
    
    /**
     * Filter by operation type
     */
    private String operationType;
    
    /**
     * Filter by target type
     */
    private String targetType;
    
    /**
     * Filter by operator ID
     */
    private String operatorId;
    
    /**
     * Filter by operation result (SUCCESS/FAILURE)
     */
    private String result;
    
    /**
     * Filter by start time (timestamp in milliseconds)
     */
    private Long startTime;
    
    /**
     * Filter by end time (timestamp in milliseconds)
     */
    private Long endTime;
    
    /**
     * Search keyword
     */
    private String keyword;
}
