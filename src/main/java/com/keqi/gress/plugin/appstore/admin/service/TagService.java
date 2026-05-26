package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagTypeRequest;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagRequest;
import com.keqi.gress.plugin.appstore.admin.dto.TagDTO;
import com.keqi.gress.plugin.appstore.admin.dto.TagTypeDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateTagRequest;
import com.keqi.gress.plugin.appstore.admin.entity.Tag;
import com.keqi.gress.plugin.appstore.admin.entity.TagType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tag Management Service
 */
@Service
@Slf4j
public class TagService {

    @Autowired
    private PluginLambdaDataSource dataSource;
    
    @Autowired
    private AuditLogService auditLogService;
    
    /**
     * Helper method to log audit events
     */
    private void logAudit(String operationType, Object targetId, Object details) {

            auditLogService.log(
                operationType,
                operationType,
                "TAG",
                String.valueOf(targetId),
                "Tag",
                "system",
                "System",
                "SUCCESS",
                null,
                details
            );

    }
    
    /**
     * Get all tags
     */
    public List<TagDTO> getAllTags() {
        List<Tag> tags = dataSource.lambdaQuery(Tag.class)
            .orderByDesc(Tag::getUsageCount)
            .orderByDesc(Tag::getCreateTime)
            .list();

        return tags.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Get tag by ID
     */
    public TagDTO getTagById(Long id) {
        Tag tag = dataSource.lambdaQuery(Tag.class)
            .eq(Tag::getId, id)
            .one();
        
        if (tag == null) {
            throw new RuntimeException("Tag not found: " + id);
        }
        
        return mapToDTO(tag);
    }
    
    /**
     * Get tag by key
     */
    public TagDTO getTagByKey(String tagKey) {
        Tag tag = dataSource.lambdaQuery(Tag.class)
            .eq(Tag::getTagKey, tagKey)
            .one();
        
        if (tag == null) {
            return null;
        }
        
        return mapToDTO(tag);
    }
    
    /**
     * Create tag with uniqueness validation
     */
    public TagDTO createTag(CreateTagRequest request) {
        if (request.getTagTypeKey() != null && !request.getTagTypeKey().isBlank()) {
            ensureTagTypeExistsByKey(request.getTagTypeKey());
        }

        // Validate uniqueness of tag name
        long nameCount = dataSource.lambdaQuery(Tag.class)
            .eq(Tag::getTagName, request.getTagName())
            .count();
        if (nameCount > 0) {
            throw new RuntimeException("Tag name already exists: " + request.getTagName());
        }
        
        // Validate uniqueness of tag key
        long keyCount = dataSource.lambdaQuery(Tag.class)
            .eq(Tag::getTagKey, request.getTagKey())
            .count();
        if (keyCount > 0) {
            throw new RuntimeException("Tag key already exists: " + request.getTagKey());
        }
        
        // Insert tag
        LocalDateTime now = LocalDateTime.now();
        Tag entity = Tag.builder()
            .tagName(request.getTagName())
            .tagKey(request.getTagKey())
            .description(request.getDescription())
            .color(request.getColor())
            .tagTypeKey(request.getTagTypeKey())
            .enabled(request.getEnabled() != null ? request.getEnabled() : Boolean.TRUE)
            .usageCount(0)
            .build();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        dataSource.insert(entity);
        
        // Record audit log
        logAudit("CREATE_TAG", entity.getId(), request);
        
        log.info("Tag created: id={}, name={}", entity.getId(), request.getTagName());
        
        return getTagById(entity.getId());
    }
    
    /**
     * Update tag
     */
    public TagDTO updateTag(Long id, UpdateTagRequest request) {
        // Check if tag exists
        TagDTO existing = getTagById(id);

        if (request.getTagName() != null) {
            // Check name uniqueness (excluding current tag)
            long count = dataSource.lambdaQuery(Tag.class)
                .eq(Tag::getTagName, request.getTagName())
                .ne(Tag::getId, id)
                .count();
            if (count > 0) {
                throw new RuntimeException("Tag name already exists: " + request.getTagName());
            }
        }
        
        if (request.getTagTypeKey() != null && !request.getTagTypeKey().isBlank()) {
            ensureTagTypeExistsByKey(request.getTagTypeKey());
        }

        dataSource.lambdaUpdate(Tag.class)
            .eq(Tag::getId, id)
            .func(chain -> {
                if (request.getTagName() != null) {
                    chain.set(Tag::getTagName, request.getTagName());
                }
                if (request.getDescription() != null) {
                    chain.set(Tag::getDescription, request.getDescription());
                }
                if (request.getColor() != null) {
                    chain.set(Tag::getColor, request.getColor());
                }
                if (request.getTagTypeKey() != null) {
                    chain.set(Tag::getTagTypeKey, request.getTagTypeKey());
                }
                if (request.getEnabled() != null) {
                    chain.set(Tag::getEnabled, request.getEnabled());
                }
                // Always update updateTime
                chain.set(Tag::getUpdateTime, LocalDateTime.now());
            })
            .update();
        
        // Record audit log
        logAudit("UPDATE_TAG", id, Map.of(
            "before", existing,
            "after", request
        ));
        
        log.info("Tag updated: id={}", id);
        
        return getTagById(id);
    }
    
    /**
     * Delete tag
     */
    public void deleteTag(Long id) {
        // Check if tag exists
        TagDTO tag = getTagById(id);
        
        // Check if any plugins are using this tag (use dynamic SQL)
        String checkUsageSql = "SELECT COUNT(*) as count FROM as_admin_plugin_tag WHERE tag_id = #{tagId}";
        List<Map<String, Object>> rows = dataSource.dynamicSql(checkUsageSql)
            .param("tagId", id)
            .query();
        
        Long usageCount = rows.isEmpty() ? 0L : ((Number) rows.get(0).get("count")).longValue();
        
        if (usageCount > 0) {
            throw new RuntimeException("Cannot delete tag: " + usageCount + " plugins are using this tag. " +
                                     "Please remove this tag from all plugins first.");
        }
        
        // Delete tag
        dataSource.lambdaUpdate(Tag.class)
            .eq(Tag::getId, id)
            .delete();
        
        // Record audit log
        logAudit("DELETE_TAG", id, tag);
        
        log.info("Tag deleted: id={}, name={}", id, tag.getTagName());
    }
    
    /**
     * Assign tag to plugin
     */
    public void assignTagToPlugin(String pluginId, Long tagId) {
        // Check if tag exists
        getTagById(tagId);
        
        // Check if already assigned (use dynamic SQL)
        String checkSql = "SELECT COUNT(*) as count FROM as_admin_plugin_tag WHERE plugin_id = #{pluginId} AND tag_id = #{tagId}";
        List<Map<String, Object>> rows = dataSource.dynamicSql(checkSql)
            .param("pluginId", pluginId)
            .param("tagId", tagId)
            .query();
        
        Long count = rows.isEmpty() ? 0L : ((Number) rows.get(0).get("count")).longValue();
        
        if (count > 0) {
            log.warn("Tag already assigned to plugin: pluginId={}, tagId={}", pluginId, tagId);
            return;
        }
        
        // Insert relationship (use dynamic SQL)
        String insertSql = "INSERT INTO as_admin_plugin_tag (plugin_id, tag_id) VALUES (#{pluginId}, #{tagId})";
        dataSource.dynamicSql(insertSql)
            .param("pluginId", pluginId)
            .param("tagId", tagId)
            .execute();
        
        // Update usage count
        updateUsageCount(tagId);
        
        log.info("Tag assigned to plugin: pluginId={}, tagId={}", pluginId, tagId);
    }
    
    /**
     * Remove all tag associations for a plugin (e.g. before deleting the plugin).
     */
    public void clearPluginTagsForPlugin(String pluginId) {
        String listSql = "SELECT tag_id FROM as_admin_plugin_tag WHERE plugin_id = #{pluginId}";
        List<Map<String, Object>> rows = dataSource.dynamicSql(listSql)
            .param("pluginId", pluginId)
            .query();
        String deleteSql = "DELETE FROM as_admin_plugin_tag WHERE plugin_id = #{pluginId}";
        dataSource.dynamicSql(deleteSql)
            .param("pluginId", pluginId)
            .execute();
        for (Map<String, Object> row : rows) {
            Object tid = row.get("tag_id");
            if (tid == null) {
                tid = row.get("TAG_ID");
            }
            if (tid instanceof Number) {
                updateUsageCount(((Number) tid).longValue());
            }
        }
        log.info("Cleared all tags for plugin: {}", pluginId);
    }
    
    /**
     * Remove tag from plugin
     */
    public void removeTagFromPlugin(String pluginId, Long tagId) {
        // Delete relationship (use dynamic SQL)
        String deleteSql = "DELETE FROM as_admin_plugin_tag WHERE plugin_id = #{pluginId} AND tag_id = #{tagId}";
        dataSource.dynamicSql(deleteSql)
            .param("pluginId", pluginId)
            .param("tagId", tagId)
            .execute();
        
        // Update usage count
        updateUsageCount(tagId);
        log.info("Tag removed from plugin: pluginId={}, tagId={}", pluginId, tagId);
    }
    
    /**
     * Get tags for a plugin
     * 连表查询使用自定义SQL
     */
    public List<TagDTO> getPluginTags(String pluginId) {
        // Use dynamic SQL for multi-table join
        String sql = """
            SELECT t.* FROM as_admin_tag t 
            INNER JOIN as_admin_plugin_tag pt ON t.id = pt.tag_id 
            WHERE pt.plugin_id = #{pluginId}
            ORDER BY t.usage_count DESC
            """;
        
        List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
            .param("pluginId", pluginId)
            .query();
        
        return rows.stream()
            .map(row -> {
                Tag tag = new Tag();
                tag.setId(((Number) row.get("id")).longValue());
                tag.setTagName((String) row.get("tag_name"));
                tag.setTagKey((String) row.get("tag_key"));
                tag.setDescription((String) row.get("description"));
                tag.setColor((String) row.get("color"));
                tag.setTagTypeKey((String) row.get("tag_type_key"));
                tag.setEnabled((Boolean) row.get("enabled"));
                tag.setUsageCount(row.get("usage_count") != null ? ((Number) row.get("usage_count")).intValue() : 0);
                tag.setCreateTime((LocalDateTime) row.get("create_time"));
                tag.setUpdateTime((LocalDateTime) row.get("update_time"));
                return mapToDTO(tag);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Update usage count for a tag
     */
    public void updateUsageCount(Long tagId) {
        // Count plugins using this tag (use dynamic SQL)
        String countSql = "SELECT COUNT(*) as count FROM as_admin_plugin_tag WHERE tag_id = #{tagId}";
        List<Map<String, Object>> rows = dataSource.dynamicSql(countSql)
            .param("tagId", tagId)
            .query();
        
        Long count = rows.isEmpty() ? 0L : ((Number) rows.get(0).get("count")).longValue();
        
        // Update tag
        dataSource.lambdaUpdate(Tag.class)
            .set(Tag::getUsageCount, count.intValue())
            .eq(Tag::getId, tagId)
            .update();
    }

    public List<TagTypeDTO> getAllTagTypes() {
        List<TagType> tagTypes = dataSource.lambdaQuery(TagType.class)
            .orderByDesc(TagType::getCreateTime)
            .list();
        return tagTypes.stream().map(this::mapToTypeDTO).collect(Collectors.toList());
    }

    public TagTypeDTO createTagType(CreateTagTypeRequest request) {
        long nameCount = dataSource.lambdaQuery(TagType.class)
            .eq(TagType::getTypeName, request.getTypeName())
            .count();
        if (nameCount > 0) {
            throw new RuntimeException("Tag type name already exists: " + request.getTypeName());
        }

        long keyCount = dataSource.lambdaQuery(TagType.class)
            .eq(TagType::getTypeKey, request.getTypeKey())
            .count();
        if (keyCount > 0) {
            throw new RuntimeException("Tag type key already exists: " + request.getTypeKey());
        }

        LocalDateTime now = LocalDateTime.now();
        TagType entity = TagType.builder()
            .typeName(request.getTypeName())
            .typeKey(request.getTypeKey())
            .description(request.getDescription())
            .enabled(request.getEnabled() != null ? request.getEnabled() : Boolean.TRUE)
            .build();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);

        dataSource.insert(entity);
        return mapToTypeDTO(entity);
    }

    public void deleteTagType(Long id) {
        TagType tagType = dataSource.lambdaQuery(TagType.class)
            .eq(TagType::getId, id)
            .one();
        if (tagType == null) {
            throw new RuntimeException("Tag type not found: " + id);
        }

        long usedCount = dataSource.lambdaQuery(Tag.class)
            .eq(Tag::getTagTypeKey, tagType.getTypeKey())
            .count();
        if (usedCount > 0) {
            throw new RuntimeException("Cannot delete tag type: " + usedCount + " tags are using this type.");
        }

        dataSource.lambdaUpdate(TagType.class)
            .eq(TagType::getId, id)
            .delete();
    }

    private void ensureTagTypeExistsByKey(String tagTypeKey) {
        long count = dataSource.lambdaQuery(TagType.class)
            .eq(TagType::getTypeKey, tagTypeKey)
            .count();
        if (count == 0) {
            throw new RuntimeException("Tag type not found: " + tagTypeKey);
        }
    }
    
    /**
     * Map database row to DTO
     */
    private TagDTO mapToDTO(Tag entity) {
        return TagDTO.builder()
            .id(entity.getId())
            .tagName(entity.getTagName())
            .tagKey(entity.getTagKey())
            .description(entity.getDescription())
            .color(entity.getColor())
            .tagTypeKey(entity.getTagTypeKey())
            .enabled(entity.getEnabled())
            .usageCount(entity.getUsageCount() != null ? entity.getUsageCount() : 0)
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }

    private TagTypeDTO mapToTypeDTO(TagType entity) {
        return TagTypeDTO.builder()
            .id(entity.getId())
            .typeName(entity.getTypeName())
            .typeKey(entity.getTypeKey())
            .description(entity.getDescription())
            .enabled(entity.getEnabled())
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }
}
