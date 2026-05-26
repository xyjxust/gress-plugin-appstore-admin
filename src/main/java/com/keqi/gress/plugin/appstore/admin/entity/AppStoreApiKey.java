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
@TableName("as_admin_api_key")
public class AppStoreApiKey extends BaseEntity {

    @TableField("user_id")
    private String userId;

    @TableField("key_id")
    private String keyId;

    @TableField("secret_hash")
    private String secretHash;

    @TableField("secret_enc")
    private String secretEnc;

    @TableField("scopes")
    private String scopes;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("expire_at")
    private LocalDateTime expireAt;

    @TableField("last_used_at")
    private LocalDateTime lastUsedAt;
}
