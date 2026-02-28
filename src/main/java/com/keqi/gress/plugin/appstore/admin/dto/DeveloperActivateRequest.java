package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Developer Activate Request
 * Request to activate a suspended developer account
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperActivateRequest {
    
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
