package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.support.OperatorContextHelper;
import com.keqi.gress.plugin.appstore.admin.support.RequestActorContextBinder;
import com.keqi.gress.plugin.appstore.admin.service.PluginManagementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Plugin Management Controller
 * REST API for listed plugin management
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/plugins")
@PluginMenu(id = "plugins", name = "插件列表", managementEnabled = true)
public class PluginManagementController {

    private static final Log log = LogFactory.get(PluginManagementController.class);
    private static final List<PluginTypeInfo> PLUGIN_TYPES = List.of(
            PluginTypeInfo.builder()
                    .code("TASK")
                    .label("任务节点")
                    .description("工作流中的任务执行节点")
                    .tagType("info")
                    .build(),
            PluginTypeInfo.builder()
                    .code("TRIGGER")
                    .label("触发器")
                    .description("工作流的触发节点")
                    .tagType("success")
                    .build(),
            PluginTypeInfo.builder()
                    .code("APPLICATION")
                    .label("应用插件")
                    .description("提供应用级功能的插件")
                    .tagType("warning")
                    .build(),
            PluginTypeInfo.builder()
                    .code("MIDDLEWARE")
                    .label("中间件")
                    .description("基础设施中间件插件（如 Milvus、Redis 等）")
                    .tagType("error")
                    .build()
    );
    
