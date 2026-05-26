package com.keqi.gress.plugin.appstore.admin.security;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Minimal scope matcher.
 *
 * - scopes empty -> allow all (for backward compatibility)
 * - supports:
 *   - appstore:*
 *   - appstore:read (GET)
 *   - appstore:download (download endpoints)
 */
public final class ScopeMatcher {
    private ScopeMatcher() {}

    public static boolean allow(String scopes, String method, String uri) {
        if (StringUtils.isBlank(scopes)) return true;
        Set<String> set = parse(scopes);
        if (set.contains("appstore:*")) return true;

        String m = method == null ? "" : method.toUpperCase();
        boolean isRead = "GET".equals(m);
        boolean isDownload = uri != null && uri.contains("/download");

        if (isDownload) {
            return set.contains("appstore:download") || set.contains("appstore:read");
        }
        if (isRead) {
            return set.contains("appstore:read");
        }
        // user-side endpoints are expected to be read-only for now
        return false;
    }

    private static Set<String> parse(String scopes) {
        String s = scopes.trim();
        // accept comma-separated or JSON-like ["a","b"] (best-effort)
        s = s.replace("[", "").replace("]", "").replace("\"", "");
        String[] parts = s.split(",");
        Set<String> out = new HashSet<>();
        Arrays.stream(parts).map(String::trim).filter(p -> !p.isEmpty()).forEach(out::add);
        return out;
    }
}

