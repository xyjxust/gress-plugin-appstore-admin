package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Tag Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTagRequest {
    
    private String tagName;
    
    private String description;
    
    private String color;
    
    private Boolean enabled;
}
