package com.keqi.gress.plugin.appstore.admin.service.impl;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.ApprovePermissionRequest;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionRequestDTO;
import com.keqi.gress.plugin.appstore.admin.dto.RejectPermissionRequest;
import com.keqi.gress.plugin.appstore.admin.entity.SysPluginTablePermissionRequest;
import com.keqi.gress.plugin.appstore.admin.support.RequestActorContextBinder;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionRequestService;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 插件系统表访问权限申请服务实现
 */
@Service
public class PluginTablePermissionRequestServiceImpl implements PluginTablePermissionRequestService {
    
    private static final Log log = LogFactory.get(PluginTablePermissionRequestServiceImpl.class);
    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private PluginTablePermissionService permissionService;
    
    @Override
    public PluginTablePermissionRequestDTO getById(Long id) {
        SysPluginTablePermissionRequest entity = dataSource.lambdaQuery(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getId, id)
                .one();
        return entity != null ? convertToDTO(entity) : null;
    }
    
    @Override
    public List<PluginTablePermissionRequestDTO> listPendingRequests() {
        List<SysPluginTablePermissionRequest> entities = dataSource.lambdaQuery(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getStatus, "PENDING")
                .orderByDesc(SysPluginTablePermissionRequest::getCreateTime)
                .list();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PluginTablePermissionRequestDTO> listAll(String pluginId, String status) {
        var query = dataSource.lambdaQuery(SysPluginTablePermissionRequest.class);
        
        if (StrUtil.isNotBlank(pluginId)) {
            query.eq(SysPluginTablePermissionRequest::getPluginId, pluginId);
        }
        if (StrUtil.isNotBlank(status)) {
            query.eq(SysPluginTablePermissionRequest::getStatus, status);
        }
        
        List<SysPluginTablePermissionRequest> entities = query
                .orderByDesc(SysPluginTablePermissionRequest::getCreateTime)
                .list();
        
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public PluginTablePermissionRequestDTO approveRequest(Long id, ApprovePermissionRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("申请ID不能为空");
        }
        if (request == null) {
            throw new IllegalArgumentException("批准请求不能为空");
        }
        RequestActorContextBinder.bindReviewer(request);
        
        SysPluginTablePermissionRequest entity = dataSource.lambdaQuery(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getId, id)
                .one();
        
        if (entity == null) {
            throw new IllegalArgumentException("申请不存在: " + id);
        }
        
        if (!entity.isPending()) {
            throw new IllegalArgumentException("申请状态不允许审批: " + entity.getStatus());
        }
        
        // 更新申请状态
        entity.setStatus("APPROVED");
        entity.setReviewerId(request.getReviewerId());
        entity.setReviewerName(request.getReviewerName());
        entity.setReviewComment(request.getComment());
        entity.setReviewTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        
        dataSource.lambdaUpdate(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getId, id)
                .set(SysPluginTablePermissionRequest::getStatus, "APPROVED")
                .set(SysPluginTablePermissionRequest::getReviewerId, request.getReviewerId())
                .set(SysPluginTablePermissionRequest::getReviewerName, request.getReviewerName())
                .set(SysPluginTablePermissionRequest::getReviewComment, request.getComment())
                .set(SysPluginTablePermissionRequest::getReviewTime, LocalDateTime.now())
                .set(SysPluginTablePermissionRequest::getUpdateTime, LocalDateTime.now())
                .update();
        
        // 自动创建权限配置
        PluginTablePermissionDTO permission = new PluginTablePermissionDTO();
        permission.setPluginId(entity.getPluginId());
        permission.setTableName(entity.getTableName());
        permission.setAllowedOperations(entity.getRequestedOperations());
        permission.setIsReadonly(entity.getIsReadonly());
        permission.setDescription(entity.getDescription());
        permission.setEnabled(true);
        
        try {
            permissionService.create(permission);
            log.info("批准申请并创建权限配置: requestId={}, pluginId={}, tableName={}", 
                    id, entity.getPluginId(), entity.getTableName());
        } catch (Exception e) {
            log.error("批准申请后创建权限配置失败: requestId={}", id, e);
            // 不抛出异常，因为申请已经批准，权限配置可以后续手动创建
        }
        
        return convertToDTO(entity);
    }
    
    @Override
    public PluginTablePermissionRequestDTO rejectRequest(Long id, RejectPermissionRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("申请ID不能为空");
        }
        if (request == null) {
            throw new IllegalArgumentException("拒绝请求不能为空");
        }
        RequestActorContextBinder.bindReviewer(request);
        if (StrUtil.isBlank(request.getReason())) {
            throw new IllegalArgumentException("拒绝原因不能为空");
        }
        
        SysPluginTablePermissionRequest entity = dataSource.lambdaQuery(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getId, id)
                .one();
        
        if (entity == null) {
            throw new IllegalArgumentException("申请不存在: " + id);
        }
        
        if (!entity.isPending()) {
            throw new IllegalArgumentException("申请状态不允许审批: " + entity.getStatus());
        }
        
        // 更新申请状态
        entity.setStatus("REJECTED");
        entity.setReviewerId(request.getReviewerId());
        entity.setReviewerName(request.getReviewerName());
        entity.setReviewComment(request.getReason());
        entity.setReviewTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        
        dataSource.lambdaUpdate(SysPluginTablePermissionRequest.class)
                .eq(SysPluginTablePermissionRequest::getId, id)
                .set(SysPluginTablePermissionRequest::getStatus, "REJECTED")
                .set(SysPluginTablePermissionRequest::getReviewerId, request.getReviewerId())
                .set(SysPluginTablePermissionRequest::getReviewerName, request.getReviewerName())
                .set(SysPluginTablePermissionRequest::getReviewComment, request.getReason())
                .set(SysPluginTablePermissionRequest::getReviewTime, LocalDateTime.now())
                .set(SysPluginTablePermissionRequest::getUpdateTime, LocalDateTime.now())
                .update();
        
        log.info("拒绝申请: requestId={}, pluginId={}, tableName={}, reason={}", 
                id, entity.getPluginId(), entity.getTableName(), request.getReason());
        
        return convertToDTO(entity);
    }

    /**
     * 实体转DTO
     */
    private PluginTablePermissionRequestDTO convertToDTO(SysPluginTablePermissionRequest entity) {
        if (entity == null) {
            return null;
        }
        PluginTablePermissionRequestDTO dto = new PluginTablePermissionRequestDTO();
        dto.setId(entity.getId());
        dto.setPluginId(entity.getPluginId());
        dto.setTableName(entity.getTableName());
        dto.setRequestedOperations(entity.getRequestedOperations());
        dto.setIsReadonly(entity.getIsReadonly());
        dto.setReason(entity.getReason());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setApplicantId(entity.getApplicantId());
        dto.setApplicantName(entity.getApplicantName());
        dto.setReviewerId(entity.getReviewerId());
        dto.setReviewerName(entity.getReviewerName());
        dto.setReviewComment(entity.getReviewComment());
        dto.setReviewTime(entity.getReviewTime());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
