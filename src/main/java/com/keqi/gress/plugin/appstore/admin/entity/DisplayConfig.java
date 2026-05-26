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

/**
 * Display Configuration Entity
 * Stores display rules and configurations for the plugin store
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_display_config")
public class DisplayConfig extends BaseEntity {
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
    
}
