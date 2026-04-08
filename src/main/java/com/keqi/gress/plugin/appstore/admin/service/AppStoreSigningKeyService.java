package com.keqi.gress.plugin.appstore.admin.service;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.appstore.admin.dto.ActivateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.GenerateSigningKeyRequest;
import com.keqi.gress.plugin.appstore.admin.dto.SigningKeyDTO;
import com.keqi.gress.plugin.appstore.admin.dto.TrustedRootDTO;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreSigningKey;
import com.keqi.gress.plugin.appstore.admin.service.crypto.AesGcmCryptoUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * AppStore signing keys management.
 *
 * <p>Stage B: supports rotation by keeping multiple trusted roots during a window.</p>
 */
@Service
public class AppStoreSigningKeyService {

    private static final Log log = LogFactory.get(AppStoreSigningKeyService.class);

    private static final String KEYTOOL_ALGO = "RSA";
    private static final int KEYTOOL_KEYSIZE = 2048;

    private static final String SIGNER_DNAME = "CN=Gress AppStore Signing, OU=Gress, O=Gress, L=Shanghai, ST=Shanghai, C=CN";

    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;

    @Inject(source = Inject.BeanSource.SPRING)
    private FileStorageService fileStorageService;

    /**
     * List keys for admin UI.
     */
    public List<SigningKeyDTO> listSigningKeys() {
        List<AppStoreSigningKey> keys = dataSource.lambdaQuery(AppStoreSigningKey.class)
                .orderByDesc(AppStoreSigningKey::getCreateTime)
                .list();

        return keys.stream().map(this::toSigningKeyDTO).toList();
    }

    /**
     * Get active key for signing newly uploaded jars.
     */
    public AppStoreSigningKey getActiveKeyOrNull() {
        return dataSource.lambdaQuery(AppStoreSigningKey.class)
                .eq(AppStoreSigningKey::getActive, true)
                .one();
    }

    public String getPublicKeyPemOrNull(String keyId) {
        if (keyId == null || keyId.isBlank()) {
            return null;
        }
        AppStoreSigningKey key = dataSource.lambdaQuery(AppStoreSigningKey.class)
                .eq(AppStoreSigningKey::getKeyId, keyId)
                .one();
        return key == null ? null : key.getPublicKeyPem();
    }

