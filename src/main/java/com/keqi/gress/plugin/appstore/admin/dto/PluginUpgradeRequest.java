package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.keqi.gress.plugin.appstore.admin.support.OperatorAwareRequest;

/**
 * Plugin Upgrade Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginUpgradeRequest implements OperatorAwareRequest {
    
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * New version number (e.g., 1.2.0)
     */
    private String version;
    
    /**
     * Update notes/changelog
     */
    private String updateNotes;
    
    /**
     * Auto list to store after upgrade
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
