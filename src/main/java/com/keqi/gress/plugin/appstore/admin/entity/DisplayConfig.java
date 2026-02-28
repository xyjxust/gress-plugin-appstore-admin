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
 * Display Configuration Entity
 * Stores display rules and configurations for the plugin store
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_display_config")
public class DisplayConfig {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * Configuration key (unique identifier)
     */
    @TableField("config_key")
    private String configKey;
    
    /**
     * Configuration value (stored as JSON in database)
     */
    @TableField("config_value")
    private String configValue;
    
    /**
     * Configuration description
     */
    @TableField("description")
    private String description;
    
    /**
     * Creation time
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
