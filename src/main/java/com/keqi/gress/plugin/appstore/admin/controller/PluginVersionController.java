package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.PluginVersionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Plugin Version Controller
 * Handles plugin version management operations
 * 
 * @author Gress Team
 */
@Service
@RestController
@RequestMapping("/plugins")
public class PluginVersionController {
    private final static Log log = LogFactory.get(PluginVersionController.class);
    
    @Autowired
    private PluginVersionService pluginVersionService;
    
    /**
     * Get all versions of a plugin
     * 
     * @param pluginId Plugin ID
     * @return List of plugin versions
     */
    @GetMapping("/{pluginId}/versions")
    public Result<List<PluginVersionDTO>> getVersions(@PathVariable String pluginId) {

            List<PluginVersionDTO> versions = pluginVersionService.getVersions(pluginId);
            return Result.success(versions);

    }
    
    /**
     * Get version detail
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @return Version detail
     */
    @GetMapping("/{pluginId}/versions/{version}")
    public Result<PluginVersionDTO> getVersionDetail(
            @PathVariable String pluginId,
            @PathVariable String version) {

            PluginVersionDTO versionDetail = pluginVersionService.getVersionDetail(pluginId, version);
            
            if (versionDetail == null) {
                return Result.error("版本不存在");
            }
            
            return Result.success(versionDetail);

    }
    
    /**
     * Get current version of a plugin
     * 
     * @param pluginId Plugin ID
     * @return Current version
     */
    @GetMapping("/{pluginId}/versions/current")
    public Result<PluginVersionDTO> getCurrentVersion(@PathVariable String pluginId) {

            PluginVersionDTO currentVersion = pluginVersionService.getCurrentVersion(pluginId);
            
            if (currentVersion == null) {
                return Result.error("未找到当前版本");
            }
            
            return Result.success(currentVersion);

    }
    
    /**
     * Set current version
     * Validates: Requirements 5.3
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @param request Set current version request
     * @return Result
     */
    @PostMapping("/{pluginId}/versions/{version}/set-current")
    @PluginAction(id = "set-current-version", name = "设为当前版本", managementEnabled = true, actionCode = "MANAGE")
    public Result<Void> setCurrentVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestBody SetCurrentVersionRequest request) {

            // Validate request
            if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
                return Result.error("操作人ID不能为空");
            }
            
            pluginVersionService.setCurrentVersion(pluginId, version, request);
            
            Result<Void> result = Result.success();
//            result.setMessage("版本切换成功");
            return result;

    }
    
    /**
     * Rollback to a specific version
     * Validates: Requirements 5.5
     * 
     * @param pluginId Plugin ID
     * @param version Target version to rollback to
     * @param request Rollback request
     * @return Result
     */
    @PostMapping("/{pluginId}/versions/{version}/rollback")
    @PluginAction(id = "rollback-version", name = "版本回滚", managementEnabled = true, actionCode = "MANAGE")
    public Result<Void> rollbackVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestBody RollbackVersionRequest request) {

            // Validate request
            if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
                return Result.error("操作人ID不能为空");
            }
            
            if (request.getReason() == null || request.getReason().trim().isEmpty()) {
                return Result.error("回滚原因不能为空");
            }
            
            pluginVersionService.rollbackVersion(pluginId, version, request);
            
            Result<Void> result = Result.success();
//            result.setMessage("版本回滚成功");
            return result;

    }
    
    /**
     * Delete a version
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @param operatorId Operator ID
     * @return Result
     */
    @DeleteMapping("/{pluginId}/versions/{version}")
    @PluginAction(id = "delete-version", name = "删除版本", managementEnabled = true, actionCode = "DELETE")
    public Result<Void> deleteVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestParam String operatorId) {

            // Validate request
            if (operatorId == null || operatorId.trim().isEmpty()) {
                return Result.error("操作人ID不能为空");
            }
            
            pluginVersionService.deleteVersion(pluginId, version, operatorId);
            
            Result<Void> result = Result.success();
//            result.setMessage("版本删除成功");
            return result;

    }
}
