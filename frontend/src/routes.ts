/**
 * 插件路由配置
 * 
 * 注意：组件直接引入，不使用动态 import
 */

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
import PluginTablePermissionManagement from './views/PluginTablePermissionManagement.vue'
import PermissionRequestReview from './views/PermissionRequestReview.vue'
import IconTest from './components/IconTest.vue'

export const routes = [
  {
    path: '/plugins/plugin-admin',
    name: 'PluginAdmin',
    component: PluginAdminLayout,
    meta: {
      title: '插件管理',
      icon: 'apps-outline',
      requiresAuth: true,
      permissions: ['admin']
    },
    children: [
      {
        path: 'submissions',
        name: 'PluginSubmissions',
        component: PluginSubmissions,
        meta: {
          title: '插件审核',
          icon: 'checkmark-circle-outline'
        }
      },
      {
        path: 'plugins',
        name: 'PluginManagement',
        component: PluginManagement,
        meta: {
          title: '插件列表',
          icon: 'list-outline'
        }
      },
      {
        path: 'statistics',
        name: 'StatisticsAnalysis',
        component: StatisticsAnalysis,
        meta: {
          title: '统计分析',
          icon: 'bar-chart-outline'
        }
      },
      {
        path: 'categories',
        name: 'CategoryManagement',
        component: CategoryManagement,
        meta: {
          title: '分类管理',
          icon: 'folder-outline'
        }
      },
      {
        path: 'tags',
        name: 'TagManagement',
        component: TagManagement,
        meta: {
          title: '标签管理',
          icon: 'pricetags-outline'
        }
      },
      {
        path: 'developers',
        name: 'DeveloperManagement',
        component: DeveloperManagement,
        meta: {
          title: '开发者管理',
          icon: 'people-outline'
        }
      },
      {
        path: 'audit-logs',
        name: 'AuditLog',
        component: AuditLog,
        meta: {
          title: '审计日志',
          icon: 'document-text-outline'
        }
      },
      {
        path: 'review-rules',
        name: 'ReviewRules',
        component: ReviewRules,
        meta: {
          title: '审核规则',
          icon: 'shield-checkmark-outline'
        }
      },
      {
        path: 'feedbacks',
        name: 'FeedbackManagement',
        component: FeedbackManagement,
        meta: {
          title: '用户反馈',
          icon: 'chatbubbles-outline'
        }
      },
      {
        path: 'table-permissions',
        name: 'PluginTablePermissionManagement',
        component: PluginTablePermissionManagement,
        meta: {
          title: '表权限管理',
          icon: 'shield-outline'
        }
      },
      {
        path: 'permission-requests',
        name: 'PermissionRequestReview',
        component: PermissionRequestReview,
        meta: {
          title: '权限申请审核',
          icon: 'checkmark-circle-outline'
        }
      },
      {
        path: 'icon-test',
        name: 'IconTest',
        component: IconTest,
        meta: {
          title: 'SVG 图标测试',
          icon: 'color-palette-outline'
        }
      }
    ]
  }
]
