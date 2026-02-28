package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 插件表权限管理 Controller
 * 提供插件系统表访问权限配置的管理接口
 */
@Service
@RestController
@RequestMapping("/plugins/appstore-admin/table-permissions")
public class PluginTablePermissionController {

    private static final Log log = LogFactory.get(PluginTablePermissionController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginTablePermissionService permissionService;
    
    /**
     * 获取所有权限配置
     */
    @GetMapping
    public Result<List<PluginTablePermissionDTO>> listAll(
            @RequestParam(required = false) String pluginId,
            @RequestParam(required = false) String tableName) {
        log.info("GET /plugins/appstore-admin/table-permissions - pluginId: {}, tableName: {}", pluginId, tableName);
        
        try {
            List<PluginTablePermissionDTO> list;
            if (pluginId != null && !pluginId.trim().isEmpty()) {
                list = permissionService.listByPluginId(pluginId);
            } else if (tableName != null && !tableName.trim().isEmpty()) {
                list = permissionService.listByTableName(tableName);
            } else {
                list = permissionService.listAll();
            }
            return Result.success(list);
        } catch (Exception e) {
            log.error("Failed to list table permissions", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询权限配置
     */
    @GetMapping("/{id}")
    public Result<PluginTablePermissionDTO> getById(@PathVariable Long id) {
        log.info("GET /plugins/appstore-admin/table-permissions/{}", id);
        
        try {
            PluginTablePermissionDTO dto = permissionService.getById(id);
            return dto != null ? Result.success(dto) : Result.error("权限配置不存在");
        } catch (Exception e) {
            log.error("Failed to get table permission by id: {}", id, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建权限配置
     */
    @PostMapping
    public Result<PluginTablePermissionDTO> create(@RequestBody PluginTablePermissionDTO dto) {
        log.info("POST /plugins/appstore-admin/table-permissions - pluginId: {}, tableName: {}", 
                dto.getPluginId(), dto.getTableName());
        
        try {
            PluginTablePermissionDTO created = permissionService.create(dto);
            return Result.success(created);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to create table permission", e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新权限配置
     */
    @PutMapping("/{id}")
    public Result<PluginTablePermissionDTO> update(
            @PathVariable Long id,
            @RequestBody PluginTablePermissionDTO dto) {
        log.info("PUT /plugins/appstore-admin/table-permissions/{}", id);
        
        try {
            PluginTablePermissionDTO updated = permissionService.update(id, dto);
            return Result.success(updated);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to update table permission: {}", id, e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除权限配置
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("DELETE /plugins/appstore-admin/table-permissions/{}", id);
        
        try {
            permissionService.delete(id);
            return Result.success();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to delete table permission: {}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用权限配置
     */
    @PutMapping("/{id}/enabled")
    public Result<PluginTablePermissionDTO> setEnabled(
            @PathVariable Long id,
            @RequestParam Boolean enabled) {
        log.info("PUT /plugins/appstore-admin/table-permissions/{}/enabled - enabled: {}", id, enabled);
        
        try {
            PluginTablePermissionDTO updated = permissionService.setEnabled(id, enabled);
            return Result.success(updated);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to set enabled for table permission: {}", id, e);
            return Result.error("操作失败: " + e.getMessage());
        }
    }
}

