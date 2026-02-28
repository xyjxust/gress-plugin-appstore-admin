package com.keqi.gress.plugin.appstore.admin.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagRequest;
import com.keqi.gress.plugin.appstore.admin.dto.TagDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateTagRequest;
import com.keqi.gress.plugin.appstore.admin.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Tag Management Controller
 * Provides REST API for tag management
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/tags")
public class TagController {

    private final static Log log = LogFactory.get(TagController.class);
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private TagService tagService;
    
    /**
     * Get all tags
     * GET /tags
     */
    @GetMapping
    public Result<List<TagDTO>> getAllTags() {
        try {
            List<TagDTO> tags = tagService.getAllTags();
            return Result.success(tags);
            
        } catch (Exception e) {
            log.error("Failed to get tags", e);
            return Result.error("获取标签列表失败: " + e.getMessage());
        }
    }
    
    /**
     * Get tag by ID
     * GET /tags/{id}
     */
    @GetMapping("/{id}")
    public Result<TagDTO> getTagById(@PathVariable Long id) {
        try {
            TagDTO tag = tagService.getTagById(id);
            return Result.success(tag);
            
        } catch (Exception e) {
            log.error("Failed to get tag: id={}", id, e);
            return Result.error("获取标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Create tag
     * POST /tags
     */
    @PostMapping
    public Result<TagDTO> createTag(@RequestBody CreateTagRequest request) {
        try {
            TagDTO tag = tagService.createTag(request);
            return Result.success(tag);
            
        } catch (Exception e) {
            log.error("Failed to create tag", e);
            return Result.error("创建标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Update tag
     * PUT /tags/{id}
     */
    @PutMapping("/{id}")
    public Result<TagDTO> updateTag(
            @PathVariable Long id,
            @RequestBody UpdateTagRequest request) {
        try {
            TagDTO tag = tagService.updateTag(id, request);
            return Result.success(tag);
            
        } catch (Exception e) {
            log.error("Failed to update tag: id={}", id, e);
            return Result.error("更新标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Delete tag
     * DELETE /tags/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return Result.success();
            
        } catch (Exception e) {
            log.error("Failed to delete tag: id={}", id, e);
            return Result.error("删除标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Get tags for a plugin
     * GET /plugins/{pluginId}/tags
     */
    @GetMapping("/plugins/{pluginId}")
    public Result<List<TagDTO>> getPluginTags(@PathVariable String pluginId) {
        try {
            List<TagDTO> tags = tagService.getPluginTags(pluginId);
            return Result.success(tags);
            
        } catch (Exception e) {
            log.error("Failed to get plugin tags: pluginId={}", pluginId, e);
            return Result.error("获取插件标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Assign tag to plugin
     * POST /plugins/{pluginId}/tags/{tagId}
     */
    @PostMapping("/plugins/{pluginId}/tags/{tagId}")
    public Result<Void> assignTagToPlugin(
            @PathVariable String pluginId,
            @PathVariable Long tagId) {
        try {
            tagService.assignTagToPlugin(pluginId, tagId);
            return Result.success();
            
        } catch (Exception e) {
            log.error("Failed to assign tag to plugin: pluginId={}, tagId={}", pluginId, tagId, e);
            return Result.error("分配标签失败: " + e.getMessage());
        }
    }
    
    /**
     * Remove tag from plugin
     * DELETE /plugins/{pluginId}/tags/{tagId}
     */
    @DeleteMapping("/plugins/{pluginId}/tags/{tagId}")
    public Result<Void> removeTagFromPlugin(
            @PathVariable String pluginId,
            @PathVariable Long tagId) {
        try {
            tagService.removeTagFromPlugin(pluginId, tagId);
            return Result.success();
            
        } catch (Exception e) {
            log.error("Failed to remove tag from plugin: pluginId={}, tagId={}", pluginId, tagId, e);
            return Result.error("移除标签失败: " + e.getMessage());
        }
    }
}
