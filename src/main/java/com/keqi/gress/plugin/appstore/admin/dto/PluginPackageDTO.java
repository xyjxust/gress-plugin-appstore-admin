package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 插件包 DTO
 * 用于用户端应用商店和管理端版本管理
 */
@Data
public class PluginPackageDTO {
    
    /** 应用包ID */
    private Long id;
    
    /** 插件ID */
    private String pluginId;
    
    /** 插件名称 */
    private String pluginName;
    
    /** 插件类型（TASK/TRIGGER/APPLICATION） */
    private String pluginType;
    
    /** 版本号 */
    private String version;
    
    /** 文件存储URL */
    private String fileUrl;
    
    /** 文件名 */
    private String fileName;
    
    /** 文件大小（字节） */
    private Long fileSize;
    
    /** 版本描述 */
    private String description;
    
    /** 发布说明 */
    private String releaseNotes;
    
    /** 图标路径 */
    private String icon;
    
    /** 分类 */
    private String category;
    
    /** 开发者ID */
    private String developerId;
    
    /** 开发者名称 */
    private String developerName;
    
    /** 上传人 */
    private String uploadBy;
    
    /** 上传时间 */
    private LocalDateTime uploadTime;
    
    /** 状态（ONLINE/OFFLINE/DELISTED 或 PENDING/APPROVED/REJECTED） */
    private String status;
    
    /** 下载/安装次数 */
    private Integer downloadCount;
    
    /** 平均评分 */
    private Double ratingAverage;
    
    /** MD5校验值 */
    private String md5;
    
    /** SHA256校验值 */
    private String sha256;
    
    /** 依赖信息（JSON格式） */
    private String dependencies;
}
