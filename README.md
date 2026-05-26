# Gress Plugin Store Admin

插件商店管理系统 - 为管理员提供完整的插件生命周期管理能力

## 功能特性

### 核心功能
- ✅ 插件提交审核管理
- ✅ 插件上架/下架管理
- ✅ 插件版本管理
- ✅ 统计分析与报表
- ✅ 开发者账户管理
- ✅ 审计日志追踪
- ✅ 审核规则引擎
- ✅ 用户反馈处理
- ✅ 安全扫描集成
- ✅ 批量操作支持

### 技术架构
- **插件类型**: Application Plugin
- **后端框架**: Spring Boot + PF4J
- **前端框架**: Vue 3 (UMD)
- **数据库**: MySQL 5.7+ (Flyway 管理)
- **UI 组件**: Naive UI

## 项目结构

```
gress-plugin-appstore-admin/
├── src/main/
│   ├── java/com/gress/plugin/appstore/admin/
│   │   ├── AppStoreAdminPlugin.java          # 插件主类
│   │   ├── controller/                        # REST API Controllers
│   │   ├── service/                           # 业务逻辑层
│   │   ├── entity/                            # 实体类
│   │   └── dto/                               # 数据传输对象
│   └── resources/
│       ├── db/migration/                      # Flyway 数据库迁移脚本
│       ├── icons/                             # 插件图标
│       ├── js/                                # 前端构建产物
│       └── plugin.yml                         # 单一清单（元数据 + ui + plugin.config）；plugin.properties 构建期生成
├── frontend/                                  # 前端源码
│   ├── src/
│   │   ├── index.ts                          # 前端入口
│   │   ├── components/                        # Vue 组件
│   │   └── types/                            # TypeScript 类型定义
│   ├── package.json
│   └── vite.config.ts
├── pom.xml
└── README.md
```

## 构建说明

### 前置要求
- JDK 11+
- Maven 3.6+
- Node.js 18+ (Maven 会自动下载)

### 构建步骤

```bash
# 在 gress-plugins 目录下执行
mvn clean package -pl gress-plugin-appstore-admin -am

# 构建产物位于
# target/gress-plugin-appstore-admin-1.0-SNAPSHOT.jar
```

### 开发模式

```bash
# 后端开发：在 IDEA 中直接运行 Gress 主应用

# 前端开发：
cd frontend
npm install
npm run dev
```

## API 文档

### 插件提交管理
- `GET /api/v2/admin/plugins/submissions` - 获取插件提交列表
- `GET /api/v2/admin/plugins/submissions/{id}` - 获取提交详情
- `POST /api/v2/admin/plugins/submissions/{id}/approve` - 批准插件
- `POST /api/v2/admin/plugins/submissions/{id}/reject` - 拒绝插件

### 插件管理
- `GET /api/v2/admin/plugins` - 获取已上架插件列表
- `POST /api/v2/admin/plugins/{id}/delist` - 下架插件
- `POST /api/v2/admin/plugins/{id}/relist` - 重新上架

### 版本管理
- `GET /api/v2/admin/plugins/{pluginId}/versions` - 获取版本列表
- `POST /api/v2/admin/plugins/{pluginId}/versions/{version}/set-current` - 设置当前版本
- `POST /api/v2/admin/plugins/{pluginId}/versions/{version}/rollback` - 版本回滚

### 统计分析
- `GET /api/v2/admin/plugins/{pluginId}/statistics` - 获取插件统计
- `GET /api/v2/admin/plugins/statistics/overview` - 获取整体统计

### 开发者管理
- `GET /api/v2/admin/developers` - 获取开发者列表
- `POST /api/v2/admin/developers/{id}/approve` - 批准开发者资格
- `POST /api/v2/admin/developers/{id}/suspend` - 暂停开发者账户

### 审计日志
- `GET /api/v2/admin/audit-logs` - 获取审计日志列表
- `GET /api/v2/admin/audit-logs/export` - 导出审计日志

## 数据库表

插件使用独立的数据库 Schema，通过 Flyway 管理：

- `appstore_plugin_submission` - 插件提交表
- `appstore_plugin_review_history` - 审核历史表
- `appstore_plugin_statistics` - 插件统计表
- `appstore_developer` - 开发者表
- `appstore_review_rule` - 审核规则表
- `appstore_feedback` - 用户反馈表
- `appstore_audit_log` - 审计日志表
- `appstore_display_config` - 展示配置表

## 开发指南

### 添加新的 Controller

```java
@Service
@RestController
@RequestMapping("/api/v2/admin/your-endpoint")
public class YourController {
    
    @Inject(source = Inject.BeanSource.PLUGIN)
    private YourService yourService;
    
    @GetMapping
    public Result<List<YourDTO>> list() {
        // 实现
    }
}
```

### 使用 PluginDataSource

```java
@Service
public class YourService {
    
    @Inject(source = Inject.BeanSource.SPRING)
    private PluginDataSource pluginDataSource;
    
    public List<YourEntity> query() {
        String sql = "SELECT * FROM your_table WHERE condition = ?";
        List<Map<String, Object>> rows = pluginDataSource.queryForMapList(sql, param);
        return rows.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
```

### 添加数据库迁移

在 `src/main/resources/db/migration/` 目录下创建新的 SQL 文件：

```
V1__init_tables.sql
V2__add_new_column.sql
V3__create_index.sql
```

## 测试

### 单元测试
```bash
mvn test
```

### 属性测试
使用 jqwik 框架进行属性测试，每个测试运行 100+ 次迭代。

## 许可证

MIT License

## 作者

Gress Team
