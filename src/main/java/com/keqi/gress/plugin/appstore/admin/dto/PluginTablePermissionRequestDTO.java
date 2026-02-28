package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 插件系统表访问权限申请 DTO
 */
@Data
public class PluginTablePermissionRequestDTO {
    
    private Long id;
    
    private String pluginId;
    
    private String tableName;
    
    private String requestedOperations;
    
    private Boolean isReadonly;
    
    private String reason;
    
    private String description;
    
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED
    
    private String applicantId;
    
    private String applicantName;
    
    private String reviewerId;
    
    private String reviewerName;
    
    private String reviewComment;
    
    private LocalDateTime reviewTime;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}

