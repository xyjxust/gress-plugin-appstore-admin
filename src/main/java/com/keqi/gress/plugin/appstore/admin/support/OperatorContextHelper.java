package com.keqi.gress.plugin.appstore.admin.support;

import com.keqi.gress.common.context.AppContext;

/**
 * 统一读取当前操作者信息，避免在各 controller/service 中写死 admin。
 */
public final class OperatorContextHelper {

    private static final String DEFAULT_OPERATOR = "system";

    private OperatorContextHelper() {
    }

    public static String getOperatorId() {
        Long userId = AppContext.getUserId();
        if (userId != null) {
            return String.valueOf(userId);
        }

        String username = AppContext.getUsername();
        if (username != null && !username.isBlank()) {
            return username;
        }

        return DEFAULT_OPERATOR;
    }

    public static String getOperatorName() {
        String username = AppContext.getUsername();
        if (username != null && !username.isBlank()) {
            return username;
        }

        Long userId = AppContext.getUserId();
        if (userId != null) {
            return String.valueOf(userId);
        }

        return DEFAULT_OPERATOR;
    }
}
