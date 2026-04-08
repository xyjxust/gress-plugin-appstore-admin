/**
 * 插件商店管理前端类型定义
 */

/**
 * 插件提交状态
 */
export type SubmissionStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

/**
 * 插件类型
 */
export type PluginType = 'TASK' | 'TRIGGER' | 'APPLICATION' | 'MIDDLEWARE'

/**
 * 插件提交记录
 */
export interface PluginSubmission {
  id: number
  pluginId: string
  pluginName: string
  pluginType: PluginType
  version: string
  developerId: string
  developerName?: string
  description?: string
  icon?: string
  tags?: string[]
  category?: string
  status: SubmissionStatus
  reviewerId?: string
  reviewerName?: string
  reviewTime?: string
  reviewComment?: string
  submitTime: string
  createTime: string
  updateTime: string
}

/**
 * 分类
 */
export interface Category {
  id: number
  categoryName: string
  categoryKey: string
  description?: string
  icon?: string
  displayOrder: number
  enabled: boolean
  pluginCount: number
  createTime: string
  updateTime: string
}

/**
 * 标签
 */
export interface Tag {
  id: number
  tagName: string
  tagKey: string
  description?: string
  color?: string
  enabled: boolean
  usageCount: number
  createTime: string
  updateTime: string
}

/**
 * 分页结果
 */
export interface PageResult<T> {
  items: T[]
  total: number
  page: number
  size: number
  totalPages: number
}

/**
 * API响应
 */
export interface ApiResponse<T = any> {
  success: boolean
  data?: T
  message?: string
  total?: number
}

/**
 * 查询参数
 */
export interface QueryParams {
  page?: number
  size?: number
  [key: string]: any
}

/**
 * 插件状态
 */
export type PluginStatus = 'ONLINE' | 'OFFLINE' | 'DELISTED'

/**
 * 已上架插件
 */
export interface Plugin {
  id: number
  pluginId: string
  pluginName: string
  pluginType: PluginType
  currentVersion: string
  developerId: string
  developerName?: string
  description?: string
  icon?: string
  tags?: string[]
  category?: string
  status: PluginStatus
  installCount: number
  activeUsers: number
  rating?: number
  listingTime: string
  updateTime: string
  createTime: string
}

/**
 * 插件版本
 */
export interface PluginVersion {
  id: number
  pluginId: string
  version: string
  description?: string
  releaseNotes?: string
  filePath: string
  fileSize: number
  fileHash: string
  isCurrent: boolean
  status: SubmissionStatus
  downloadCount: number
  createTime: string
  updateTime: string
}

/**
 * 插件统计数据
 */
export interface PluginStatistics {
  pluginId: string
  pluginName: string
  installCount: number
  activeUsers: number
  totalDownloads: number
  rating?: number
  reviewCount: number
  installTrend: TrendPoint[]
  activeTrend: TrendPoint[]
}

/**
 * 趋势数据点
 */
export interface TrendPoint {
  date: string
  value: number
}

/**
 * 统计概览
 */
export interface StatisticsOverview {
  totalPlugins: number
  totalInstalls: number
  totalActiveUsers: number
  totalDownloads: number
  pendingReviews: number
  approvedToday: number
  rejectedToday: number
  newSubmissionsToday: number
  topPlugins: TopPlugin[]
}

/**
 * 热门插件
 */
export interface TopPlugin {
  pluginId: string
  pluginName: string
  installCount: number
  activeUsers: number
  rating?: number
}

/**
 * 趋势数据
 */
export interface TrendData {
  installTrend: TrendPoint[]
  activeTrend: TrendPoint[]
  downloadTrend: TrendPoint[]
  submissionTrend: TrendPoint[]
}

/**
 * 统计查询请求
 */
export interface StatisticsQueryRequest {
  startDate?: string
  endDate?: string
  pluginId?: string
  pluginType?: PluginType
  category?: string
}

/**
 * 导出请求
 */
export interface ExportRequest {
  format: 'CSV' | 'EXCEL' | 'JSON'
  startDate?: string
  endDate?: string
  pluginId?: string
  includeDetails?: boolean
}

/**
 * 开发者状态
 */
export type DeveloperStatus = 'PENDING' | 'ACTIVE' | 'SUSPENDED'

/**
 * 开发者信息
 */
export interface Developer {
  id: number
  userId: string
  username: string
  email: string
  displayName?: string
  company?: string
  website?: string
  bio?: string
  status: DeveloperStatus
  verified: boolean
  pluginCount: number
  totalDownloads: number
  applyTime: string
  reviewTime?: string
}

/**
 * 开发者详情
 */
export interface DeveloperDetail extends Developer {
  reviewerId?: string
  reviewComment?: string
  createTime: string
  updateTime: string
  plugins: PluginSubmission[]
}

/**
 * 开发者查询请求
 */
export interface DeveloperQueryRequest {
  page?: number
  size?: number
  status?: DeveloperStatus
  keyword?: string
  verified?: boolean
}

/**
 * 审计日志操作类型
 */
