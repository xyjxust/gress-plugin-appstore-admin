package com.keqi.gress.plugin.appstore.admin.service.impl;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.entity.SysPluginTablePermission;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 插件系统表访问权限配置服务实现
 */
@Service
public class PluginTablePermissionServiceImpl implements PluginTablePermissionService {
    
    private static final Log log = LogFactory.get(PluginTablePermissionServiceImpl.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Override
    public PluginTablePermissionDTO getById(Long id) {
        SysPluginTablePermission entity = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .one();
        return entity != null ? convertToDTO(entity) : null;
    }
    
    @Override
    public PluginTablePermissionDTO getByPluginIdAndTableName(String pluginId, String tableName) {
        if (StrUtil.isBlank(pluginId) || StrUtil.isBlank(tableName)) {
            return null;
        }
        SysPluginTablePermission entity = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getPluginId, pluginId)
                .eq(SysPluginTablePermission::getTableName, tableName.toLowerCase())
                .eq(SysPluginTablePermission::getEnabled, true)
                .one();
        return entity != null ? convertToDTO(entity) : null;
    }
    
    @Override
    public List<PluginTablePermissionDTO> listByPluginId(String pluginId) {
        if (StrUtil.isBlank(pluginId)) {
            return List.of();
        }
        List<SysPluginTablePermission> entities = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getPluginId, pluginId)
                .eq(SysPluginTablePermission::getEnabled, true)
                .orderByAsc(SysPluginTablePermission::getTableName)
                .list();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginTablePermissionDTO> listByTableName(String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return List.of();
        }
        List<SysPluginTablePermission> entities = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getTableName, tableName.toLowerCase())
                .eq(SysPluginTablePermission::getEnabled, true)
                .orderByAsc(SysPluginTablePermission::getPluginId)
                .list();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginTablePermissionDTO> listAll() {
        List<SysPluginTablePermission> entities = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .list();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public PluginTablePermissionDTO create(PluginTablePermissionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("权限配置不能为空");
        }
        if (StrUtil.isBlank(dto.getPluginId())) {
            throw new IllegalArgumentException("插件ID不能为空");
        }
        if (StrUtil.isBlank(dto.getTableName())) {
            throw new IllegalArgumentException("表名不能为空");
        }
        
        // 检查是否已存在
        SysPluginTablePermission existing = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getPluginId, dto.getPluginId())
                .eq(SysPluginTablePermission::getTableName, dto.getTableName().toLowerCase())
                .one();
        if (existing != null) {
            throw new IllegalArgumentException(
                    String.format("插件 %s 对表 %s 的权限配置已存在", dto.getPluginId(), dto.getTableName()));
        }
        
        SysPluginTablePermission entity = convertToEntity(dto);
        entity.setTableName(entity.getTableName().toLowerCase());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        if (entity.getEnabled() == null) {
            entity.setEnabled(true);
        }
        if (entity.getIsReadonly() == null) {
            entity.setIsReadonly(true);
        }
        
        dataSource.insert(entity);
        log.info("创建插件表权限配置: pluginId={}, tableName={}", entity.getPluginId(), entity.getTableName());
        
        return convertToDTO(entity);
    }
    
    @Override
    public PluginTablePermissionDTO update(Long id, PluginTablePermissionDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        if (dto == null) {
            throw new IllegalArgumentException("权限配置不能为空");
        }
        
        SysPluginTablePermission existing = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .one();
        if (existing == null) {
            throw new IllegalArgumentException("权限配置不存在: " + id);
        }
        
        // 如果修改了插件ID或表名，需要检查是否冲突
        if (StrUtil.isNotBlank(dto.getPluginId()) && !dto.getPluginId().equals(existing.getPluginId())) {
            SysPluginTablePermission conflict = dataSource.lambdaQuery(SysPluginTablePermission.class)
                    .eq(SysPluginTablePermission::getPluginId, dto.getPluginId())
                    .eq(SysPluginTablePermission::getTableName, 
                            StrUtil.isNotBlank(dto.getTableName()) ? dto.getTableName().toLowerCase() : existing.getTableName())
                    .one();
            if (conflict != null && !conflict.getId().equals(id)) {
                throw new IllegalArgumentException(
                        String.format("插件 %s 对表 %s 的权限配置已存在", dto.getPluginId(), dto.getTableName()));
            }
        }
        
        // 更新字段
        if (StrUtil.isNotBlank(dto.getPluginId())) {
            existing.setPluginId(dto.getPluginId());
        }
        if (StrUtil.isNotBlank(dto.getTableName())) {
            existing.setTableName(dto.getTableName().toLowerCase());
        }
        if (dto.getAllowedOperations() != null) {
            existing.setAllowedOperations(dto.getAllowedOperations());
        }
        if (dto.getIsReadonly() != null) {
            existing.setIsReadonly(dto.getIsReadonly());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }
        if (dto.getEnabled() != null) {
            existing.setEnabled(dto.getEnabled());
        }
        existing.setUpdateTime(LocalDateTime.now());
        if (StrUtil.isNotBlank(dto.getUpdateBy())) {
            existing.setUpdateBy(dto.getUpdateBy());
        }
        
        dataSource.lambdaUpdate(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .set(SysPluginTablePermission::getPluginId, existing.getPluginId())
                .set(SysPluginTablePermission::getTableName, existing.getTableName())
                .set(SysPluginTablePermission::getAllowedOperations, existing.getAllowedOperations())
                .set(SysPluginTablePermission::getIsReadonly, existing.getIsReadonly())
                .set(SysPluginTablePermission::getDescription, existing.getDescription())
                .set(SysPluginTablePermission::getEnabled, existing.getEnabled())
                .set(SysPluginTablePermission::getUpdateTime, existing.getUpdateTime())
                .set(SysPluginTablePermission::getUpdateBy, existing.getUpdateBy())
                .update();
        
        log.info("更新插件表权限配置: id={}, pluginId={}, tableName={}", 
                id, existing.getPluginId(), existing.getTableName());
        
        return convertToDTO(existing);
    }
    
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        
        SysPluginTablePermission existing = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .one();
        if (existing == null) {
            throw new IllegalArgumentException("权限配置不存在: " + id);
        }
        
        dataSource.lambdaUpdate(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .delete();
        
        log.info("删除插件表权限配置: id={}, pluginId={}, tableName={}", 
                id, existing.getPluginId(), existing.getTableName());
    }
    
    @Override
    public PluginTablePermissionDTO setEnabled(Long id, Boolean enabled) {
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        if (enabled == null) {
            throw new IllegalArgumentException("启用状态不能为空");
        }
        
        SysPluginTablePermission existing = dataSource.lambdaQuery(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .one();
        if (existing == null) {
            throw new IllegalArgumentException("权限配置不存在: " + id);
        }
        
        existing.setEnabled(enabled);
        existing.setUpdateTime(LocalDateTime.now());
        
        dataSource.lambdaUpdate(SysPluginTablePermission.class)
                .eq(SysPluginTablePermission::getId, id)
                .set(SysPluginTablePermission::getEnabled, enabled)
                .set(SysPluginTablePermission::getUpdateTime, LocalDateTime.now())
                .update();
        
        log.info("{}插件表权限配置: id={}, pluginId={}, tableName={}", 
                enabled ? "启用" : "禁用", id, existing.getPluginId(), existing.getTableName());
        
        return convertToDTO(existing);
    }
    
    /**
     * 实体转DTO
     */
    private PluginTablePermissionDTO convertToDTO(SysPluginTablePermission entity) {
        if (entity == null) {
            return null;
        }
        PluginTablePermissionDTO dto = new PluginTablePermissionDTO();
        dto.setId(entity.getId());
        dto.setPluginId(entity.getPluginId());
        dto.setTableName(entity.getTableName());
        dto.setAllowedOperations(entity.getAllowedOperations());
        dto.setIsReadonly(entity.getIsReadonly());
        dto.setDescription(entity.getDescription());
        dto.setEnabled(entity.getEnabled());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreateBy(entity.getCreateBy());
        dto.setUpdateBy(entity.getUpdateBy());
        dto.setAllowedOperationsSet(entity.getAllowedOperationsSet());
        return dto;
    }
    
    /**
     * DTO转实体
     */
    private SysPluginTablePermission convertToEntity(PluginTablePermissionDTO dto) {
        if (dto == null) {
            return null;
        }
        SysPluginTablePermission entity = new SysPluginTablePermission();
        entity.setId(dto.getId());
        entity.setPluginId(dto.getPluginId());
        entity.setTableName(dto.getTableName());
        entity.setAllowedOperations(dto.getAllowedOperations());
        entity.setIsReadonly(dto.getIsReadonly());
        entity.setDescription(dto.getDescription());
        entity.setEnabled(dto.getEnabled());
        entity.setCreateTime(dto.getCreateTime());
        entity.setUpdateTime(dto.getUpdateTime());
        entity.setCreateBy(dto.getCreateBy());
        entity.setUpdateBy(dto.getUpdateBy());
        return entity;
    }
}

