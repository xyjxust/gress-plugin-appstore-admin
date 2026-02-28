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
 * Plugin Tag Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_tag")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("tag_name")
    private String tagName;

    @TableField("tag_key")
    private String tagKey;

    @TableField("description")
    private String description;

    @TableField("color")
    private String color;

    @TableField("enabled")
    private Boolean enabled;

    @TableField("usage_count")
    private Integer usageCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}


