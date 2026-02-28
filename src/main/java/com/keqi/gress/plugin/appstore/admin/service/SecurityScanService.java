package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.ScanResult;
import com.keqi.gress.plugin.appstore.admin.dto.Vulnerability;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import com.keqi.gress.plugin.appstore.admin.enums.ScanStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

/**
 * Security Scan Service
 * Performs security scanning on plugin packages
 */
@Service
public class SecurityScanService {
    
    private static final Log log = LogFactory.get(SecurityScanService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.SPRING)
    private FileStorageService fileStorageService;
    
    // Known vulnerable patterns (simplified for demonstration)
    private static final List<VulnerabilityPattern> KNOWN_VULNERABILITIES = new ArrayList<>();
    
    static {
        // Add some example vulnerability patterns
        KNOWN_VULNERABILITIES.add(new VulnerabilityPattern(
            "CVE-2021-44228",
            "Log4j Remote Code Execution",
            "CRITICAL",
            Pattern.compile("log4j-core-2\\.(0|1[0-4]|15|16)\\."),
            "org.apache.logging.log4j:log4j-core",
            "2.17.0+",
            9.8,
            "Upgrade to log4j-core 2.17.0 or later"
        ));
        
        KNOWN_VULNERABILITIES.add(new VulnerabilityPattern(
            "CVE-2022-22965",
            "Spring Framework RCE (Spring4Shell)",
            "CRITICAL",
            Pattern.compile("spring-beans-5\\.(0|1|2|3\\.[0-1])\\."),
            "org.springframework:spring-beans",
            "5.3.18+",
            9.8,
            "Upgrade to Spring Framework 5.3.18 or later"
        ));
        
        KNOWN_VULNERABILITIES.add(new VulnerabilityPattern(
            "CVE-2021-42392",
            "H2 Database Console JNDI Injection",
            "HIGH",
            Pattern.compile("h2-1\\.(4\\.[0-9]+|[0-3]\\.[0-9]+)\\.jar"),
            "com.h2database:h2",
            "2.0.206+",
            8.5,
            "Upgrade to H2 Database 2.0.206 or later"
        ));
    }
    
    /**
     * Scan a plugin package for security vulnerabilities
     *
     * @param submissionId Submission ID
     * @param fileUrl Plugin file URL
     * @return Scan result
     */
    public ScanResult scanPlugin(Long submissionId, String fileUrl) {
        log.info("Starting security scan for submission: {}, fileUrl: {}", submissionId, fileUrl);
        
        LocalDateTime scanStartTime = LocalDateTime.now();
        
        try {
            // Update scan status to SCANNING
            updateScanStatus(submissionId, ScanStatus.SCANNING, null);
            
            // Download plugin file
            byte[] pluginBytes = downloadPluginFile(fileUrl);
            
            if (pluginBytes == null || pluginBytes.length == 0) {
                return handleScanFailure(submissionId, "Failed to download plugin file", scanStartTime);
            }
            
            // Perform security scan
            List<Vulnerability> vulnerabilities = performSecurityScan(pluginBytes);
            
            // Calculate risk level
            String riskLevel = calculateRiskLevel(vulnerabilities);
            
            // Build scan result
            LocalDateTime scanEndTime = LocalDateTime.now();
            long durationMs = java.time.Duration.between(scanStartTime, scanEndTime).toMillis();
            
            ScanResult result = ScanResult.builder()
                .status("COMPLETED")
                .riskLevel(riskLevel)
                .vulnerabilities(vulnerabilities)
                .scanStartTime(scanStartTime)
                .scanEndTime(scanEndTime)
                .scanDurationMs(durationMs)
                .scannerVersion("1.0.0")
                .build();
            
            // Save scan result
            saveScanResult(submissionId, result);
            
            // Update submission scan status
            updateScanStatus(submissionId, ScanStatus.COMPLETED, JSON.toJSONString(result));
            
            log.info("Security scan completed for submission: {}, found {} vulnerabilities, risk level: {}", 
                submissionId, vulnerabilities.size(), riskLevel);
            
            return result;
            
        } catch (Exception e) {
            log.error("Security scan failed for submission: " + submissionId, e);
            return handleScanFailure(submissionId, e.getMessage(), scanStartTime);
        }
    }
    
    /**
     * Download plugin file from storage
     *
     * @param fileUrl File URL
     * @return File bytes
     */
    private byte[] downloadPluginFile(String fileUrl) {
        log.debug("Downloading plugin file from: {}", fileUrl);
        
        try {
            AtomicReference<byte[]> bytesRef = new AtomicReference<>();
            
            fileStorageService.download(fileUrl)
                .toBytes(bytes -> {
                    bytesRef.set(bytes);
                    log.debug("Downloaded {} bytes", bytes.length);
                })
                .onError(e -> {
                    log.error("Failed to download plugin file", e);
                })
                .executeVoid();
            
            return bytesRef.get();
            
        } catch (Exception e) {
            log.error("Error downloading plugin file", e);
            return null;
        }
    }
    
