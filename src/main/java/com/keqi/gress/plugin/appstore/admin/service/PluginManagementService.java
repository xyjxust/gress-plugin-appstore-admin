package com.keqi.gress.plugin.appstore.admin.service;

import cn.hutool.crypto.digest.MD5;
import com.keqi.gress.common.utils.PluginVersionComparator;
import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.common.plugin.PluginMetadataParser;
import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.config.AppStoreAdminConfig;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginStatistics;
import com.keqi.gress.plugin.appstore.admin.entity.PluginVersion;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.keqi.gress.plugin.appstore.admin.service.crypto.AesGcmCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Plugin Management Service
 * Handles listed plugin management operations
 */
@Service
@Slf4j
public class PluginManagementService {
    

    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Inject
    private AuditLogService auditLogService;
    
    @Inject
    private FileStorageService fileStorageService;
    
    @Inject
    private PluginDependencyValidationService dependencyValidationService;

    @Inject
    private AppStoreSigningKeyService signingKeyService;

    @Inject
    private AppStoreAdminConfig appStoreAdminConfig;
    
    @Inject
    private TagService tagService;

    private static final String ENV_KEYSTORE_PATH = "APPSTORE_SIGNING_KEYSTORE_PATH";
    private static final String ENV_KEYSTORE_PASSWORD = "APPSTORE_SIGNING_KEYSTORE_PASSWORD";
    private static final String ENV_KEY_ALIAS = "APPSTORE_SIGNING_KEY_ALIAS";
    private static final String ENV_KEY_PASSWORD = "APPSTORE_SIGNING_KEY_PASSWORD";
    
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
     * Permanently delete a plugin that is not online: remove manager row, version rows,
     * tag links, statistics, then delete stored JAR URLs (and remote icon if applicable).
     */
    public void deletePluginPermanently(String pluginId, String operatorId, String operatorName) {
        log.info("Permanently deleting plugin: {}", pluginId);
        
        PluginManager plugin = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getPluginId, pluginId)
            .one();
        
        if (plugin == null) {
            throw new IllegalArgumentException("插件不存在：" + pluginId);
        }
        if ("ONLINE".equals(plugin.getStatus())) {
            throw new IllegalStateException("上架中的插件不能删除，请先下架");
        }
        
        List<PluginVersion> versions = dataSource.lambdaQuery(PluginVersion.class)
            .eq(PluginVersion::getPluginId, pluginId)
            .list();
        
        Set<String> fileUrls = new LinkedHashSet<>();
        for (PluginVersion v : versions) {
            if (v.getFilePath() != null && !v.getFilePath().isBlank()) {
                fileUrls.add(v.getFilePath().trim());
            }
        }
        String iconUrl = plugin.getIcon();
        
        Map<String, Object> beforeData = Map.of(
            "pluginId", pluginId,
            "pluginName", plugin.getPluginName() != null ? plugin.getPluginName() : "",
            "status", plugin.getStatus(),
            "versionCount", versions.size()
        );
        
        dataSource.executeTransaction(() -> {
            tagService.clearPluginTagsForPlugin(pluginId);
            dataSource.lambdaUpdate(PluginStatistics.class)
                .eq(PluginStatistics::getPluginId, pluginId)
                .delete();
            dataSource.lambdaUpdate(PluginVersion.class)
                .eq(PluginVersion::getPluginId, pluginId)
                .delete();
            int removed = dataSource.lambdaUpdate(PluginManager.class)
                .eq(PluginManager::getPluginId, pluginId)
                .delete();
            if (removed == 0) {
                throw new IllegalStateException("删除应用记录失败");
            }
        });
        
