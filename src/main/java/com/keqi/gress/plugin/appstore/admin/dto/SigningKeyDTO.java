package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for signing key management UI.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigningKeyDTO {
    private String keyId;
    private String alias;
    private String fingerprintSha256;
    private Boolean active;
    private LocalDateTime trustedUntil;
    private LocalDateTime revokedAt;
    private LocalDateTime createTime;
}

