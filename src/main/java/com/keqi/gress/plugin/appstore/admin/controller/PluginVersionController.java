package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.PluginVersionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginVersionService pluginVersionService;
    
    /**
     * Get all versions of a plugin
     * 
     * @param pluginId Plugin ID
     * @return List of plugin versions
     */
    @GetMapping("/{pluginId}/versions")
    public Result<List<PluginVersionDTO>> getVersions(@PathVariable String pluginId) {
        try {
            List<PluginVersionDTO> versions = pluginVersionService.getVersions(pluginId);
            return Result.success(versions);
            
        } catch (Exception e) {
            return Result.error("获取版本列表失败: " + e.getMessage());
        }
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
        try {
            PluginVersionDTO versionDetail = pluginVersionService.getVersionDetail(pluginId, version);
            
            if (versionDetail == null) {
                return Result.error("版本不存在");
            }
            
            return Result.success(versionDetail);
            
        } catch (Exception e) {
            return Result.error("获取版本详情失败: " + e.getMessage());
        }
    }
    
    /**
     * Get current version of a plugin
     * 
     * @param pluginId Plugin ID
     * @return Current version
     */
    @GetMapping("/{pluginId}/versions/current")
    public Result<PluginVersionDTO> getCurrentVersion(@PathVariable String pluginId) {
        try {
            PluginVersionDTO currentVersion = pluginVersionService.getCurrentVersion(pluginId);
            
            if (currentVersion == null) {
                return Result.error("未找到当前版本");
            }
            
            return Result.success(currentVersion);
            
        } catch (Exception e) {
            return Result.error("获取当前版本失败: " + e.getMessage());
        }
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
    public Result<Void> setCurrentVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestBody SetCurrentVersionRequest request) {
        try {
            // Validate request
            if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
                return Result.error("操作人ID不能为空");
            }
            
            pluginVersionService.setCurrentVersion(pluginId, version, request);
            
            Result<Void> result = Result.success();
//            result.setMessage("版本切换成功");
            return result;
            
        } catch (Exception e) {
            return Result.error("版本切换失败: " + e.getMessage());
        }
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
    public Result<Void> rollbackVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestBody RollbackVersionRequest request) {
        try {
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
            
        } catch (Exception e) {
            return Result.error("版本回滚失败: " + e.getMessage());
        }
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
    public Result<Void> deleteVersion(
            @PathVariable String pluginId,
            @PathVariable String version,
            @RequestParam String operatorId) {
        try {
            // Validate request
            if (operatorId == null || operatorId.trim().isEmpty()) {
                return Result.error("操作人ID不能为空");
            }
            
            pluginVersionService.deleteVersion(pluginId, version, operatorId);
            
            Result<Void> result = Result.success();
//            result.setMessage("版本删除成功");
            return result;
            
        } catch (Exception e) {
            return Result.error("版本删除失败: " + e.getMessage());
        }
    }
}
