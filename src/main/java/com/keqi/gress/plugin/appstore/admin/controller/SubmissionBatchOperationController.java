package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.BatchOperationResult;
import com.keqi.gress.plugin.appstore.admin.dto.BatchReviewRequest;
import com.keqi.gress.plugin.appstore.admin.service.BatchOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 批量审核（菜单 submissions，与 plugin.yml plugin.ui.menus 对齐）
 */
@Service
@RestController
@RequestMapping("/batch")
@PluginMenu(id = "submissions", name = "插件审核", managementEnabled = true)
public class SubmissionBatchOperationController {

    @Autowired
    private BatchOperationService batchOperationService;

    @PostMapping("/review")
    @PluginAction(id = "batch-review", name = "批量审核")
    public Result<BatchOperationResult> batchReview(@RequestBody BatchReviewRequest request) {
        if (request.getSubmissionIds() == null || request.getSubmissionIds().isEmpty()) {
            return Result.error("提交ID列表不能为空");
        }
        if (request.getDecision() == null || request.getDecision().isEmpty()) {
            return Result.error("审核决策不能为空");
        }
        if (!"APPROVE".equalsIgnoreCase(request.getDecision())
                && !"REJECT".equalsIgnoreCase(request.getDecision())) {
            return Result.error("审核决策必须是 APPROVE 或 REJECT");
        }
        if ("REJECT".equalsIgnoreCase(request.getDecision())
                && (request.getComment() == null || request.getComment().isEmpty())) {
            return Result.error("拒绝时必须提供原因");
        }
        BatchOperationResult result = batchOperationService.batchReview(request);
        return Result.success(result);
    }
}
