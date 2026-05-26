package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * Plugin Tag Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_tag")
public class Tag extends BaseEntity {

    @TableField("tag_name")
    private String tagName;

    @TableField("tag_key")
    private String tagKey;

    @TableField("description")
    private String description;

    @TableField("color")
    private String color;

    @TableField("tag_type_key")
    private String tagTypeKey;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("usage_count")
    private Integer usageCount;

}
