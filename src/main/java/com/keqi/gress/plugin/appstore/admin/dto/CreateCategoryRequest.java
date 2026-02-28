package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Category Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryRequest {
    
    private String categoryName;
    
    private String categoryKey;
    
    private String description;
    
    private String icon;
    
    private Integer displayOrder;
    
    private Boolean enabled;
}
