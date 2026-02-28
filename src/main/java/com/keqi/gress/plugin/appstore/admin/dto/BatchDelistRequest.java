package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量下架请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDelistRequest {
    
    /**
     * 要下架的插件ID列表
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
     * 下架原因
     */
    private String reason;
}
