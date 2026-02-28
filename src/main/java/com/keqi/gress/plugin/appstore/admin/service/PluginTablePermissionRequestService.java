package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.appstore.admin.dto.ApprovePermissionRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionRequestDTO;
import com.keqi.gress.plugin.appstore.admin.dto.RejectPermissionRequest;

import java.util.List;

/**
 * 插件系统表访问权限申请服务接口
 */
public interface PluginTablePermissionRequestService {
    
    /**
     * 根据ID查询申请
     * 
     * @param id 申请ID
     * @return 申请信息
     */
    PluginTablePermissionRequestDTO getById(Long id);
    
    /**
     * 查询待审核的申请列表
     * 
     * @return 待审核申请列表
     */
    List<PluginTablePermissionRequestDTO> listPendingRequests();
    
    /**
     * 查询所有申请（支持筛选）
     * 
     * @param pluginId 插件ID（可选）
     * @param status 状态（可选）
     * @return 申请列表
     */
    List<PluginTablePermissionRequestDTO> listAll(String pluginId, String status);
    
    /**
     * 批准申请
     * 
     * @param id 申请ID
     * @param request 批准请求
     * @return 更新后的申请信息
     */
    PluginTablePermissionRequestDTO approveRequest(Long id, ApprovePermissionRequest request);
    
    /**
     * 拒绝申请
     * 
     * @param id 申请ID
     * @param request 拒绝请求
     * @return 更新后的申请信息
     */
    PluginTablePermissionRequestDTO rejectRequest(Long id, RejectPermissionRequest request);
}

