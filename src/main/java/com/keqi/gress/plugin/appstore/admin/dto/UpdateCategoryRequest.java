package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Update Category Request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {
    
    private String categoryName;
    
    private String description;
    
    private String icon;
    
    private Integer displayOrder;
    
    private Boolean enabled;
}
