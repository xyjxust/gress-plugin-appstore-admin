/**
 * API接口定义
 */

import { http } from '@keqi.gress/shared-utils'
import type { 
  PluginSubmission,
  Plugin,
  PluginVersion,
  Category, 
  Tag, 
  PageResult, 
  ApiResponse,
  QueryParams,
  PluginStatistics,
  StatisticsOverview,
  TrendData,
  StatisticsQueryRequest,
  ExportRequest,
  Developer,
  DeveloperDetail,
  AuditLog,
  AuditLogExportRequest,
  ReviewRule,
  ReviewRuleRequest,
  Feedback,
  FeedbackProcessRequest,
  SigningKeyDTO,
  GenerateSigningKeyRequest,
  ActivateSigningKeyRequest
} from '../types'

export { tablePermissionApi } from './tablePermission'
export type { PluginTablePermission } from './tablePermission'
export { permissionRequestApi } from './permissionRequest'
export type { 
  PluginTablePermissionRequest, 
  ApprovePermissionRequest, 
  RejectPermissionRequest 
} from './permissionRequest'

const API_BASE = '/plugins/appstore-admin'

/**
 * 插件提交API
 */
export const submissionApi = {
  /**
   * 获取插件提交列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<PluginSubmission>>> {
    return http.get(`${API_BASE}/plugins/submissions`, params)
  },

  /**
   * 获取插件提交详情
   */
  getDetail(id: number): Promise<ApiResponse<PluginSubmission>> {
    return http.get(`${API_BASE}/plugins/submissions/${id}`)
  },

  /**
   * 批准插件
   */
  approve(id: number, data: { comment?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/submissions/${id}/approve`, data)
  },

  /**
   * 拒绝插件
   */
  reject(id: number, data: { reason: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/submissions/${id}/reject`, data)
  }
}

/**
 * 版本管理API
 */
export const versionApi = {
  /**
   * 获取插件版本列表
   */
  getList(pluginId: string): Promise<ApiResponse<PluginVersion[]>> {
    return http.get(`${API_BASE}/plugins/${pluginId}/versions`)
  },

  /**
   * 获取版本详情
   */
  getDetail(pluginId: string, version: string): Promise<ApiResponse<PluginVersion>> {
    return http.get(`${API_BASE}/plugins/${pluginId}/versions/${version}`)
  },

  /**
   * 设置当前版本
   */
  setCurrent(pluginId: string, version: string): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/${pluginId}/versions/${version}/set-current`)
  },

  /**
   * 回滚到指定版本
   */
  rollback(pluginId: string, version: string): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/${pluginId}/versions/${version}/rollback`)
  },

  /**
   * 删除版本
   */
  delete(pluginId: string, version: string): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/plugins/${pluginId}/versions/${version}`)
  }
}

/**
 * 分类API
 */
export const categoryApi = {
  /**
   * 获取所有分类
   */
  getAll(): Promise<ApiResponse<Category[]>> {
    return http.get(`${API_BASE}/categories`)
  },

  /**
   * 获取分类详情
   */
  getById(id: number): Promise<ApiResponse<Category>> {
    return http.get(`${API_BASE}/categories/${id}`)
  },

  /**
   * 创建分类
   */
  create(data: Partial<Category>): Promise<ApiResponse<Category>> {
    return http.post(`${API_BASE}/categories`, data)
  },

  /**
   * 更新分类
   */
  update(id: number, data: Partial<Category>): Promise<ApiResponse<Category>> {
    return http.put(`${API_BASE}/categories/${id}`, data)
  },

  /**
   * 删除分类
   */
  delete(id: number): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/categories/${id}`)
  }
}

/**
 * 插件类型信息
 */
export interface PluginTypeInfo {
  code: string
  label: string
  description?: string
  tagType?: 'info' | 'success' | 'warning' | 'error'
  icon?: string
}

/**
 * 插件管理API
 */
