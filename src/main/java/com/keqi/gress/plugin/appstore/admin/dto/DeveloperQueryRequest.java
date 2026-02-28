package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Developer Query Request
 * Request parameters for querying developers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperQueryRequest {
    
    /**
     * Page number (1-based)
     */
    private Integer page;
    
    /**
     * Page size
     */
    private Integer size;
    
    /**
     * Filter by developer status (PENDING/ACTIVE/SUSPENDED)
     */
    private String status;
    
    /**
     * Search keyword (searches in username, email, display name, company)
     */
    private String keyword;
    
    /**
     * Filter by verified status
     */
    private Boolean verified;
}
