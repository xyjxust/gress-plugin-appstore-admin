package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.DeveloperManagementService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Developer Management Controller
 * REST API for developer account management
 */
@Service
@RestController
@RequestMapping("/developers")
public class DeveloperManagementController {
    
    private static final Log log = LogFactory.get(DeveloperManagementController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private DeveloperManagementService developerManagementService;
    
    /**
     * Get developers with filtering and pagination
     *
     * @param page Page number (1-based), default 1
     * @param size Page size, default 20
     * @param status Filter by developer status
     * @param keyword Search keyword
     * @param verified Filter by verified status
     * @return Paginated list of developers
     */
    @GetMapping
    public Result<PageResult<DeveloperDTO>> getDevelopers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean verified) {
        
        log.info("GET /developers - page: {}, size: {}, status: {}, keyword: {}, verified: {}", 
                 page, size, status, keyword, verified);
        
        try {
            // Build query request
            DeveloperQueryRequest request = DeveloperQueryRequest.builder()
                    .page(page)
                    .size(size)
                    .keyword(keyword)
                    .verified(verified)
                    .build();
            
            // Parse status enum
            if (status != null && !status.trim().isEmpty()) {
                request.setStatus(status.toUpperCase());
            }
            
            // Query developers
            PageResult<DeveloperDTO> result = developerManagementService.getDevelopers(request);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("Failed to get developers", e);
            return Result.error("Failed to query developers: " + e.getMessage());
        }
    }
    
    /**
     * Get developer detail by ID
     *
     * @param id Developer ID
     * @return Developer detail with plugin list
     */
    @GetMapping("/{id}")
    public Result<DeveloperDetailDTO> getDeveloperDetail(@PathVariable Long id) {
        log.info("GET /developers/{}", id);
        
        try {
            DeveloperDetailDTO detail = developerManagementService.getDeveloperDetail(id);
            
            if (detail == null) {
                return Result.error("Developer not found: " + id);
            }
            
            return Result.success(detail);
            
        } catch (Exception e) {
            log.error("Failed to get developer detail: {}", id, e);
            return Result.error("Failed to get developer detail: " + e.getMessage());
        }
    }
    
    /**
     * Approve a developer application
     *
     * @param id Developer ID
     * @param request Approval request
     * @return Success result
     */
    @PostMapping("/{id}/approve")
    public Result<Void> approveDeveloper(@PathVariable Long id, @RequestBody DeveloperApprovalRequest request) {
        log.info("POST /developers/{}/approve - reviewer: {}", 
                 id, request.getReviewerName());
        
        try {
            developerManagementService.approveDeveloper(id, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid approval request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for approval: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to approve developer: {}", id, e);
            return Result.error("Failed to approve developer: " + e.getMessage());
        }
    }
    
    /**
     * Suspend a developer account
     *
     * @param id Developer ID
     * @param request Suspend request
     * @return Success result
     */
    @PostMapping("/{id}/suspend")
    public Result<Void> suspendDeveloper(@PathVariable Long id, @RequestBody DeveloperSuspendRequest request) {
        log.info("POST /developers/{}/suspend - operator: {}, reason: {}", 
                 id, request.getOperatorName(), request.getReason());
        
        try {
            developerManagementService.suspendDeveloper(id, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid suspend request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for suspension: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to suspend developer: {}", id, e);
            return Result.error("Failed to suspend developer: " + e.getMessage());
        }
    }
    
    /**
     * Activate a suspended developer account
     *
     * @param id Developer ID
     * @param request Activate request
     * @return Success result
     */
    @PostMapping("/{id}/activate")
    public Result<Void> activateDeveloper(@PathVariable Long id, @RequestBody DeveloperActivateRequest request) {
        log.info("POST /developers/{}/activate - operator: {}", 
                 id, request.getOperatorName());
        
        try {
            developerManagementService.activateDeveloper(id, request);
            return Result.success();
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid activate request: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (IllegalStateException e) {
            log.warn("Invalid state for activation: {}", e.getMessage());
            return Result.error(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to activate developer: {}", id, e);
            return Result.error("Failed to activate developer: " + e.getMessage());
        }
    }
}
