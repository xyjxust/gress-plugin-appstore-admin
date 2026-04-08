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

            List<CategoryDTO> categories = categoryService.getAllCategories();
            return Result.success(categories);

    }
    
    /**
     * Get category by ID
     * GET /categories/{id}
     */
    @GetMapping("/{id}")
    public Result<CategoryDTO> getCategoryById(@PathVariable Long id) {

            CategoryDTO category = categoryService.getCategoryById(id);
            return Result.success(category);
            

    }
    
    /**
     * Create category
     * POST /categories
     */
    @PostMapping
    public Result<CategoryDTO> createCategory(@RequestBody CreateCategoryRequest request) {

            CategoryDTO category = categoryService.createCategory(request);
            return Result.success(category);
            

    }
    
    /**
     * Update category
     * PUT /categories/{id}
     */
    @PutMapping("/{id}")
    public Result<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody UpdateCategoryRequest request) {

            CategoryDTO category = categoryService.updateCategory(id, request);
            return Result.success(category);
            

    }
    
    /**
     * Delete category
     * DELETE /categories/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {

            categoryService.deleteCategory(id);
            return Result.success();
            

    }
}
