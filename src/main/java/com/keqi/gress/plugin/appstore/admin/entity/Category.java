package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Plugin Category Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;

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

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}


