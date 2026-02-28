package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.service.CategoryService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category Management Controller
 * Provides REST API for category management
 */
@Service
@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private static final Log log = LogFactory.get(CategoryController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private CategoryService categoryService;
    
    /**
     * Get all categories
     * GET /categories
     */
    @GetMapping
    public Result<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return Result.success(categories);
            
        } catch (Exception e) {
            log.error("Failed to get categories", e);
            return Result.error("获取分类列表失败: " + e.getMessage());
        }
    }
    
    /**
     * Get category by ID
     * GET /categories/{id}
     */
    @GetMapping("/{id}")
    public Result<CategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            CategoryDTO category = categoryService.getCategoryById(id);
            return Result.success(category);
            
        } catch (Exception e) {
            log.error("Failed to get category: id={}", id, e);
            return Result.error("获取分类失败: " + e.getMessage());
        }
    }
    
    /**
     * Create category
     * POST /categories
     */
    @PostMapping
    public Result<CategoryDTO> createCategory(@RequestBody CreateCategoryRequest request) {
        try {
            CategoryDTO category = categoryService.createCategory(request);
            return Result.success(category);
            
        } catch (Exception e) {
            log.error("Failed to create category", e);
            return Result.error("创建分类失败: " + e.getMessage());
        }
    }
    
    /**
     * Update category
     * PUT /categories/{id}
     */
    @PutMapping("/{id}")
    public Result<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {
        try {
            CategoryDTO category = categoryService.updateCategory(id, request);
            return Result.success(category);
            
        } catch (Exception e) {
            log.error("Failed to update category: id={}", id, e);
            return Result.error("更新分类失败: " + e.getMessage());
        }
    }
    
    /**
     * Delete category
     * DELETE /categories/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return Result.success();
            
        } catch (Exception e) {
            log.error("Failed to delete category: id={}", id, e);
            return Result.error("删除分类失败: " + e.getMessage());
        }
    }
}