    /**
     * Trusted roots for client verification (non-revoked and within trusted window).
     */
    public List<TrustedRootDTO> getTrustedRoots() {
        String sql = """
                SELECT key_id, alias, public_key_pem, public_fingerprint_sha256
                FROM appstore_signing_key
                WHERE revoked_at IS NULL
                  AND (
                        active = 1
                        OR (trusted_until IS NOT NULL AND trusted_until > NOW())
                  )
                ORDER BY active DESC, create_time DESC
                """;

        List<Map<String, Object>> rows = dataSource.dynamicSql(sql).query();
        if (rows == null || rows.isEmpty()) {
            return List.of();
        }

        List<TrustedRootDTO> out = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            TrustedRootDTO dto = TrustedRootDTO.builder()
                    .keyId((String) row.get("key_id"))
                    .alias((String) row.get("alias"))
                    .publicKeyPem((String) row.get("public_key_pem"))
                    .fingerprintSha256((String) row.get("public_fingerprint_sha256"))
                    .build();
            out.add(dto);
        }
        return out;
    }

    /**
     * Generate a new RSA self-signed key and store to DB + FileStorage.
     *
     * @param request generate settings
     * @return created keyId
     */
    public Result<SigningKeyDTO> generateSigningKey(GenerateSigningKeyRequest request, String operatorName) {
        try {
            if (request == null) {
                return Result.error("request is null");
            }

            int validityDays = request.getValidityDays() != null && request.getValidityDays() > 0
                    ? request.getValidityDays()
                    : 3650;

            boolean makeActive = Boolean.TRUE.equals(request.getMakeActive());
            int trustedWindowDays = request.getTrustedWindowDays() != null && request.getTrustedWindowDays() > 0
                    ? request.getTrustedWindowDays()
                    : 30;

            String alias = request.getAlias();
            if (alias == null || alias.trim().isEmpty()) {
                alias = "appstore-signing-" + randomAlphaNum(10).toLowerCase();
            }

            String keyId = UUID.randomUUID().toString().replace("-", "");

            // Generate passwords internally; don't expose to UI.
            String keystorePassword = randomAlphaNum(32);
            String keyPassword = keystorePassword;

            Path tempKeystore = Files.createTempFile("appstore-signing-", ".p12");
            // keytool -genkeypair 期望自己创建 keystore，如果文件已存在且为空会报错：
            // "密钥库文件存在, 但为空"。因此这里先删掉，占位只用路径。
            try {
                Files.deleteIfExists(tempKeystore);
            } catch (Exception ignore) {
            }
            Path tempCertPem = Files.createTempFile("appstore-signing-", ".pem");

            try {
                // 1) Generate key pair (self-signed)
                List<String> genCmd = new ArrayList<>();
                genCmd.add("keytool");
                genCmd.add("-genkeypair");
                genCmd.add("-alias");
                genCmd.add(alias);
                genCmd.add("-keyalg");
                genCmd.add(KEYTOOL_ALGO);
                genCmd.add("-keysize");
                genCmd.add(String.valueOf(KEYTOOL_KEYSIZE));
                genCmd.add("-sigalg");
                genCmd.add("SHA256withRSA");
                genCmd.add("-validity");
                genCmd.add(String.valueOf(validityDays));
                genCmd.add("-storetype");
                genCmd.add("PKCS12");
                genCmd.add("-keystore");
                genCmd.add(tempKeystore.toString());
                genCmd.add("-storepass");
                genCmd.add(keystorePassword);
                genCmd.add("-keypass");
                genCmd.add(keyPassword);
                genCmd.add("-dname");
                genCmd.add(SIGNER_DNAME);

                ProcessBuilder genPb = new ProcessBuilder(genCmd);
                genPb.redirectErrorStream(true);
                Process genP = genPb.start();
                String genOut = new String(genP.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                int genExit = genP.waitFor();
                if (genExit != 0) {
                    return Result.error("keytool genkeypair failed: exitCode=" + genExit + ", output=" + genOut);
                }

                // 2) Export cert PEM
                List<String> exportCmd = new ArrayList<>();
                exportCmd.add("keytool");
                exportCmd.add("-exportcert");
                exportCmd.add("-rfc");
                exportCmd.add("-alias");
                exportCmd.add(alias);
                exportCmd.add("-keystore");
                exportCmd.add(tempKeystore.toString());
                exportCmd.add("-storetype");
                exportCmd.add("PKCS12");
                exportCmd.add("-storepass");
                exportCmd.add(keystorePassword);
                exportCmd.add("-file");
                exportCmd.add(tempCertPem.toString());

                ProcessBuilder exportPb = new ProcessBuilder(exportCmd);
                exportPb.redirectErrorStream(true);
                Process exportP = exportPb.start();
                String exportOut = new String(exportP.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                int exportExit = exportP.waitFor();
                if (exportExit != 0) {
                    return Result.error("keytool exportcert failed: exitCode=" + exportExit + ", output=" + exportOut);
                }

                String publicPem = Files.readString(tempCertPem, StandardCharsets.UTF_8);
                publicPem = publicPem.trim();

                // 3) fingerprint from cert bytes
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(
                        new java.io.ByteArrayInputStream(publicPem.getBytes(StandardCharsets.UTF_8)));
                String fingerprint = sha256Hex(cert.getEncoded());

                // 4) Upload keystore to FileStorage
                String fileName = "appstore-signing-keystore-" + keyId + ".p12";
                String keystoreUrl;
                try (InputStream in = Files.newInputStream(tempKeystore)) {
                    keystoreUrl = fileStorageService
                            .upload(in, fileName)
                            .withMetadata("category", "appstore-signing")
                            .withMetadata("keyId", keyId)
                            .onSuccess(savedUrl -> log.info("Signing keystore saved: {}", savedUrl))
                            .onError(e -> {
                                throw new RuntimeException("Signing keystore upload failed: " + e.getMessage(), e);
                            })
                            .get();
                }

                if (keystoreUrl == null || keystoreUrl.isBlank()) {
                    return Result.error("keystore upload returned empty url");
                }

                String storeEnc = AesGcmCryptoUtil.encrypt(keystorePassword);
                String keyEnc = AesGcmCryptoUtil.encrypt(keyPassword);

                LocalDateTime now = LocalDateTime.now();
                AppStoreSigningKey entity = AppStoreSigningKey.builder()
                        .keyId(keyId)
                        .alias(alias)
                        .keystoreUrl(keystoreUrl)
                        .storePasswordEnc(storeEnc)
                        .keyPasswordEnc(keyEnc)
                        .publicKeyPem(publicPem)
                        .publicFingerprintSha256(fingerprint)
                        .active(false) // activated later if needed
                        .trustedUntil(null)
                        .revokedAt(null)
                        .createdBy(operatorName)
                        .updatedBy(operatorName)
                        .createTime(now)
                        .updateTime(now)
                        .build();

                dataSource.insert(entity);

                if (makeActive) {
                    activateKey(keyId, trustedWindowDays, operatorName);
                }

                return Result.success(toSigningKeyDTO(entity));
            } finally {
                try {
                    Files.deleteIfExists(tempKeystore);
                } catch (Exception ignore) {}
                try {
                    Files.deleteIfExists(tempCertPem);
                } catch (Exception ignore) {}
            }
        } catch (Exception e) {
            log.error("generateSigningKey failed", e);
            return Result.error("生成签名密钥失败: " + e.getMessage());
        }
    }

    public Result<Void> activateKey(String keyId, ActivateSigningKeyRequest request, String operatorName) {
        if (keyId == null || keyId.isBlank()) {
            return Result.error("keyId is empty");
        }
        int trustedWindowDays = request == null || request.getTrustedWindowDays() == null || request.getTrustedWindowDays() <= 0
                ? 30
                : request.getTrustedWindowDays();
        try {
            activateKey(keyId, trustedWindowDays, operatorName);
            return Result.success();
        } catch (Exception e) {
            log.error("activateKey failed: keyId={}", keyId, e);
            return Result.error("激活密钥失败: " + e.getMessage());
        }
    }

    private void activateKey(String keyId, int trustedWindowDays, String operatorName) {
        dataSource.executeTransaction(() -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime until = now.plusDays(trustedWindowDays);

            // 1) Deactivate currently ACTIVE key(s) and set their trustedUntil (rotation window)
            dataSource.lambdaUpdate(AppStoreSigningKey.class)
                    .eq(AppStoreSigningKey::getActive, true)
                    .set(AppStoreSigningKey::getActive, false)
                    .set(AppStoreSigningKey::getTrustedUntil, until)
                    .set(AppStoreSigningKey::getUpdatedBy, operatorName)
                    .update();

            // 2) Extend already-trusted keys so that multiple rotations within the window stay smooth.
            String extendSql = """
                    UPDATE appstore_signing_key
                    SET trusted_until = #{trustedUntil},
                        updated_by = #{updatedBy}
                    WHERE revoked_at IS NULL
                      AND trusted_until IS NOT NULL
                      AND trusted_until > NOW()
                    """;
            dataSource.dynamicSql(extendSql)
                    .param("trustedUntil", until)
                    .param("updatedBy", operatorName)
                    .execute();

            // 3) Activate target key
            AppStoreSigningKey target = dataSource.lambdaQuery(AppStoreSigningKey.class)
                    .eq(AppStoreSigningKey::getKeyId, keyId)
                    .one();

            if (target == null) {
                throw new IllegalStateException("Key not found: " + keyId);
            }

            dataSource.lambdaUpdate(AppStoreSigningKey.class)
                    .eq(AppStoreSigningKey::getKeyId, keyId)
                    .set(AppStoreSigningKey::getActive, true)
                    .set(AppStoreSigningKey::getTrustedUntil, until)
                    .set(AppStoreSigningKey::getRevokedAt, null)
                    .set(AppStoreSigningKey::getUpdatedBy, operatorName)
                    .update();
        });
    }

    /**
     * Download & decrypt active keystore for signing.
     */
    public Result<java.util.Map<String, Object>> getActiveKeystoreMaterialOrError() {
        try {
            AppStoreSigningKey active = getActiveKeyOrNull();
            if (active == null) {
                return Result.error("No active signing key configured");
            }

            String storePass = AesGcmCryptoUtil.decrypt(active.getStorePasswordEnc());
            String keyPass = AesGcmCryptoUtil.decrypt(active.getKeyPasswordEnc());

            return Result.success(java.util.Map.of(
                    "keystoreUrl", active.getKeystoreUrl(),
                    "alias", active.getAlias(),
                    "storePassword", storePass,
                    "keyPassword", keyPass
            ));
        } catch (Exception e) {
            return Result.error("Failed to decrypt active signing key: " + e.getMessage());
        }
    }

    private SigningKeyDTO toSigningKeyDTO(AppStoreSigningKey entity) {
        if (entity == null) {
            return null;
        }
        return SigningKeyDTO.builder()
                .keyId(entity.getKeyId())
                .alias(entity.getAlias())
                .fingerprintSha256(entity.getPublicFingerprintSha256())
                .active(Boolean.TRUE.equals(entity.getActive()))
                .trustedUntil(entity.getTrustedUntil())
                .revokedAt(entity.getRevokedAt())
                .createTime(entity.getCreateTime())
                .build();
    }

    private static String sha256Hex(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);
        return HexFormat.of().formatHex(digest);
    }

    private static String randomAlphaNum(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(r.nextInt(chars.length())));
        }
        return sb.toString();
    }
}

