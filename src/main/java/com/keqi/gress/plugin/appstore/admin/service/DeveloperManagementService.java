package com.keqi.gress.plugin.appstore.admin.service;

import com.alibaba.fastjson2.JSON;
import cn.hutool.core.util.StrUtil;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.Developer;
import com.keqi.gress.plugin.appstore.admin.support.RequestActorContextBinder;
import com.keqi.gress.common.plugin.PluginType;
import com.keqi.gress.plugin.appstore.admin.enums.SubmissionStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Developer Management Service
 * Handles developer account management operations
 */
@Service
public class DeveloperManagementService {
    
    private static final Log log = LogFactory.get(DeveloperManagementService.class);
    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private AuditLogService auditLogService;
    
    /**
     * Get developers with filtering and pagination
     *
     * @param request Query request with filters
     * @return Paginated list of developers
     */
    public PageResult<DeveloperDTO> getDevelopers(DeveloperQueryRequest request) {
        log.info("Querying developers with request: {}", request);
        
        // Use Lambda Query Chain for single-table query with pagination
        var query = dataSource.lambdaQuery(Developer.class);
        
        // Apply status filter
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            query.eq(Developer::getStatus, request.getStatus());
        }
        
        // Apply verified filter
        if (request.getVerified() != null) {
            query.eq(Developer::getVerified, request.getVerified());
        }
        
