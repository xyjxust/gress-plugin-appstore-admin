package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.Data;

/**
 * 拒绝权限申请请求
 */
@Data
public class RejectPermissionRequest {
    
    /** 拒绝原因（必填） */
    private String reason;
    
    /** 审核人ID */
    private String reviewerId;
    
    /** 审核人姓名 */
    private String reviewerName;
}

