package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.BatchOperationService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 批量操作控制器
 * 提供插件批量管理的REST API
 */
@Service
@RestController
@RequestMapping("/batch")
public class BatchOperationController {
    
    private static final Log log = LogFactory.get(BatchOperationController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private BatchOperationService batchOperationService;
    
    /**
     * 批量审核插件提交
     *
     * @param request 批量审核请求
     * @return 批量操作结果
     */
    @PostMapping("/review")
    public Result<BatchOperationResult> batchReview(@RequestBody BatchReviewRequest request) {
        log.info("收到批量审核请求，提交数量: {}", request.getSubmissionIds().size());
        
        try {
            // 参数验证
            if (request.getSubmissionIds() == null || request.getSubmissionIds().isEmpty()) {
                return Result.error("提交ID列表不能为空");
            }
            
            if (request.getDecision() == null || request.getDecision().isEmpty()) {
                return Result.error("审核决策不能为空");
            }
            
            if (!"APPROVE".equalsIgnoreCase(request.getDecision()) && 
                !"REJECT".equalsIgnoreCase(request.getDecision())) {
                return Result.error("审核决策必须是 APPROVE 或 REJECT");
            }
            
            if ("REJECT".equalsIgnoreCase(request.getDecision()) && 
                (request.getComment() == null || request.getComment().isEmpty())) {
                return Result.error("拒绝时必须提供原因");
            }
            
            // 执行批量审核
            BatchOperationResult result = batchOperationService.batchReview(request);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("批量审核失败", e);
            return Result.error("批量审核失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量下架插件
     *
     * @param request 批量下架请求
     * @return 批量操作结果
     */
    @PostMapping("/delist")
    public Result<BatchOperationResult> batchDelist(@RequestBody BatchDelistRequest request) {
        log.info("收到批量下架请求，插件数量: {}", request.getPluginIds().size());
        
        try {
            // 参数验证
            if (request.getPluginIds() == null || request.getPluginIds().isEmpty()) {
                return Result.error("插件ID列表不能为空");
            }
            
            if (request.getReason() == null || request.getReason().isEmpty()) {
                return Result.error("下架原因不能为空");
            }
            
            // 执行批量下架
            BatchOperationResult result = batchOperationService.batchDelist(request);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("批量下架失败", e);
            return Result.error("批量下架失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量更新插件分类
     *
     * @param request 批量分类更新请求
     * @return 批量操作结果
     */
    @PostMapping("/update-category")
    public Result<BatchOperationResult> batchUpdateCategory(@RequestBody BatchCategoryUpdateRequest request) {
        log.info("收到批量更新分类请求，插件数量: {}", request.getPluginIds().size());
        
        try {
            // 参数验证
            if (request.getPluginIds() == null || request.getPluginIds().isEmpty()) {
                return Result.error("插件ID列表不能为空");
            }
            
            if (request.getCategory() == null || request.getCategory().isEmpty()) {
                return Result.error("分类不能为空");
            }
            
            // 执行批量更新分类
            BatchOperationResult result = batchOperationService.batchUpdateCategory(request);
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("批量更新分类失败", e);
            return Result.error("批量更新分类失败: " + e.getMessage());
        }
    }
}
