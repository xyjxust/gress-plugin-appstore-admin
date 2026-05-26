package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyDownloadLogDTO {
    private Long id;
    private String keyId;
    private String userId;
    private String pluginId;
    private String version;
    private String issuedIp;
    private String consumedIp;
    private String issuedUa;
    private String consumedUa;
    private Boolean used;
    private LocalDateTime createTime;
    private LocalDateTime usedAt;
}