        // Apply keyword search (multiple fields with OR)
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            String keyword = "%" + request.getKeyword().trim() + "%";
            query.and().func(q -> q
                .like(Developer::getUsername, keyword)
                .or()
                .like(Developer::getEmail, keyword)
                .or()
                .like(Developer::getDisplayName, keyword)
                .or()
                .like(Developer::getCompany, keyword)
            );
        }
        
        // Order by apply time descending and execute with pagination
        IPage<Developer> page = query.orderByDesc(Developer::getApplyTime)
                                     .page(request.getPage(), request.getSize());
        
        // Convert to DTOs
        List<DeveloperDTO> developers = page.getRecords().stream()
                .map(this::mapToDeveloperDTO)
                .collect(Collectors.toList());
        
        log.info("Found {} developers out of {} total", developers.size(), page.getTotal());
        
        return PageResult.of(developers, page.getTotal(), (int) page.getCurrent(), (int) page.getSize());
    }
    
    /**
     * Get developer detail by ID
     *
     * @param id Developer ID
     * @return Developer detail with plugin list
     */
    public DeveloperDetailDTO getDeveloperDetail(Long id) {
        log.info("Getting developer detail for ID: {}", id);
        
        // Query developer using Lambda Query Chain (single-table)
        Developer developer = dataSource.lambdaQuery(Developer.class)
                                       .eq(Developer::getId, id)
                                       .one();
        
        if (developer == null) {
            log.warn("Developer not found: {}", id);
            return null;
        }
        
        // Query developer's plugins using Dynamic SQL (multi-table join potential)
        // For now it's single table, but using dynamic SQL for consistency with multi-table pattern
        String pluginsSql = "SELECT * FROM as_admin_plugin_submission " +
                           "<where>" +
                           "  <if test='developerId != nil'>AND developer_id = #{developerId}</if>" +
                           "</where>" +
                           "ORDER BY submit_time DESC";
        
        List<Map<String, Object>> pluginRows = dataSource.dynamicSql(pluginsSql)
                .param("developerId", developer.getUserId())
                .query();
        
        List<PluginSubmissionDTO> plugins = pluginRows.stream()
                .map(this::mapToPluginSubmissionDTO)
                .collect(Collectors.toList());
        
        // Convert to detail DTO
        DeveloperDetailDTO detail = mapToDeveloperDetailDTO(developer);
        detail.setPlugins(plugins);
        
        log.info("Retrieved developer detail for: {}", detail.getUsername());
        
        return detail;
    }
    
    /**
     * Approve a developer application
     *
     * @param id Developer ID
     * @param request Approval request
     */
    public void approveDeveloper(Long id, DeveloperApprovalRequest request) {
        log.info("Approving developer: {}, reviewer: {}", id, request.getReviewerName());
        RequestActorContextBinder.bindReviewer(request);
        
        // Validate request
        if (StrUtil.isBlank(request.getReviewerId())) {
            throw new IllegalArgumentException("Reviewer ID is required");
        }
        
        // Check developer exists and is in PENDING status using Lambda Query
        Developer developer = dataSource.lambdaQuery(Developer.class)
                                       .eq(Developer::getId, id)
                                       .one();
        
        if (developer == null) {
            throw new IllegalArgumentException("开发者不存在：" + id);
        }
        
        if (developer.getStatus() == null || !"PENDING".equals(developer.getStatus())) {
            throw new IllegalStateException("开发者状态不是待审核：" + developer.getStatus());
        }
        
        // Update developer status using Lambda Update Chain in transaction
        dataSource.executeTransaction(() -> {
            LocalDateTime now = LocalDateTime.now();
            dataSource.lambdaUpdate(Developer.class)
                     .set(Developer::getStatus, "ACTIVE")
                     .set(Developer::getReviewerId, request.getReviewerId())
                     .set(Developer::getReviewTime, now)
                     .set(Developer::getReviewComment, request.getComment())
                     .eq(Developer::getId, id)
                     .update();
        });

        // Record audit log
        auditLogService.log(
                "APPROVE_DEVELOPER",
                "DEVELOPER",
                id.toString(),
                developer.getUsername(),
                request.getReviewerId(),
                request.getReviewerName(),
                "SUCCESS",
                null,
                JSON.toJSONString(request)
        );
        
        log.info("Developer approved successfully: {}", id);
    }
    
    /**
     * Suspend a developer account
     *
     * @param id Developer ID
     * @param request Suspend request
     */
    public void suspendDeveloper(Long id, DeveloperSuspendRequest request) {
        log.info("Suspending developer: {}, operator: {}", id, request.getOperatorName());
        RequestActorContextBinder.bindOperator(request);
        
        // Validate request
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("Suspension reason is required");
        }
        
        if (StrUtil.isBlank(request.getOperatorId())) {
            throw new IllegalArgumentException("Operator ID is required");
        }
        
        // Check developer exists and is ACTIVE using Lambda Query
        Developer developer = dataSource.lambdaQuery(Developer.class)
                                       .eq(Developer::getId, id)
                                       .one();
        
        if (developer == null) {
            throw new IllegalArgumentException("开发者不存在：" + id);
        }
        
        if (developer.getStatus() == null || !"ACTIVE".equals(developer.getStatus())) {
            throw new IllegalStateException("开发者状态不是活跃：" + developer.getStatus());
        }
        
        // Update developer status using Lambda Update Chain in transaction
        dataSource.executeTransaction(() -> {
            dataSource.lambdaUpdate(Developer.class)
                     .set(Developer::getStatus, "SUSPENDED")
                     .eq(Developer::getId, id)
                     .update();
        });

        // Record audit log
        auditLogService.log(
                "SUSPEND_DEVELOPER",
                "DEVELOPER",
                id.toString(),
                developer.getUsername(),
                request.getOperatorId(),
                request.getOperatorName(),
                "SUCCESS",
                null,
                JSON.toJSONString(request)
        );
        
        log.info("Developer suspended successfully: {}", id);
    }
    
    /**
     * Activate a suspended developer account
     *
     * @param id Developer ID
     * @param request Activate request
     */
    public void activateDeveloper(Long id, DeveloperActivateRequest request) {
        log.info("Activating developer: {}, operator: {}", id, request.getOperatorName());
        RequestActorContextBinder.bindOperator(request);
        
        // Validate request
        if (StrUtil.isBlank(request.getOperatorId())) {
            throw new IllegalArgumentException("Operator ID is required");
        }
        
        // Check developer exists and is SUSPENDED using Lambda Query
        Developer developer = dataSource.lambdaQuery(Developer.class)
                                       .eq(Developer::getId, id)
                                       .one();
        
        if (developer == null) {
            throw new IllegalArgumentException("开发者不存在：" + id);
        }
        
        if (developer.getStatus() == null || !"SUSPENDED".equals(developer.getStatus())) {
            throw new IllegalStateException("开发者状态不是已暂停：" + developer.getStatus());
        }
        
        // Update developer status using Lambda Update Chain in transaction
        dataSource.executeTransaction(() -> {
            dataSource.lambdaUpdate(Developer.class)
                     .set(Developer::getStatus, "ACTIVE")
                     .eq(Developer::getId, id)
                     .update();
        });
    
        // Record audit log
        auditLogService.log(
                "ACTIVATE_DEVELOPER",
                "DEVELOPER",
                id.toString(),
                developer.getUsername(),
                request.getOperatorId(),
                request.getOperatorName(),
                "SUCCESS",
                null,
                JSON.toJSONString(request)
        );
        
        log.info("Developer activated successfully: {}", id);
    }

    /**
     * Map Developer entity to DeveloperDTO
     */
    private DeveloperDTO mapToDeveloperDTO(Developer developer) {
        return DeveloperDTO.builder()
                .id(developer.getId())
                .userId(developer.getUserId())
                .username(developer.getUsername())
                .email(developer.getEmail())
                .displayName(developer.getDisplayName())
                .company(developer.getCompany())
                .website(developer.getWebsite())
                .bio(developer.getBio())
                .status(developer.getStatus())
                .verified(developer.getVerified())
                .pluginCount(developer.getPluginCount())
                .totalDownloads(developer.getTotalDownloads())
                .applyTime(developer.getApplyTime())
                .reviewTime(developer.getReviewTime())
                .build();
    }
    
    /**
     * Map Developer entity to DeveloperDetailDTO
     */
    private DeveloperDetailDTO mapToDeveloperDetailDTO(Developer developer) {
        return DeveloperDetailDTO.builder()
                .id(developer.getId())
                .userId(developer.getUserId())
                .username(developer.getUsername())
                .email(developer.getEmail())
                .displayName(developer.getDisplayName())
                .company(developer.getCompany())
                .website(developer.getWebsite())
                .bio(developer.getBio())
                .status(developer.getStatus())
                .verified(developer.getVerified())
                .pluginCount(developer.getPluginCount())
                .totalDownloads(developer.getTotalDownloads())
                .reviewerId(developer.getReviewerId())
                .reviewTime(developer.getReviewTime())
                .reviewComment(developer.getReviewComment())
                .applyTime(developer.getApplyTime())
                .createTime(developer.getCreateTime())
                .updateTime(developer.getUpdateTime())
                .build();
    }
    
    /**
     * Map database row to PluginSubmissionDTO
     */
    private PluginSubmissionDTO mapToPluginSubmissionDTO(Map<String, Object> row) {
        return PluginSubmissionDTO.builder()
                .id(getLong(row, "id"))
                .pluginId(getString(row, "plugin_id"))
                .pluginName(getString(row, "plugin_name"))
                .pluginType(getEnum(row, "plugin_type", PluginType.class))
                .version(getString(row, "version"))
                .developerId(getString(row, "developer_id"))
                .developerName(getString(row, "developer_name"))
                .description(getString(row, "description"))
                .status(getEnum(row, "status", SubmissionStatus.class))
                .submitTime(getLocalDateTime(row, "submit_time"))
                .reviewTime(getLocalDateTime(row, "review_time"))
                .build();
    }
    
    // Helper methods for type conversion from Map
    
    private String getString(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : null;
    }
    
    private Long getLong(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.parseLong(value.toString());
    }
    
    private LocalDateTime getLocalDateTime(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) return null;
        if (value instanceof java.sql.Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (value instanceof java.sql.Date date) {
            return date.toLocalDate().atStartOfDay();
        }
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
        return null;
    }
    
    private <E extends Enum<E>> E getEnum(Map<String, Object> row, String key, Class<E> enumClass) {
        String value = getString(row, key);
        if (value == null) return null;
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid enum value: {} for class: {}", value, enumClass.getName());
            return null;
        }
    }
}