export type AuditOperationType = 
  | 'APPROVE_PLUGIN' 
  | 'REJECT_PLUGIN' 
  | 'DELIST_PLUGIN' 
  | 'RELIST_PLUGIN'
  | 'DELETE_PLUGIN'
  | 'APPROVE_DEVELOPER'
  | 'SUSPEND_DEVELOPER'
  | 'ACTIVATE_DEVELOPER'
  | 'CREATE_RULE'
  | 'UPDATE_RULE'
  | 'DELETE_RULE'
  | 'BATCH_APPROVE'
  | 'BATCH_REJECT'
  | 'BATCH_DELIST'
  | 'OTHER'

/**
 * 审计日志目标类型
 */
export type AuditTargetType = 'PLUGIN' | 'DEVELOPER' | 'RULE' | 'CATEGORY' | 'TAG' | 'OTHER'

/**
 * 审计日志操作结果
 */
export type AuditResult = 'SUCCESS' | 'FAILURE'

/**
 * 审计日志记录
 */
export interface AuditLog {
  id: number
  operationType: AuditOperationType
  operationName: string
  operationDesc?: string
  targetType?: AuditTargetType
  targetId?: string
  targetName?: string
  operatorId: string
  operatorName?: string
  operatorIp?: string
  result: AuditResult
  errorMessage?: string
  beforeData?: Record<string, any>
  afterData?: Record<string, any>
  operationTime: string
}

/**
 * 审计日志查询请求
 */
export interface AuditLogQueryRequest {
  page?: number
  size?: number
  operationType?: AuditOperationType
  targetType?: AuditTargetType
  operatorId?: string
  result?: AuditResult
  startTime?: string
  endTime?: string
  keyword?: string
}

/**
 * 审计日志导出请求
 */
export interface AuditLogExportRequest {
  format: 'CSV' | 'EXCEL' | 'JSON'
  operationType?: AuditOperationType
  targetType?: AuditTargetType
  operatorId?: string
  result?: AuditResult
  startTime?: string
  endTime?: string
}

/**
 * 审核规则类型
 */
export type RuleType = 'AUTO_APPROVE' | 'AUTO_REJECT' | 'FLAG'

/**
 * 审核规则条件
 */
export interface RuleCondition {
  field: string
  operator: 'equals' | 'contains' | 'greater_than' | 'less_than' | 'in' | 'not_in'
  value: any
}

/**
 * 审核规则条件组
 */
export interface RuleConditions {
  logic: 'AND' | 'OR'
  conditions: RuleCondition[]
}

/**
 * 审核规则动作
 */
export interface RuleAction {
  type: 'approve' | 'reject' | 'flag' | 'notify'
  params?: Record<string, any>
}

/**
 * 审核规则
 */
export interface ReviewRule {
  id: number
  ruleName: string
  description?: string
  ruleType: RuleType
  conditions: RuleConditions
  actions: RuleAction[]
  priority: number
  enabled: boolean
  matchCount: number
  lastMatchTime?: string
  createdBy?: string
  createTime: string
  updateTime: string
}

/**
 * 审核规则查询请求
 */
export interface ReviewRuleQueryRequest {
  page?: number
  size?: number
  ruleType?: RuleType
  enabled?: boolean
  keyword?: string
}

/**
 * 审核规则创建/更新请求
 */
export interface ReviewRuleRequest {
  ruleName: string
  description?: string
  ruleType: RuleType
  conditions: RuleConditions
  actions: RuleAction[]
  priority: number
  enabled: boolean
}

/**
 * 反馈类型
 */
export type FeedbackType = 'BUG' | 'FEATURE' | 'REPORT' | 'OTHER'

/**
 * 反馈严重程度
 */
export type FeedbackSeverity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

/**
 * 反馈状态
 */
export type FeedbackStatus = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED'

/**
 * 用户反馈
 */
export interface Feedback {
  id: number
  pluginId: string
  pluginVersion?: string
  feedbackType: FeedbackType
  title: string
  content: string
  severity?: FeedbackSeverity
  userId: string
  username?: string
  contact?: string
  status: FeedbackStatus
  handlerId?: string
  handlerName?: string
  handleTime?: string
  handleComment?: string
  attachments?: string[]
  submitTime: string
  createTime: string
  updateTime: string
}

/**
 * 反馈查询请求
 */
export interface FeedbackQueryRequest {
  page?: number
  size?: number
  pluginId?: string
  feedbackType?: FeedbackType
  status?: FeedbackStatus
  severity?: FeedbackSeverity
  userId?: string
  keyword?: string
  startTime?: string
  endTime?: string
}

/**
 * 反馈处理请求
 */
export interface FeedbackProcessRequest {
  handlerId: string
  handlerName: string
  handleComment: string
  status: FeedbackStatus
}

/**
 * Signing key management
 */
export interface SigningKeyDTO {
  keyId: string
  alias: string
  fingerprintSha256: string
  active: boolean
  trustedUntil?: string | null
  revokedAt?: string | null
  createTime: string
}

export interface GenerateSigningKeyRequest {
  alias?: string
  validityDays?: number
  makeActive?: boolean
  trustedWindowDays?: number
}

export interface ActivateSigningKeyRequest {
  trustedWindowDays?: number
}
