package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_download_token")
public class AppStoreDownloadToken extends BaseEntity {
    @TableField("token_hash")
    private String tokenHash;

    @TableField("plugin_id")
    private String pluginId;

    @TableField("version")
    private String version;

    @TableField("key_id")
    private String keyId;

    @TableField("user_id")
    private String userId;

    @TableField("issued_ip")
    private String issuedIp;

    @TableField("issued_ua")
    private String issuedUa;

    @TableField("expire_at")
    private LocalDateTime expireAt;

    @TableField("used")
    private Boolean used;

    @TableField("used_at")
    private LocalDateTime usedAt;

    @TableField("consumed_ip")
    private String consumedIp;

    @TableField("consumed_ua")
    private String consumedUa;
}
