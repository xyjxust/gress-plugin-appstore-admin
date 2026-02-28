package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit Log DTO
 * Data transfer object for audit log records
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    
    /**
     * Log ID
     */
    private Long id;
    
    /**
     * Operation type
     */
    private String operationType;
    
    /**
     * Operation name
     */
    private String operationName;
    
    /**
     * Operation description
     */
    private String operationDesc;
    
    /**
     * Target type
     */
    private String targetType;
    
    /**
     * Target ID
     */
    private String targetId;
    
    /**
     * Target name
     */
    private String targetName;
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Operator IP
     */
    private String operatorIp;
    
    /**
     * Operation result
     */
    private String result;
    
    /**
     * Error message
     */
    private String errorMessage;
    
    /**
     * Before data
     */
    private Object beforeData;
    
    /**
     * After data
     */
    private Object afterData;
    
    /**
     * Operation time
     */
    private LocalDateTime operationTime;
}
