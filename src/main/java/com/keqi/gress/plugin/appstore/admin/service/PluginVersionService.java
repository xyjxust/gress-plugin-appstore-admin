package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginVersion;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Plugin Version Service
 * Handles plugin version management operations
 * 
 * @author Gress Team
 */
@Service
@Slf4j
public class PluginVersionService {
    
    @Autowired
    private PluginLambdaDataSource pluginDataSource;
    
    @Autowired
    private AuditLogService auditLogService;

    
    /**
     * Get all versions of a plugin
     * 
     * @param pluginId Plugin ID
     * @return List of plugin versions
     */
    public List<PluginVersionDTO> getVersions(String pluginId) {
        List<PluginVersion> versions = pluginDataSource.lambdaQuery(PluginVersion.class)
            .eq(PluginVersion::getPluginId, pluginId)
            .orderByDesc(PluginVersion::getCreateTime)
            .list();
        
        return versions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get version detail
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @return Version detail
     */
    public PluginVersionDTO getVersionDetail(String pluginId, String version) {
        PluginVersion pluginVersion = pluginDataSource.lambdaQuery(PluginVersion.class)
            .eq(PluginVersion::getPluginId, pluginId)
            .eq(PluginVersion::getVersion, version)
            .one();
        
        return pluginVersion != null ? convertToDTO(pluginVersion) : null;
    }
    
    /**
     * Get current version of a plugin
     * 
     * @param pluginId Plugin ID
     * @return Current version
     */
    public PluginVersionDTO getCurrentVersion(String pluginId) {
        PluginVersion pluginVersion = pluginDataSource.lambdaQuery(PluginVersion.class)
            .eq(PluginVersion::getPluginId, pluginId)
            .eq(PluginVersion::getIsCurrent, true)
            .one();
        
        return pluginVersion != null ? convertToDTO(pluginVersion) : null;
    }
    
    /**
     * Set current version
     * Validates: Requirements 5.3
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @param request Set current version request
     */
    public void setCurrentVersion(String pluginId, String version, SetCurrentVersionRequest request) {
        pluginDataSource.executeTransaction(() -> {
            // 1. Verify the version exists and is approved
            PluginVersion targetVersion = pluginDataSource.lambdaQuery(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, version)
                .one();
            
            if (targetVersion == null) {
                throw new RuntimeException("版本不存在：" + version);
            }
            
            if (!"APPROVED".equals(targetVersion.getStatus())) {
                throw new RuntimeException("只有已批准的版本才能设为当前版本，当前状态：" + targetVersion.getStatus());
            }
            
            // 2. Unset previous current version (if any)
            pluginDataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getIsCurrent, true)
                .set(PluginVersion::getIsCurrent, false)
                .update();
            
            // 3. Set new current version
            pluginDataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, version)
                .set(PluginVersion::getIsCurrent, true)
                .set(PluginVersion::getStatus, "CURRENT")
                .update();
            
            // 4. Update plugin manager table
            pluginDataSource.lambdaUpdate(PluginManager.class)
                .eq(PluginManager::getPluginId, pluginId)
                .set(PluginManager::getCurrentVersion, version)
                .update();
        });
        
        // 5. Record audit log
        Map<String, String> details = new HashMap<>();
        details.put("version", version);
        details.put("comment", request.getComment() != null ? request.getComment() : "");
        
