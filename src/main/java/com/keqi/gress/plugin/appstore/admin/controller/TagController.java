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
    public Result<TagDTO> createTag(@RequestBody CreateTagRequest request) {

            TagDTO tag = tagService.createTag(request);
            return Result.success(tag);

    }
    
    /**
     * Update tag
     * PUT /tags/{id}
     */
    @PutMapping("/{id}")
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
}
