package com.keqi.gress.plugin.appstore.admin.service.crypto;

import com.keqi.gress.plugin.appstore.admin.config.AppStoreAdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-GCM helper for encrypting keystore passwords in DB.
 *
 * <p>Master key is provided by plugin config.</p>
 */
@Service
public class AesGcmCryptoUtil {

    private static final int IV_LEN_BYTES = 12; // recommended for GCM
    private static final int TAG_BITS = 128;

    @Autowired
    private AppStoreAdminConfig appStoreAdminConfig;

    public String encrypt(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        String masterKey = requireMasterKey();
        byte[] keyBytes = sha256(masterKey);

        byte[] iv = new byte[IV_LEN_BYTES];
        new SecureRandom().nextBytes(iv);

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(cipherBytes);
        } catch (Exception e) {
            throw new IllegalStateException("Encrypt failed: " + e.getMessage(), e);
        }
    }

    public String decrypt(String encrypted) {
        if (encrypted == null || encrypted.isEmpty()) {
            return encrypted;
        }
        String masterKey = requireMasterKey();
        byte[] keyBytes = sha256(masterKey);

        String[] parts = encrypted.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalStateException("Invalid encrypted value format");
        }
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] cipherBytes = Base64.getDecoder().decode(parts[1]);

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_BITS, iv));
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Decrypt failed: " + e.getMessage(), e);
        }
    }

    private String requireMasterKey() {
        String masterKey = appStoreAdminConfig != null && appStoreAdminConfig.getSecurity() != null
                ? appStoreAdminConfig.getSecurity().getMasterEncryptionKey()
                : null;
        if (masterKey == null || masterKey.isBlank()) {
            throw new IllegalStateException("Missing plugin config: appstoreAdmin.security.masterEncryptionKey");
        }
        return masterKey;
    }

    private static byte[] sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("sha256 failed: " + e.getMessage(), e);
        }
    }
}
