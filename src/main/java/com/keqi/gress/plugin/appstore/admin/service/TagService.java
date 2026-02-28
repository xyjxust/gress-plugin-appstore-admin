package com.keqi.gress.plugin.appstore.admin.service;

import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.CreateTagRequest;
import com.keqi.gress.plugin.appstore.admin.dto.TagDTO;
import com.keqi.gress.plugin.appstore.admin.dto.UpdateTagRequest;
import com.keqi.gress.plugin.appstore.admin.entity.Tag;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tag Management Service
 */
@Service
public class TagService {
    
    private static final Log log = LogFactory.get(TagService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private AuditLogService auditLogService;
    
    /**
     * Helper method to log audit events
     */
    private void logAudit(String operationType, Object targetId, Object details) {
        try {
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
        } catch (Exception e) {
            log.error("Failed to record audit log", e);
        }
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
            .enabled(request.getEnabled() != null ? request.getEnabled() : Boolean.TRUE)
            .usageCount(0)
            .createTime(now)
            .updateTime(now)
            .build();

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
        String checkUsageSql = "SELECT COUNT(*) as count FROM appstore_plugin_tag WHERE tag_id = #{tagId}";
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
        String checkSql = "SELECT COUNT(*) as count FROM appstore_plugin_tag WHERE plugin_id = #{pluginId} AND tag_id = #{tagId}";
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
        String insertSql = "INSERT INTO appstore_plugin_tag (plugin_id, tag_id) VALUES (#{pluginId}, #{tagId})";
        dataSource.dynamicSql(insertSql)
            .param("pluginId", pluginId)
            .param("tagId", tagId)
            .execute();
        
        // Update usage count
        updateUsageCount(tagId);
        
        log.info("Tag assigned to plugin: pluginId={}, tagId={}", pluginId, tagId);
    }
    
    /**
     * Remove tag from plugin
     */
    public void removeTagFromPlugin(String pluginId, Long tagId) {
        // Delete relationship (use dynamic SQL)
        String deleteSql = "DELETE FROM appstore_plugin_tag WHERE plugin_id = #{pluginId} AND tag_id = #{tagId}";
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
            SELECT t.* FROM appstore_tag t 
            INNER JOIN appstore_plugin_tag pt ON t.id = pt.tag_id 
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
        String countSql = "SELECT COUNT(*) as count FROM appstore_plugin_tag WHERE tag_id = #{tagId}";
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
            .enabled(entity.getEnabled())
            .usageCount(entity.getUsageCount() != null ? entity.getUsageCount() : 0)
            .createTime(entity.getCreateTime())
            .updateTime(entity.getUpdateTime())
            .build();
    }
}
