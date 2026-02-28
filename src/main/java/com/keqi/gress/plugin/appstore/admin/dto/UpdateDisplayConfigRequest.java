package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新展示配置请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDisplayConfigRequest {
    
    /**
     * 配置键
     */
    private String configKey;
    
    /**
     * 配置值（JSON对象）
     */
    private Object configValue;
    
    /**
     * 配置描述
     */
    private String description;
}
