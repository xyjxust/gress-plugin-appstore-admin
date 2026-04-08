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
        

            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
            
            PluginStatisticsDTO statistics = pluginStatisticsService.getPluginStatistics(pluginId, start, end);
            return Result.success(statistics);
            

    }
    
    /**
     * Get overall statistics overview
     *
     * @return Statistics overview
     */
    @GetMapping("/overview")
    public Result<StatisticsOverviewDTO> getStatisticsOverview() {

            StatisticsOverviewDTO overview = pluginStatisticsService.getStatisticsOverview();
            return Result.success(overview);

    }
    
    /**
     * Get trend data for analysis
     *
     * @param request Statistics query request
     * @return Trend data
     */
    @PostMapping("/trending")
    public Result<TrendDataDTO> getTrendData(@RequestBody StatisticsQueryRequest request) {

            // Set default date range if not provided
            if (request.getStartDate() == null) {
                request.setStartDate(LocalDate.now().minusDays(30));
            }
            if (request.getEndDate() == null) {
                request.setEndDate(LocalDate.now());
            }
            
            TrendDataDTO trendData = pluginStatisticsService.getTrendData(request);
            return Result.success(trendData);

    }
    
    /**
     * Export statistics data
     *
     * @param request Export request
     * @return Export result with data
     */
    @PostMapping("/export")
    public Result<String> exportStatistics(@RequestBody ExportRequest request) {

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

    }
}
