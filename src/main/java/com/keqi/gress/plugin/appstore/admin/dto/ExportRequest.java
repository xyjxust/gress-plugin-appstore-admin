package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Export Request
 * Request parameters for exporting statistics data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequest {
    
    /**
     * Export format (CSV, JSON, EXCEL)
     */
    private String format;
    
    /**
     * Plugin IDs to export (null or empty for all)
     */
    private List<String> pluginIds;
    
    /**
     * Start date for the export range
     */
    private LocalDate startDate;
    
    /**
     * End date for the export range
     */
    private LocalDate endDate;
    
    /**
     * Include detailed statistics
     */
    private Boolean includeDetails;
}