    /**
     * Perform security scan on plugin bytes
     *
     * @param pluginBytes Plugin file bytes
     * @return List of vulnerabilities found
     */
    private List<Vulnerability> performSecurityScan(byte[] pluginBytes) {
        log.debug("Performing security scan on {} bytes", pluginBytes.length);
        
        List<Vulnerability> vulnerabilities = new ArrayList<>();
        
        try (JarInputStream jarStream = new JarInputStream(new ByteArrayInputStream(pluginBytes))) {
            JarEntry entry;
            
            while ((entry = jarStream.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                
                // Check for vulnerable dependencies
                if (entryName.endsWith(".jar") || entryName.contains("lib/")) {
                    checkForKnownVulnerabilities(entryName, vulnerabilities);
                }
                
                // Check for suspicious patterns in class files
                if (entryName.endsWith(".class")) {
                    // Could add bytecode analysis here
                }
            }
            
        } catch (IOException e) {
            log.error("Error scanning JAR file", e);
        }
        
        return vulnerabilities;
    }
    
    /**
     * Check for known vulnerabilities in dependencies
     *
     * @param fileName File name
     * @param vulnerabilities List to add found vulnerabilities to
     */
    private void checkForKnownVulnerabilities(String fileName, List<Vulnerability> vulnerabilities) {
        for (VulnerabilityPattern pattern : KNOWN_VULNERABILITIES) {
            if (pattern.pattern.matcher(fileName).find()) {
                Vulnerability vuln = Vulnerability.builder()
                    .id(pattern.cveId)
                    .name(pattern.name)
                    .severity(pattern.severity)
                    .description("Known vulnerability detected in dependency")
                    .affectedComponent(pattern.affectedComponent)
                    .affectedVersion(extractVersion(fileName))
                    .fixedVersion(pattern.fixedVersion)
                    .cvssScore(pattern.cvssScore)
                    .referenceUrl("https://nvd.nist.gov/vuln/detail/" + pattern.cveId)
                    .recommendation(pattern.recommendation)
                    .build();
                
                vulnerabilities.add(vuln);
                log.warn("Vulnerability detected: {} in {}", pattern.cveId, fileName);
            }
        }
    }
    
    /**
     * Extract version from file name
     *
     * @param fileName File name
     * @return Version string
     */
    private String extractVersion(String fileName) {
        // Simple version extraction (e.g., "library-1.2.3.jar" -> "1.2.3")
        Pattern versionPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+)");
        java.util.regex.Matcher matcher = versionPattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "unknown";
    }
    