    @Autowired
    private PluginManagementService pluginManagementService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    /**
     * Upload plugin package
     *
     * @param file Plugin JAR file
     * @param pluginType Plugin type
     * @param description Plugin description
     * @param autoList Auto list to store
     * @return Uploaded plugin info
     */
    @PostMapping("/upload")
    @PluginAction(id = "upload", name = "上传插件")
    public Result<PluginManagerDTO> uploadPlugin(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pluginType") String pluginType,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "priceType", required = false) String priceType,
            @RequestParam(value = "autoList", defaultValue = "false") Boolean autoList) {
        
        log.info("POST /plugins/upload - file: {}, type: {}, autoList: {}", 
                 file.getOriginalFilename(), pluginType, autoList);
        
        File tempFile = null;
        try {
            validateJarFile(file);

            // Save uploaded file to temp location
            tempFile = File.createTempFile("plugin-upload-", ".jar");
            file.transferTo(tempFile);
            
            // Build request
            PluginUploadRequest request = RequestActorContextBinder.bindOperator(PluginUploadRequest.builder()
                    .pluginType(pluginType)
                    .description(description)
                    .priceType(priceType)
                    .autoList(autoList)
                    .build());
            
            // Upload plugin
            PluginManagerDTO plugin = pluginManagementService.uploadPlugin(tempFile, request);
            
            log.info("Plugin uploaded successfully: {}", plugin.getPluginId());
            
            return Result.success(plugin);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid upload request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for upload: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IOException e) {
            log.error("Failed to process uploaded file", e);
            return Result.error("处理上传文件失败：" + e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to upload plugin", e);
            return Result.error("上传插件失败：" + e.getMessage());
            
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * Upgrade plugin version
     *
     * @param file Plugin JAR file
     * @param pluginId Plugin ID
     * @param version New version number
     * @param updateNotes Update notes
     * @param autoList Auto list to store
     * @return Upgraded plugin info
     */
    @PostMapping("/upgrade")
    @PluginAction(id = "upgrade", name = "升级插件")
    public Result<PluginManagerDTO> upgradePlugin(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pluginId") String pluginId,
            @RequestParam("updateNotes") String updateNotes,
            @RequestParam(value = "autoList", defaultValue = "false") Boolean autoList) {
        
        log.info("POST /plugins/upgrade - pluginId: {}, file: {}, autoList: {}", 
                 pluginId, file.getOriginalFilename(), autoList);
        
        try {
            validateJarFile(file);

            // Upload file to storage service
            String storedFilePath = fileStorageService.upload(file)
                    .withMetadata("pluginId", pluginId)
                    .withMetadata("type", "plugin-upgrade")
                    .get();
            
            log.info("Plugin file uploaded to: {}", storedFilePath);
            
            // Build request (version will be parsed from JAR in service)
            PluginUpgradeRequest request = RequestActorContextBinder.bindOperator(PluginUpgradeRequest.builder()
                    .pluginId(pluginId)
                    .version(null) // Will be parsed from JAR
                    .updateNotes(updateNotes)
                    .autoList(autoList)
                    .build());
            
            // Upgrade plugin
            PluginManagerDTO plugin = pluginManagementService.upgradePlugin(storedFilePath, request);
            
            log.info("Plugin upgraded successfully: {} to version {}", pluginId, plugin.getCurrentVersion());
            
            return Result.success(plugin);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid upgrade request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for upgrade: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to upgrade plugin: {}", pluginId, e);
            return Result.error("升级插件失败：" + e.getMessage());
        }
    }
    
    /**
     * Get listed plugins with filtering and pagination
     *
     * @param page Page number (1-based), default 1
     * @param size Page size, default 20
     * @param status Filter by status (ONLINE/OFFLINE/DELISTED)
     * @param type Filter by plugin type
     * @param keyword Search keyword
     * @return Paginated list of listed plugins
     */
    @GetMapping
    @PluginAction(id = "refresh", name = "刷新")
    public Result<PageResult<PluginManagerDTO>> getListedPlugins(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String priceType) {
        
        log.info("GET /plugins - page: {}, size: {}, status: {}, type: {}, keyword: {}, priceType: {}", 
                 page, size, status, type, keyword, priceType);
        
        try {
            PageResult<PluginManagerDTO> result = pluginManagementService.getListedPlugins(
                page, size, status, type, keyword, priceType);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("Failed to get listed plugins", e);
            return Result.error("Failed to query listed plugins: " + e.getMessage());
        }
    }
    
    /**
     * Get plugin detail by plugin ID
     *
     * @param pluginId Plugin ID
     * @return Plugin detail
     */
    @GetMapping("/{pluginId}")
    public Result<PluginManagerDTO> getPluginDetail(@PathVariable String pluginId) {
        log.info("GET /plugins/{}", pluginId);
        
        try {
            PluginManagerDTO plugin = pluginManagementService.getPluginDetail(pluginId);
            
            if (plugin == null) {
                return Result.error("Plugin not found: " + pluginId);
            }
            
            return Result.success(plugin);
            
        } catch (Exception e) {
            log.error("Failed to get plugin detail: {}", pluginId, e);
            return Result.error("Failed to get plugin detail: " + e.getMessage());
        }
    }
    
    /**
     * Update plugin basic information
     *
     * @param pluginId Plugin ID
     * @param request Update request
     * @return Updated plugin info
     */
    @PutMapping("/{pluginId}")
    public Result<PluginManagerDTO> updatePlugin(
            @PathVariable String pluginId, 
            @RequestBody PluginUpdateRequest request) {
        log.info("PUT /plugins/{} - update request: {}", pluginId, request);
        
        try {
            // Set plugin ID from path
            request.setPluginId(pluginId);
            RequestActorContextBinder.bindOperator(request);
            
            PluginManagerDTO plugin = pluginManagementService.updatePlugin(request);
            
            log.info("Plugin updated successfully: {}", pluginId);
            
            return Result.success(plugin);
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid update request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for update: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to update plugin: {}", pluginId, e);
            return Result.error("更新插件失败：" + e.getMessage());
        }
    }
    
    /**
     * Delist a plugin from the public store
     *
     * @param pluginId Plugin ID
     * @param request Delist request
     * @return Success result
     */
    @PostMapping("/{pluginId}/delist")
    @PluginAction(id = "delist", name = "下架", managementEnabled = true, actionCode = "MANAGE")
    public Result<Void> delistPlugin(@PathVariable String pluginId, @RequestBody DelistRequest request) {
        log.info("POST /plugins/{}/delist - operator: {}, reason: {}", 
                 pluginId, request.getOperatorName(), request.getReason());
        
        try {
            RequestActorContextBinder.bindOperator(request);
            pluginManagementService.delistPlugin(pluginId, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid delist request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for delist: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to delist plugin: {}", pluginId, e);
            return Result.error("Failed to delist plugin: " + e.getMessage());
        }
    }
    
    /**
     * Relist a delisted plugin
     *
     * @param pluginId Plugin ID
     * @param request Relist request
     * @return Success result
     */
    @PostMapping("/{pluginId}/relist")
    @PluginAction(id = "relist", name = "重新上架", managementEnabled = true, actionCode = "MANAGE")
    public Result<Void> relistPlugin(
            @PathVariable String pluginId,
            @RequestBody(required = false) RelistRequest request) {
        RelistRequest safeRequest = request != null ? request : new RelistRequest();
        log.info("POST /plugins/{}/relist - operator: {}",
                pluginId, safeRequest.getOperatorName());
        
        try {
            RequestActorContextBinder.bindOperator(safeRequest);
            pluginManagementService.relistPlugin(pluginId, safeRequest);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid relist request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for relist: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to relist plugin: {}", pluginId, e);
            return Result.error("Failed to relist plugin: " + e.getMessage());
        }
    }
    
    /**
     * Permanently delete plugin (not ONLINE): app row, versions, stored JARs.
     */
    @DeleteMapping("/{pluginId}")
    public Result<Void> deletePlugin(@PathVariable String pluginId) {
        log.info("DELETE /plugins/{}", pluginId);
        try {
            pluginManagementService.deletePluginPermanently(
                    pluginId,
                    OperatorContextHelper.getOperatorId(),
                    OperatorContextHelper.getOperatorName()
            );
            return Result.success();
        } catch (IllegalArgumentException e) {
            log.warn("Invalid delete request: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Invalid state for delete: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to delete plugin: {}", pluginId, e);
            return Result.error("删除插件失败：" + e.getMessage());
        }
    }
    
    /**
     * Get available plugin types
     * 
     * @return List of plugin type information
     */
    @GetMapping("/types")
    public Result<java.util.List<PluginTypeInfo>> getPluginTypes() {
        
        try {
            return Result.success(PLUGIN_TYPES);
            
        } catch (Exception e) {
            log.error("Failed to get plugin types", e);
            return Result.error("获取插件类型失败：" + e.getMessage());
        }
    }

    private void validateJarFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("请选择插件文件");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".jar")) {
            throw new IllegalArgumentException("只支持 JAR 格式的插件包");
        }
    }
}
