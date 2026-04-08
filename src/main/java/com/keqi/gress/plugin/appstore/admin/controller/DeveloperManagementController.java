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
            

    }
    
    /**
     * Get developer detail by ID
     *
     * @param id Developer ID
     * @return Developer detail with plugin list
     */
    @GetMapping("/{id}")
    public Result<DeveloperDetailDTO> getDeveloperDetail(@PathVariable Long id) {

            DeveloperDetailDTO detail = developerManagementService.getDeveloperDetail(id);
            
            if (detail == null) {
                return Result.error("Developer not found: " + id);
            }
            
            return Result.success(detail);
            

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

            developerManagementService.approveDeveloper(id, request);
            return Result.success();
            

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

            developerManagementService.suspendDeveloper(id, request);
            return Result.success();
            

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

            developerManagementService.activateDeveloper(id, request);
            return Result.success();

    }
}
