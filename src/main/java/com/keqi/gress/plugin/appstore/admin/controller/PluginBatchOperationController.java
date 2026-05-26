package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.BatchCategoryUpdateRequest;
import com.keqi.gress.plugin.appstore.admin.dto.BatchDelistRequest;
import com.keqi.gress.plugin.appstore.admin.dto.BatchOperationResult;
import com.keqi.gress.plugin.appstore.admin.service.BatchOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 插件批量下架/分类（菜单 plugins）
 */
@Service
@RestController
@RequestMapping("/batch")
@PluginMenu(id = "plugins", name = "插件列表", managementEnabled = true)
public class PluginBatchOperationController {

    @Autowired
    private BatchOperationService batchOperationService;

    @PostMapping("/delist")
    @PluginAction(id = "batch-delist", name = "批量下架")
    public Result<BatchOperationResult> batchDelist(@RequestBody BatchDelistRequest request) {
        if (request.getPluginIds() == null || request.getPluginIds().isEmpty()) {
            return Result.error("插件ID列表不能为空");
        }
        if (request.getReason() == null || request.getReason().isEmpty()) {
            return Result.error("下架原因不能为空");
        }
        BatchOperationResult result = batchOperationService.batchDelist(request);
        return Result.success(result);
    }

    @PostMapping("/update-category")
    @PluginAction(id = "batch-update-category", name = "批量更新分类")
    public Result<BatchOperationResult> batchUpdateCategory(@RequestBody BatchCategoryUpdateRequest request) {
        if (request.getPluginIds() == null || request.getPluginIds().isEmpty()) {
            return Result.error("插件ID列表不能为空");
        }
        if (request.getCategory() == null || request.getCategory().isEmpty()) {
            return Result.error("分类不能为空");
        }
        BatchOperationResult result = batchOperationService.batchUpdateCategory(request);
        return Result.success(result);
    }
}
