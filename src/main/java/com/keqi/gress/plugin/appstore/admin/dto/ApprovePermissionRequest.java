package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.Data;

/**
 * 批准权限申请请求
 */
@Data
public class ApprovePermissionRequest {
    
    /** 审核意见（可选） */
    private String comment;
    
    /** 审核人ID */
    private String reviewerId;
    
    /** 审核人姓名 */
    private String reviewerName;
}

