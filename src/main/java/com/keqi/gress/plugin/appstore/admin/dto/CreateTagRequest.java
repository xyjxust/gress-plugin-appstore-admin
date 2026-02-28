package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Tag Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequest {
    
    private String tagName;
    
    private String tagKey;
    
    private String description;
    
    private String color;
    
    private Boolean enabled;
}
