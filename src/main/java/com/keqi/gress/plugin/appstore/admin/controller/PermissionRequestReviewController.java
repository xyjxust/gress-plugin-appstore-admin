package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.ApprovePermissionRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionRequestDTO;
import com.keqi.gress.plugin.appstore.admin.dto.RejectPermissionRequest;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限申请审核 Controller
 * 提供权限申请审核管理接口
 */
@Service
@RestController
@RequestMapping("/plugins/as-admin/permission-requests")
@PluginMenu(id = "permission-requests", name = "权限申请审核", managementEnabled = true)
public class PermissionRequestReviewController {

    private static final Log log = LogFactory.get(PermissionRequestReviewController.class);
    
    @Autowired
    private PluginTablePermissionRequestService requestService;
    
    /**
     * 查询待审核的申请列表
     */
    @GetMapping("/pending")
    public Result<List<PluginTablePermissionRequestDTO>> listPendingRequests() {

            List<PluginTablePermissionRequestDTO> list = requestService.listPendingRequests();
            return Result.success(list);

    }
    
    /**
     * 查询所有申请（支持筛选）
     */
    @GetMapping
    @PluginAction(id = "refresh", name = "刷新")
    public Result<List<PluginTablePermissionRequestDTO>> listAll(
            @RequestParam(required = false) String pluginId,
            @RequestParam(required = false) String status) {

            List<PluginTablePermissionRequestDTO> list = requestService.listAll(pluginId, status);
            return Result.success(list);

    }
    
    /**
     * 根据ID查询申请
     */
    @GetMapping("/{id}")
    public Result<PluginTablePermissionRequestDTO> getById(@PathVariable Long id) {

            PluginTablePermissionRequestDTO dto = requestService.getById(id);
            return dto != null ? Result.success(dto) : Result.error("申请不存在");

    }
    
    /**
     * 批准申请
     */
    @PostMapping("/{id}/approve")
    @PluginAction(id = "approve", name = "批准申请", managementEnabled = true, actionCode = "MANAGE")
    public Result<PluginTablePermissionRequestDTO> approveRequest(
            @PathVariable Long id,
            @RequestBody ApprovePermissionRequest request) {

            PluginTablePermissionRequestDTO updated = requestService.approveRequest(id, request);
            return Result.success(updated);

    }
    
    /**
     * 拒绝申请
     */
    @PostMapping("/{id}/reject")
    @PluginAction(id = "reject", name = "拒绝申请", managementEnabled = true, actionCode = "MANAGE")
    public Result<PluginTablePermissionRequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestBody RejectPermissionRequest request) {

            PluginTablePermissionRequestDTO updated = requestService.rejectRequest(id, request);
            return Result.success(updated);

    }
}