        auditLogService.logSuccess(
            "SET_CURRENT_VERSION",
            "设置当前版本",
            "PLUGIN",
            pluginId,
            pluginId,
            request.getOperatorId(),
            request.getOperatorName(),
            null,
            details
        );
    }
    
    /**
     * Rollback to a specific version
     * Validates: Requirements 5.5
     * 
     * @param pluginId Plugin ID
     * @param targetVersion Target version to rollback to
     * @param request Rollback request
     */
    public void rollbackVersion(String pluginId, String targetVersion, RollbackVersionRequest request) {
        // Get current version before rollback
        PluginVersionDTO currentVersion = getCurrentVersion(pluginId);
        String previousVersion = currentVersion != null ? currentVersion.getVersion() : "unknown";
        
        pluginDataSource.executeTransaction(() -> {
            // 1. Verify target version exists and is approved
            PluginVersion target = pluginDataSource.lambdaQuery(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, targetVersion)
                .one();
            
            if (target == null) {
                throw new RuntimeException("目标版本不存在：" + targetVersion);
            }
            
            String status = target.getStatus();
            if (!"APPROVED".equals(status) && !"CURRENT".equals(status) && !"ARCHIVED".equals(status)) {
                throw new RuntimeException("无法回滚到状态为 " + status + " 的版本");
            }
            
            // 2. Unset current version
            pluginDataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getIsCurrent, true)
                .set(PluginVersion::getIsCurrent, false)
                .set(PluginVersion::getStatus, "ARCHIVED")
                .update();
            
            // 3. Set target version as current
            pluginDataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, targetVersion)
                .set(PluginVersion::getIsCurrent, true)
                .set(PluginVersion::getStatus, "CURRENT")
                .update();
            
            // 4. Update plugin manager table
            pluginDataSource.lambdaUpdate(PluginManager.class)
                .eq(PluginManager::getPluginId, pluginId)
                .set(PluginManager::getCurrentVersion, targetVersion)
                .update();
        });
        
        // 6. Record audit log
        Map<String, String> details = new HashMap<>();
        details.put("previousVersion", previousVersion);
        details.put("targetVersion", targetVersion);
        details.put("reason", request.getReason());
        
        auditLogService.logSuccess(
            "ROLLBACK_VERSION",
            "回滚版本",
            "PLUGIN",
            pluginId,
            pluginId,
            request.getOperatorId(),
            request.getOperatorName(),
            null,
            details
        );
    }
    
    /**
     * Delete a version
     * 
     * @param pluginId Plugin ID
     * @param version Version number
     * @param operatorId Operator ID
     */
    public void deleteVersion(String pluginId, String version, String operatorId) {
        pluginDataSource.executeTransaction(() -> {
            // 1. Check if it's the current version
            PluginVersion pluginVersion = pluginDataSource.lambdaQuery(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, version)
                .one();
            
            if (pluginVersion == null) {
                throw new RuntimeException("版本不存在：" + version);
            }
            
            if (Boolean.TRUE.equals(pluginVersion.getIsCurrent())) {
                throw new RuntimeException("无法删除当前版本，请先设置其他版本为当前版本");
            }
            
            // 2. Delete the version using Lambda
            pluginDataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, version)
                .delete();
        });
        
        // 3. Record audit log
        Map<String, String> details = new HashMap<>();
        details.put("version", version);
        
        auditLogService.logSuccess(
            "DELETE_VERSION",
            "删除版本",
            "PLUGIN",
            pluginId,
            pluginId,
            operatorId,
            operatorId,
            null,
            details
        );
    }
    
    /**
     * Create a new version from submission
     * 
     * @param submissionId Submission ID
     */
    public void createVersionFromSubmission(Long submissionId) {
        pluginDataSource.executeTransaction(() -> {
            // 1. Get submission details using Lambda
            PluginSubmission submission = pluginDataSource.lambdaQuery(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .one();
            
            if (submission == null) {
                throw new RuntimeException("Submission not found: " + submissionId);
            }
            
            String pluginId = submission.getPluginId();
            String version = submission.getVersion();
            
            // 2. Check if version already exists using Lambda
            long count = pluginDataSource.lambdaQuery(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .eq(PluginVersion::getVersion, version)
                .count();
            
            if (count > 0) {
                return;
            }
            
            // 3. Insert new version using Lambda
        PluginVersion newVersion = PluginVersion.builder()
            .pluginId(pluginId)
            .version(version)
                .releaseNotes(submission.getDescription())
                .filePath(submission.getFilePath())
                .fileSize(submission.getFileSize())
            .fileHash(submission.getFileHash())
            .status("APPROVED")
            .isCurrent(false)
            .build();
        newVersion.setCreateTime(LocalDateTime.now());
        newVersion.setUpdateTime(LocalDateTime.now());
            
            pluginDataSource.insert(newVersion);
        });
    }
    
    /**
     * Convert PluginVersion entity to DTO
     */
    private PluginVersionDTO convertToDTO(PluginVersion entity) {
        return PluginVersionDTO.builder()
            .id(entity.getId())
            .pluginId(entity.getPluginId())
            .version(entity.getVersion())
            .releaseNotes(entity.getReleaseNotes())
            .filePath(entity.getFilePath())
            .fileSize(entity.getFileSize())
            .fileHash(entity.getFileHash())
            .status(entity.getStatus())
            .isCurrent(entity.getIsCurrent())
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }
}
