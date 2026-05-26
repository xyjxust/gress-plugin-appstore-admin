package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 插件表权限管理 Controller
 * 提供插件系统表访问权限配置的管理接口
 */
@Service
@RestController
@RequestMapping("/plugins/as-admin/table-permissions")
@PluginMenu(id = "table-permissions", name = "表权限管理", managementEnabled = true)
public class PluginTablePermissionController {

    private static final Log log = LogFactory.get(PluginTablePermissionController.class);
    
    @Autowired
    private PluginTablePermissionService permissionService;
    
    /**
     * 获取所有权限配置
     */
    @GetMapping
    @PluginAction(id = "refresh", name = "刷新")
    public Result<List<PluginTablePermissionDTO>> listAll(
            @RequestParam(required = false) String pluginId,
            @RequestParam(required = false) String tableName) {

            List<PluginTablePermissionDTO> list;
            if (pluginId != null && !pluginId.trim().isEmpty()) {
                list = permissionService.listByPluginId(pluginId);
            } else if (tableName != null && !tableName.trim().isEmpty()) {
                list = permissionService.listByTableName(tableName);
            } else {
                list = permissionService.listAll();
            }
            return Result.success(list);

    }
    
    /**
     * 根据ID查询权限配置
     */
    @GetMapping("/{id}")
    public Result<PluginTablePermissionDTO> getById(@PathVariable Long id) {

            PluginTablePermissionDTO dto = permissionService.getById(id);
            return dto != null ? Result.success(dto) : Result.error("权限配置不存在");

    }
    
    /**
     * 创建权限配置
     */
    @PostMapping
    @PluginAction(id = "grant", name = "授予权限")
    public Result<PluginTablePermissionDTO> create(@RequestBody PluginTablePermissionDTO dto) {

            PluginTablePermissionDTO created = permissionService.create(dto);
            return Result.success(created);

    }
    
    /**
     * 更新权限配置
     */
    @PutMapping("/{id}")
    public Result<PluginTablePermissionDTO> update(
            @PathVariable Long id,
            @RequestBody PluginTablePermissionDTO dto) {

            PluginTablePermissionDTO updated = permissionService.update(id, dto);
            return Result.success(updated);

    }
    
    /**
     * 删除权限配置
     */
    @DeleteMapping("/{id}")
    @PluginAction(id = "revoke", name = "撤销权限", managementEnabled = true, actionCode = "DELETE")
    public Result<Void> delete(@PathVariable Long id) {

            permissionService.delete(id);
            return Result.success();

    }
    
    /**
     * 启用/禁用权限配置
     */
    @PutMapping("/{id}/enabled")
    public Result<PluginTablePermissionDTO> setEnabled(
            @PathVariable Long id,
            @RequestParam Boolean enabled) {

            PluginTablePermissionDTO updated = permissionService.setEnabled(id, enabled);
            return Result.success(updated);

    }
}

