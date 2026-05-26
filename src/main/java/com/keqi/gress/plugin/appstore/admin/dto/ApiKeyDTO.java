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
public class ApiKeyDTO {
    private Long id;
    private String userId;
    private String keyId;
    private String scopes;
    private Boolean enabled;
    private LocalDateTime expireAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

