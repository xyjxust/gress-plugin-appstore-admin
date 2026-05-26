package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApiKeyRequest {
    /**
     * 绑定用户ID（每用户一个 key）
     */
    private String userId;

    /**
     * scopes（可选，逗号分隔或 JSON）
     */
    private String scopes;

    /**
     * 过期秒数（可选，<=0 或空表示不过期）
     */
    private Long expireInSeconds;
}

