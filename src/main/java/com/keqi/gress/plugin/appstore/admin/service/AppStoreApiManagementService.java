package com.keqi.gress.plugin.appstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keqi.gress.common.data.TypeConverter;
import com.keqi.gress.common.model.Result;
import com.keqi.gress.common.plugin.annotion.Inject;
import com.keqi.gress.common.plugin.annotion.Service;
import com.keqi.gress.common.storage.FileStorageService;
import com.keqi.gress.plugin.api.database.page.IPage;
import com.keqi.gress.plugin.api.service.PluginLambdaDataSource;
import com.keqi.gress.plugin.appstore.admin.dto.PageResult;
import com.keqi.gress.plugin.appstore.admin.dto.PluginPackageDTO;
import com.keqi.gress.plugin.appstore.admin.entity.PluginManager;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 应用商店 API 服务（用户端）
 * 
 * 提供应用查询和下载功能
 * 从 appstore_manager 和 appstore_version 表查询数据
 */
@Service
public class AppStoreApiManagementService {
    
    private static final Log log = LogFactory.get(AppStoreApiManagementService.class);
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginLambdaDataSource dataSource;
    
    @Inject(source = Inject.BeanSource.SPRING)
    private FileStorageService fileStorageService;
    
    /**
     * 查询应用列表（用户端）
     * 使用 MyBatis 动态 SQL 进行多表联查
     * 
     * @param page 页码
     * @param size 每页大小
     * @param pluginType 插件类型（可选）
     * @param category 分类（可选）
     * @param keyword 关键词搜索（可选）
     * @return 应用列表
     */
    public PageResult<PluginPackageDTO> getPackages(
            Integer page, 
            Integer size, 
            String pluginType,
            String category,
            String keyword) {
        
        log.info("查询应用列表: page={}, size={}, pluginType={}, category={}, keyword={}", 
                page, size, pluginType, category, keyword);
        
        // 使用 MyBatis 动态 SQL 进行多表联查
        // 注意：使用 page() 方法时，SQL 中不需要手动添加 LIMIT
        String sql = """
                SELECT m.*, v.file_path, v.file_size, v.release_notes
                FROM appstore_manager m
                LEFT JOIN appstore_version v ON m.plugin_id = v.plugin_id AND v.is_current = 1
                <where>
                  m.status = 'ONLINE'
                  <if test='pluginType != nil'>
                    AND m.plugin_type = #{pluginType}
                  </if>
                  <if test='category != nil'>
                    AND m.category = #{category}
                  </if>
                  <if test='keyword != nil'>
                    AND (m.plugin_name LIKE #{keywordPattern} OR m.description LIKE #{keywordPattern})
                  </if>
                </where>
                ORDER BY m.install_count DESC, m.rating_average DESC, m.update_time DESC
                """;
        
        // 构建查询参数
        String keywordPattern = keyword != null && !keyword.trim().isEmpty() ? "%" + keyword + "%" : null;
        
        // 执行分页查询 - 使用 page() 和 queryPage() 自动处理分页和总数统计
        IPage<Map<String, Object>> pageResult = dataSource.dynamicSql(sql)
                .param("pluginType", pluginType != null && !pluginType.trim().isEmpty() ? pluginType.toUpperCase() : null)
                .param("category", category != null && !category.trim().isEmpty() ? category : null)
                .param("keyword", keyword != null && !keyword.trim().isEmpty() ? keyword : null)
                .param("keywordPattern", keywordPattern)
                .page(page, size)
                .queryPage();
        
        // 转换为DTO
        List<PluginPackageDTO> packages = pageResult.getRecords().stream()
                .map(this::mapManagerToPluginPackageDTO)
                .collect(Collectors.toList());
        
        log.info("查询到 {} 个应用，共 {} 个", packages.size(), pageResult.getTotal());
        
        return PageResult.of(packages, pageResult.getTotal(), page, size);
    }
    
