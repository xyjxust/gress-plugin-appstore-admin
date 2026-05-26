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
 * Plugin Category Entity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_category")
public class Category extends BaseEntity {

    @TableField("category_name")
    private String categoryName;

    @TableField("category_key")
    private String categoryKey;

    @TableField("description")
    private String description;

    @TableField("icon")
    private String icon;

    @TableField("display_order")
    private Integer displayOrder;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("plugin_count")
    private Integer pluginCount;
}