    /**
     * Calculate overall risk level based on vulnerabilities
     *
     * @param vulnerabilities List of vulnerabilities
     * @return Risk level (LOW, MEDIUM, HIGH, CRITICAL)
     */
    private String calculateRiskLevel(List<Vulnerability> vulnerabilities) {
        if (vulnerabilities.isEmpty()) {
            return "LOW";
        }
        
        boolean hasCritical = false;
        boolean hasHigh = false;
        boolean hasMedium = false;
        
        for (Vulnerability vuln : vulnerabilities) {
            switch (vuln.getSeverity()) {
                case "CRITICAL":
                    hasCritical = true;
                    break;
                case "HIGH":
                    hasHigh = true;
                    break;
                case "MEDIUM":
                    hasMedium = true;
                    break;
            }
        }
        
        if (hasCritical) {
            return "CRITICAL";
        } else if (hasHigh) {
            return "HIGH";
        } else if (hasMedium) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    /**
     * Save scan result to database
     *
     * @param submissionId Submission ID
     * @param result Scan result
     */
    private void saveScanResult(Long submissionId, ScanResult result) {
        try {
            dataSource.dynamicSql("""
                INSERT INTO appstore_security_scan 
                (submission_id, scan_status, risk_level, vulnerabilities_count, 
                scan_result, scan_start_time, scan_end_time, scan_duration_ms, scanner_version) 
                VALUES (#{submissionId}, #{status}, #{riskLevel}, #{vulnCount}, 
                #{scanResult}, #{startTime}, #{endTime}, #{duration}, #{version})
                """)
                .param("submissionId", submissionId)
                .param("status", result.getStatus())
                .param("riskLevel", result.getRiskLevel())
                .param("vulnCount", result.getVulnerabilities().size())
                .param("scanResult", JSON.toJSONString(result))
                .param("startTime", Timestamp.valueOf(result.getScanStartTime()))
                .param("endTime", Timestamp.valueOf(result.getScanEndTime()))
                .param("duration", result.getScanDurationMs())
                .param("version", result.getScannerVersion())
                .execute();
            
            log.debug("Scan result saved to database for submission: {}", submissionId);
            
        } catch (Exception e) {
            log.error("Failed to save scan result to database", e);
        }
    }
    
    /**
     * Update scan status in submission table
     *
     * @param submissionId Submission ID
     * @param status Scan status
     * @param scanResult Scan result JSON (nullable)
     */
    private void updateScanStatus(Long submissionId, ScanStatus status, String scanResult) {
        try {
            // Update scan status using Lambda
            dataSource.lambdaUpdate(PluginSubmission.class)
                .eq(PluginSubmission::getId, submissionId)
                .set(PluginSubmission::getScanStatus, status.name())
                .set(PluginSubmission::getScanResult, scanResult)
                .update();
            
            log.debug("Updated scan status to {} for submission: {}", status, submissionId);
            
        } catch (Exception e) {
            log.error("Failed to update scan status", e);
        }
    }
    
    /**
     * Handle scan failure
     *
     * @param submissionId Submission ID
     * @param errorMessage Error message
     * @param scanStartTime Scan start time
     * @return Failed scan result
     */
    private ScanResult handleScanFailure(Long submissionId, String errorMessage, LocalDateTime scanStartTime) {
        LocalDateTime scanEndTime = LocalDateTime.now();
        long durationMs = java.time.Duration.between(scanStartTime, scanEndTime).toMillis();
        
        ScanResult result = ScanResult.builder()
            .status("FAILED")
            .riskLevel("UNKNOWN")
            .vulnerabilities(new ArrayList<>())
            .scanStartTime(scanStartTime)
            .scanEndTime(scanEndTime)
            .scanDurationMs(durationMs)
            .scannerVersion("1.0.0")
            .errorMessage(errorMessage)
            .build();
        
        // Update scan status to FAILED
        updateScanStatus(submissionId, ScanStatus.FAILED, JSON.toJSONString(result));
        
        return result;
    }
    
    /**
     * Get scan result for a submission
     *
     * @param submissionId Submission ID
     * @return Scan result or null if not found
     */
    public ScanResult getScanResult(Long submissionId) {
        try {
            List<Map<String, Object>> results = dataSource.dynamicSql("""
                SELECT scan_result FROM appstore_security_scan 
                WHERE submission_id = #{submissionId} 
                ORDER BY scan_start_time DESC 
                LIMIT 1
                """)
                .param("submissionId", submissionId)
                .query();
            
            if (!results.isEmpty()) {
                String scanResultJson = (String) results.get(0).get("scan_result");
                if (scanResultJson != null) {
                    return JSON.parseObject(scanResultJson, ScanResult.class);
                }
            }
            
        } catch (Exception e) {
            log.error("Failed to get scan result for submission: " + submissionId, e);
        }
        
        return null;
    }
    
    /**
     * Check if a submission should be auto-rejected based on scan results
     *
     * @param submissionId Submission ID
     * @return true if should be auto-rejected
     */
    public boolean shouldAutoReject(Long submissionId) {
        ScanResult result = getScanResult(submissionId);
        
        if (result == null || !"COMPLETED".equals(result.getStatus())) {
            return false;
        }
        
        // Auto-reject if CRITICAL risk level
        if ("CRITICAL".equals(result.getRiskLevel())) {
            log.info("Submission {} should be auto-rejected due to CRITICAL risk level", submissionId);
            return true;
        }
        
        // Auto-reject if has CRITICAL severity vulnerabilities
        for (Vulnerability vuln : result.getVulnerabilities()) {
            if ("CRITICAL".equals(vuln.getSeverity())) {
                log.info("Submission {} should be auto-rejected due to CRITICAL vulnerability: {}", 
                    submissionId, vuln.getId());
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Vulnerability pattern for matching known vulnerabilities
     */
    private static class VulnerabilityPattern {
        String cveId;
        String name;
        String severity;
        Pattern pattern;
        String affectedComponent;
        String fixedVersion;
        Double cvssScore;
        String recommendation;
        
        VulnerabilityPattern(String cveId, String name, String severity, Pattern pattern,
                           String affectedComponent, String fixedVersion, Double cvssScore,
                           String recommendation) {
            this.cveId = cveId;
            this.name = name;
            this.severity = severity;
            this.pattern = pattern;
            this.affectedComponent = affectedComponent;
            this.fixedVersion = fixedVersion;
            this.cvssScore = cvssScore;
            this.recommendation = recommendation;
        }
    }
}
