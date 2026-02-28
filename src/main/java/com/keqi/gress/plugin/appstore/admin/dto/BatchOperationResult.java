package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 批量操作结果 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchOperationResult {
    
    /**
     * 操作类型
     */
    private String operationType;
    
    /**
     * 总数
     */
    private int total;
    
    /**
     * 成功数量
     */
    private int successCount;
    
    /**
     * 失败数量
     */
    private int failureCount;
    
    /**
     * 成功的ID列表
     */
    private List<String> successIds;
    
    /**
     * 失败的详情（ID -> 失败原因）
     */
    private Map<String, String> failures;
    
    /**
     * 操作摘要信息
     */
    private String summary;
}
