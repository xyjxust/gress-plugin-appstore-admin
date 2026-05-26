package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 插件系统表访问权限配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("as_admin_plugin_table_permission")
public class SysPluginTablePermission extends BaseEntity {
    
    /** 插件ID */
    @TableField("plugin_id")
    private String pluginId;
    
    /** 系统表名（小写） */
    @TableField("table_name")
    private String tableName;
    
    /** 允许的操作：SELECT,INSERT,UPDATE,DELETE，多个用逗号分隔 */
    @TableField("allowed_operations")
    private String allowedOperations;
    
    /** 是否只读（1:只读 0:可写） */
    @TableField("is_readonly")
    private Boolean isReadonly;
    
    /** 权限描述 */
    @TableField("description")
    private String description;
    
    /** 是否启用（1:启用 0:禁用） */
    @TableField("enabled")
    private Boolean enabled;
    
    /** 创建人 */
    @TableField("create_by")
    private String createBy;
    
    /** 更新人 */
    @TableField("update_by")
    private String updateBy;
    
    /**
     * 检查权限是否启用
     */
    public boolean isEnabled() {
        return enabled != null && enabled;
    }
    
    /**
     * 检查是否只读
     */
    public boolean isReadonly() {
        return isReadonly != null && isReadonly;
    }
    
    /**
     * 获取允许的操作集合
     */
    public Set<String> getAllowedOperationsSet() {
        if (allowedOperations == null || allowedOperations.trim().isEmpty()) {
            return Set.of();
        }
        return Arrays.stream(allowedOperations.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    }
    
    /**
     * 检查是否允许指定操作
     */
    public boolean isOperationAllowed(String operation) {
        if (operation == null) {
            return false;
        }
        Set<String> allowedOps = getAllowedOperationsSet();
        // 如果没有配置允许的操作，默认允许所有操作（但受 isReadonly 限制）
        if (allowedOps.isEmpty()) {
            return true;
        }
        return allowedOps.contains(operation.toUpperCase());
    }
}
