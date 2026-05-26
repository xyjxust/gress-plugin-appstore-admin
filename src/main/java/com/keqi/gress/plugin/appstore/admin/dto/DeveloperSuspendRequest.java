package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.OperatorAwareRequest;

/**
 * Developer Suspend Request
 * Request to suspend a developer account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperSuspendRequest implements OperatorAwareRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Reason for suspension
     */
    private String reason;
}
