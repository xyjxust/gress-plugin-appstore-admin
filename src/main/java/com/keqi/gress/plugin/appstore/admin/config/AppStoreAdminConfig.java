package com.keqi.gress.plugin.appstore.admin.config;

import com.keqi.gress.common.plugin.annotion.FormField;
import com.keqi.gress.common.plugin.dto.Input;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AppStore Admin configuration.
 */
@Data
@ConfigurationProperties(prefix = "appstoreAdmin")
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

        @FormField(
                label = "加密主密钥",
                description = "用于加密 API Secret 和签名密钥口令。建议填写一段高强度随机字符串，并在生产环境保持稳定不变。",
                type = FormField.FieldType.STRING,
                component = FormField.ComponentType.INPUT,
                placeholder = "请输入至少 16 位的高强度随机字符串",
                order = 2
        )
        private String masterEncryptionKey;

        @FormField(
                label = "兼容签名配置",
                description = "当数据库中没有激活签名密钥时，回退使用这一组历史平台签名配置。",
                type = FormField.FieldType.OBJECT,
                order = 3
        )
        private LegacySigningConfig legacySigning;

        @FormField(
                label = "下载Token TTL（秒）",
                description = "下载令牌有效期（秒），默认120秒",
                type = FormField.FieldType.INTEGER,
                defaultValue = "120",
                order = 4
        )
        private Integer downloadTokenTtlSeconds = 120;
    }

    @Data
    public static class LegacySigningConfig {

        @FormField(
                label = "Keystore 路径",
                description = "历史平台签名 keystore 文件路径，仅在未启用数据库签名密钥时作为兼容回退。",
                type = FormField.FieldType.STRING,
                component = FormField.ComponentType.INPUT,
                placeholder = "/path/to/keystore.p12",
                order = 1
        )
        private String keystorePath;

        @FormField(
                label = "Keystore 密码",
                description = "历史平台签名 keystore 密码。",
                type = FormField.FieldType.STRING,
                component = FormField.ComponentType.INPUT,
                order = 2
        )
        private String keystorePassword;

        @FormField(
                label = "Key Alias",
                description = "历史平台签名证书别名。",
                type = FormField.FieldType.STRING,
                component = FormField.ComponentType.INPUT,
                order = 3
        )
        private String keyAlias;

        @FormField(
                label = "Key 密码",
                description = "历史平台签名 key 密码；为空时默认复用 keystore 密码。",
                type = FormField.FieldType.STRING,
                component = FormField.ComponentType.INPUT,
                order = 4
        )
        private String keyPassword;
    }
}
