package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.api.ui.annotation.PluginAction;
import com.keqi.gress.plugin.api.ui.annotation.PluginMenu;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagTypeRequest;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagRequest;
import com.keqi.gress.plugin.appstore.admin.dto.TagDTO;
import com.keqi.gress.plugin.appstore.admin.dto.TagTypeDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateTagRequest;
import com.keqi.gress.plugin.appstore.admin.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tag Management Controller
 * Provides REST API for tag management
 */
//@Slf4j
@Service
@RestController
@RequestMapping("/tags")
@PluginMenu(id = "tags", name = "标签管理", managementEnabled = true)
public class TagController {

    @Autowired
    private TagService tagService;
    
    /**
     * Get all tags
     * GET /tags
     */
    @GetMapping
    public Result<List<TagDTO>> getAllTags() {

            List<TagDTO> tags = tagService.getAllTags();
            return Result.success(tags);

    }
    
    /**
     * Get tag by ID
     * GET /tags/{id}
     */
    @GetMapping("/{id}")
    public Result<TagDTO> getTagById(@PathVariable Long id) {

            TagDTO tag = tagService.getTagById(id);
            return Result.success(tag);

    }
    
    /**
     * Create tag
     * POST /tags
     */
    @PostMapping
    @PluginAction(id = "create", name = "新建标签")
    public Result<TagDTO> createTag(@RequestBody CreateTagRequest request) {

            TagDTO tag = tagService.createTag(request);
            return Result.success(tag);

    }
    
    /**
     * Update tag
     * PUT /tags/{id}
     */
    @PutMapping("/{id}")
    @PluginAction(id = "update", name = "编辑标签", managementEnabled = true, actionCode = "UPDATE")
    public Result<TagDTO> updateTag(
            @PathVariable Long id,
            @RequestBody UpdateTagRequest request) {

            TagDTO tag = tagService.updateTag(id, request);
            return Result.success(tag);
            

    }
    
    /**
     * Delete tag
     * DELETE /tags/{id}
     */
    @DeleteMapping("/{id}")
    @PluginAction(id = "delete", name = "删除标签", managementEnabled = true, actionCode = "DELETE")
    public Result<Void> deleteTag(@PathVariable Long id) {

            tagService.deleteTag(id);
            return Result.success();

    }
    
    /**
     * Get tags for a plugin
     * GET /plugins/{pluginId}/tags
     */
    @GetMapping("/plugins/{pluginId}")
    public Result<List<TagDTO>> getPluginTags(@PathVariable String pluginId) {

            List<TagDTO> tags = tagService.getPluginTags(pluginId);
            return Result.success(tags);

    }
    
    /**
     * Assign tag to plugin
     * POST /plugins/{pluginId}/tags/{tagId}
     */
    @PostMapping("/plugins/{pluginId}/tags/{tagId}")
    public Result<Void> assignTagToPlugin(
            @PathVariable String pluginId,
            @PathVariable Long tagId) {

            tagService.assignTagToPlugin(pluginId, tagId);
            return Result.success();
            

    }
    
    /**
     * Remove tag from plugin
     * DELETE /plugins/{pluginId}/tags/{tagId}
     */
    @DeleteMapping("/plugins/{pluginId}/tags/{tagId}")
    public Result<Void> removeTagFromPlugin(
            @PathVariable String pluginId,
            @PathVariable Long tagId) {

            tagService.removeTagFromPlugin(pluginId, tagId);
            return Result.success();

    }

    @GetMapping("/types")
    public Result<List<TagTypeDTO>> getAllTagTypes() {
        List<TagTypeDTO> tagTypes = tagService.getAllTagTypes();
        return Result.success(tagTypes);
    }

    @PostMapping("/types")
    public Result<TagTypeDTO> createTagType(@RequestBody CreateTagTypeRequest request) {
        TagTypeDTO tagType = tagService.createTagType(request);
        return Result.success(tagType);
    }

    @DeleteMapping("/types/{id}")
    public Result<Void> deleteTagType(@PathVariable Long id) {
        tagService.deleteTagType(id);
        return Result.success();
    }
}
