package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 插件系统表访问权限申请表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("as_admin_plugin_table_permission_request")
public class SysPluginTablePermissionRequest extends BaseEntity {
    /** 插件ID */
    @TableField("plugin_id")
    private String pluginId;
    
    /** 系统表名（小写） */
    @TableField("table_name")
    private String tableName;
    
    /** 申请的操作：SELECT,INSERT,UPDATE,DELETE，多个用逗号分隔 */
    @TableField("requested_operations")
    private String requestedOperations;
    
    /** 是否只读（1:只读 0:可写） */
    @TableField("is_readonly")
    private Boolean isReadonly;
    
    /** 申请原因 */
    @TableField("reason")
    private String reason;
    
    /** 权限描述 */
    @TableField("description")
    private String description;
    
    /** 状态：PENDING-待审核, APPROVED-已批准, REJECTED-已拒绝, CANCELLED-已取消 */
    @TableField("status")
    private String status;
    
    /** 申请人ID */
    @TableField("applicant_id")
    private String applicantId;
    
    /** 申请人姓名 */
    @TableField("applicant_name")
    private String applicantName;
    
    /** 审核人ID */
    @TableField("reviewer_id")
    private String reviewerId;
    
    /** 审核人姓名 */
    @TableField("reviewer_name")
    private String reviewerName;
    
    /** 审核意见 */
    @TableField("review_comment")
    private String reviewComment;
    
    /** 审核时间 */
    @TableField("review_time")
    private LocalDateTime reviewTime;
    
    /**
     * 检查申请状态是否为待审核
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    /**
     * 检查申请状态是否为已批准
     */
    public boolean isApproved() {
        return "APPROVED".equals(status);
    }
    
    /**
     * 检查申请状态是否为已拒绝
     */
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
    
    /**
     * 检查申请状态是否为已取消
     */
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
}
