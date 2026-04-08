package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for generating a new self-signed signing key.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateSigningKeyRequest {
    /**
     * Keystore alias. If null/blank, server will generate one.
     */
    private String alias;

    /**
     * Cert validity in days.
     */
    private Integer validityDays;

    /**
     * Whether to activate newly generated key.
     */
    private Boolean makeActive;

    /**
     * Trusted window days for the newly activated key AND the previous active key(s).
     */
    private Integer trustedWindowDays;
}

