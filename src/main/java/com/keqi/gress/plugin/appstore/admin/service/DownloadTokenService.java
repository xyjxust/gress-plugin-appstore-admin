package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.plugin.appstore.admin.config.AppStoreAdminConfig;
import com.keqi.gress.plugin.appstore.admin.dto.ApiKeyDownloadLogDTO;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreApiKey;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreDownloadToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DownloadTokenService {

    @Autowired
    private PluginLambdaDataSource dataSource;

    @Autowired
    private AppStoreAdminConfig config;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private AuditLogService auditLogService;

    public TokenIssue issue(String pluginId, String version, String keyId, String ip, String ua) {
        if (pluginId == null || pluginId.isBlank()) {
            throw new IllegalArgumentException("pluginId 不能为空");
        }
        if (keyId == null || keyId.isBlank()) {
            throw new IllegalArgumentException("keyId 不能为空");
        }
        AppStoreApiKey apiKey = apiKeyService.getByKeyId(keyId);
        String userId = apiKey != null ? apiKey.getUserId() : null;

        String token = "dt_" + UUID.randomUUID().toString().replace("-", "");
        String hash = sha256Hex(token);
        long ttl = resolveTtlSeconds();
        LocalDateTime expireAt = LocalDateTime.now().plusSeconds(ttl);
        LocalDateTime now = LocalDateTime.now();

        AppStoreDownloadToken entity = AppStoreDownloadToken.builder()
            .tokenHash(hash)
            .pluginId(pluginId.trim())
            .version(version != null && !version.isBlank() ? version.trim() : null)
            .keyId(keyId.trim())
            .userId(userId)
            .issuedIp(safe(ip, 64))
            .issuedUa(safe(ua, 512))
            .expireAt(expireAt)
            .used(Boolean.FALSE)
            .build();
        entity.setCreateTime(now);
        dataSource.insert(entity);

        Map<String, Object> after = new LinkedHashMap<>();
        after.put("pluginId", entity.getPluginId());
        after.put("version", entity.getVersion());
        after.put("keyId", entity.getKeyId());
        after.put("issuedIp", entity.getIssuedIp());
        after.put("issuedUa", entity.getIssuedUa());
        after.put("expireAtEpochMs", expireAt.toInstant(ZoneOffset.UTC).toEpochMilli());
        auditLogService.logSuccess(
            "ISSUE_DOWNLOAD_TOKEN",
            "签发下载令牌",
            "PLUGIN",
            entity.getPluginId(),
            entity.getPluginId(),
            userId != null ? userId : keyId,
            userId != null ? userId : keyId,
            null,
            after
        );
        return new TokenIssue(token, expireAt.toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    /**
     * 一次性消费 token
     */
    public ConsumeResult consume(String token, String pluginId, String version, String keyId, String ip, String ua) {
        if (token == null || token.isBlank()) {
            auditConsumeFailure("MISSING_TOKEN", pluginId, version, keyId, ip, ua);
            return ConsumeResult.fail("MISSING_TOKEN");
        }
        String hash = sha256Hex(token.trim());
        AppStoreDownloadToken row = dataSource.lambdaQuery(AppStoreDownloadToken.class)
            .eq(AppStoreDownloadToken::getTokenHash, hash)
            .one();
        if (row == null) {
            auditConsumeFailure("TOKEN_NOT_FOUND", pluginId, version, keyId, ip, ua);
            return ConsumeResult.fail("TOKEN_NOT_FOUND");
        }
        if (Boolean.TRUE.equals(row.getUsed())) {
            auditConsumeFailure("TOKEN_ALREADY_USED", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("TOKEN_ALREADY_USED");
        }
        if (row.getExpireAt() == null || row.getExpireAt().isBefore(LocalDateTime.now())) {
            auditConsumeFailure("TOKEN_EXPIRED", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("TOKEN_EXPIRED");
        }
        if (pluginId == null || !pluginId.equals(row.getPluginId())) {
            auditConsumeFailure("PLUGIN_MISMATCH", pluginId, version, keyId, ip, ua);
            return ConsumeResult.fail("PLUGIN_MISMATCH");
        }
        if (keyId == null || keyId.isBlank() || !keyId.equals(row.getKeyId())) {
            auditConsumeFailure("KEY_MISMATCH", row.getPluginId(), row.getVersion(), keyId, ip, ua);
            return ConsumeResult.fail("KEY_MISMATCH");
        }
        String expectVersion = row.getVersion();
        String actualVersion = (version != null && !version.isBlank()) ? version.trim() : null;
        if (expectVersion != null ? !expectVersion.equals(actualVersion) : actualVersion != null) {
            auditConsumeFailure("VERSION_MISMATCH", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("VERSION_MISMATCH");
        }
        if (row.getIssuedIp() != null && !row.getIssuedIp().isBlank() && !row.getIssuedIp().equals(safe(ip, 64))) {
            auditConsumeFailure("IP_MISMATCH", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("IP_MISMATCH");
        }
        if (row.getIssuedUa() != null && !row.getIssuedUa().isBlank() && !row.getIssuedUa().equals(safe(ua, 512))) {
            auditConsumeFailure("UA_MISMATCH", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("UA_MISMATCH");
        }

        int updated = dataSource.lambdaUpdate(AppStoreDownloadToken.class)
            .eq(AppStoreDownloadToken::getId, row.getId())
            .eq(AppStoreDownloadToken::getUsed, Boolean.FALSE)
            .set(AppStoreDownloadToken::getUsed, Boolean.TRUE)
            .set(AppStoreDownloadToken::getUsedAt, LocalDateTime.now())
            .set(AppStoreDownloadToken::getConsumedIp, safe(ip, 64))
            .set(AppStoreDownloadToken::getConsumedUa, safe(ua, 512))
            .update();
        if (updated > 0) {
            Map<String, Object> after = new LinkedHashMap<>();
            after.put("pluginId", row.getPluginId());
            after.put("version", row.getVersion());
            after.put("keyId", row.getKeyId());
            after.put("issuedIp", row.getIssuedIp());
            after.put("consumedIp", safe(ip, 64));
            after.put("issuedUa", row.getIssuedUa());
            after.put("consumedUa", safe(ua, 512));
            after.put("usedAt", LocalDateTime.now().toString());
            String op = row.getVersion() == null ? "下载当前版本插件包" : "下载指定版本插件包";
            auditLogService.logSuccess(
                "CONSUME_DOWNLOAD_TOKEN",
                op,
                "PLUGIN",
                row.getPluginId(),
                row.getPluginId(),
                row.getUserId() != null ? row.getUserId() : row.getKeyId(),
                row.getUserId() != null ? row.getUserId() : row.getKeyId(),
                null,
                after
            );
        }
        if (updated <= 0) {
            auditConsumeFailure("TOKEN_RACE_CONFLICT", row.getPluginId(), row.getVersion(), row.getKeyId(), ip, ua);
            return ConsumeResult.fail("TOKEN_RACE_CONFLICT");
        }
        return ConsumeResult.ok();
    }

    private static String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long resolveTtlSeconds() {
        Integer ttl = config != null && config.getSecurity() != null
            ? config.getSecurity().getDownloadTokenTtlSeconds()
            : null;
        if (ttl == null || ttl <= 0) return 120L;
        return ttl;
    }

    private static String safe(String value, int maxLen) {
        if (value == null) return null;
        String v = value.trim();
        if (v.length() <= maxLen) return v;
        return v.substring(0, maxLen);
    }

    private void auditConsumeFailure(String reason, String pluginId, String version, String keyId, String ip, String ua) {
        Map<String, Object> after = new LinkedHashMap<>();
        after.put("pluginId", pluginId);
        after.put("version", version);
        after.put("keyId", keyId);
        after.put("ip", safe(ip, 64));
        after.put("ua", safe(ua, 512));
        after.put("reason", reason);
        auditLogService.logFailure(
            "CONSUME_DOWNLOAD_TOKEN",
            "消费下载令牌",
            "PLUGIN",
            pluginId != null ? pluginId : "-",
            pluginId != null ? pluginId : "-",
            keyId != null ? keyId : "anonymous",
            keyId != null ? keyId : "anonymous",
            after.toString()
        );
    }

    public PageResult<ApiKeyDownloadLogDTO> queryDownloadLogsByKeyId(String keyId, Integer page, Integer size) {
        if (keyId == null || keyId.isBlank()) {
            throw new IllegalArgumentException("keyId 不能为空");
        }
        int p = page == null || page < 1 ? 1 : page;
        int s = size == null || size < 1 ? 20 : size;
        IPage<AppStoreDownloadToken> tokenPage = dataSource.lambdaQuery(AppStoreDownloadToken.class)
            .eq(AppStoreDownloadToken::getKeyId, keyId.trim())
            .orderByDesc(AppStoreDownloadToken::getCreateTime)
            .page(p, s);

        return PageResult.of(
            tokenPage.getRecords().stream().map(this::toLogDto).collect(Collectors.toList()),
            tokenPage.getTotal(),
            p,
            s
        );
    }

    private ApiKeyDownloadLogDTO toLogDto(AppStoreDownloadToken row) {
        return ApiKeyDownloadLogDTO.builder()
            .id(row.getId())
            .keyId(row.getKeyId())
            .userId(row.getUserId())
            .pluginId(row.getPluginId())
            .version(row.getVersion())
            .issuedIp(row.getIssuedIp())
            .consumedIp(row.getConsumedIp())
            .issuedUa(row.getIssuedUa())
            .consumedUa(row.getConsumedUa())
            .used(row.getUsed())
            .createTime(row.getCreateTime())
            .usedAt(row.getUsedAt())
            .build();
    }

    public record TokenIssue(String token, long expireAtEpochMs) {}
    public record ConsumeResult(boolean success, String reason) {
        public static ConsumeResult ok() { return new ConsumeResult(true, "OK"); }
        public static ConsumeResult fail(String reason) { return new ConsumeResult(false, reason); }
    }
}