        for (String url : fileUrls) {
            safeDeleteStoredFile(url);
        }
        if (iconUrl != null && !iconUrl.isBlank()) {
            String trimmed = iconUrl.trim();
            if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                safeDeleteStoredFile(trimmed);
            }
        }
        
        auditLogService.logSuccess(
            "DELETE_PLUGIN",
            "永久删除插件",
            "PLUGIN",
            pluginId,
            plugin.getPluginName(),
            operatorId,
            operatorName,
            beforeData,
            Map.of("deletedVersions", versions.size(), "removedFiles", fileUrls.size())
        );
        
        log.info("Plugin permanently deleted: {} ({} versions)", pluginId, versions.size());
    }
    
    private void safeDeleteStoredFile(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        try {
            fileStorageService.delete(url).get();
            log.info("Removed stored file: {}", url);
        } catch (Exception e) {
            log.warn("Could not remove stored file (may already be deleted): {}", url, e);
        }
    }
    
    /**
     * Upload plugin package
     *
     * @param jarFile Plugin JAR file
     * @param request Upload request
     * @return Uploaded plugin info
     */
    public PluginManagerDTO uploadPlugin(File jarFile, PluginUploadRequest request) {
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
            StoredPluginFileInfo stored;
            String storedFilePath = "";
            long size = Files.size(jarFile.toPath());
            String fileHash = "";
            if(this.isVerifySignatureEnabled()){
                stored = storeSignedPluginFile(jarFile, metadata);
                size = stored.fileSize;
                fileHash = stored.sha256;
            }else{
                stored = null;
                storedFilePath  =    storePluginFile(jarFile, metadata);
                fileHash = MD5.create().digestHex16(jarFile);
            }
            if (stored != null) {
                storedFilePath = stored.fileUrl;
            }

            
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
            
            LocalDateTime now = LocalDateTime.now();
            
            // Execute insert operations in transaction
            String finalStoredFilePath = storedFilePath;
            Long finalSize = size;
            String finalFileHash = fileHash;
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
                        .filePath(finalStoredFilePath)
                        .fileSize(finalSize)
                        .fileHash(finalFileHash)
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
            File tempJarFile = downloadJarFromStorage(storedFilePath);
            
            // 2. Parse plugin metadata from JAR file to get version
            PluginMetadataParser.PluginMetadata metadata = parsePluginMetadata(tempJarFile);
            String newVersion = metadata.getVersion();
            
            log.info("Parsed version from JAR: {}", newVersion);
            
            // Validate version format
            if (!PluginVersionComparator.isValid(newVersion)) {
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
            if (!PluginVersionComparator.canUpgrade(currentVersion, newVersion)) {
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

            if(isVerifySignatureEnabled()){
                // Store signed jar (Marketplace 签发) and calculate SHA-256 for stored/signed jar
                StoredPluginFileInfo stored = storeSignedPluginFile(tempJarFile, metadata);
                 fileSize = stored.fileSize;
                 fileHash = stored.sha256;
                storedFilePath = stored.fileUrl;
            }

            
            LocalDateTime now = LocalDateTime.now();
            
            // Store before data for audit
            Map<String, Object> beforeData = Map.of(
                "pluginId", request.getPluginId(),
                "currentVersion", currentVersion,
                "status", existingPlugin.getStatus()
            );
            
            // Execute upgrade operations in transaction
            long finalFileSize = fileSize;
            String finalFileHash = fileHash;
            String finalStoredFilePath = storedFilePath;
            dataSource.executeTransaction(() -> {
                // 7. Insert new version record in appstore_version
                PluginVersion pluginVersion = PluginVersion.builder()
                        .pluginId(request.getPluginId())
                        .version(newVersion)
                        .filePath(finalStoredFilePath)
                        .fileSize(finalFileSize)
                        .fileHash(finalFileHash)
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
     * Calculate file hash from file path (simplified version)
     */
    private String calculateFileHash(String filePath) {
        // Since we're using FileStorageService, we don't have direct file access
        // Return a hash based on timestamp and path
        return String.valueOf(System.currentTimeMillis()) + "-" + filePath.hashCode();
    }
    
    /**
     * Download JAR file from storage to local temp file for parsing
     */
    private File downloadJarFromStorage(String storedFilePath) {
        File tempFile = null;
        try {
            // Create temp file
            tempFile = File.createTempFile("plugin-upgrade-", ".jar");
            tempFile.deleteOnExit();
            
            final File finalTempFile = tempFile;
            
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
     * Store signed plugin file to storage (Marketplace 签发包)
     */
    private StoredPluginFileInfo storeSignedPluginFile(File jarFile, PluginMetadataParser.PluginMetadata metadata) {
        if (jarFile == null || !jarFile.exists()) {
            throw new IllegalArgumentException("插件文件不存在");
        }

        Path unsignedJar = jarFile.toPath();
        Path signedJar = null;
        String sha256 = "";
        try {
            long size = Files.size(unsignedJar);
            if (isVerifySignatureEnabled()) {
                signedJar = Files.createTempFile("appstore-admin-signed-", ".jar");
                signJarWithActiveKey(unsignedJar, signedJar);
               // PublicKey expectedPublicKey = loadExpectedSigningPublicKeyOrThrow();
              //  verifySignedJarWithExpectedPublicKey(signedJar, expectedPublicKey);
                size = Files.size(signedJar);
                sha256 = sha256Hex(signedJar);
            }

            String fileName = metadata.getPluginId() + "-" + metadata.getVersion() + ".jar";

            String fileUrl;
            try (InputStream in = Files.newInputStream(signedJar)) {
                fileUrl = fileStorageService
                    .upload(in, fileName)
                    .withMetadata("pluginId", metadata.getPluginId())
                    .withMetadata("version", metadata.getVersion())
                    .withMetadata("category", "plugin")
                    .withMetadata("sha256", sha256)
                    .onSuccess(savedUrl -> log.info("插件签名包上传成功: {}", savedUrl))
                    .onError(e -> {
                        log.error("插件签名包上传失败", e);
                        throw new RuntimeException("插件签名包上传失败: " + e.getMessage(), e);
                    })
                    .get();
            }

            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new RuntimeException("文件保存失败，返回的 URL 为空");
            }

            return new StoredPluginFileInfo(fileUrl, sha256, size);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("保存签名插件包失败: " + e.getMessage(), e);
        } finally {
            if (signedJar != null) {
                try {
                    Files.deleteIfExists(signedJar);
                } catch (IOException ignore) {
                }
            }
        }
    }

    private boolean isVerifySignatureEnabled() {
        return appStoreAdminConfig != null
                && appStoreAdminConfig.getSecurity() != null
                && Boolean.TRUE.equals(appStoreAdminConfig.getSecurity().getVerifySignature());
    }

    private PublicKey loadExpectedSigningPublicKeyOrThrow() {
        try {
            var activeKey = signingKeyService != null ? signingKeyService.getActiveKeyOrNull() : null;
            if (activeKey != null && activeKey.getPublicKeyPem() != null && !activeKey.getPublicKeyPem().isBlank()) {
                return parsePublicKeyFromPem(activeKey.getPublicKeyPem());
            }

            // fallback: legacy env-based signing
            String keystorePath = getenvRequired(ENV_KEYSTORE_PATH);
            String storePassword = getenvRequired(ENV_KEYSTORE_PASSWORD);
            String keyAlias = getenvRequired(ENV_KEY_ALIAS);
            return loadPublicKeyFromKeystore(keystorePath, storePassword, keyAlias);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load expected signing public key: " + e.getMessage(), e);
        }
    }

    private void verifySignedJarWithExpectedPublicKey(Path signedJar, PublicKey expectedPublicKey) throws Exception {
        if (signedJar == null || !Files.exists(signedJar)) {
            throw new IllegalArgumentException("signedJar not found: " + signedJar);
        }
        if (expectedPublicKey == null) {
            throw new IllegalStateException("expectedPublicKey is null");
        }
        String expectedEnc = encodePublicKey(expectedPublicKey);
        String expectedFingerprint = HexFormat.of().formatHex(expectedPublicKey.getEncoded());
        log.info("[SigningVerify] expected public key fingerprint(sha256)={}", expectedFingerprint);

        boolean hasSf = false;
        boolean hasRsaOrDsa = false;
        boolean matched = false;
        java.util.Set<String> jarSignerFingerprints = new java.util.LinkedHashSet<>();
        java.util.Set<String> jarSignerSubjects = new java.util.LinkedHashSet<>();
        java.util.Set<String> jarCertTypes = new java.util.LinkedHashSet<>();

        byte[] buffer = new byte[8192];

        try (JarFile jarFile = new JarFile(signedJar.toFile(), true)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name == null) continue;

                // 关键点：先读取 entry 流，触发 JAR 验签，才能让 certificates 被填充
                if (!entry.isDirectory()) {
                    try (InputStream is = jarFile.getInputStream(entry)) {
                        while (is.read(buffer) != -1) {
                            // no-op, 只为触发校验
                        }
                    }
                }

                if (name.startsWith("META-INF/") && name.endsWith(".SF")) {
                    hasSf = true;
                    continue;
                }
                if (name.startsWith("META-INF/") && (name.endsWith(".RSA") || name.endsWith(".DSA"))) {
                    hasRsaOrDsa = true;
                    continue;
                }

                Certificate[] certs;
                try {
                    certs = entry.getCertificates();
                } catch (SecurityException se) {
                    throw new IllegalStateException("Jar signature verification failed: " + se.getMessage(), se);
                }

                if (certs == null || certs.length == 0) {
                    continue;
                }

                for (Certificate cert : certs) {
                    if (!(cert instanceof X509Certificate signerCert)) {
                        if (cert != null) {
                            jarCertTypes.add(cert.getClass().getName());
                        }
                        continue;
                    }
                    PublicKey signerPk = signerCert.getPublicKey();
                    if (signerPk == null) continue;

                    String signerEnc = encodePublicKey(signerPk);
                    String signerFingerprint = HexFormat.of().formatHex(signerPk.getEncoded());
                    jarSignerFingerprints.add(signerFingerprint);
                    jarSignerSubjects.add(String.valueOf(signerCert.getSubjectX500Principal()));
                    log.info("[SigningVerify] found signer cert subject={}, fingerprint(sha256)={}",
                            signerCert.getSubjectX500Principal(), signerFingerprint);

                    if (expectedEnc.equals(signerEnc)) {
                        matched = true;
                        break;
                    }
                }

                if (matched) {
                    break;
                }
            }
        }

        // Keep behavior aligned with client verifier: if signature blocks absent => fail.
        if (!hasSf || !hasRsaOrDsa) {
            log.error("[SigningVerify] jar={} signature blocks missing: hasSf={}, hasRsaOrDsa={}, expectedFingerprint(sha256)={}",
                    signedJar, hasSf, hasRsaOrDsa, expectedFingerprint);
            throw new IllegalStateException("Jar signature files missing: hasSF=" + hasSf + ", hasRsaOrDsa=" + hasRsaOrDsa);
        }
        if (!matched) {
            log.error("[SigningVerify] jar={} public key mismatch: hasSf={}, hasRsaOrDsa={}, expectedFingerprint(sha256)={}, jarSignerFingerprints={}, jarSignerSubjects={}, jarCertTypes={}",
                    signedJar,
                    hasSf,
                    hasRsaOrDsa,
                    expectedFingerprint,
                    jarSignerFingerprints,
                    jarSignerSubjects,
                    jarCertTypes);
            throw new IllegalStateException("Jar signed by an unexpected signer (public key mismatch)");
        }
    }

    private static PublicKey parsePublicKeyFromPem(String pem) throws Exception {
        String normalized = pem.trim();
        if (normalized.contains("BEGIN CERTIFICATE")) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(
                    new ByteArrayInputStream(normalized.getBytes(StandardCharsets.UTF_8)));
            return cert.getPublicKey();
        }
        if (normalized.contains("BEGIN PUBLIC KEY")) {
            // Not expected in admin self-signed flow; keep minimal compatibility.
            throw new IllegalStateException("Unsupported PEM type (PUBLIC KEY) for expectedPublicKey in admin verifier");
        }
        throw new IllegalStateException("Unsupported PEM format for public key");
    }

    private static PublicKey loadPublicKeyFromKeystore(String keystorePath, String storePassword, String alias) throws Exception {
        if (keystorePath == null || alias == null) {
            throw new IllegalArgumentException("keystorePath/alias required");
        }
        char[] passwordChars = storePassword != null ? storePassword.toCharArray() : new char[0];
        List<String> types = Arrays.asList("PKCS12", "JKS");
        Exception last = null;
        for (String type : types) {
            try (InputStream in = new FileInputStream(keystorePath)) {
                KeyStore ks = KeyStore.getInstance(type);
                ks.load(in, passwordChars);
                Certificate cert = ks.getCertificate(alias);
                if (cert instanceof X509Certificate x509) {
                    return x509.getPublicKey();
                }
            } catch (Exception e) {
                last = e;
            }
        }
        throw new IllegalStateException("Failed to load keystore or certificate: " + (last == null ? "" : last.getMessage()), last);
    }

    private static String encodePublicKey(PublicKey pk) {
        return Base64.getEncoder().encodeToString(pk.getEncoded());
    }

    /**
     * Sign jar using active signing key from appstore_signing_key table.
     * <p>
     * Fallback: if there is no active key in DB, use legacy env-based signing.
     * </p>
     */
    private void signJarWithActiveKey(Path inputJar, Path outputJar) throws Exception {
        var activeKey = signingKeyService != null ? signingKeyService.getActiveKeyOrNull() : null;
        if (activeKey == null) {
            // Backward compatibility: old deployments use env vars only.
            signJarWithPlatformKey(inputJar, outputJar);
            return;
        }

        String storePassword = AesGcmCryptoUtil.decrypt(activeKey.getStorePasswordEnc());
        String keyPassword = AesGcmCryptoUtil.decrypt(activeKey.getKeyPasswordEnc());

        Path tempKeystore = null;
        try {
            tempKeystore = Files.createTempFile("appstore-signing-keystore-", ".p12");
            final Path finalTempKeystore = tempKeystore;
            // Download keystore from FileStorage to local temp file
            fileStorageService.download(activeKey.getKeystoreUrl())
                    .toStream(inputStream -> {
                        try {
                            Files.copy(inputStream, finalTempKeystore, StandardCopyOption.REPLACE_EXISTING);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to copy keystore to temp file", e);
                        }
                    })
                    .onError(e -> {
                        throw new RuntimeException("Failed to download keystore from storage: " + e.getMessage(), e);
                    })
                    .executeVoid();

            List<String> cmd = new ArrayList<>();
            cmd.add("jarsigner");
            cmd.add("-sigalg");
            cmd.add("SHA256withRSA");
            cmd.add("-digestalg");
            cmd.add("SHA-256");
            cmd.add("-keystore");
            cmd.add(tempKeystore.toString());
            cmd.add("-storepass");
            cmd.add(storePassword);
            cmd.add("-keypass");
            cmd.add(keyPassword);
            cmd.add("-signedjar");
            cmd.add(outputJar.toString());
            cmd.add(inputJar.toString());
            cmd.add(activeKey.getAlias());

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String output = new String(p.getInputStream().readAllBytes());
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("jarsigner 执行失败: exitCode=" + exitCode + ", output=" + output);
            }
        } finally {
            if (tempKeystore != null) {
                try {
                    Files.deleteIfExists(tempKeystore);
                } catch (Exception ignore) {}
            }
        }
    }

    private void signJarWithPlatformKey(Path inputJar, Path outputJar) throws Exception {
        String keystorePath = getenvRequired(ENV_KEYSTORE_PATH);
        String storePassword = getenvRequired(ENV_KEYSTORE_PASSWORD);
        String keyAlias = getenvRequired(ENV_KEY_ALIAS);
        String keyPassword = System.getenv(ENV_KEY_PASSWORD);
        if (keyPassword == null || keyPassword.isBlank()) {
            keyPassword = storePassword;
        }

        List<String> cmd = new ArrayList<>();
        cmd.add("jarsigner");
        cmd.add("-sigalg");
        cmd.add("SHA256withRSA");
        cmd.add("-digestalg");
        cmd.add("SHA-256");
        cmd.add("-keystore");
        cmd.add(keystorePath);
        cmd.add("-storepass");
        cmd.add(storePassword);
        cmd.add("-keypass");
        cmd.add(keyPassword);
        cmd.add("-signedjar");
        cmd.add(outputJar.toString());
        cmd.add(inputJar.toString());
        cmd.add(keyAlias);

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        String output = new String(p.getInputStream().readAllBytes());
        int exitCode = p.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("jarsigner 执行失败: exitCode=" + exitCode + ", output=" + output);
        }
    }

    private static String getenvRequired(String key) {
        String v = System.getenv(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing env var: " + key);
        }
        return v;
    }

    private static String sha256Hex(Path file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (InputStream in = Files.newInputStream(file)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                md.update(buf, 0, n);
            }
        }
        return HexFormat.of().formatHex(md.digest());
    }

    private static class StoredPluginFileInfo {
        final String fileUrl;
        final String sha256;
        final long fileSize;

        StoredPluginFileInfo(String fileUrl, String sha256, long fileSize) {
            this.fileUrl = fileUrl;
            this.sha256 = sha256;
            this.fileSize = fileSize;
        }
    }
    
    /**
     * Parse plugin metadata from JAR file
     */
    private PluginMetadataParser.PluginMetadata parsePluginMetadata(File jarFile) {
        try {
            return PluginMetadataParser.parseFromJar(jarFile.getAbsolutePath());
        } catch (Exception e) {
            throw new IllegalArgumentException("解析插件元数据失败：" + e.getMessage(), e);
        }
    }
    
    /**
     * Store plugin file to storage
     */
    private String storePluginFile(File jarFile, PluginMetadataParser.PluginMetadata metadata) {
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
