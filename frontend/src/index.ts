/**
 * App Store Admin 插件
 *
 * 使用工厂函数返回 PluginManifest
 */

// 导入共享包样式
import '@keqi.gress/plugin-ui/style.css'

import PluginAdminLayout from './views/PluginAdminLayout.vue'
import PluginSubmissions from './views/PluginSubmissions.vue'
import PluginManagement from './views/PluginManagement.vue'
import StatisticsAnalysis from './views/StatisticsAnalysis.vue'
import CategoryManagement from './views/CategoryManagement.vue'
import TagManagement from './views/TagManagement.vue'
import DeveloperManagement from './views/DeveloperManagement.vue'
import AuditLog from './views/AuditLog.vue'
import ReviewRules from './views/ReviewRules.vue'
import FeedbackManagement from './views/FeedbackManagement.vue'
import PermissionRequestReview from './views/PermissionRequestReview.vue'
import PluginTablePermissionManagement from './views/PluginTablePermissionManagement.vue'

export interface AppStoreAdminConfig {
  enabled?: boolean
  debug?: boolean
}

const defaultConfig: Required<AppStoreAdminConfig> = {
  enabled: true,
  debug: false
}

/**
 * 插件工厂函数
 * 
 * 这是插件的唯一导出，宿主通过调用此函数来实例化插件
 * 
 * @param bridge - 宿主机提供的桥接对象，包含所有能力
 *   - bridge.http: HTTP 客户端
 *   - bridge.i18n: 国际化
 *   - bridge.store: 状态管理
 *   - bridge.router: 路由
 *   - bridge.ui: UI 组件
 *   - bridge.events: 事件总线
 *   - bridge.utils: 工具函数
 *   - bridge.auth: 权限
 *   - bridge.notification: 通知
 *   - bridge.app: Vue 应用实例
 * @param properties - 插件初始化配置（可选）
 * @returns 插件清单（manifest）
 */
export default (bridge: any, properties?: AppStoreAdminConfig): any => {
 

  const config: Required<AppStoreAdminConfig> = {
    ...defaultConfig,
    ...(properties || {})
  }

  // 使用 bridge 提供的能力
  const { ui, app } = bridge || {}


  return {
    id: 'appstore-admin',
    name: '插件商店管理',
    version: '1.0.0',
    description: '插件商店管理后台，提供插件审核、上架、下架、版本管理等功能',
    author: {
      name: 'Gress Team'
    },
    icon: 'shield-checkmark-outline',

    permissions: [
      'NETWORK_ACCESS',
      'ROUTER_REGISTER',
      'ROUTER_NAVIGATE',
      'COMPONENT_REGISTER',
      'DATA_READ',
      'DATA_WRITE',
      'STORAGE_READ',
      'STORAGE_WRITE',
      'UI_MENU'
    ],

    loadStrategy: 'lazy',

    /**
     * 组件注册表（名称 -> 组件实例）
     *
     * - 后端 plugin-ui.yml 中只写组件名称（如 PluginAdminLayout / PluginSubmissions）
     * - 宿主通过 PluginRuntime 获取 manifest.components 后按名称查找组件：
     *   const runtime = getPluginRuntime()
     *   const plugin = runtime.get('appstore-admin')
     *   const comp = plugin?.manifest.components?.['PluginAdminLayout']
     */
    components: {
      PluginAdminLayout,
      PluginSubmissions,
      PluginManagement,
      StatisticsAnalysis,
      CategoryManagement,
      TagManagement,
      DeveloperManagement,
      AuditLog,
      ReviewRules,
      FeedbackManagement,
      PermissionRequestReview,
      PluginTablePermissionManagement
    },

    extensions: {
      // 路由和菜单交由后端 plugin-ui.yml 管理，避免前后端信息重复维护
      routes: [],
      components: [],
      menus: []
    },

    lifecycle: {
      async install(context: any) {
        const { logger } = context

        logger.info('Installing AppStoreAdmin plugin')

        // 使用 bridge.ui 注册 NaiveUI 组件，而不是从 window 获取
        const naiveComponents = [
          'NButton',
          'NCard',
          'NInput',
          'NPagination',
          'NSpin',
          'NTag',
          'NModal',
          'NSelect',
          'NTable',
          'NSpace',
          'NAlert',
          'NForm',
          'NFormItem',
          'NInputNumber',
          'NDataTable',
          'NDrawer',
          'NDrawerContent',
          'NEmpty',
          'NTabPane',
          'NTabs',
          'NDescriptions',
          'NDescriptionsItem',
          'NDivider',
          'NList',
          'NListItem',
          'NThing',
          'NPopconfirm',
          'NIcon'
        ]

        // 使用 bridge 提供的能力
        if (ui && ui.components && app) {
          naiveComponents.forEach(name => {
            const component = ui.components[name]
            if (component) {
              app.component(name, component)
            }
          })
          logger.debug('NaiveUI components registered via bridge')
        } else {
          // 向后兼容：如果没有 bridge，尝试从 window 获取
          const naiveUI = (window as any).NaiveUI
          if (naiveUI) {
            const vueApp = (window as any).VueApp
            if (vueApp) {
              naiveComponents.forEach(name => {
                const component = naiveUI[name]
                if (component) {
                  vueApp.component(name, component)
                }
              })
              logger.debug('NaiveUI components registered via window (fallback)')
            }
          }
        }

        if (config.debug) {
          logger.debug('AppStoreAdmin plugin installed with config:', config)
        }
      },

      async activate(context: any) {
        const { logger } = context
        logger.info('Activating AppStoreAdmin plugin')
      },

      async deactivate(context: any) {
        const { logger } = context
        logger.info('Deactivating AppStoreAdmin plugin')
      }
    },

    config: {
      default: config
    },

    extra: {
      category: 'application',
      tags: ['admin', 'management', 'plugin-store']
    }
  }
}

/**
 * 插件不再自动注册到 window.GressPlugins
 * 
 * UMD 构建后，IIFE 会自动将 default export 赋值给 window.__GRESS_PLUGIN__
 * 宿主机加载后会立即获取并删除，避免污染全局命名空间
 * 
 * 注意：UMD 包装器会自动处理导出，这里不需要手动注册
 */
