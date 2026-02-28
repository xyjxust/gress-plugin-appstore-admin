package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;

import java.util.List;

/**
 * 插件系统表访问权限配置服务接口
 */
public interface PluginTablePermissionService {
    
    /**
     * 根据ID查询权限配置
     * 
     * @param id 权限ID
     * @return 权限配置
     */
    PluginTablePermissionDTO getById(Long id);
    
    /**
     * 根据插件ID和表名查询权限配置
     * 
     * @param pluginId 插件ID
     * @param tableName 表名（小写）
     * @return 权限配置，如果不存在返回 null
     */
    PluginTablePermissionDTO getByPluginIdAndTableName(String pluginId, String tableName);
    
    /**
     * 根据插件ID查询所有权限配置
     * 
     * @param pluginId 插件ID
     * @return 权限配置列表
     */
    List<PluginTablePermissionDTO> listByPluginId(String pluginId);
    
    /**
     * 根据表名查询所有权限配置
     * 
     * @param tableName 表名（小写）
     * @return 权限配置列表
     */
    List<PluginTablePermissionDTO> listByTableName(String tableName);
    
    /**
     * 查询所有权限配置
     * 
     * @return 权限配置列表
     */
    List<PluginTablePermissionDTO> listAll();
    
    /**
     * 创建权限配置
     * 
     * @param dto 权限配置DTO
     * @return 创建后的权限配置
     */
    PluginTablePermissionDTO create(PluginTablePermissionDTO dto);
    
    /**
     * 更新权限配置
     * 
     * @param id 权限ID
     * @param dto 权限配置DTO
     * @return 更新后的权限配置
     */
    PluginTablePermissionDTO update(Long id, PluginTablePermissionDTO dto);
    
    /**
     * 删除权限配置
     * 
     * @param id 权限ID
     */
    void delete(Long id);
    
    /**
     * 启用/禁用权限配置
     * 
     * @param id 权限ID
     * @param enabled 是否启用
     * @return 更新后的权限配置
     */
    PluginTablePermissionDTO setEnabled(Long id, Boolean enabled);
}