    /**
     * 获取应用详情（用户端）
     * 使用 MyBatis 动态 SQL 进行多表联查
     * 递归解析所有间接依赖，将依赖链中的所有依赖都包含在返回结果中
     * 
     * @param pluginId 插件ID
     * @return 应用详情（包含所有直接和间接依赖）
     */
    public Result<PluginPackageDTO> getPackageDetail(String pluginId) {
        log.info("获取应用详情: pluginId={}", pluginId);
        
        try {
            // 使用 MyBatis 动态 SQL 进行多表联查
            String sql = """
                    SELECT m.*, v.file_path, v.file_size, v.release_notes, v.file_hash, v.dependencies, v.version
                    FROM appstore_manager m
                    LEFT JOIN appstore_version v ON m.plugin_id = v.plugin_id AND v.is_current = 1
                    WHERE m.plugin_id = #{pluginId}
                    """;
            
            List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
                    .param("pluginId", pluginId)
                    .query();
            
            if (rows == null || rows.isEmpty()) {
                return Result.error("应用不存在");
            }
            
            PluginPackageDTO packageDTO = mapManagerToPluginPackageDTO(rows.get(0));
            
            // 递归解析所有间接依赖
            String version = (String) rows.get(0).get("version");
            String allDependenciesJson = resolveAllDependencies(pluginId, version);
            if (allDependenciesJson != null) {
                packageDTO.setDependencies(allDependenciesJson);
                log.info("递归解析依赖完成: pluginId={}, 依赖数量={}", 
                        pluginId, countDependencies(allDependenciesJson));
            }
            
            return Result.success(packageDTO);
            
        } catch (Exception e) {
            log.error("获取应用详情失败: pluginId={}", pluginId, e);
            return Result.error("获取应用详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 递归解析所有依赖（包括直接依赖和间接依赖）
     * 
     * 例如：a依赖b，b依赖c，c依赖d
     * 返回结果将包含 b、c、d 的所有依赖信息，并按倒序排列：d、c、b
     * （最底层依赖在前，直接依赖在后）
     * 
     * @param pluginId 插件ID
     * @param version 版本号（可选）
     * @return 所有依赖的JSON字符串（按倒序排列）
     */
    private String resolveAllDependencies(String pluginId, String version) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            // 用于存储所有依赖（去重）
            Map<String, DependencyInfo> allDependencies = new LinkedHashMap<>();
            
            // 用于跟踪已处理的插件，避免循环依赖
            Set<String> processedPlugins = new HashSet<>();
            
            // 用于构建依赖图（记录每个依赖的依赖关系）
            Map<String, Set<String>> dependencyGraph = new HashMap<>();
            
            // 递归收集所有依赖并构建依赖图
            collectDependenciesRecursive(pluginId, version, allDependencies, processedPlugins, dependencyGraph);
            
            // 使用拓扑排序确定正确的顺序（安装顺序：最底层依赖 -> 直接依赖）
            List<String> installOrder = calculateInstallOrder(allDependencies, dependencyGraph);
            
            // 按照安装顺序倒序排列（最底层依赖在前，直接依赖在后）
            List<DependencyInfo> orderedDependencies = new ArrayList<>();
            for (String key : installOrder) {
                DependencyInfo dep = allDependencies.get(key);
                if (dep != null) {
                    orderedDependencies.add(dep);
                }
            }
            
            log.debug("依赖排序完成: pluginId={}, 顺序={}", pluginId, installOrder);
            
            return mapper.writeValueAsString(orderedDependencies);
            
        } catch (Exception e) {
            log.error("解析依赖链失败: pluginId={}, version={}", pluginId, version, e);
            return null;
        }
    }
    
    /**
     * 递归收集依赖信息并构建依赖图
     * 
     * @param pluginId 插件ID
     * @param version 版本号（可选）
     * @param allDependencies 存储所有依赖的Map（用于去重）
     * @param processedPlugins 已处理的插件集合（用于避免循环依赖）
     * @param dependencyGraph 依赖图（key -> 依赖它的插件集合）
     */
    private void collectDependenciesRecursive(
            String pluginId, 
            String version,
            Map<String, DependencyInfo> allDependencies,
            Set<String> processedPlugins,
            Map<String, Set<String>> dependencyGraph) {
        
        // 生成唯一标识
        String key = pluginId + "@" + (version != null ? version : "latest");
        
        // 检查是否已处理过（避免循环依赖）
        if (processedPlugins.contains(key)) {
            log.debug("插件已处理，跳过: {}", key);
            return;
        }
        
        // 标记为正在处理
        processedPlugins.add(key);
        
        // 初始化依赖图的节点
        if (!dependencyGraph.containsKey(key)) {
            dependencyGraph.put(key, new HashSet<>());
        }
        
        try {
            // 从数据库获取当前插件的依赖信息
            List<DependencyInfo> directDependencies = getDirectDependencies(pluginId, version);
            
            if (directDependencies == null || directDependencies.isEmpty()) {
                log.debug("插件无依赖: pluginId={}, version={}", pluginId, version);
                return;
            }
            
            log.debug("获取到直接依赖: pluginId={}, version={}, count={}", 
                    pluginId, version, directDependencies.size());
            
            // 将直接依赖添加到总依赖集合中（去重），并构建依赖图
            for (DependencyInfo dep : directDependencies) {
                String depKey = dep.getPluginId() + "@" + (dep.getVersion() != null ? dep.getVersion() : "latest");
                
                // 添加到依赖集合
                if (!allDependencies.containsKey(depKey)) {
                    allDependencies.put(depKey, dep);
                    log.debug("添加依赖: {}", depKey);
                }
                
                // 构建依赖图：depKey 被 key 依赖
                // 即 depKey -> key（depKey 是 key 的依赖）
                if (!dependencyGraph.containsKey(depKey)) {
                    dependencyGraph.put(depKey, new HashSet<>());
                }
                dependencyGraph.get(depKey).add(key);
            }
            
            // 递归处理每个直接依赖
            for (DependencyInfo dep : directDependencies) {
                collectDependenciesRecursive(
                        dep.getPluginId(),
                        dep.getVersion(),
                        allDependencies,
                        processedPlugins,
                        dependencyGraph
                );
            }
            
        } catch (Exception e) {
            log.warn("收集依赖失败: pluginId={}, version={}, error={}", 
                    pluginId, version, e.getMessage());
        }
    }
    
    /**
     * 计算依赖的安装顺序（拓扑排序）
     * 
     * 返回的顺序是：最底层依赖（没有依赖的） -> 被依赖的 -> 直接依赖
     * 例如：a依赖b，b依赖c，c依赖d
     * 返回顺序：d、c、b（d是最底层，b是直接依赖）
     * 
     * 依赖关系：a -> b -> c -> d
     * dependencyGraph: b -> {a}, c -> {b}, d -> {c}
     * 我们需要构建反向图：b 依赖 c，c 依赖 d
     * 
     * @param allDependencies 所有依赖的Map
     * @param dependencyGraph 依赖图（key -> 依赖它的插件集合）
     * @return 安装顺序列表（从最底层依赖到直接依赖）
     */
    private List<String> calculateInstallOrder(
            Map<String, DependencyInfo> allDependencies,
            Map<String, Set<String>> dependencyGraph) {
        
        // 构建反向依赖图：key -> key依赖的插件集合
        // 例如：a依赖b，b依赖c，则 reverseGraph: b -> {c}, a -> {b}
        Map<String, Set<String>> reverseGraph = new HashMap<>();
        
        // 初始化所有节点
        for (String key : allDependencies.keySet()) {
            reverseGraph.put(key, new HashSet<>());
        }
        
        // 构建反向图：如果 key 被 dependent 依赖，且 dependent 也在 allDependencies 中
        // 则 reverseGraph[dependent].add(key)，表示 dependent 依赖 key
        for (Map.Entry<String, Set<String>> entry : dependencyGraph.entrySet()) {
            String key = entry.getKey(); // 被依赖的插件
            Set<String> dependents = entry.getValue(); // 依赖 key 的插件集合
            
            for (String dependent : dependents) {
                if (allDependencies.containsKey(dependent)) {
                    // dependent 依赖 key
                    if (!reverseGraph.containsKey(dependent)) {
                        reverseGraph.put(dependent, new HashSet<>());
                    }
                    reverseGraph.get(dependent).add(key);
                }
            }
        }
        
        // 还需要从 allDependencies 中获取每个依赖的直接依赖关系
        // 遍历所有依赖，获取它们的直接依赖
        for (String key : allDependencies.keySet()) {
            String[] parts = key.split("@");
            String depPluginId = parts[0];
            String depVersion = parts.length > 1 && !"latest".equals(parts[1]) ? parts[1] : null;
            
            try {
                List<DependencyInfo> directDeps = getDirectDependencies(depPluginId, depVersion);
                if (directDeps != null && !directDeps.isEmpty()) {
                    if (!reverseGraph.containsKey(key)) {
                        reverseGraph.put(key, new HashSet<>());
                    }
                    for (DependencyInfo directDep : directDeps) {
                        String directDepKey = directDep.getPluginId() + "@" + 
                                (directDep.getVersion() != null ? directDep.getVersion() : "latest");
                        if (allDependencies.containsKey(directDepKey)) {
                            reverseGraph.get(key).add(directDepKey);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("获取依赖关系失败: key={}", key, e);
            }
        }
        
        // 计算每个节点的入度（有多少个依赖）
        Map<String, Integer> inDegree = new HashMap<>();
        
        // 初始化所有节点的入度为0
        for (String key : allDependencies.keySet()) {
            inDegree.put(key, 0);
        }
        
        // 计算入度：如果 key 依赖 otherKey，则 key 的入度+1
        for (Map.Entry<String, Set<String>> entry : reverseGraph.entrySet()) {
            String key = entry.getKey();
            Set<String> dependencies = entry.getValue();
            inDegree.put(key, dependencies.size());
        }
        
        // 拓扑排序：从入度为0的节点开始（最底层依赖，没有依赖其他插件）
        List<String> installOrder = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        
        // 找到所有入度为0的节点（没有依赖的节点，最底层）
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // 执行拓扑排序
        while (!queue.isEmpty()) {
            String current = queue.poll();
            installOrder.add(current);
            
            // 减少依赖当前节点的其他节点的入度
            // 如果 otherKey 依赖 current，则 otherKey 的入度-1
            for (Map.Entry<String, Set<String>> entry : reverseGraph.entrySet()) {
                String otherKey = entry.getKey();
                Set<String> dependencies = entry.getValue();
                if (dependencies.contains(current)) {
                    int newInDegree = inDegree.get(otherKey) - 1;
                    inDegree.put(otherKey, newInDegree);
                    if (newInDegree == 0) {
                        queue.offer(otherKey);
                    }
                }
            }
        }
        
        // 如果还有节点未处理，说明存在循环依赖（但我们已经用 processedPlugins 避免了）
        if (installOrder.size() != allDependencies.size()) {
            log.warn("拓扑排序未处理所有节点: 已处理={}, 总数={}", 
                    installOrder.size(), allDependencies.size());
            // 将未处理的节点添加到末尾
            for (String key : allDependencies.keySet()) {
                if (!installOrder.contains(key)) {
                    installOrder.add(key);
                }
            }
        }
        
        log.debug("拓扑排序完成: 顺序={}", installOrder);
        return installOrder;
    }
    
    /**
     * 从数据库获取插件的直接依赖信息
     * 
     * @param pluginId 插件ID
     * @param version 版本号（可选，如果为null则获取当前版本）
     * @return 依赖列表
     */
    private List<DependencyInfo> getDirectDependencies(String pluginId, String version) {
        try {
            String sql;
            List<Map<String, Object>> rows;
            
            if (version != null && !version.trim().isEmpty()) {
                // 获取指定版本的依赖
                sql = """
                        SELECT v.dependencies
                        FROM appstore_version v
                        WHERE v.plugin_id = #{pluginId} AND v.version = #{version}
                        """;
                rows = dataSource.dynamicSql(sql)
                        .param("pluginId", pluginId)
                        .param("version", version)
                        .query();
            } else {
                // 获取当前版本的依赖
                sql = """
                        SELECT v.dependencies
                        FROM appstore_manager m
                        LEFT JOIN appstore_version v ON m.plugin_id = v.plugin_id AND v.is_current = 1
                        WHERE m.plugin_id = #{pluginId}
                        """;
                rows = dataSource.dynamicSql(sql)
                        .param("pluginId", pluginId)
                        .query();
            }
            
            if (rows == null || rows.isEmpty()) {
                log.debug("未找到插件版本信息: pluginId={}, version={}", pluginId, version);
                return Collections.emptyList();
            }
            
            String dependenciesJson = (String) rows.get(0).get("dependencies");
            if (dependenciesJson == null || dependenciesJson.trim().isEmpty()) {
                return Collections.emptyList();
            }
            
            // 解析JSON字符串
            ObjectMapper mapper = new ObjectMapper();
            List<DependencyInfo> dependencies = mapper.readValue(
                    dependenciesJson,
                    new TypeReference<List<DependencyInfo>>() {}
            );
            
            return dependencies != null ? dependencies : Collections.emptyList();
            
        } catch (Exception e) {
            log.error("获取直接依赖失败: pluginId={}, version={}", pluginId, version, e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 统计依赖数量（用于日志）
     */
    private int countDependencies(String dependenciesJson) {
        if (dependenciesJson == null || dependenciesJson.trim().isEmpty()) {
            return 0;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<?> list = mapper.readValue(dependenciesJson, List.class);
            return list != null ? list.size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 依赖信息内部类
     */
    private static class DependencyInfo {
        private String pluginId;
        private String version;
        private Boolean optional;
        private String versionRange;
        
        public String getPluginId() {
            return pluginId;
        }
        
        public void setPluginId(String pluginId) {
            this.pluginId = pluginId;
        }
        
        public String getVersion() {
            return version;
        }
        
        public void setVersion(String version) {
            this.version = version;
        }
        
        public Boolean getOptional() {
            return optional;
        }
        
        public void setOptional(Boolean optional) {
            this.optional = optional;
        }
        
        public String getVersionRange() {
            return versionRange;
        }
        
        public void setVersionRange(String versionRange) {
            this.versionRange = versionRange;
        }
    }
    
    /**
     * 下载应用包
     * 使用 MyBatis 动态 SQL 进行多表联查
     * 
     * @param pluginId 插件ID
     * @return 文件资源
     */
    public Result<org.springframework.core.io.Resource> downloadPackage(String pluginId) {
        log.info("下载应用包: pluginId={}", pluginId);
        
        try {
            // 使用 MyBatis 动态 SQL 进行多表联查
            String sql = """
                    SELECT v.file_path
                    FROM appstore_manager m
                    JOIN appstore_version v ON m.plugin_id = v.plugin_id AND v.is_current = 1
                    WHERE m.plugin_id = #{pluginId}
                      AND m.status = 'ONLINE'
                      AND v.status = 'ONLINE'
                      AND v.file_hash IS NOT NULL
                      AND v.file_hash <> ''
                    """;
            
            List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
                    .param("pluginId", pluginId)
                    .query();
            
            if (rows == null || rows.isEmpty()) {
                return Result.error("应用不存在或已下架");
            }
            
            String filePath = (String) rows.get(0).get("file_path");
            if (filePath == null || filePath.trim().isEmpty()) {
                return Result.error("应用文件不存在");
            }
            
            // 2. 使用 FileStorageService 下载文件到字节数组
            final byte[][] fileBytes = new byte[1][];
            
            fileStorageService
                .download(filePath)
                .toBytes(bytes -> fileBytes[0] = bytes)
                .onError(e -> log.error("应用包下载失败: pluginId={}, filePath={}", pluginId, filePath, e))
                .executeVoid();
            
            if (fileBytes[0] == null) {
                return Result.error("文件下载失败");
            }
            
            // 3. 将字节数组转换为 Resource
            org.springframework.core.io.Resource resource = 
                new org.springframework.core.io.ByteArrayResource(fileBytes[0]);
            
            log.info("应用包下载成功: pluginId={}, filePath={}, size={}", 
                    pluginId, filePath, fileBytes[0].length);
            return Result.success(resource);
            
        } catch (Exception e) {
            log.error("下载应用包失败: pluginId={}", pluginId, e);
            return Result.error("下载应用包失败: " + e.getMessage());
        }
    }

    /**
     * 根据插件ID和版本获取版本详情（用户端）
     * 递归解析所有间接依赖，将依赖链中的所有依赖都包含在返回结果中
     *
     * @param pluginId 插件ID
     * @param version  版本号
     * @return 版本详情（包含所有直接和间接依赖）
     */
    public Result<PluginPackageDTO> getPackageVersionDetail(String pluginId, String version) {
        log.info("获取应用版本详情: pluginId={}, version={}", pluginId, version);

        try {
            String sql = """
                    SELECT v.*
                    FROM appstore_version v
                    WHERE v.plugin_id = #{pluginId}
                      AND v.version = #{version}
                    """;

            List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
                    .param("pluginId", pluginId)
                    .param("version", version)
                    .query();

            if (rows == null || rows.isEmpty()) {
                return Result.error("指定版本不存在");
            }

            PluginPackageDTO dto = mapVersionToPluginPackageDTO(rows.get(0));
            
            // 递归解析所有间接依赖
            String allDependenciesJson = resolveAllDependencies(pluginId, version);
            if (allDependenciesJson != null) {
                dto.setDependencies(allDependenciesJson);
                log.info("递归解析依赖完成: pluginId={}, version={}, 依赖数量={}", 
                        pluginId, version, countDependencies(allDependenciesJson));
            }
            
            return Result.success(dto);

        } catch (Exception e) {
            log.error("获取应用版本详情失败: pluginId={}, version={}", pluginId, version, e);
            return Result.error("获取应用版本详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据插件ID和版本下载应用包
     *
     * @param pluginId 插件ID
     * @param version  版本号
     * @return 文件资源
     */
    public Result<org.springframework.core.io.Resource> downloadPackageByVersion(String pluginId, String version) {
        log.info("按版本下载应用包: pluginId={}, version={}", pluginId, version);

        try {
            String sql = """
                    SELECT v.file_path
                    FROM appstore_version v
                    WHERE v.plugin_id = #{pluginId}
                      AND v.version = #{version}
                      AND v.status = 'ONLINE'
                      AND v.file_hash IS NOT NULL
                      AND v.file_hash <> ''
                    """;

            List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
                    .param("pluginId", pluginId)
                    .param("version", version)
                    .query();

            if (rows == null || rows.isEmpty()) {
                return Result.error("指定版本的应用不存在");
            }

            String filePath = (String) rows.get(0).get("file_path");
            if (filePath == null || filePath.trim().isEmpty()) {
                return Result.error("指定版本的应用文件不存在");
            }

            final byte[][] fileBytes = new byte[1][];

            fileStorageService
                .download(filePath)
                .toBytes(bytes -> fileBytes[0] = bytes)
                .onError(e -> log.error("按版本下载应用包失败: pluginId={}, version={}, filePath={}", pluginId, version, filePath, e))
                .executeVoid();

            if (fileBytes[0] == null) {
                return Result.error("文件下载失败");
            }

            org.springframework.core.io.Resource resource =
                new org.springframework.core.io.ByteArrayResource(fileBytes[0]);

            log.info("按版本下载应用包成功: pluginId={}, version={}, filePath={}, size={}",
                    pluginId, version, filePath, fileBytes[0].length);
            return Result.success(resource);

        } catch (Exception e) {
            log.error("按版本下载应用包失败: pluginId={}, version={}", pluginId, version, e);
            return Result.error("下载应用包失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据插件ID和版本下载应用包（返回字节数组）
     *
     * @param pluginId 插件ID
     * @param version  版本号
     * @return 文件字节数组
     */
    public Result<byte[]> downloadPackageBytesByVersion(String pluginId, String version) {
        log.info("按版本下载应用包(字节数组): pluginId={}, version={}", pluginId, version);

        try {
            String sql = """
                    SELECT v.file_path
                    FROM appstore_version v
                    WHERE v.plugin_id = #{pluginId}
                      AND v.version = #{version}
                      AND v.status = 'ONLINE'
                      AND v.file_hash IS NOT NULL
                      AND v.file_hash <> ''
                    """;

            List<Map<String, Object>> rows = dataSource.dynamicSql(sql)
                    .param("pluginId", pluginId)
                    .param("version", version)
                    .query();

            if (rows == null || rows.isEmpty()) {
                return Result.error("指定版本的应用不存在");
            }

            String filePath = (String) rows.get(0).get("file_path");
            if (filePath == null || filePath.trim().isEmpty()) {
                return Result.error("指定版本的应用文件不存在");
            }

            final byte[][] fileBytes = new byte[1][];

            fileStorageService
                .download(filePath)
                .toBytes(bytes -> fileBytes[0] = bytes)
                .onError(e -> log.error("按版本下载应用包失败: pluginId={}, version={}, filePath={}", pluginId, version, filePath, e))
                .executeVoid();

            if (fileBytes[0] == null) {
                return Result.error("文件下载失败");
            }

            log.info("按版本下载应用包成功: pluginId={}, version={}, filePath={}, size={}",
                    pluginId, version, filePath, fileBytes[0].length);
            return Result.success(fileBytes[0]);

        } catch (Exception e) {
            log.error("按版本下载应用包失败: pluginId={}, version={}", pluginId, version, e);
            return Result.error("下载应用包失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取应用包文件名
     * 使用 Lambda Query Chain（单表查询）
     * 
     * @param pluginId 插件ID
     * @return 文件名
     */
    public String getPackageFileName(String pluginId) {
        try {
            // 使用 Lambda Query Chain 进行单表查询
            PluginManager manager = dataSource.lambdaQuery(PluginManager.class)
                    .eq(PluginManager::getPluginId, pluginId)
                    .one();
            
            if (manager != null && manager.getCurrentVersion() != null) {
                return pluginId + "-" + manager.getCurrentVersion() + ".jar";
            }
            
            return pluginId + ".jar";
            
        } catch (Exception e) {
            log.warn("获取文件名失败: pluginId={}", pluginId, e);
            return pluginId + ".jar";
        }
    }
    
    /**
     * 增加下载计数
     * 使用 Lambda Update Chain（单表更新）
     * 
     * @param pluginId 插件ID
     */
    public void incrementDownloadCount(String pluginId) {
        try {
            // 使用 setSql 方法进行原子性自增操作
            int rows = dataSource.lambdaUpdate(PluginManager.class)
                    .setSql("install_count = install_count + 1")
                    .eq(PluginManager::getPluginId, pluginId)
                    .update();
            
            if (rows > 0) {
                log.info("下载计数更新成功: pluginId={}", pluginId);
            }
            
        } catch (Exception e) {
            log.error("更新下载计数失败: pluginId={}", pluginId, e);
            // 不抛出异常，下载计数失败不应该影响下载
        }
    }
    
    /**
     * 映射 appstore_manager + appstore_version 联表查询结果到DTO（用户端）
     */
    private PluginPackageDTO mapManagerToPluginPackageDTO(Map<String, Object> row) {
        PluginPackageDTO dto = new PluginPackageDTO();
        dto.setId(((Number) row.get("id")).longValue());
        dto.setPluginId((String) row.get("plugin_id"));
        dto.setPluginName((String) row.get("plugin_name"));
        dto.setPluginType((String) row.get("plugin_type"));
        dto.setVersion((String) row.get("current_version"));
        dto.setFileUrl((String) row.get("file_path"));
        dto.setFileName(extractFileName((String) row.get("file_path")));
        dto.setFileSize(row.get("file_size") != null ? ((Number) row.get("file_size")).longValue() : null);
        dto.setDescription((String) row.get("description"));
        dto.setIcon((String) row.get("icon"));
        dto.setCategory((String) row.get("category"));
        dto.setDeveloperId((String) row.get("developer_id"));
        dto.setDeveloperName((String) row.get("developer_name"));
        dto.setStatus((String) row.get("status"));
        dto.setDownloadCount(row.get("install_count") != null ? ((Number) row.get("install_count")).intValue() : 0);
        dto.setRatingAverage(row.get("rating_average") != null ? ((Number) row.get("rating_average")).doubleValue() : null);
        dto.setReleaseNotes((String) row.get("release_notes"));
        dto.setDependencies((String) row.get("dependencies")); // 依赖信息
        dto.setUploadTime(TypeConverter.toLocalDateTime(row.get("update_time")));
        return dto;
    }
    
    /**
     * 映射 appstore_version 表数据到DTO（管理端版本列表）
     */
    private PluginPackageDTO mapVersionToPluginPackageDTO(Map<String, Object> row) {
        PluginPackageDTO dto = new PluginPackageDTO();
        dto.setId(((Number) row.get("id")).longValue());
        dto.setPluginId((String) row.get("plugin_id"));
        dto.setVersion((String) row.get("version"));
        dto.setFileUrl((String) row.get("file_path"));
        dto.setFileName(extractFileName((String) row.get("file_path")));
        dto.setFileSize(row.get("file_size") != null ? ((Number) row.get("file_size")).longValue() : null);
        dto.setDescription((String) row.get("description"));
        dto.setReleaseNotes((String) row.get("release_notes"));
        dto.setUploadBy((String) row.get("reviewer_name"));
        dto.setUploadTime(TypeConverter.toLocalDateTime(row.get("create_time")));
        dto.setStatus((String) row.get("status"));
        dto.setSha256((String) row.get("file_hash"));
        dto.setDependencies((String) row.get("dependencies")); // 依赖信息
        return dto;
    }
    
    /**
     * 从文件URL提取文件名
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        int lastSlash = fileUrl.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlash + 1);
        }
        return fileUrl;
    }
}
