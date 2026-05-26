package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Tag Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    
    private Long id;
    
    private String tagName;
    
    private String tagKey;
    
    private String description;
    
    private String color;

    private String tagTypeKey;
    
    private Boolean enabled;
    
    private Integer usageCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
