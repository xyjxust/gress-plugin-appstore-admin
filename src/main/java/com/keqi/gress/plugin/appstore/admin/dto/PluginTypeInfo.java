package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 插件类型信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginTypeInfo {
    
    /**
     * 插件类型代码（如 TASK, TRIGGER, APPLICATION, MIDDLEWARE）
     */
    private String code;
    
    /**
     * 插件类型显示名称
     */
    private String label;
    
    /**
     * 类型描述
     */
    private String description;
    
    /**
     * 标签类型（用于 UI 显示，如 info, success, warning, error）
     */
    private String tagType;
    
    /**
     * 图标路径（可选）
     */
    private String icon;
}
