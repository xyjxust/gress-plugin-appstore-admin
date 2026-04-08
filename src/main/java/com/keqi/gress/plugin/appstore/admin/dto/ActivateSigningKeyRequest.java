package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Activate key request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateSigningKeyRequest {
    /**
     * Trusted window days for new active key and previous active key(s).
     */
    private Integer trustedWindowDays;
}

