package com.keqi.gress.plugin.appstore.admin.entity;

import com.keqi.gress.plugin.api.database.annotation.IdType;
import com.keqi.gress.plugin.api.database.annotation.TableField;
import com.keqi.gress.plugin.api.database.annotation.TableId;
import com.keqi.gress.plugin.api.database.annotation.TableName;
import com.keqi.gress.plugin.api.database.mapping.FastJsonListTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Plugin Submission Entity
 * Represents a plugin submission for review
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("appstore_plugin_submission")
public class PluginSubmission {
    
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
     * Plugin name
     */
    private String pluginName;
    
    /**
     * Plugin type (TASK/TRIGGER/APPLICATION)
     */
    private String pluginType;
    
    /**
     * Version number
     */
    private String version;
    
    /**
     * Developer ID
     */
    private String developerId;
    
    /**
     * Developer name
     */
    private String developerName;
    
    // Plugin Metadata
    
    /**
     * Plugin description
     */
    private String description;
    
    /**
     * Icon path
     */
    private String icon;
    
    /**
     * Tags (stored as JSON array in database)
     * Note: TypeHandler will be resolved at runtime by the framework
     */
    @TableField(value = "tags", typeHandler = FastJsonListTypeHandler.class)
    private List<String> tags;
    
    /**
     * Category
     */
    private String category;
    
    // File Information
    
    /**
     * Plugin file path
     */
    private String filePath;
    
    /**
     * File size in bytes
     */
    private Long fileSize;
    
    /**
     * File hash for integrity verification
     */
    private String fileHash;
    
    // Review Information
    
    /**
     * Submission status (stored as String to avoid ClassNotFoundException during deserialization)
     */
    private String status;
    
    /**
     * Reviewer ID
     */
    private String reviewerId;
    
    /**
     * Reviewer name
     */
    private String reviewerName;
    
    /**
     * Review time
     */
    private LocalDateTime reviewTime;
    
    /**
     * Review comment
     */
    private String reviewComment;
    
    // Security Scan
    
    /**
     * Security scan status (stored as String to avoid ClassNotFoundException during deserialization)
     */
    private String scanStatus;
    
    /**
     * Security scan result (stored as JSON in database)
     */
    private String scanResult;
    
    // Timestamps
    
    /**
     * Submission time
     */
    private LocalDateTime submitTime;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Last update time
     */
    private LocalDateTime updateTime;
}
