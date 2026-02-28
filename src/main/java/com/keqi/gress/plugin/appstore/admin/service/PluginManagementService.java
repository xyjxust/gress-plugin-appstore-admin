package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.common.plugin.PluginMetadataParser;
import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginVersion;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Plugin Management Service
 * Handles listed plugin management operations
 */
@Service
public class PluginManagementService {
    
    private static final Log log = LogFactory.get(PluginManagementService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    @Inject(source = Inject.BeanSource.SPRING)
    private FileStorageService fileStorageService;
    
    @Inject
    private PluginDependencyValidationService dependencyValidationService;
    
    /**
     * Get listed plugins with filtering and pagination
     *
     * @param page Page number (1-based)
     * @param size Page size
     * @param status Filter by status (ONLINE/OFFLINE/DELISTED)
     * @param type Filter by plugin type
     * @param keyword Search keyword
     * @return Paginated list of listed plugins
     */
    public PageResult<PluginManagerDTO> getListedPlugins(
            Integer page, Integer size, String status, String type, String keyword) {
        
        log.info("Querying listed plugins - page: {}, size: {}, status: {}, type: {}, keyword: {}", 
                 page, size, status, type, keyword);
        
        // Build query using Lambda
        IPage<PluginManager> pageResult = dataSource.lambdaQuery(PluginManager.class)
            .func(wrapper -> {
                // Apply status filter
                if (status != null && !status.trim().isEmpty()) {
                    wrapper.eq(PluginManager::getStatus, status.toUpperCase());
                }
                
                // Apply plugin type filter
                if (type != null && !type.trim().isEmpty()) {
                    try {
                        // Validate the plugin type
                        PluginType.valueOf(type.toUpperCase());
                        wrapper.eq(PluginManager::getPluginType, type.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        log.warn("Invalid plugin type: {}", type);
                    }
                }
                
                // Apply keyword search
                if (keyword != null && !keyword.trim().isEmpty()) {
                    String keywordPattern = "%" + keyword.trim() + "%";
                    wrapper.and()
                        .like(PluginManager::getPluginName, keywordPattern)
                        .or()
                        .like(PluginManager::getDeveloperName, keywordPattern)
                        .or()
                        .like(PluginManager::getDescription, keywordPattern);
                }
                
                // Order by update time desc
                wrapper.orderByDesc(PluginManager::getUpdateTime);
            })
            .page(page, size);
        
        // Convert to DTOs
        List<PluginManagerDTO> plugins = pageResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        log.info("Found {} listed plugins out of {} total", plugins.size(), pageResult.getTotal());
        
        return PageResult.of(plugins, pageResult.getTotal(), page, size);
    }
    
    /**
     * Get plugin detail by plugin ID
     *
     * @param pluginId Plugin ID
     * @return Plugin detail
     */
    public PluginManagerDTO getPluginDetail(String pluginId) {
        log.info("Getting plugin detail for: {}", pluginId);
        
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .one();
        
        if (plugin == null) {
            log.warn("Plugin not found: {}", pluginId);
            return null;
        }
        
        PluginManagerDTO dto = convertToDTO(plugin);
        
        log.info("Retrieved plugin detail: {}", dto.getPluginName());
        
        return dto;
    }
    
    /**
     * Update plugin basic information
     *
     * @param request Update request
     * @return Updated plugin info
     */
    public PluginManagerDTO updatePlugin(PluginUpdateRequest request) {
        log.info("Updating plugin: {}", request.getPluginId());
        
        // Validate plugin exists
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, request.getPluginId())
            .one();
        
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin not found: " + request.getPluginId());
        }
        
        // Update fields using Lambda Update Chain
        int updated = dataSource.lambdaUpdate(PluginManager.class)
            .eq(PluginManager::getPluginId, request.getPluginId())
            .func(wrapper -> {
                // Update fields if provided
                if (request.getPluginName() != null && !request.getPluginName().trim().isEmpty()) {
                    wrapper.set(PluginManager::getPluginName, request.getPluginName().trim());
                }
                
                if (request.getDescription() != null) {
                    wrapper.set(PluginManager::getDescription, request.getDescription().trim());
                }
                
                if (request.getCategory() != null) {
                    wrapper.set(PluginManager::getCategory, request.getCategory().trim());
                }
                
                if (request.getTags() != null) {
                    wrapper.set(PluginManager::getTags, String.join(",", request.getTags()));
                }
                
                if (request.getIconUrl() != null) {
                    wrapper.set(PluginManager::getIcon, request.getIconUrl().trim());
                }
                
                // Always update timestamp
                wrapper.set(PluginManager::getUpdateTime, LocalDateTime.now());
            })
            .update();
        
        if (updated == 0) {
            throw new IllegalStateException("Failed to update plugin: " + request.getPluginId());
        }
        
        // Record audit log
        auditLogService.logSuccess(
            "UPDATE",
            "更新插件信息",
            "PLUGIN",
            request.getPluginId(),
            request.getPluginName() != null ? request.getPluginName() : plugin.getPluginName(),
            request.getOperatorId(),
            request.getOperatorName(),
            null,
            null
        );
        
        log.info("Plugin updated successfully: {}", request.getPluginId());
        
        // Fetch and return updated plugin
        return getPluginDetail(request.getPluginId());
    }
    
