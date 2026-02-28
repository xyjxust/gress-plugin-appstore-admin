package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.PluginStatistics;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import com.keqi.gress.plugin.appstore.admin.entity.PluginSubmission;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Plugin Statistics Service
 * Handles plugin statistics data collection, aggregation, and analysis
 */
@Service
public class PluginStatisticsService {
    
    private static final Log log = LogFactory.get(PluginStatisticsService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    /**
     * Get plugin statistics for a specific plugin
     *
     * @param pluginId Plugin ID
     * @param startDate Start date
     * @param endDate End date
     * @return Plugin statistics
     */
    public PluginStatisticsDTO getPluginStatistics(String pluginId, LocalDate startDate, LocalDate endDate) {
        log.info("Getting statistics for plugin: {}, from {} to {}", pluginId, startDate, endDate);
        
        List<Map<String, Object>> results = dataSource.dynamicSql("""
            SELECT 
                SUM(install_count) as total_installs, 
                SUM(uninstall_count) as total_uninstalls, 
                MAX(active_installs) as active_installs, 
                MAX(active_users) as active_users, 
                SUM(new_users) as new_users, 
                AVG(rating_average) as avg_rating, 
                SUM(rating_count) as total_ratings, 
                SUM(review_count) as total_reviews, 
                SUM(feedback_count) as total_feedback, 
                SUM(report_count) as total_reports 
            FROM appstore_plugin_statistics 
            WHERE plugin_id = #{pluginId} AND stat_date BETWEEN #{startDate} AND #{endDate}
            """)
            .param("pluginId", pluginId)
            .param("startDate", startDate)
            .param("endDate", endDate)
            .query();
        
        Map<String, Object> result = results.isEmpty() ? null : results.get(0);
        
        if (result == null || result.isEmpty()) {
            log.warn("No statistics found for plugin: {}", pluginId);
            return PluginStatisticsDTO.builder()
                    .pluginId(pluginId)
                    .statDate(endDate)
                    .installCount(0)
                    .uninstallCount(0)
                    .activeInstalls(0)
                    .activeUsers(0)
                    .newUsers(0)
                    .ratingAverage(BigDecimal.ZERO)
                    .ratingCount(0)
                    .reviewCount(0)
                    .feedbackCount(0)
                    .reportCount(0)
                    .build();
        }
        
        return PluginStatisticsDTO.builder()
                .pluginId(pluginId)
                .statDate(endDate)
                .installCount(getIntValue(result, "total_installs"))
                .uninstallCount(getIntValue(result, "total_uninstalls"))
                .activeInstalls(getIntValue(result, "active_installs"))
                .activeUsers(getIntValue(result, "active_users"))
                .newUsers(getIntValue(result, "new_users"))
                .ratingAverage(getBigDecimalValue(result, "avg_rating"))
                .ratingCount(getIntValue(result, "total_ratings"))
                .reviewCount(getIntValue(result, "total_reviews"))
                .feedbackCount(getIntValue(result, "total_feedback"))
                .reportCount(getIntValue(result, "total_reports"))
                .build();
    }
    
    /**
     * Get overall statistics overview
     *
     * @return Statistics overview
     */
    public StatisticsOverviewDTO getStatisticsOverview() {
        log.info("Getting statistics overview");
        
        // Get total plugins count using Lambda
        long totalPluginsCount = dataSource.lambdaQuery(PluginManager.class)
            .eq(PluginManager::getStatus, "ONLINE")
            .count();
        Integer totalPlugins = (int) totalPluginsCount;
        
        // Get active plugins count (plugins with installs in last 30 days)
        List<Map<String, Object>> activePluginsResult = dataSource.dynamicSql("""
            SELECT COUNT(DISTINCT plugin_id) as active 
            FROM appstore_plugin_statistics 
            WHERE stat_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND active_installs > 0
            """)
            .query();
        Integer activePlugins = getIntValue(activePluginsResult.isEmpty() ? null : activePluginsResult.get(0), "active");
        
        // Get pending submissions count using Lambda
        long pendingSubmissionsCount = dataSource.lambdaQuery(PluginSubmission.class)
            .eq(PluginSubmission::getStatus, "PENDING")
            .count();
        Integer pendingSubmissions = (int) pendingSubmissionsCount;
        
        // Get aggregated statistics
        List<Map<String, Object>> statsResult = dataSource.dynamicSql("""
            SELECT 
                SUM(install_count) as total_installs, 
                SUM(active_users) as total_active_users, 
                AVG(rating_average) as avg_rating, 
                SUM(feedback_count) as total_feedback, 
                SUM(report_count) as total_reports 
            FROM appstore_plugin_statistics 
            WHERE stat_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
            """)
            .query();
        
        Map<String, Object> stats = statsResult.isEmpty() ? null : statsResult.get(0);
        
        return StatisticsOverviewDTO.builder()
                .totalPlugins(totalPlugins != null ? totalPlugins : 0)
                .activePlugins(activePlugins != null ? activePlugins : 0)
                .pendingSubmissions(pendingSubmissions != null ? pendingSubmissions : 0)
                .totalInstalls(getLongValue(stats, "total_installs"))
                .totalActiveUsers(getLongValue(stats, "total_active_users"))
                .averageRating(getBigDecimalValue(stats, "avg_rating"))
                .totalFeedback(getIntValue(stats, "total_feedback"))
                .totalReports(getIntValue(stats, "total_reports"))
                .build();
    }
    
    /**
     * Get trend data for analysis
     *
     * @param request Statistics query request
     * @return Trend data
     */
    public TrendDataDTO getTrendData(StatisticsQueryRequest request) {
        log.info("Getting trend data: {}", JSON.toJSONString(request));
        
        List<Map<String, Object>> rows;
        
        if (request.getPluginId() != null) {
            rows = dataSource.dynamicSql("""
                SELECT stat_date, install_count, uninstall_count, active_installs, active_users, new_users 
                FROM appstore_plugin_statistics 
                WHERE plugin_id = #{pluginId} AND stat_date BETWEEN #{startDate} AND #{endDate} 
                ORDER BY stat_date ASC
                """)
                .param("pluginId", request.getPluginId())
                .param("startDate", request.getStartDate())
                .param("endDate", request.getEndDate())
                .query();
        } else {
            rows = dataSource.dynamicSql("""
                SELECT stat_date, 
                    SUM(install_count) as install_count, 
                    SUM(uninstall_count) as uninstall_count, 
                    SUM(active_installs) as active_installs, 
                    SUM(active_users) as active_users, 
                    SUM(new_users) as new_users 
                FROM appstore_plugin_statistics 
                WHERE stat_date BETWEEN #{startDate} AND #{endDate} 
                GROUP BY stat_date 
                ORDER BY stat_date ASC
                """)
                .param("startDate", request.getStartDate())
                .param("endDate", request.getEndDate())
                .query();
        }
        
        List<TrendDataDTO.TrendDataPoint> dataPoints = rows.stream()
                .map(row -> TrendDataDTO.TrendDataPoint.builder()
                        .date(getLocalDateValue(row, "stat_date"))
                        .installCount(getIntValue(row, "install_count"))
                        .uninstallCount(getIntValue(row, "uninstall_count"))
                        .activeInstalls(getIntValue(row, "active_installs"))
                        .activeUsers(getIntValue(row, "active_users"))
                        .newUsers(getIntValue(row, "new_users"))
                        .build())
                .collect(Collectors.toList());
        
        return TrendDataDTO.builder()
                .pluginId(request.getPluginId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .dataPoints(dataPoints)
                .build();
    }
    
    /**
     * Export statistics data
     *
     * @param request Export request
     * @return Export result with file path or data
     */
    public Result<String> exportStatistics(ExportRequest request) {
        log.info("Exporting statistics: {}", JSON.toJSONString(request));
        
        try {
            // Build dynamic SQL query
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("""
                SELECT s.*, m.plugin_name, m.developer_name 
                FROM appstore_plugin_statistics s 
                LEFT JOIN appstore_manager m ON s.plugin_id = m.plugin_id 
                WHERE s.stat_date BETWEEN #{startDate} AND #{endDate}
                """);
            
            if (request.getPluginIds() != null && !request.getPluginIds().isEmpty()) {
                sqlBuilder.append(" AND s.plugin_id IN (");
                for (int i = 0; i < request.getPluginIds().size(); i++) {
                    if (i > 0) sqlBuilder.append(", ");
                    sqlBuilder.append("#{pluginId").append(i).append("}");
                }
                sqlBuilder.append(")");
            }
            
            sqlBuilder.append(" ORDER BY s.stat_date DESC, s.plugin_id ASC");
            
            // Execute query with parameters
            var query = dataSource.dynamicSql(sqlBuilder.toString())
                .param("startDate", request.getStartDate())
                .param("endDate", request.getEndDate());
            
            if (request.getPluginIds() != null) {
                for (int i = 0; i < request.getPluginIds().size(); i++) {
                    query.param("pluginId" + i, request.getPluginIds().get(i));
                }
            }
            
            List<Map<String, Object>> data = query.query();
            
            // Convert to requested format
            String exportData;
            switch (request.getFormat().toUpperCase()) {
                case "CSV":
                    exportData = convertToCSV(data);
                    break;
                case "JSON":
                    exportData = JSON.toJSONString(data);
                    break;
                case "EXCEL":
                    // For Excel, we would need to use Apache POI or similar library
                    // For now, return CSV format
                    exportData = convertToCSV(data);
                    break;
                default:
                    return Result.error("Unsupported export format: " + request.getFormat());
            }
            
            log.info("Export completed, data size: {} bytes", exportData.length());
            return Result.success(exportData);
            
        } catch (Exception e) {
            log.error("Failed to export statistics", e);
            return Result.error("Export failed: " + e.getMessage());
        }
    }
    
    /**
     * Update install count for a plugin
     *
     * @param pluginId Plugin ID
     */
    public void updateInstallCount(String pluginId) {
        log.info("Updating install count for plugin: {}", pluginId);
        
        LocalDate today = LocalDate.now();
        
        // Try to find existing record for today
        PluginStatistics existing = dataSource.lambdaQuery(PluginStatistics.class)
            .eq(PluginStatistics::getPluginId, pluginId)
            .eq(PluginStatistics::getStatDate, today)
            .one();
        
        if (existing != null) {
            // Update existing record using Lambda
            // Note: Lambda API doesn't support SQL expressions like "install_count + 1"
            // So we need to read, update, and save
            existing.setInstallCount((existing.getInstallCount() != null ? existing.getInstallCount() : 0) + 1);
            existing.setActiveInstalls((existing.getActiveInstalls() != null ? existing.getActiveInstalls() : 0) + 1);
            existing.setUpdateTime(LocalDateTime.now());
            
            dataSource.lambdaUpdate(PluginStatistics.class)
                .eq(PluginStatistics::getId, existing.getId())
                .set(PluginStatistics::getInstallCount, existing.getInstallCount())
                .set(PluginStatistics::getActiveInstalls, existing.getActiveInstalls())
                .set(PluginStatistics::getUpdateTime, existing.getUpdateTime())
                .update();
        } else {
            // Create new record using Lambda
            PluginStatistics newStats = PluginStatistics.builder()
                .pluginId(pluginId)
                .statDate(today)
                .installCount(1)
                .activeInstalls(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
            
            dataSource.insert(newStats);
            log.info("Created new statistics record for plugin: {}", pluginId);
        }
    }
    
    /**
     * Update uninstall count for a plugin
     *
     * @param pluginId Plugin ID
     */
    public void updateUninstallCount(String pluginId) {
        log.info("Updating uninstall count for plugin: {}", pluginId);
        
        LocalDate today = LocalDate.now();
        
        // Try to find existing record for today
        PluginStatistics existing = dataSource.lambdaQuery(PluginStatistics.class)
            .eq(PluginStatistics::getPluginId, pluginId)
            .eq(PluginStatistics::getStatDate, today)
            .one();
        
        if (existing != null) {
            // Update existing record using Lambda
            existing.setUninstallCount((existing.getUninstallCount() != null ? existing.getUninstallCount() : 0) + 1);
            int currentActive = existing.getActiveInstalls() != null ? existing.getActiveInstalls() : 0;
            existing.setActiveInstalls(Math.max(currentActive - 1, 0));
            existing.setUpdateTime(LocalDateTime.now());
            
            dataSource.lambdaUpdate(PluginStatistics.class)
                .eq(PluginStatistics::getId, existing.getId())
                .set(PluginStatistics::getUninstallCount, existing.getUninstallCount())
                .set(PluginStatistics::getActiveInstalls, existing.getActiveInstalls())
                .set(PluginStatistics::getUpdateTime, existing.getUpdateTime())
                .update();
        } else {
            // Create new record using Lambda
            PluginStatistics newStats = PluginStatistics.builder()
                .pluginId(pluginId)
                .statDate(today)
                .uninstallCount(1)
                .activeInstalls(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
            
            dataSource.insert(newStats);
            log.info("Created new statistics record for plugin: {}", pluginId);
        }
    }
    
    /**
     * Convert data to CSV format
     */
    private String convertToCSV(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return "";
        }
        
        StringBuilder csv = new StringBuilder();
        
        // Header
        Set<String> headers = data.get(0).keySet();
        csv.append(String.join(",", headers)).append("\n");
        
        // Data rows
        for (Map<String, Object> row : data) {
            List<String> values = new ArrayList<>();
            for (String header : headers) {
                Object value = row.get(header);
                values.add(value != null ? value.toString() : "");
            }
            csv.append(String.join(",", values)).append("\n");
        }
        
        return csv.toString();
    }
    
    // Helper methods to safely extract values from Map
    
    private Integer getIntValue(Map<String, Object> map, String key) {
        if (map == null) return 0;
        Object value = map.get(key);
        if (value == null) return 0;
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
    
    private Long getLongValue(Map<String, Object> map, String key) {
        if (map == null) return 0L;
        Object value = map.get(key);
        if (value == null) return 0L;
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }
    
    private BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        if (map == null) return BigDecimal.ZERO;
        Object value = map.get(key);
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(2, RoundingMode.HALF_UP);
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
    
    private LocalDate getLocalDateValue(Map<String, Object> map, String key) {
        if (map == null) return null;
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate();
        }
        if (value instanceof String) {
            return LocalDate.parse((String) value, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return null;
    }
}
