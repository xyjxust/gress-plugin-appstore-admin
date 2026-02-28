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
        log.info("GET /plugins/appstore-admin/permission-requests/pending");
        
        try {
            List<PluginTablePermissionRequestDTO> list = requestService.listPendingRequests();
            return Result.success(list);
        } catch (Exception e) {
            log.error("Failed to list pending requests", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询所有申请（支持筛选）
     */
    @GetMapping
    public Result<List<PluginTablePermissionRequestDTO>> listAll(
            @RequestParam(required = false) String pluginId,
            @RequestParam(required = false) String status) {
        log.info("GET /plugins/appstore-admin/permission-requests - pluginId: {}, status: {}", pluginId, status);
        
        try {
            List<PluginTablePermissionRequestDTO> list = requestService.listAll(pluginId, status);
            return Result.success(list);
        } catch (Exception e) {
            log.error("Failed to list requests", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询申请
     */
    @GetMapping("/{id}")
    public Result<PluginTablePermissionRequestDTO> getById(@PathVariable Long id) {
        log.info("GET /plugins/appstore-admin/permission-requests/{}", id);
        
        try {
            PluginTablePermissionRequestDTO dto = requestService.getById(id);
            return dto != null ? Result.success(dto) : Result.error("申请不存在");
        } catch (Exception e) {
            log.error("Failed to get request by id: {}", id, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 批准申请
     */
    @PostMapping("/{id}/approve")
    public Result<PluginTablePermissionRequestDTO> approveRequest(
            @PathVariable Long id,
            @RequestBody ApprovePermissionRequest request) {
        log.info("POST /plugins/appstore-admin/permission-requests/{}/approve", id);
        
        try {
            PluginTablePermissionRequestDTO updated = requestService.approveRequest(id, request);
            return Result.success(updated);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to approve request: {}", id, e);
            return Result.error("批准失败: " + e.getMessage());
        }
    }
    
    /**
     * 拒绝申请
     */
    @PostMapping("/{id}/reject")
    public Result<PluginTablePermissionRequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestBody RejectPermissionRequest request) {
        log.info("POST /plugins/appstore-admin/permission-requests/{}/reject", id);
        
        try {
            PluginTablePermissionRequestDTO updated = requestService.rejectRequest(id, request);
            return Result.success(updated);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to reject request: {}", id, e);
            return Result.error("拒绝失败: " + e.getMessage());
        }
    }
}

