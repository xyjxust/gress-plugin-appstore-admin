package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Relist Request
 * Request to relist a delisted plugin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelistRequest {
    
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
