/**
 * 权限申请审核 API
 */

import { http } from '@keqi.gress/shared-utils'
import type { ApiResponse } from '../types'

export interface PluginTablePermissionRequest {
  id?: number
  pluginId: string
  tableName: string
  requestedOperations?: string
  isReadonly?: boolean
  reason?: string
  description?: string
  status?: 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED'
  applicantId?: string
  applicantName?: string
  reviewerId?: string
  reviewerName?: string
  reviewComment?: string
  reviewTime?: string
  createTime?: string
  updateTime?: string
}

export interface ApprovePermissionRequest {
  comment?: string
  reviewerId?: string
  reviewerName?: string
}

export interface RejectPermissionRequest {
  reason: string
  reviewerId?: string
  reviewerName?: string
}

const API_BASE = '/plugins/as-admin'

/**
 * 权限申请审核 API
 */
export const permissionRequestApi = {
  /**
   * 获取待审核申请列表
   */
  getPendingList(): Promise<ApiResponse<PluginTablePermissionRequest[]>> {
    return http.get(`${API_BASE}/permission-requests/pending`)
  },

  /**
   * 获取所有申请（支持筛选）
   */
  getList(params?: { pluginId?: string; status?: string }): Promise<ApiResponse<PluginTablePermissionRequest[]>> {
    return http.get(`${API_BASE}/permission-requests`, params)
  },

  /**
   * 根据ID查询申请
   */
  getById(id: number): Promise<ApiResponse<PluginTablePermissionRequest>> {
    return http.get(`${API_BASE}/permission-requests/${id}`)
  },

  /**
   * 批准申请
   */
  approve(id: number, data: ApprovePermissionRequest): Promise<ApiResponse<PluginTablePermissionRequest>> {
    return http.post(`${API_BASE}/permission-requests/${id}/approve`, data)
  },

  /**
   * 拒绝申请
   */
  reject(id: number, data: RejectPermissionRequest): Promise<ApiResponse<PluginTablePermissionRequest>> {
    return http.post(`${API_BASE}/permission-requests/${id}/reject`, data)
  }
}