export const pluginApi = {
  /**
   * 获取已上架插件列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<Plugin>>> {
    return http.get(`${API_BASE}/plugins`, params)
  },

  /**
   * 获取插件类型列表
   */
  getTypes(): Promise<ApiResponse<PluginTypeInfo[]>> {
    return http.get(`${API_BASE}/plugins/types`)
  },

  /**
   * 获取插件详情
   */
  getDetail(pluginId: string): Promise<ApiResponse<Plugin>> {
    return http.get(`${API_BASE}/plugins/${pluginId}`)
  },

  /**
   * 上传插件包
   */
  upload(formData: FormData, onProgress?: (progress: number) => void): Promise<ApiResponse<Plugin>> {
    return http.post(`${API_BASE}/plugins/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: (progressEvent: any) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(progress)
        }
      }
    })
  },

  /**
   * 升级插件版本
   */
  upgrade(formData: FormData, onProgress?: (progress: number) => void): Promise<ApiResponse<Plugin>> {
    return http.post(`${API_BASE}/plugins/upgrade`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: (progressEvent: any) => {
        if (onProgress && progressEvent.total) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(progress)
        }
      }
    })
  },

  /**
   * 下架插件
   */
  delist(pluginId: string, data: { reason: string; operatorId?: string; operatorName?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/${pluginId}/delist`, {
      reason: data.reason,
      operatorId: data.operatorId || 'admin',
      operatorName: data.operatorName || '管理员'
    })
  },

  /**
   * 重新上架插件
   */
  relist(pluginId: string, data?: { operatorId?: string; operatorName?: string; comment?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/plugins/${pluginId}/relist`, data || {
      operatorId: 'admin',
      operatorName: '管理员'
    })
  },

  /**
   * 更新插件信息
   */
  update(pluginId: string, data: Partial<Plugin>): Promise<ApiResponse<Plugin>> {
    return http.put(`${API_BASE}/plugins/${pluginId}`, data)
  },

  /**
   * 删除插件
   */
  delete(pluginId: string): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/plugins/${pluginId}`)
  }
}

/**
 * 标签API
 */
export const tagApi = {
  /**
   * 获取所有标签
   */
  getAll(): Promise<ApiResponse<Tag[]>> {
    return http.get(`${API_BASE}/tags`)
  },

  /**
   * 获取标签详情
   */
  getById(id: number): Promise<ApiResponse<Tag>> {
    return http.get(`${API_BASE}/tags/${id}`)
  },

  /**
   * 创建标签
   */
  create(data: Partial<Tag>): Promise<ApiResponse<Tag>> {
    return http.post(`${API_BASE}/tags`, data)
  },

  /**
   * 更新标签
   */
  update(id: number, data: Partial<Tag>): Promise<ApiResponse<Tag>> {
    return http.put(`${API_BASE}/tags/${id}`, data)
  },

  /**
   * 删除标签
   */
  delete(id: number): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/tags/${id}`)
  },

  /**
   * 获取插件的标签
   */
  getPluginTags(pluginId: string): Promise<ApiResponse<Tag[]>> {
    return http.get(`${API_BASE}/tags/plugins/${pluginId}`)
  },

  /**
   * 为插件分配标签
   */
  assignToPlugin(pluginId: string, tagId: number): Promise<ApiResponse> {
    return http.post(`${API_BASE}/tags/plugins/${pluginId}/tags/${tagId}`)
  },

  /**
   * 从插件移除标签
   */
  removeFromPlugin(pluginId: string, tagId: number): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/tags/plugins/${pluginId}/tags/${tagId}`)
  }
}

/**
 * 统计分析API
 */
export const statisticsApi = {
  /**
   * 获取插件统计数据
   */
  getPluginStatistics(pluginId: string, params?: { startDate?: string; endDate?: string }): Promise<ApiResponse<PluginStatistics>> {
    return http.get(`${API_BASE}/plugins/statistics/${pluginId}`, params)
  },

  /**
   * 获取统计概览
   */
  getOverview(): Promise<ApiResponse<StatisticsOverview>> {
    return http.get(`${API_BASE}/plugins/statistics/overview`)
  },

  /**
   * 获取趋势数据
   */
  getTrendData(data: StatisticsQueryRequest): Promise<ApiResponse<TrendData>> {
    return http.post(`${API_BASE}/plugins/statistics/trending`, data)
  },

  /**
   * 导出统计数据
   */
  exportStatistics(data: ExportRequest): Promise<ApiResponse<string>> {
    return http.post(`${API_BASE}/plugins/statistics/export`, data)
  }
}

/**
 * 开发者管理API
 */
