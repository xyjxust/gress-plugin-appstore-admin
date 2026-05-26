package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.ApiKeyDTO;
import com.keqi.gress.plugin.appstore.admin.dto.CreateApiKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.CreateApiKeyResponse;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreApiKey;
import com.keqi.gress.plugin.appstore.admin.service.crypto.AesGcmCryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApiKeyService {

    @Autowired
    private PluginLambdaDataSource dataSource;

    @Autowired
    private AesGcmCryptoUtil aesGcmCryptoUtil;

    private static final SecureRandom RANDOM = new SecureRandom();

    public List<ApiKeyDTO> list(String userId) {
        List<AppStoreApiKey> rows = dataSource.lambdaQuery(AppStoreApiKey.class)
            .func(q -> {
                if (userId != null && !userId.isBlank()) {
                    q.eq(AppStoreApiKey::getUserId, userId.trim());
                }
                q.orderByDesc(AppStoreApiKey::getUpdateTime);
            })
            .list();
        return rows.stream().map(this::toDto).collect(Collectors.toList());
    }

    public CreateApiKeyResponse createOrReset(CreateApiKeyRequest req) {
        if (req == null || req.getUserId() == null || req.getUserId().isBlank()) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        String userId = req.getUserId().trim();

        // 每用户一个 key：存在则 reset；不存在则 create
        AppStoreApiKey existing = dataSource.lambdaQuery(AppStoreApiKey.class)
            .eq(AppStoreApiKey::getUserId, userId)
            .one();

        String keyId = existing != null ? existing.getKeyId() : generateKeyId();
        String secret = generateSecret();
        String secretHash = sha256Hex(secret);
        String secretEnc = aesGcmCryptoUtil.encrypt(secret);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireAt = null;
        if (req.getExpireInSeconds() != null && req.getExpireInSeconds() > 0) {
            expireAt = now.plusSeconds(req.getExpireInSeconds());
        }

        if (existing == null) {
            AppStoreApiKey entity = AppStoreApiKey.builder()
                .userId(userId)
                .keyId(keyId)
                .secretHash(secretHash)
                .secretEnc(secretEnc)
                .scopes(req.getScopes())
                .enabled(Boolean.TRUE)
                .expireAt(expireAt)
                .build();
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            dataSource.insert(entity);
            existing = entity;
        } else {
            String scopes = req.getScopes() != null ? req.getScopes() : existing.getScopes();
            dataSource.lambdaUpdate(AppStoreApiKey.class)
                .eq(AppStoreApiKey::getId, existing.getId())
                .set(AppStoreApiKey::getSecretHash, secretHash)
                .set(AppStoreApiKey::getSecretEnc, secretEnc)
                .set(AppStoreApiKey::getScopes, scopes)
                .set(AppStoreApiKey::getEnabled, Boolean.TRUE)
                .set(AppStoreApiKey::getExpireAt, expireAt)
                .set(AppStoreApiKey::getUpdateTime, now)
                .update();
            existing.setSecretHash(secretHash);
            existing.setSecretEnc(secretEnc);
            existing.setScopes(scopes);
            existing.setEnabled(Boolean.TRUE);
            existing.setExpireAt(expireAt);
            existing.setUpdateTime(now);
        }

        String decrypted = decryptSecret(existing);
        if (!secret.equals(decrypted)) {
            log.error("createOrReset: stored secretEnc does not roundtrip to plaintext for userId={}", userId);
            throw new IllegalStateException("密钥存储校验失败");
        }

        return CreateApiKeyResponse.builder()
            .apiKey(toDto(existing))
            .secret(secret)
            .build();
    }

    public void setEnabled(String keyId, boolean enabled) {
        if (keyId == null || keyId.isBlank()) throw new IllegalArgumentException("keyId 不能为空");
        dataSource.lambdaUpdate(AppStoreApiKey.class)
            .eq(AppStoreApiKey::getKeyId, keyId.trim())
            .set(AppStoreApiKey::getEnabled, enabled)
            .set(AppStoreApiKey::getUpdateTime, LocalDateTime.now())
            .update();
    }

    public AppStoreApiKey getByKeyId(String keyId) {
        if (keyId == null || keyId.isBlank()) return null;
        return dataSource.lambdaQuery(AppStoreApiKey.class)
            .eq(AppStoreApiKey::getKeyId, keyId.trim())
            .one();
    }

    public boolean verifySecret(AppStoreApiKey key, String secret) {
        if (key == null || secret == null) return false;
        return sha256Hex(secret).equalsIgnoreCase(key.getSecretHash());
    }

    public String decryptSecret(AppStoreApiKey key) {
        if (key == null) return null;
        if (key.getSecretEnc() == null || key.getSecretEnc().isBlank()) return null;
        return aesGcmCryptoUtil.decrypt(key.getSecretEnc());
    }

    /**
     * 管理端按 keyId 解密并返回明文 secret（用于复制等）
     */
    public String revealSecret(String keyId) {
        if (keyId == null || keyId.isBlank()) {
            throw new IllegalArgumentException("keyId 不能为空");
        }
        AppStoreApiKey key = getByKeyId(keyId);
        if (key == null) {
            throw new IllegalArgumentException("Key 不存在");
        }
        String secret = decryptSecret(key);
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("密钥不可用");
        }
        if (!verifySecret(key, secret)) {
            log.error("revealSecret: decrypted secret does not match secret_hash for keyId={}", keyId);
            throw new IllegalStateException("密钥校验失败");
        }
        return secret;
    }

    public void touchLastUsed(String keyId) {
        if (keyId == null || keyId.isBlank()) return;
        dataSource.lambdaUpdate(AppStoreApiKey.class)
            .eq(AppStoreApiKey::getKeyId, keyId.trim())
            .set(AppStoreApiKey::getLastUsedAt, LocalDateTime.now())
            .update();
    }

    private ApiKeyDTO toDto(AppStoreApiKey e) {
        if (e == null) return null;
        return ApiKeyDTO.builder()
            .id(e.getId())
            .userId(e.getUserId())
            .keyId(e.getKeyId())
            .scopes(e.getScopes())
            .enabled(e.getEnabled())
            .expireAt(e.getExpireAt())
            .lastUsedAt(e.getLastUsedAt())
            .createTime(e.getCreateTime())
            .updateTime(e.getUpdateTime())
            .build();
    }

    private static String generateKeyId() {
        // 短且可读：k_ + 16 hex
        byte[] buf = new byte[8];
        RANDOM.nextBytes(buf);
        return "k_" + HexFormat.of().formatHex(buf);
    }

    private static String generateSecret() {
        // 足够强：uuid + 16 bytes random
        byte[] buf = new byte[16];
        RANDOM.nextBytes(buf);
        return "s_" + UUID.randomUUID() + "_" + HexFormat.of().formatHex(buf);
    }

    private static String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(dig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
