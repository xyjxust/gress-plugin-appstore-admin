package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Plugin Version Entity
 * Represents a version of a plugin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("as_admin_version")
public class PluginVersion extends BaseEntity {
    /**
     * Plugin ID
     */
    private String pluginId;
    
    /**
     * Version number
     */
    private String version;
    
    /**
     * File path
     */
    private String filePath;
    
    /**
     * File size in bytes
     */
    private Long fileSize;
    
    /**
     * File hash (SHA-256)
     */
    private String fileHash;
    
    /**
     * Release notes
     */
    private String releaseNotes;
    
    /**
     * Is current version
     */
    private Boolean isCurrent;
    
    /**
     * Version status (ONLINE/OFFLINE/DELISTED)
     */
    private String status;
    
    /**
     * Upload time
     */
    private LocalDateTime uploadTime;
    
    /**
     * Dependencies (JSON format)
     * Format: [{"pluginId":"plugin-b","version":"1.0.0","optional":false,"versionRange":null}]
     */
    private String dependencies;
}
