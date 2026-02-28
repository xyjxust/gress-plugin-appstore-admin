package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量分类调整请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchCategoryUpdateRequest {
    
    /**
     * 要更新的插件ID列表
     */
    private List<String> pluginIds;
    
    /**
     * 操作人ID
     */
    private String operatorId;
    
    /**
     * 操作人名称
     */
    private String operatorName;
    
    /**
     * 新的分类
     */
    private String category;
    
    /**
     * 新的标签列表
     */
    private List<String> tags;
}
