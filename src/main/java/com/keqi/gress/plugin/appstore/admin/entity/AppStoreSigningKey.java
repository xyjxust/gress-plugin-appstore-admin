package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * AppStore Signing Key Entity
 *
 * <p>Stores signing keystore metadata (keystore is stored in FileStorageService).</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_signing_key")
public class AppStoreSigningKey extends BaseEntity {

    /**
     * Stable key id used by UI/client.
     */
    private String keyId;

    /**
     * Keystore alias.
     */
    private String alias;

    /**
     * Keystore URL in FileStorageService.
     */
    private String keystoreUrl;

    /**
     * Encrypted store password (AES-GCM).
     */
    private String storePasswordEnc;

    /**
     * Encrypted key password (AES-GCM).
     */
    private String keyPasswordEnc;

    /**
     * Trusted public material PEM (CERTIFICATE/PUBLIC KEY).
     */
    private String publicKeyPem;

    /**
     * SHA-256 fingerprint (hex).
     */
    private String publicFingerprintSha256;

    /**
     * Whether this key is currently active for signing newly uploaded jars.
     */
    private Boolean active;

    /**
     * Trusted until (rotation window). null => always trusted.
     */
    private LocalDateTime trustedUntil;

    /**
     * Revoked time. null => not revoked.
     */
    private LocalDateTime revokedAt;
}