export const developerApi = {
  /**
   * 获取开发者列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<Developer>>> {
    return http.get(`${API_BASE}/developers`, params)
  },

  /**
   * 获取开发者详情
   */
  getDetail(id: number): Promise<ApiResponse<DeveloperDetail>> {
    return http.get(`${API_BASE}/developers/${id}`)
  },

  /**
   * 批准开发者资格
   */
  approve(id: number, data: { reviewerId: string; reviewerName: string; comment?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/developers/${id}/approve`, data)
  },

  /**
   * 暂停开发者账户
   */
  suspend(id: number, data: { operatorId: string; operatorName: string; reason: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/developers/${id}/suspend`, data)
  },

  /**
   * 激活开发者账户
   */
  activate(id: number, data: { operatorId: string; operatorName: string; comment?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/developers/${id}/activate`, data)
  }
}

/**
 * 审计日志API
 */
export const auditLogApi = {
  /**
   * 获取审计日志列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<AuditLog>>> {
    return http.get(`${API_BASE}/audit-logs`, params)
  },

  /**
   * 获取审计日志详情
   */
  getDetail(id: number): Promise<ApiResponse<AuditLog>> {
    return http.get(`${API_BASE}/audit-logs/${id}`)
  },

  /**
   * 导出审计日志
   */
  export(data: AuditLogExportRequest): Promise<ApiResponse<string>> {
    return http.post(`${API_BASE}/audit-logs/export`, data)
  }
}

/**
 * 审核规则API
 */
export const reviewRuleApi = {
  /**
   * 获取审核规则列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<ReviewRule>>> {
    return http.get(`${API_BASE}/review-rules`, params)
  },

  /**
   * 获取审核规则详情
   */
  getDetail(id: number): Promise<ApiResponse<ReviewRule>> {
    return http.get(`${API_BASE}/review-rules/${id}`)
  },

  /**
   * 创建审核规则
   */
  create(data: ReviewRuleRequest): Promise<ApiResponse<ReviewRule>> {
    return http.post(`${API_BASE}/review-rules`, data)
  },

  /**
   * 更新审核规则
   */
  update(id: number, data: ReviewRuleRequest): Promise<ApiResponse<ReviewRule>> {
    return http.put(`${API_BASE}/review-rules/${id}`, data)
  },

  /**
   * 删除审核规则
   */
  delete(id: number): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/review-rules/${id}`)
  },

  /**
   * 启用规则
   */
  enable(id: number): Promise<ApiResponse> {
    return http.post(`${API_BASE}/review-rules/${id}/enable`)
  },

  /**
   * 禁用规则
   */
  disable(id: number): Promise<ApiResponse> {
    return http.post(`${API_BASE}/review-rules/${id}/disable`)
  }
}

/**
 * 用户反馈API
 */
export const feedbackApi = {
  /**
   * 获取反馈列表
   */
  getList(params?: QueryParams): Promise<ApiResponse<PageResult<Feedback>>> {
    return http.get(`${API_BASE}/feedbacks`, params)
  },

  /**
   * 获取反馈详情
   */
  getDetail(id: number): Promise<ApiResponse<Feedback>> {
    return http.get(`${API_BASE}/feedbacks/${id}`)
  },

  /**
   * 处理反馈
   */
  process(id: number, data: FeedbackProcessRequest): Promise<ApiResponse> {
    return http.post(`${API_BASE}/feedbacks/${id}/process`, data)
  },

  /**
   * 关闭反馈
   */
  close(id: number, data: { handlerId: string; handlerName: string; comment?: string }): Promise<ApiResponse> {
    return http.post(`${API_BASE}/feedbacks/${id}/close`, data)
  }
}

/**
 * Signing key management API
 */
export const signingKeyApi = {
  list(): Promise<ApiResponse<SigningKeyDTO[]>> {
    return http.get(`${API_BASE}/signing-keys`)
  },

  getPublicKeyPem(keyId: string): Promise<ApiResponse<string>> {
    return http.get(`${API_BASE}/signing-keys/${keyId}/public-key-pem`)
  },

  generate(data: GenerateSigningKeyRequest): Promise<ApiResponse<SigningKeyDTO>> {
    return http.post(`${API_BASE}/signing-keys/generate`, data)
  },

  activate(keyId: string, data: ActivateSigningKeyRequest): Promise<ApiResponse> {
    return http.post(`${API_BASE}/signing-keys/${keyId}/activate`, data)
  }
}
