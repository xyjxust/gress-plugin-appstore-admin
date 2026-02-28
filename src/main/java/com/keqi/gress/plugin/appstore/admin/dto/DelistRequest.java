package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Delist Request
 * Request to delist a plugin from the public store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelistRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Reason for delisting (required)
     */
    private String reason;
}
