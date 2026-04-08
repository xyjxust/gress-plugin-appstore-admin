package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO returned to client for jar signature verification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrustedRootDTO {
    private String keyId;
    private String alias;
    private String publicKeyPem;
    private String fingerprintSha256;
}

