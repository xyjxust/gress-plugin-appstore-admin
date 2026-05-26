package com.keqi.gress.plugin.appstore.admin.dto;

import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Plugin Submission Query Request
 * Request parameters for querying plugin submissions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionQueryRequest {
    
    /**
     * Page number (1-based)
     */
    @Builder.Default
    private Integer page = 1;
    
    /**
     * Page size
     */
    @Builder.Default
    private Integer size = 20;
    
    /**
     * Filter by submission status
     */
    private SubmissionStatus status;
    
    /**
     * Filter by plugin type
     */
    private PluginType pluginType;
    
    /**
     * Search keyword (searches in plugin name, developer name, description)
     */
    private String keyword;
    
    /**
     * Filter by start time (timestamp in milliseconds)
     */
    private Long startTime;
    
    /**
     * Filter by end time (timestamp in milliseconds)
     */
    private Long endTime;
}
