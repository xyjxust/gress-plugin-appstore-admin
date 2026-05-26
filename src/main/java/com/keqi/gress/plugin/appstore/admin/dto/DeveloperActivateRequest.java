package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.OperatorAwareRequest;

/**
 * Developer Activate Request
 * Request to activate a suspended developer account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperActivateRequest implements OperatorAwareRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Reason for activation
     */
    private String reason;
}
