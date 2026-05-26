package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.dto.PluginPackageDTO;
import com.keqi.gress.plugin.appstore.admin.dto.PluginTablePermissionDTO;
import com.keqi.gress.plugin.appstore.admin.dto.AppStorePackageVersionDTO;
import com.keqi.gress.plugin.appstore.admin.dto.DownloadTokenResponse;
import com.keqi.gress.plugin.appstore.admin.service.AppStoreApiManagementService;
import com.keqi.gress.plugin.appstore.admin.service.DownloadTokenService;
import com.keqi.gress.plugin.appstore.admin.service.PluginTablePermissionService;
import com.keqi.gress.plugin.appstore.admin.service.PluginVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 应用商店 API 控制器（用户端）
 * 
 * 提供应用查询和下载功能
 */
@Service
@RestController
@RequestMapping("/anon/packages")
@Slf4j
public class AppStoreApiController {

    
    @Autowired
    private AppStoreApiManagementService appStoreApiManagementService;

    @Autowired
    private PluginTablePermissionService pluginTablePermissionService;

    @Autowired
    private PluginVersionService pluginVersionService;

    @Autowired
    private DownloadTokenService downloadTokenService;
    
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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String priceType) {
        
        PageResult<PluginPackageDTO> result = appStoreApiManagementService.getPackages(
            page, size, pluginType, category, keyword, tag, priceType);
        
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
        return appStoreApiManagementService.getPackageDetail(pluginId);
    }

    /**
     * 获取某个插件的发布版本列表（用户端）
     *
     * GET /anon/packages/{pluginId}/versions
     */
    @GetMapping("/{pluginId}/versions")
    public Result<List<AppStorePackageVersionDTO>> getPackageVersions(@PathVariable String pluginId) {
        List<com.keqi.gress.plugin.appstore.admin.dto.PluginVersionDTO> versions = pluginVersionService.getVersions(pluginId);
        if (versions == null) return Result.success(List.of());

        return Result.success(
            versions.stream()
                .map(v -> AppStorePackageVersionDTO.builder()
                    .pluginId(v.getPluginId())
                    .version(v.getVersion())
                    .releaseNotes(v.getReleaseNotes())
                    .fileSize(v.getFileSize())
                    .uploadTime(v.getCreateTime() != null ? v.getCreateTime() : v.getUpdateTime())
                    .current(Boolean.TRUE.equals(v.getIsCurrent()))
                    .build()
                )
                .collect(Collectors.toList())
        );
    }
    
    /**
     * 下载应用包
     * 
     * @param pluginId 插件ID
     * @return 文件流
     */
    @GetMapping("/{pluginId}/download")
    public ResponseEntity<Resource> downloadPackage(
            @PathVariable String pluginId,
            @RequestParam String token,
            HttpServletRequest request) {
            String keyId = request.getHeader("X-AppStore-KeyId");
            String ip = resolveClientIp(request);
            String ua = request.getHeader("User-Agent");
            DownloadTokenService.ConsumeResult consumeResult = downloadTokenService.consume(token, pluginId, null, keyId, ip, ua);
            if (!consumeResult.success()) {
                log.warn("消费下载token失败: pluginId={}, reason={}", pluginId, consumeResult.reason());
                return ResponseEntity.status(401).build();
            }

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
            @PathVariable String version,
            @RequestParam String token,
            HttpServletRequest request) {
        String keyId = request.getHeader("X-AppStore-KeyId");
        String ip = resolveClientIp(request);
        String ua = request.getHeader("User-Agent");
        DownloadTokenService.ConsumeResult consumeResult = downloadTokenService.consume(token, pluginId, version, keyId, ip, ua);
        if (!consumeResult.success()) {
            log.warn("消费下载token失败: pluginId={}, version={}, reason={}", pluginId, version, consumeResult.reason());
            return ResponseEntity.status(401).build();
        }
        log.info("按版本下载应用包: pluginId={}, version={}", pluginId, version);
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
    }

    /**
     * 换取当前版本下载 token（短期一次性）
     */
    @GetMapping("/{pluginId}/download-token")
    public Result<DownloadTokenResponse> issueDownloadToken(
            @PathVariable String pluginId,
            HttpServletRequest request) {
        String keyId = request.getHeader("X-AppStore-KeyId");
        String ip = resolveClientIp(request);
        String ua = request.getHeader("User-Agent");
        DownloadTokenService.TokenIssue issue = downloadTokenService.issue(pluginId, null, keyId, ip, ua);
        return Result.success(DownloadTokenResponse.builder()
            .token(issue.token())
            .expireAtEpochMs(issue.expireAtEpochMs())
            .build());
    }

    /**
     * 换取指定版本下载 token（短期一次性）
     */
    @GetMapping("/{pluginId}/versions/{version}/download-token")
    public Result<DownloadTokenResponse> issueVersionDownloadToken(
            @PathVariable String pluginId,
            @PathVariable String version,
            HttpServletRequest request) {
        String keyId = request.getHeader("X-AppStore-KeyId");
        String ip = resolveClientIp(request);
        String ua = request.getHeader("User-Agent");
        DownloadTokenService.TokenIssue issue = downloadTokenService.issue(pluginId, version, keyId, ip, ua);
        return Result.success(DownloadTokenResponse.builder()
            .token(issue.token())
            .expireAtEpochMs(issue.expireAtEpochMs())
            .build());
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            String[] parts = xff.split(",");
            if (parts.length > 0 && parts[0] != null && !parts[0].trim().isBlank()) {
                return parts[0].trim();
            }
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * 根据插件ID获取表授权信息
     *
     * @param pluginId 插件ID
     * @return 表授权信息列表
     */
    @GetMapping("/{pluginId}/table-permissions")
    public Result<List<PluginTablePermissionDTO>> getTablePermissions(@PathVariable String pluginId) {
            List<PluginTablePermissionDTO> permissions = pluginTablePermissionService.listByPluginId(pluginId);
            return Result.success(permissions);
    }

    /**
     * 获取插件配置元数据（从 jar 包中解析）
     * 
     * @param pluginId 插件ID
     * @return 配置元数据列表
     */
    @GetMapping("/{pluginId}/config/metadata")
    public Result<List<com.keqi.gress.common.plugin.FormMetadataParser.FieldMetadata>> getPluginConfigMetadata(@PathVariable String pluginId) {

            return Result.error("此接口需要通过 AppStoreApiService 调用，请使用 /plugins/appstore/middlewares/remote/{pluginId}/config/metadata");
    }

}
