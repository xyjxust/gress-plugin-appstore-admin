package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Audit Log Entity
 * Records all administrative operations for audit trail
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_audit_log")
public class AuditLog {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // Operation Information
    
    /**
     * Operation type (e.g., APPROVE_PLUGIN, REJECT_PLUGIN)
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
    
    // Operation Target
    
    /**
     * Target type (e.g., PLUGIN, DEVELOPER, RULE)
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
    
    // Operator
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Operator IP address
     */
    private String operatorIp;
    
    // Operation Result
    
    /**
     * Operation result (SUCCESS/FAILURE)
     */
    private String result;
    
    /**
     * Error message if operation failed
     */
    private String errorMessage;
    
    // Change Content
    
    /**
     * Data before change (stored as JSON in database)
     */
    private String beforeData;
    
    /**
     * Data after change (stored as JSON in database)
     */
    private String afterData;
    
    /**
     * Operation time
     */
    private LocalDateTime operationTime;
}
