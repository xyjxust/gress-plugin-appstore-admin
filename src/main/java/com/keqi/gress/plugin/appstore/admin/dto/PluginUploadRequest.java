package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.OperatorAwareRequest;

/**
 * Plugin Upload Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginUploadRequest implements OperatorAwareRequest {
    
    /**
     * Plugin type (TASK/TRIGGER/APPLICATION)
     */
    private String pluginType;
    
    /**
     * Plugin description
     */
    private String description;

    /**
     * Price type (free/paid)
     */
    private String priceType;
    
    /**
     * Auto list to store after upload
     */
    private Boolean autoList;
    
    /**
     * Operator ID
     */
    private String operatorId;
    
    /**
     * Operator name
     */
    private String operatorName;
}
