package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Security Scan Result DTO
 * Contains the results of a security scan on a plugin package
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanResult {
    
    /**
     * Scan status (COMPLETED, FAILED)
     */
    private String status;
    
    /**
     * Overall risk level (LOW, MEDIUM, HIGH, CRITICAL)
     */
    private String riskLevel;
    
    /**
     * List of vulnerabilities found
     */
    private List<Vulnerability> vulnerabilities;
    
    /**
     * Scan start time
     */
    private LocalDateTime scanStartTime;
    
    /**
     * Scan end time
     */
    private LocalDateTime scanEndTime;
    
    /**
     * Scan duration in milliseconds
     */
    private Long scanDurationMs;
    
    /**
     * Scanner version
     */
    private String scannerVersion;
    
    /**
     * Additional scan metadata
     */
    private String metadata;
    
    /**
     * Error message if scan failed
     */
    private String errorMessage;
}
