package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.PluginStatisticsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Plugin Statistics Controller
 * Provides REST API endpoints for plugin statistics and analytics
 */

@Service
@RestController
@RequestMapping("/plugins/statistics")
public class PluginStatisticsController {

    private final static Log log = LogFactory.get(PluginStatisticsController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private PluginStatisticsService pluginStatisticsService;
    
    /**
     * Get statistics for a specific plugin
     *
     * @param pluginId Plugin ID
     * @param startDate Start date (optional, defaults to 30 days ago)
     * @param endDate End date (optional, defaults to today)
     * @return Plugin statistics
     */
    @GetMapping("/{pluginId}")
    public Result<PluginStatisticsDTO> getPluginStatistics(
            @PathVariable String pluginId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        log.info("Getting statistics for plugin: {}, startDate: {}, endDate: {}", pluginId, startDate, endDate);
        
        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
            
            PluginStatisticsDTO statistics = pluginStatisticsService.getPluginStatistics(pluginId, start, end);
            return Result.success(statistics);
            
        } catch (Exception e) {
            log.error("Failed to get plugin statistics", e);
            return Result.error("Failed to get statistics: " + e.getMessage());
        }
    }
    
    /**
     * Get overall statistics overview
     *
     * @return Statistics overview
     */
    @GetMapping("/overview")
    public Result<StatisticsOverviewDTO> getStatisticsOverview() {
        log.info("Getting statistics overview");
        
        try {
            StatisticsOverviewDTO overview = pluginStatisticsService.getStatisticsOverview();
            return Result.success(overview);
            
        } catch (Exception e) {
            log.error("Failed to get statistics overview", e);
            return Result.error("Failed to get overview: " + e.getMessage());
        }
    }
    
    /**
     * Get trend data for analysis
     *
     * @param request Statistics query request
     * @return Trend data
     */
    @PostMapping("/trending")
    public Result<TrendDataDTO> getTrendData(@RequestBody StatisticsQueryRequest request) {
        log.info("Getting trend data");
        
        try {
            // Set default date range if not provided
            if (request.getStartDate() == null) {
                request.setStartDate(LocalDate.now().minusDays(30));
            }
            if (request.getEndDate() == null) {
                request.setEndDate(LocalDate.now());
            }
            
            TrendDataDTO trendData = pluginStatisticsService.getTrendData(request);
            return Result.success(trendData);
            
        } catch (Exception e) {
            log.error("Failed to get trend data", e);
            return Result.error("Failed to get trend data: " + e.getMessage());
        }
    }
    
    /**
     * Export statistics data
     *
     * @param request Export request
     * @return Export result with data
     */
    @PostMapping("/export")
    public Result<String> exportStatistics(@RequestBody ExportRequest request) {
        log.info("Exporting statistics");
        
        try {
            // Validate request
            if (request.getFormat() == null || request.getFormat().isEmpty()) {
                return Result.error("Export format is required");
            }
            
            // Set default date range if not provided
            if (request.getStartDate() == null) {
                request.setStartDate(LocalDate.now().minusDays(30));
            }
            if (request.getEndDate() == null) {
                request.setEndDate(LocalDate.now());
            }
            
            return pluginStatisticsService.exportStatistics(request);
            
        } catch (Exception e) {
            log.error("Failed to export statistics", e);
            return Result.error("Failed to export: " + e.getMessage());
        }
    }
}
