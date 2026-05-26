package com.keqi.gress.plugin.appstore.admin.controller;

import com.keqi.gress.common.model.Result;
import com.keqi.gress.plugin.appstore.admin.dto.CategoryDTO;
import com.keqi.gress.plugin.appstore.admin.dto.TagDTO;
import com.keqi.gress.plugin.appstore.admin.service.CategoryService;
import com.keqi.gress.plugin.appstore.admin.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用商店分类 API（用户端）
 */
@Service
@RestController
@RequestMapping("/anon")
public class AppStoreCategoryApiController {

    private static final String PLUGIN_BIZ_TYPE = "plugin_biz_type";

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @GetMapping("/categories")
    public Result<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        if (categories == null) {
            return Result.success(Collections.emptyList());
        }
        return Result.success(categories.stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getEnabled() == null || Boolean.TRUE.equals(it.getEnabled()))
                .collect(Collectors.toList())
        );
    }

    /**
     * 获取业务标签（默认 tagTypeKey=plugin_biz_type）
     */
    @GetMapping("/tags")
    public Result<List<TagDTO>> getBizTags(@RequestParam(required = false) String typeKey) {
        String resolvedTypeKey = (typeKey == null || typeKey.isBlank()) ? PLUGIN_BIZ_TYPE : typeKey;
        List<TagDTO> tags = tagService.getAllTags();
        if (tags == null) {
            return Result.success(Collections.emptyList());
        }
        return Result.success(
            tags.stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getEnabled() == null || Boolean.TRUE.equals(it.getEnabled()))
                .filter(it -> resolvedTypeKey.equals(it.getTagTypeKey()))
                .collect(Collectors.toList())
        );
    }
}
