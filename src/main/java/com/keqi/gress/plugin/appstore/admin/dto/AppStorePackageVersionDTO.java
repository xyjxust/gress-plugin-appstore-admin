package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户端应用商店：发布版本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppStorePackageVersionDTO {
    private String pluginId;
    private String version;
    private String releaseNotes;
    private Long fileSize;
    private LocalDateTime uploadTime;
    private Boolean current;
}

