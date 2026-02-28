package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Audit Log Query Request
 * Request parameters for querying audit logs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogQueryRequest {
    
    /**
     * Page number (1-based)
     */
    private Integer page;
    
    /**
     * Page size
     */
    private Integer size;
    
    /**
     * Filter by operation type
     */
    private String operationType;
    
    /**
     * Filter by target type
     */
    private String targetType;
    
    /**
     * Filter by target ID
     */
    private String targetId;
    
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
     * Search keyword (searches in operation name, description, target name)
     */
    private String keyword;
}
