package com.keqi.gress.plugin.appstore.admin.config;

import com.keqi.gress.common.plugin.annotion.ConfigurationProperties;
import com.keqi.gress.common.plugin.annotion.FormField;
import com.keqi.gress.common.plugin.dto.Input;
import lombok.Data;

/**
 * AppStore Admin configuration.
 */
@Data
@ConfigurationProperties(prefix = "appstoreAdmin", order = -100)
public class AppStoreAdminConfig implements Input {

    @FormField(
            label = "安全配置",
            description = "客户端安装前的验签开关等安全策略",
            type = FormField.FieldType.OBJECT,
            order = 10,
            group = "security"
    )
    private SecurityConfig security;

    @Data
    public static class SecurityConfig {

        @FormField(
                label = "启用验签",
                description = "是否启用客户端安装前验签（客户端会从 admin 拉取 trusted roots 并验证 JAR 签名）",
                type = FormField.FieldType.BOOLEAN,
                defaultValue = "false",
                component = FormField.ComponentType.SWITCH,
                order = 1
        )
        private Boolean verifySignature;
    }
}

