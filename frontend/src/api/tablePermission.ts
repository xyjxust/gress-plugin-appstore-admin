/**
 * 插件表权限管理 API
 */

import { http } from '@keqi.gress/shared-utils'
import type { ApiResponse } from '../types'

export interface PluginTablePermission {
  id?: number
  pluginId: string
  tableName: string
  allowedOperations?: string
  isReadonly?: boolean
  description?: string
  enabled?: boolean
  createTime?: string
  updateTime?: string
  createBy?: string
  updateBy?: string
}

const API_BASE = '/plugins/as-admin'

/**
 * 插件表权限管理 API
 */
export const tablePermissionApi = {
  /**
   * 获取所有权限配置
   */
  getList(params?: { pluginId?: string; tableName?: string }): Promise<ApiResponse<PluginTablePermission[]>> {
    return http.get(`${API_BASE}/table-permissions`, params)
  },

  /**
   * 根据ID查询权限配置
   */
  getById(id: number): Promise<ApiResponse<PluginTablePermission>> {
    return http.get(`${API_BASE}/table-permissions/${id}`)
  },

  /**
   * 创建权限配置
   */
  create(data: PluginTablePermission): Promise<ApiResponse<PluginTablePermission>> {
    return http.post(`${API_BASE}/table-permissions`, data)
  },

  /**
   * 更新权限配置
   */
  update(id: number, data: PluginTablePermission): Promise<ApiResponse<PluginTablePermission>> {
    return http.put(`${API_BASE}/table-permissions/${id}`, data)
  },

  /**
   * 删除权限配置
   */
  delete(id: number): Promise<ApiResponse> {
    return http.delete(`${API_BASE}/table-permissions/${id}`)
  },

  /**
   * 启用/禁用权限配置
   */
  setEnabled(id: number, enabled: boolean): Promise<ApiResponse<PluginTablePermission>> {
    return http.put(`${API_BASE}/table-permissions/${id}/enabled?enabled=${enabled}`)
  }
}

