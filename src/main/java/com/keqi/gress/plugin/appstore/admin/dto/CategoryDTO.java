package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Category Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    
    private Long id;
    
    private String categoryName;
    
    private String categoryKey;
    
    private String description;
    
    private String icon;
    
    private Integer displayOrder;
    
    private Boolean enabled;
    
    private Integer pluginCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
