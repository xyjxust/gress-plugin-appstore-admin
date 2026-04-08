package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.ApprovePermissionRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionRequestDTO;
import com.keqi.gress.plugin.appstore.admin.dto.RejectPermissionRequest;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限申请审核 Controller
 * 提供权限申请审核管理接口
 */
@Service
@RestController
@RequestMapping("/plugins/appstore-admin/permission-requests")
public class PermissionRequestReviewController {

    private static final Log log = LogFactory.get(PermissionRequestReviewController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
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
    public Result<PluginTablePermissionRequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestBody RejectPermissionRequest request) {

            PluginTablePermissionRequestDTO updated = requestService.rejectRequest(id, request);
            return Result.success(updated);

    }
}

