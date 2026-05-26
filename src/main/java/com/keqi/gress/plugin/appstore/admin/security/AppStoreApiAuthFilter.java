package com.keqi.gress.plugin.appstore.admin.security;

import com.keqi.gress.common.utils.ServletPathUtils;
import com.keqi.gress.plugin.appstore.admin.entity.AppStoreApiKey;
import com.keqi.gress.plugin.appstore.admin.service.ApiKeyService;
import com.keqi.gress.plugin.appstore.admin.service.ApiNonceService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

/**
 * HMAC auth filter for user-facing store APIs.
 *
 * Protects:
 * - /plugins/as-admin/api/appstore/** (excluding /anon/**)
 * - /api/appstore/** (excluding /anon/**)
 *
 * Headers:
 * - X-AppStore-KeyId
 * - X-AppStore-Timestamp (ms)
 * - X-AppStore-Nonce
 * - X-AppStore-Signature (base64)
 *
 * canonical = METHOD \n PATH \n RAW_QUERY \n TIMESTAMP \n NONCE
 */
@Component
@Slf4j
public class AppStoreApiAuthFilter implements Filter {

    public static final String H_KEY_ID = "X-AppStore-KeyId";
    public static final String H_TS = "X-AppStore-Timestamp";
    public static final String H_NONCE = "X-AppStore-Nonce";
    public static final String H_SIG = "X-AppStore-Signature";

    private static final long ALLOWED_SKEW_MS = 5 * 60_000L;
    private static final long NONCE_TTL_SECONDS = 5 * 60L;

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private ApiNonceService apiNonceService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest req) || !(response instanceof HttpServletResponse resp)) {
            chain.doFilter(request, response);
            return;
        }

        String uri = ServletPathUtils.stripContextPath(req.getRequestURI());
        if (!isProtected(uri)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (!check(req, resp)) return;
            chain.doFilter(request, response);
        } catch (Exception e) {
            reject(resp, "auth_error");
        }
    }

    private boolean check(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String keyId = req.getHeader(H_KEY_ID);
        String tsStr = req.getHeader(H_TS);
        String nonce = req.getHeader(H_NONCE);
        String sig = req.getHeader(H_SIG);

        if (StringUtils.isBlank(keyId) || StringUtils.isBlank(tsStr) || StringUtils.isBlank(nonce) || StringUtils.isBlank(sig)) {
            return reject(resp, "missing_auth_headers");
        }

        long ts;
        try {
            ts = Long.parseLong(tsStr.trim());
        } catch (Exception e) {
            return reject(resp, "invalid_timestamp");
        }

        long now = System.currentTimeMillis();
        if (Math.abs(now - ts) > ALLOWED_SKEW_MS) {
            return reject(resp, "timestamp_out_of_range");
        }

        AppStoreApiKey key = apiKeyService.getByKeyId(keyId);
        if (key == null) return reject(resp, "unknown_key");
        if (!Boolean.TRUE.equals(key.getEnabled())) return reject(resp, "key_disabled");
        if (key.getExpireAt() != null) {
            long exp = key.getExpireAt().toInstant(ZoneOffset.UTC).toEpochMilli();
            if (now > exp) return reject(resp, "key_expired");
        }

        LocalDateTime nonceExpireAt = LocalDateTime.ofEpochSecond((ts / 1000L) + NONCE_TTL_SECONDS, 0, ZoneOffset.UTC);
        boolean okNonce = apiNonceService.tryConsume(keyId.trim(), nonce.trim(), nonceExpireAt);
        if (!okNonce) return reject(resp, "replay_detected");

        String secret = apiKeyService.decryptSecret(key);
        if (StringUtils.isBlank(secret)) return reject(resp, "secret_unavailable");

        String canonical = canonical(req, tsStr.trim(), nonce.trim());
        String expected = hmacBase64(secret, canonical);
        if (!constantTimeEquals(expected, sig.trim())) {
            log.warn("签名校验失败: uri={}, normalizedPath={}",
                    req.getRequestURI(),
                    ServletPathUtils.stripContextPath(req.getRequestURI()));
            return reject(resp, "bad_signature");
        }

        if (!ScopeMatcher.allow(key.getScopes(), req.getMethod(),
                ServletPathUtils.stripContextPath(req.getRequestURI()))) {
            return reject(resp, "insufficient_scope");
        }

        apiKeyService.touchLastUsed(keyId);
        return true;
    }

    private static boolean isProtected(String uri) {
        if (uri == null) return false;
        if (uri.contains("/api/appstore/anon/")) return false;
        return uri.startsWith("/plugins/as-admin/api/appstore/")
            || uri.startsWith("/api/appstore/");
    }

    private String canonical(HttpServletRequest req, String ts, String nonce) {
        String method = req.getMethod().toUpperCase();
        String path = ServletPathUtils.stripContextPath(req.getRequestURI());
        String query = req.getQueryString();
        String q = query == null ? "" : query;
        return method + "\n" + path + "\n" + q + "\n" + ts + "\n" + nonce;
    }

    private static String hmacBase64(String secret, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] out = mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(out);
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] x = a.getBytes(StandardCharsets.UTF_8);
        byte[] y = b.getBytes(StandardCharsets.UTF_8);
        if (x.length != y.length) return false;
        int r = 0;
        for (int i = 0; i < x.length; i++) r |= x[i] ^ y[i];
        return r == 0;
    }

    private static boolean reject(HttpServletResponse resp, String code) throws IOException {
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"success\":false,\"message\":\"unauthorized\",\"errorMessage\":\"" + code + "\"}");
        return false;
    }
}
