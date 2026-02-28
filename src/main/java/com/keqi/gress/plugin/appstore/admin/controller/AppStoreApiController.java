package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.dto.PluginPackageDTO;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreApiManagementService;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用商店 API 控制器（用户端）
 * 
 * 提供应用查询和下载功能
 */
@Service
@RestController
@RequestMapping("/api/appstore/packages")
public class AppStoreApiController {
    
    private static final Log log = LogFactory.get(AppStoreApiController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AppStoreApiManagementService appStoreApiManagementService;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginTablePermissionService pluginTablePermissionService;
    
    /**
     * 查询应用列表（用户端）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param pluginType 插件类型（可选：TASK/TRIGGER/APPLICATION）
     * @param category 分类（可选）
     * @param keyword 关键词搜索（可选）
     * @return 应用列表
     */
    @GetMapping
    public Result<PageResult<PluginPackageDTO>> getPackages(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String pluginType,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        
        log.info("查询应用列表: page={}, size={}, pluginType={}, category={}, keyword={}", 
                page, size, pluginType, category, keyword);
        
        PageResult<PluginPackageDTO> result = appStoreApiManagementService.getPackages(
            page, size, pluginType, category, keyword);
        
        return Result.success(result);
    }
    
    /**
     * 获取应用详情（用户端）
     * 
     * @param pluginId 插件ID
     * @return 应用详情
     */
    @GetMapping("/{pluginId}")
    public Result<PluginPackageDTO> getPackageDetail(@PathVariable String pluginId) {
        log.info("获取应用详情: pluginId={}", pluginId);
        return appStoreApiManagementService.getPackageDetail(pluginId);
    }
    
    /**
     * 下载应用包
     * 
     * @param pluginId 插件ID
     * @return 文件流
     */
    @GetMapping("/{pluginId}/download")
    public ResponseEntity<Resource> downloadPackage(@PathVariable String pluginId) {
        log.info("下载应用包: pluginId={}", pluginId);
        
        try {
            // 获取文件资源和文件名
            Result<Resource> result = appStoreApiManagementService.downloadPackage(pluginId);
            
            if (!result.isSuccess()) {
                log.error("下载应用包失败: pluginId={}, error={}", pluginId, result.getErrorMessage());
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = result.getData();
            String fileName = appStoreApiManagementService.getPackageFileName(pluginId);
            
            // 更新下载计数
            appStoreApiManagementService.incrementDownloadCount(pluginId);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("下载应用包异常: pluginId={}", pluginId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据插件ID和版本获取版本信息
     *
     * @param pluginId 插件ID
     * @param version  版本号
     * @return 版本信息
     */
    @GetMapping("/{pluginId}/versions/{version}")
    public Result<PluginPackageDTO> getPackageVersionDetail(
            @PathVariable String pluginId,
            @PathVariable String version) {
        log.info("获取应用版本信息: pluginId={}, version={}", pluginId, version);
        return appStoreApiManagementService.getPackageVersionDetail(pluginId, version);
    }

    /**
     * 根据插件ID和版本下载应用包
     *
     * @param pluginId 插件ID
     * @param version  版本号
     * @return 文件流
     */
    @GetMapping("/{pluginId}/versions/{version}/download")
    public ResponseEntity<byte[]> downloadPackageByVersion(
            @PathVariable String pluginId,
            @PathVariable String version) {
        log.info("按版本下载应用包: pluginId={}, version={}", pluginId, version);

        try {
            Result<byte[]> result = appStoreApiManagementService.downloadPackageBytesByVersion(pluginId, version);

            if (!result.isSuccess()) {
                log.error("按版本下载应用包失败: pluginId={}, version={}, error={}",
                        pluginId, version, result.getErrorMessage());
                return ResponseEntity.notFound().build();
            }

            byte[] fileBytes = result.getData();
            String fileName = pluginId + "-" + version + ".jar";

            // 复用下载计数逻辑：按插件维度统计
            appStoreApiManagementService.incrementDownloadCount(pluginId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                           "attachment; filename=\"" + fileName + "\"")
                    .body(fileBytes);

        } catch (Exception e) {
            log.error("按版本下载应用包异常: pluginId={}, version={}", pluginId, version, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 根据插件ID获取表授权信息
     *
     * @param pluginId 插件ID
     * @return 表授权信息列表
     */
    @GetMapping("/{pluginId}/table-permissions")
    public Result<List<PluginTablePermissionDTO>> getTablePermissions(@PathVariable String pluginId) {
        log.info("获取插件表授权信息: pluginId={}", pluginId);
        
        try {
            List<PluginTablePermissionDTO> permissions = pluginTablePermissionService.listByPluginId(pluginId);
            return Result.success(permissions);
        } catch (Exception e) {
            log.error("获取插件表授权信息失败: pluginId={}", pluginId, e);
            return Result.error("获取表授权信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取插件配置元数据（从 jar 包中解析）
     * 
     * @param pluginId 插件ID
     * @return 配置元数据列表
     */
    @GetMapping("/{pluginId}/config/metadata")
    public Result<List<com.keqi.gress.common.plugin.FormMetadataParser.FieldMetadata>> getPluginConfigMetadata(@PathVariable String pluginId) {
        log.info("获取插件配置元数据: pluginId={}", pluginId);
        
        try {
            // 通过 AppStoreApiService 从远程 jar 包解析配置元数据
            // 注意：这里需要注入 AppStoreApiService，但它是插件服务，需要通过其他方式获取
            // 暂时返回错误，提示需要通过 AppStoreApiService 调用
            return Result.error("此接口需要通过 AppStoreApiService 调用，请使用 /plugins/appstore/middlewares/remote/{pluginId}/config/metadata");
        } catch (Exception e) {
            log.error("获取插件配置元数据失败: pluginId={}", pluginId, e);
            return Result.error("获取配置元数据失败: " + e.getMessage());
        }
    }

}
