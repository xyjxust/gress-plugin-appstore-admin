package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.*;
import com.keqi.gress.plugin.appstore.admin.entity.Category;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Category Management Service
 */
@Service
public class CategoryService {
    
    private static final Log log = LogFactory.get(CategoryService.class);
    
    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private AuditLogService auditLogService;
    
    /**
     * Helper method to log audit events
     */
    private void log(String operationType, Object targetId, Object details) {
        try {
            auditLogService.log(
                operationType,
                operationType,
                "CATEGORY",
                String.valueOf(targetId),
                "Category",
                "system",
                "System",
                "SUCCESS",
                null,
                details
            );
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
        }
    }
    
    /**
     * Get all categories
     */
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = dataSource.lambdaQuery(Category.class)
            .orderByAsc(Category::getDisplayOrder)
            .orderByDesc(Category::getCreateTime)
            .list();
        
        return categories.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get category by ID
     */
    public CategoryDTO getCategoryById(Long id) {
        Category category = dataSource.lambdaQuery(Category.class)
            .eq(Category::getId, id)
            .one();
        
        if (category == null) {
            throw new RuntimeException("Category not found: " + id);
        }
        
        return mapToDTO(category);
    }
    
    /**
     * Get category by key
     */
    public CategoryDTO getCategoryByKey(String categoryKey) {
        Category category = dataSource.lambdaQuery(Category.class)
            .eq(Category::getCategoryKey, categoryKey)
            .one();
        
        if (category == null) {
            return null;
        }
        
        return mapToDTO(category);
    }
    
    /**
     * Create category with uniqueness validation
     */
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        // Validate uniqueness of category name
        long nameCount = dataSource.lambdaQuery(Category.class)
            .eq(Category::getCategoryName, request.getCategoryName())
            .count();
        if (nameCount > 0) {
            throw new RuntimeException("Category name already exists: " + request.getCategoryName());
        }
        
        // Validate uniqueness of category key
        long keyCount = dataSource.lambdaQuery(Category.class)
            .eq(Category::getCategoryKey, request.getCategoryKey())
            .count();
        if (keyCount > 0) {
            throw new RuntimeException("Category key already exists: " + request.getCategoryKey());
        }
        
        // Insert category
        Category entity = Category.builder()
            .categoryName(request.getCategoryName())
            .categoryKey(request.getCategoryKey())
            .description(request.getDescription())
            .icon(request.getIcon())
            .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
            .enabled(request.getEnabled() != null ? request.getEnabled() : true)
            .build();
        
        dataSource.insert(entity);
        
        // Record audit log
        log("CREATE_CATEGORY", entity.getId(), request);
        
        log.info("Category created: id={}, name={}", entity.getId(), request.getCategoryName());
        
        return getCategoryById(entity.getId());
    }
    
    /**
     * Update category
     */
    public CategoryDTO updateCategory(Long id, UpdateCategoryRequest request) {
        // Check if category exists
        CategoryDTO existing = getCategoryById(id);
        
        if (request.getCategoryName() != null) {
            // Check name uniqueness (excluding current category)
            long count = dataSource.lambdaQuery(Category.class)
                .eq(Category::getCategoryName, request.getCategoryName())
                .ne(Category::getId, id)
                .count();
            if (count > 0) {
                throw new RuntimeException("Category name already exists: " + request.getCategoryName());
            }
        }
        
        // Execute update
        dataSource.lambdaUpdate(Category.class)
            .func(chain -> {
                if (request.getCategoryName() != null) {
                    chain.set(Category::getCategoryName, request.getCategoryName());
                }
                if (request.getDescription() != null) {
                    chain.set(Category::getDescription, request.getDescription());
                }
                if (request.getIcon() != null) {
                    chain.set(Category::getIcon, request.getIcon());
                }
                if (request.getDisplayOrder() != null) {
                    chain.set(Category::getDisplayOrder, request.getDisplayOrder());
                }
                if (request.getEnabled() != null) {
                    chain.set(Category::getEnabled, request.getEnabled());
                }
            })
            .eq(Category::getId, id)
            .update();
        
        // Record audit log
        log("UPDATE_CATEGORY", id, Map.of(
            "before", existing,
            "after", request
        ));
        
        log.info("Category updated: id={}", id);
        
        return getCategoryById(id);
    }
    
    /**
     * Delete category with cascade check
     */
    public void deleteCategory(Long id) {
        // Check if category exists
        CategoryDTO category = getCategoryById(id);
        
        // Check if any plugins are using this category (使用动态 SQL)
        String checkPluginsSql = """
                SELECT COUNT(*) as count FROM as_admin_plugin_submission 
                WHERE category = #{categoryKey}
                """;
        List<Map<String, Object>> pluginRows = dataSource.dynamicSql(checkPluginsSql)
                .param("categoryKey", category.getCategoryKey())
                .query();
        
        Long pluginCount = pluginRows.isEmpty() ? 0L : ((Number) pluginRows.get(0).get("count")).longValue();
        
        if (pluginCount != null && pluginCount > 0) {
            throw new RuntimeException("Cannot delete category: " + pluginCount + " plugins are using this category. " +
                                     "Please reassign these plugins to another category first.");
        }
        
        // Check in manager table as well (使用动态 SQL)
        String checkManagerSql = """
                SELECT COUNT(*) as count FROM as_admin_manager 
                WHERE category = #{categoryKey}
                """;
        List<Map<String, Object>> managerRows = dataSource.dynamicSql(checkManagerSql)
                .param("categoryKey", category.getCategoryKey())
                .query();
        
        Long managerCount = managerRows.isEmpty() ? 0L : ((Number) managerRows.get(0).get("count")).longValue();
        
        if (managerCount != null && managerCount > 0) {
            throw new RuntimeException("Cannot delete category: " + managerCount + " listed plugins are using this category. " +
                                     "Please reassign these plugins to another category first.");
        }
        
        // Delete category
        dataSource.lambdaUpdate(Category.class)
            .eq(Category::getId, id)
            .delete();
        
        // Record audit log
        log("DELETE_CATEGORY", id, category);
        
        log.info("Category deleted: id={}, name={}", id, category.getCategoryName());
    }
    
    /**
     * Update plugin count for a category
     */
    public void updatePluginCount(String categoryKey) {
        // Count plugins in this category (使用动态 SQL)
        String countSql = """
                SELECT COUNT(*) as count FROM as_admin_manager 
                WHERE category = #{categoryKey} AND status = 'ONLINE'
                """;
        List<Map<String, Object>> rows = dataSource.dynamicSql(countSql)
                .param("categoryKey", categoryKey)
                .query();
        
        Long count = rows.isEmpty() ? 0L : ((Number) rows.get(0).get("count")).longValue();
        
        // Update category
        dataSource.lambdaUpdate(Category.class)
            .eq(Category::getCategoryKey, categoryKey)
            .set(Category::getPluginCount, count != null ? count.intValue() : 0)
            .update();
    }
    
    /**
     * Map entity to DTO
     */
    private CategoryDTO mapToDTO(Category entity) {
        return CategoryDTO.builder()
            .id(entity.getId())
            .categoryName(entity.getCategoryName())
            .categoryKey(entity.getCategoryKey())
            .description(entity.getDescription())
            .icon(entity.getIcon())
            .displayOrder(entity.getDisplayOrder() != null ? entity.getDisplayOrder() : 0)
            .enabled(entity.getEnabled())
            .pluginCount(entity.getPluginCount() != null ? entity.getPluginCount() : 0)
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }
}