    /**
     * Delist a plugin from the public store
     *
     * @param pluginId Plugin ID
     * @param request Delist request
     */
    public void delistPlugin(String pluginId, DelistRequest request) {
        log.info("Delisting plugin: {}", pluginId);
        
        // Validate request
        if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员ID不能为空");
        }
        if (request.getOperatorName() == null || request.getOperatorName().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员名称不能为空");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("下架原因不能为空");
        }
        
        // Query current plugin
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .one();
        
        if (plugin == null) {
            throw new IllegalArgumentException("插件不存在：" + pluginId);
        }
        
        // Check if already delisted
        if ("DELISTED".equals(plugin.getStatus())) {
            throw new IllegalStateException("插件已下架");
        }
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "pluginId", pluginId,
            "status", plugin.getStatus()
        );
        
        // Update plugin status to DELISTED
        int updated = dataSource.lambdaUpdate(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .set(PluginManager::getStatus, "DELISTED")
            .set(PluginManager::getUpdateTime, LocalDateTime.now())
            .update();
        
        if (updated == 0) {
            throw new RuntimeException("更新插件状态失败");
        }
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "pluginId", pluginId,
            "status", "DELISTED",
            "reason", request.getReason()
        );
        
        // Record audit log
        auditLogService.logSuccess(
            "DELIST_PLUGIN",
            "下架插件",
            "PLUGIN",
            pluginId,
            plugin.getPluginName(),
            request.getOperatorId(),
            request.getOperatorName(),
            beforeData,
            afterData
        );
        
        log.info("Plugin delisted successfully: {}", pluginId);
    }
    
    /**
     * Relist a delisted plugin
     *
     * @param pluginId Plugin ID
     * @param request Relist request
     */
    public void relistPlugin(String pluginId, RelistRequest request) {
        log.info("Relisting plugin: {}", pluginId);
        
        // Validate request
        if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员ID不能为空");
        }
        if (request.getOperatorName() == null || request.getOperatorName().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员名称不能为空");
        }
        
        // Query current plugin
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .one();
        
        if (plugin == null) {
            throw new IllegalArgumentException("插件不存在：" + pluginId);
        }
        
        // Check if currently delisted or offline
        if (!"DELISTED".equals(plugin.getStatus()) && !"OFFLINE".equals(plugin.getStatus())) {
            throw new IllegalStateException("插件未下架，当前状态：" + plugin.getStatus());
        }
        
        // Store before data for audit
        Map<String, Object> beforeData = Map.of(
            "pluginId", pluginId,
            "status", plugin.getStatus()
        );
        
        // Update plugin status to ONLINE
        int updated = dataSource.lambdaUpdate(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .set(PluginManager::getStatus, "ONLINE")
            .set(PluginManager::getUpdateTime, LocalDateTime.now())
            .update();
        
        if (updated == 0) {
            throw new RuntimeException("更新插件状态失败");
        }
        
        // Store after data for audit
        Map<String, Object> afterData = Map.of(
            "pluginId", pluginId,
            "status", "ONLINE",
            "comment", request.getComment() != null ? request.getComment() : ""
        );
        
        // Record audit log
        auditLogService.logSuccess(
            "RELIST_PLUGIN",
            "重新上架插件",
            "PLUGIN",
            pluginId,
            plugin.getPluginName(),
            request.getOperatorId(),
            request.getOperatorName(),
            beforeData,
            afterData
        );
        
        log.info("Plugin relisted successfully: {}", pluginId);
    }
    
    /**
     * Upload plugin package
     *
     * @param jarFile Plugin JAR file
     * @param request Upload request
     * @return Uploaded plugin info
     */
    public PluginManagerDTO uploadPlugin(java.io.File jarFile, PluginUploadRequest request) {
        log.info("Uploading plugin package: {}", jarFile.getName());
        
        // Validate request
        if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员ID不能为空");
        }
        if (request.getOperatorName() == null || request.getOperatorName().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员名称不能为空");
        }
        if (request.getPluginType() == null || request.getPluginType().trim().isEmpty()) {
            throw new IllegalArgumentException("插件类型不能为空");
        }
        
        // Validate file
        if (jarFile == null || !jarFile.exists()) {
            throw new IllegalArgumentException("插件文件不存在");
        }
        if (!jarFile.getName().endsWith(".jar")) {
            throw new IllegalArgumentException("只支持 JAR 格式的插件包");
        }
        
        try {
            // Parse plugin metadata from JAR file
            PluginMetadataParser.PluginMetadata metadata = parsePluginMetadata(jarFile);
            
            // Check if plugin already exists
            long count = dataSource.lambdaQuery(PluginManager.class)
                .eq(PluginManager::getPluginId, metadata.getPluginId())
                .count();
            
            if (count > 0) {
                throw new IllegalStateException("插件已存在：" + metadata.getPluginId());
            }
            
            // Store plugin file
            String storedFilePath = storePluginFile(jarFile, metadata);
            
            // Parse dependencies from JAR file
            String dependenciesJson = PluginDependencyParser.parseDependenciesFromJar(jarFile.getAbsolutePath());
            log.info("解析到依赖信息: pluginId={}, dependencies={}", metadata.getPluginId(), dependenciesJson);
            
            // Validate direct dependencies (only check direct dependencies, not indirect)
            PluginDependencyValidationService.CheckMode checkMode = getDependencyCheckMode();
            PluginDependencyValidationService.DependencyValidationResult validationResult = 
                dependencyValidationService.validateDirectDependencies(dependenciesJson, checkMode);
            
            if (!validationResult.isValid()) {
                // 严格模式下，依赖缺失时拒绝上架
                throw new IllegalStateException(validationResult.getErrorMessage());
            }
            
            // 警告模式下，记录警告信息
            if (validationResult.getWarningMessage() != null && !validationResult.getWarningMessage().isEmpty()) {
                log.warn("插件 {} 的依赖检查警告: {}", metadata.getPluginId(), validationResult.getWarningMessage());
            }
            
            // Determine initial status
            String initialStatus = Boolean.TRUE.equals(request.getAutoList()) ? "ONLINE" : "OFFLINE";
            
            // Calculate file size and hash
            long fileSize = jarFile.length();
            String fileHash = calculateFileHash(jarFile.getAbsolutePath());
            
            LocalDateTime now = LocalDateTime.now();
            
            // Execute insert operations in transaction
            dataSource.executeTransaction(() -> {
                // Insert plugin record using Lambda
                PluginManager pluginManager = PluginManager.builder()
                        .pluginId(metadata.getPluginId())
                        .pluginName(metadata.getPluginName())
                        .pluginType(request.getPluginType().toUpperCase())
                        .currentVersion(metadata.getVersion())
                        .status(initialStatus)
                        .developerId(request.getOperatorId())
                        .developerName(request.getOperatorName())
                        .description(request.getDescription() != null ? request.getDescription() : metadata.getDescription())
                        .icon(metadata.getIcon())
                        .category(metadata.getCategory())
                        .installCount(0)
                        .ratingAverage(0.0)
                        .createTime(now)
                        .updateTime(now)
                        .build();
                
                // Insert using PluginLambdaDataSource
                dataSource.insert(pluginManager);
                
                // Insert version record using Lambda
                PluginVersion pluginVersion = PluginVersion.builder()
                        .pluginId(metadata.getPluginId())
                        .version(metadata.getVersion())
                        .filePath(storedFilePath)
                        .fileSize(fileSize)
                        .fileHash(fileHash)
                        .releaseNotes(request.getDescription() != null ? request.getDescription() : "初始版本")
                        .isCurrent(true)
                        .status(initialStatus)
                        .dependencies(dependenciesJson) // 存储依赖信息
                        .uploadTime(now)
                        .createTime(now)
                        .updateTime(now)
                        .build();
                
                // Insert using PluginLambdaDataSource
                dataSource.insert(pluginVersion);
            });
            
            // Record audit log
            Map<String, Object> auditData = Map.of(
                    "pluginId", metadata.getPluginId(),
                    "pluginName", metadata.getPluginName(),
                    "version", metadata.getVersion(),
                    "pluginType", request.getPluginType(),
                    "status", initialStatus,
                    "autoList", request.getAutoList()
            );
            
            auditLogService.logSuccess(
                    "UPLOAD_PLUGIN",
                    "上传插件",
                    "PLUGIN",
                    metadata.getPluginId(),
                    metadata.getPluginName(),
                    request.getOperatorId(),
                    request.getOperatorName(),
                    null,
                    auditData
            );
            
            log.info("Plugin uploaded successfully: {} ({})", metadata.getPluginName(), metadata.getPluginId());
            
            // Return uploaded plugin info
            return getPluginDetail(metadata.getPluginId());
            
        } catch (Exception e) {
            log.error("Failed to upload plugin: {}", jarFile.getName(), e);
            throw new RuntimeException("上传插件失败：" + e.getMessage(), e);
        }
    }

    /**
     * Upgrade plugin version
     *
     * @param storedFilePath Stored file path from FileStorageService
     * @param request Upgrade request
     * @return Upgraded plugin info
     */
    public PluginManagerDTO upgradePlugin(String storedFilePath, PluginUpgradeRequest request) {
        log.info("Upgrading plugin: {}", request.getPluginId());
        
        // Validate request
        if (request.getOperatorId() == null || request.getOperatorId().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员ID不能为空");
        }
        if (request.getOperatorName() == null || request.getOperatorName().trim().isEmpty()) {
            throw new IllegalArgumentException("操作员名称不能为空");
        }
        if (request.getPluginId() == null || request.getPluginId().trim().isEmpty()) {
            throw new IllegalArgumentException("插件ID不能为空");
        }
        if (request.getUpdateNotes() == null || request.getUpdateNotes().trim().isEmpty()) {
            throw new IllegalArgumentException("更新说明不能为空");
        }
        
        // Validate file path
        if (storedFilePath == null || storedFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        
        try {
            // 1. Download the JAR file from storage to parse metadata
            java.io.File tempJarFile = downloadJarFromStorage(storedFilePath);
            
            // 2. Parse plugin metadata from JAR file to get version
            PluginMetadataParser.PluginMetadata metadata = parsePluginMetadata(tempJarFile);
            String newVersion = metadata.getVersion();
            
            log.info("Parsed version from JAR: {}", newVersion);
            
            // Validate version format
            if (!com.keqi.gress.common.utils.PluginVersionComparator.isValid(newVersion)) {
                throw new IllegalArgumentException("JAR 包中的版本号格式不正确：" + newVersion + "，应为：x.y.z");
            }
            
            // 3. Check if plugin exists
            PluginManager existingPlugin = dataSource.lambdaQuery(PluginManager.class)
                .eq(PluginManager::getPluginId, request.getPluginId())
                .one();
            
            if (existingPlugin == null) {
                throw new IllegalArgumentException("插件不存在：" + request.getPluginId());
            }
            
            // 4. Validate plugin ID matches
            if (!metadata.getPluginId().equals(request.getPluginId())) {
                throw new IllegalArgumentException(
                    String.format("JAR 包中的插件ID（%s）与请求的插件ID（%s）不匹配", 
                        metadata.getPluginId(), request.getPluginId())
                );
            }
            
            // 5. Compare versions using PluginVersionComparator
            String currentVersion = existingPlugin.getCurrentVersion();
            if (!com.keqi.gress.common.utils.PluginVersionComparator.canUpgrade(currentVersion, newVersion)) {
                throw new IllegalArgumentException(
                    String.format("新版本（%s）必须高于当前版本（%s）", newVersion, currentVersion)
                );
            }
            
            log.info("Version comparison passed: {} -> {}", currentVersion, newVersion);
            
            // 6. Check if version already exists
            long versionCount = dataSource.lambdaQuery(PluginVersion.class)
                .eq(PluginVersion::getPluginId, request.getPluginId())
                .eq(PluginVersion::getVersion, newVersion)
                .count();
            
            if (versionCount > 0) {
                throw new IllegalStateException("版本已存在：" + newVersion);
            }
            
            // Parse dependencies from JAR file
            String dependenciesJson = PluginDependencyParser.parseDependenciesFromJar(tempJarFile.getAbsolutePath());
            log.info("解析到依赖信息: pluginId={}, version={}, dependencies={}", 
                request.getPluginId(), newVersion, dependenciesJson);
            
            // Validate direct dependencies (only check direct dependencies, not indirect)
            PluginDependencyValidationService.CheckMode checkMode = getDependencyCheckMode();
            PluginDependencyValidationService.DependencyValidationResult validationResult = 
                dependencyValidationService.validateDirectDependencies(dependenciesJson, checkMode);
            
            if (!validationResult.isValid()) {
                // 严格模式下，依赖缺失时拒绝升级
                throw new IllegalStateException(validationResult.getErrorMessage());
            }
            
            // 警告模式下，记录警告信息
            if (validationResult.getWarningMessage() != null && !validationResult.getWarningMessage().isEmpty()) {
                log.warn("插件 {} 版本 {} 的依赖检查警告: {}", 
                    request.getPluginId(), newVersion, validationResult.getWarningMessage());
            }
            
            // Determine version status
            String versionStatus = Boolean.TRUE.equals(request.getAutoList()) ? "ONLINE" : "OFFLINE";
            
            // Calculate file size and hash
            long fileSize = tempJarFile.length();
            String fileHash = calculateFileHash(tempJarFile.getAbsolutePath());
            
            LocalDateTime now = LocalDateTime.now();
            
            // Store before data for audit
            Map<String, Object> beforeData = Map.of(
                "pluginId", request.getPluginId(),
                "currentVersion", currentVersion,
                "status", existingPlugin.getStatus()
            );
            
            // Execute upgrade operations in transaction
            dataSource.executeTransaction(() -> {
                // 7. Insert new version record in appstore_version
                PluginVersion pluginVersion = PluginVersion.builder()
                        .pluginId(request.getPluginId())
                        .version(newVersion)
                        .filePath(storedFilePath)
                        .fileSize(fileSize)
                        .fileHash(fileHash)
                        .releaseNotes(request.getUpdateNotes())
                        .dependencies(dependenciesJson) // 存储依赖信息
                        .isCurrent(true)
                        .status(versionStatus)
                        .uploadTime(now)
                        .createTime(now)
                        .updateTime(now)
                        .build();
                
                dataSource.insert(pluginVersion);
                
                // 8. Update old version's isCurrent to false
                dataSource.lambdaUpdate(PluginVersion.class)
                    .eq(PluginVersion::getPluginId, request.getPluginId())
                    .eq(PluginVersion::getIsCurrent, true)
                    .ne(PluginVersion::getVersion, newVersion)
                    .set(PluginVersion::getIsCurrent, false)
                    .set(PluginVersion::getUpdateTime, now)
                    .update();
                
                // 9. Update appstore_manager current version info
                dataSource.lambdaUpdate(PluginManager.class)
                    .eq(PluginManager::getPluginId, request.getPluginId())
                    .set(PluginManager::getCurrentVersion, newVersion)
                    .set(PluginManager::getUpdateTime, now)
                    .update();
                
                // If auto-list, update status to ONLINE
                if (Boolean.TRUE.equals(request.getAutoList())) {
                    dataSource.lambdaUpdate(PluginManager.class)
                        .eq(PluginManager::getPluginId, request.getPluginId())
                        .set(PluginManager::getStatus, "ONLINE")
                        .update();
                }
            });
            
            // Clean up temp file
            if (tempJarFile.exists()) {
                tempJarFile.delete();
            }
            
            // Store after data for audit
            Map<String, Object> afterData = Map.of(
                "pluginId", request.getPluginId(),
                "currentVersion", newVersion,
                "updateNotes", request.getUpdateNotes(),
                "autoList", request.getAutoList()
            );
            
            // Record audit log
            auditLogService.logSuccess(
                    "UPGRADE_PLUGIN",
                    "升级插件版本",
                    "PLUGIN",
                    request.getPluginId(),
                    existingPlugin.getPluginName(),
                    request.getOperatorId(),
                    request.getOperatorName(),
                    beforeData,
                    afterData
            );
            
            log.info("Plugin upgraded successfully: {} to version {}", request.getPluginId(), newVersion);
            
            // Return upgraded plugin info
            return getPluginDetail(request.getPluginId());
            
        } catch (Exception e) {
            log.error("Failed to upgrade plugin: {}", request.getPluginId(), e);
            throw new RuntimeException("升级插件失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * Download JAR file from storage to local temp file for parsing
     */
    private java.io.File downloadJarFromStorage(String storedFilePath) {
        java.io.File tempFile = null;
        try {
            // Create temp file
            tempFile = java.io.File.createTempFile("plugin-upgrade-", ".jar");
            tempFile.deleteOnExit();
            
            final java.io.File finalTempFile = tempFile;
            
            // Download from storage service using toStream
            fileStorageService.download(storedFilePath)
                .toStream(inputStream -> {
                    try (FileOutputStream fos = new FileOutputStream(finalTempFile)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        fos.flush();
                        log.info("Downloaded JAR file to: {}", finalTempFile.getAbsolutePath());
                    } catch (IOException e) {
                        log.error("Failed to write JAR file to temp file", e);
                        throw new RuntimeException("写入临时文件失败: " + e.getMessage(), e);
                    }
                })
                .onError(e -> {
                    log.error("Failed to download JAR file from storage", e);
                    throw new RuntimeException("下载插件文件失败: " + e.getMessage(), e);
                })
                .executeVoid();
            
            return tempFile;
            
        } catch (Exception e) {
            // Clean up temp file on error
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
            throw new RuntimeException("下载插件文件失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * Calculate file hash from file path (simplified version)
     */
    private String calculateFileHash(String filePath) {
        // Since we're using FileStorageService, we don't have direct file access
        // Return a hash based on timestamp and path
        return String.valueOf(System.currentTimeMillis()) + "-" + filePath.hashCode();
    }
    
    /**
     * Parse plugin metadata from JAR file
     */
    private PluginMetadataParser.PluginMetadata parsePluginMetadata(java.io.File jarFile) {
        try {
            return PluginMetadataParser.parseFromJar(jarFile.getAbsolutePath());
        } catch (Exception e) {
            throw new IllegalArgumentException("解析插件元数据失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * Store plugin file to storage
     */
    private String storePluginFile(java.io.File jarFile, PluginMetadataParser.PluginMetadata metadata) {
        try {
            // Generate unique file name
            String fileName = metadata.getPluginId() + "-" + metadata.getVersion() + ".jar";
            
            log.info("开始保存插件文件: {}, 大小: {} bytes", fileName, jarFile.length());
            
            // Use FileStorageService to upload file
            String fileUrl = fileStorageService
                .upload(new FileInputStream(jarFile), fileName)
                .withMetadata("pluginId", metadata.getPluginId())
                .withMetadata("version", metadata.getVersion())
                .withMetadata("category", "plugin")
                .onSuccess(savedUrl -> log.info("插件文件保存成功: {}", savedUrl))
                .onError(e -> {
                    log.error("插件文件保存失败", e);
                    throw new RuntimeException("插件文件保存失败: " + e.getMessage(), e);
                })
                .get();
            
            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new RuntimeException("文件保存失败，返回的 URL 为空");
            }
            
            log.info("Plugin file stored at: {}", fileUrl);
            
            return fileUrl;
            
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("存储插件文件失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * 获取依赖检查模式
     * 可以从配置文件中读取，默认使用 WARN 模式
     */
    private PluginDependencyValidationService.CheckMode getDependencyCheckMode() {
        // TODO: 从配置文件读取 dependency.check.mode
        // 可选值：STRICT（严格模式）、WARN（警告模式）、NONE（不检查）
        // 默认使用 WARN 模式，允许上架但记录警告
        String mode = System.getProperty("appstore.dependency.check.mode", "WARN");
        try {
            return PluginDependencyValidationService.CheckMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("无效的依赖检查模式: {}，使用默认模式 WARN", mode);
            return PluginDependencyValidationService.CheckMode.WARN;
        }
    }
    
    /**
     * Convert PluginManager entity to PluginManagerDTO
     */
    private PluginManagerDTO convertToDTO(PluginManager entity) {
        return PluginManagerDTO.builder()
                .id(entity.getId())
                .pluginId(entity.getPluginId())
                .pluginName(entity.getPluginName())
                .pluginType(entity.getPluginType())
                .currentVersion(entity.getCurrentVersion())
                .status(entity.getStatus())
                .developerId(entity.getDeveloperId())
                .developerName(entity.getDeveloperName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .category(entity.getCategory())
                .installCount(entity.getInstallCount())
                .ratingAverage(entity.getRatingAverage())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .build();
    }
}
