package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Plugin Version Entity
 * Represents a version of a plugin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_version")
public class PluginVersion {
    
    /**
     * Primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
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
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
    
    /**
     * Dependencies (JSON format)
     * Format: [{"pluginId":"plugin-b","version":"1.0.0","optional":false,"versionRange":null}]
     */
    private String dependencies;
}
