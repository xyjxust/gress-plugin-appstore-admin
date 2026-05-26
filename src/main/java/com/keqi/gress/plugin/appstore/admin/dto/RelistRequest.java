package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.OperatorAwareRequest;

/**
 * Relist Request
 * Request to relist a delisted plugin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelistRequest implements OperatorAwareRequest {
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
    
    /**
     * Comment (optional)
     */
    private String comment;
}
