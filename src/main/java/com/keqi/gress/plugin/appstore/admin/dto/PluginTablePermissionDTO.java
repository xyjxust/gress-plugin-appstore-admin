package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 插件系统表访问权限配置 DTO
 */
@Data
public class PluginTablePermissionDTO {
    
    private Long id;
    
    private String pluginId;
    
    private String tableName;
    
    private String allowedOperations;
    
    private Boolean isReadonly;
    
    private String description;
    
    private Boolean enabled;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    /**
     * 允许的操作集合（用于前端展示）
     */
    private Set<String> allowedOperationsSet;
}

