<template>
  <div class="audit-log-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="审计日志" subtitle="查看所有管理操作记录，追踪系统变更">
        <template #actions>
          <n-button type="primary" @click="handleExport" :loading="exportLoading">
            <template #icon>
              <n-icon><component :is="Download" /></n-icon>
            </template>
            导出日志
          </n-button>
          <n-button @click="loadData" :loading="refreshLoading">
            <template #icon>
              <n-icon><component :is="Refresh" /></n-icon>
            </template>
            刷新
          </n-button>
        </template>
      </PageHeader>
    </div>

    <!-- 内容区域 - 有 padding -->
    <div class="page-content">
      <!-- 过滤面板 -->
      <FilterPanel
        v-model:filters="filters"
        v-model:show-advanced="showAdvanced"
        :basic-fields="basicFields"
        :advanced-fields="advancedFields"
        @search="handleSearch"
        @reset="handleReset"
      />

      <!-- 数据表格 -->
      <div class="table-container">
        <n-data-table
          :columns="columns"
          :data="tableData"
          :loading="loading"
          :pagination="false"
          :row-key="(row: AuditLog) => row.id"
          striped
        />
        
        <!-- 独立分页器 -->
        <div class="pagination-container">
          <n-pagination
            v-model:page="pagination.page"
            v-model:page-size="pagination.pageSize"
            :item-count="pagination.itemCount"
            :page-sizes="pagination.pageSizes"
            show-size-picker
            show-quick-jumper
            @update:page="handlePageChange"
            @update:page-size="handlePageSizeChange"
          >
            <template #prefix="{ itemCount }">
              共 {{ itemCount }} 条
            </template>
          </n-pagination>
        </div>
      </div>
    </div>

    <!-- 详情抽屉 -->
    <n-drawer
      v-model:show="showDetailDrawer"
      :width="720"
      placement="right"
    >
      <n-drawer-content title="审计日志详情" closable>
        <div v-if="currentLog" class="log-detail">
          <!-- 基本信息 -->
          <n-card title="基本信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="日志ID">
                {{ currentLog.id }}
              </n-descriptions-item>
              <n-descriptions-item label="操作时间">
                {{ formatDateTime(currentLog.operationTime) }}
              </n-descriptions-item>
              <n-descriptions-item label="操作类型">
                <n-tag :type="getOperationTypeColor(currentLog.operationType)" size="small">
                  {{ getOperationTypeText(currentLog.operationType) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="操作名称">
                {{ currentLog.operationName }}
              </n-descriptions-item>
              <n-descriptions-item label="操作结果" :span="2">
                <n-tag :type="currentLog.result === 'SUCCESS' ? 'success' : 'error'" size="small">
                  {{ currentLog.result === 'SUCCESS' ? '成功' : '失败' }}
                </n-tag>
              </n-descriptions-item>
            </n-descriptions>

            <template v-if="currentLog.operationDesc">
              <n-divider />
              <n-descriptions :column="1" label-placement="left">
                <n-descriptions-item label="操作描述">
                  {{ currentLog.operationDesc }}
                </n-descriptions-item>
              </n-descriptions>
            </template>
          </n-card>

          <!-- 操作人信息 -->
          <n-card title="操作人信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="操作人ID">
                {{ currentLog.operatorId }}
              </n-descriptions-item>
              <n-descriptions-item label="操作人名称">
                {{ currentLog.operatorName || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="操作IP" :span="2">
                {{ currentLog.operatorIp || '-' }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- 目标对象信息 -->
          <n-card v-if="currentLog.targetType" title="目标对象" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="目标类型">
                <n-tag size="small">{{ getTargetTypeText(currentLog.targetType) }}</n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="目标ID">
                {{ currentLog.targetId || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="目标名称" :span="2">
                {{ currentLog.targetName || '-' }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- 错误信息 -->
          <n-card v-if="currentLog.result === 'FAILURE' && currentLog.errorMessage" 
                  title="错误信息" 
                  :bordered="false" 
                  class="detail-section">
            <n-alert type="error" :show-icon="false">
              {{ currentLog.errorMessage }}
            </n-alert>
          </n-card>

          <!-- 变更内容 -->
          <n-card v-if="currentLog.beforeData || currentLog.afterData" 
                  title="变更内容" 
                  :bordered="false" 
                  class="detail-section">
            <n-tabs type="line">
              <n-tab-pane v-if="currentLog.beforeData" name="before" tab="变更前">
                <n-code :code="JSON.stringify(currentLog.beforeData, null, 2)" language="json" />
              </n-tab-pane>
              <n-tab-pane v-if="currentLog.afterData" name="after" tab="变更后">
                <n-code :code="JSON.stringify(currentLog.afterData, null, 2)" language="json" />
              </n-tab-pane>
            </n-tabs>
          </n-card>
        </div>

        <template #footer>
          <n-space justify="end">
            <n-button @click="showDetailDrawer = false">关闭</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>

    <!-- 导出对话框 -->
    <n-modal
      v-model:show="showExportModal"
      preset="dialog"
      title="导出审计日志"
      positive-text="确认导出"
      negative-text="取消"
      @positive-click="confirmExport"
    >
      <n-form :model="exportForm">
        <n-form-item label="导出格式">
          <n-radio-group v-model:value="exportForm.format">
            <n-space>
              <n-radio value="CSV">CSV</n-radio>
              <n-radio value="EXCEL">Excel</n-radio>
              <n-radio value="JSON">JSON</n-radio>
            </n-space>
          </n-radio-group>
        </n-form-item>
        <n-form-item label="时间范围">
          <n-date-picker
            v-model:value="exportForm.timeRange"
            type="datetimerange"
            clearable
            style="width: 100%"
          />
        </n-form-item>
        <n-alert type="info" :show-icon="false" style="margin-top: 16px">
          将导出当前筛选条件下的所有日志记录
        </n-alert>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { useMessage } from '@keqi.gress/plugin-bridge'
import {
  NSpace,
  NButton,
  NDataTable,
  NPagination,
  NTag,
  NIcon,
  NDrawer,
  NDrawerContent,
  NDescriptions,
  NDescriptionsItem,
  NDivider,
  NCard,
  NAlert,
  NTabs,
  NTabPane,
  NCode,
  NModal,
  NForm,
  NFormItem,
  NRadioGroup,
  NRadio,
  NDatePicker,
  type DataTableColumns
} from 'naive-ui'
import { useIcon } from '@keqi.gress/plugin-bridge'
import { auditLogApi } from '../api'

// 图标
const Refresh = useIcon('RefreshOutline')
const Download = useIcon('DownloadOutline')
const Eye = useIcon('EyeOutline')
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const CloseCircle = useIcon('CloseCircleOutline')
import type { 
  AuditLog, 
  AuditOperationType, 
  AuditTargetType, 
  AuditResult,
  AuditLogExportRequest
} from '../types'

// FilterFieldConfig 类型定义
export type FilterFieldType = 'input' | 'select' | 'date' | 'date-range'

export type FilterFieldConfig = {
  key: string
  label?: string
  type?: FilterFieldType
  placeholder?: string
  options?: Array<{ label: string; value: unknown }>
  clearable?: boolean
  span?: number
  slotName?: string
  componentProps?: Record<string, unknown>
}

// Message
const message = useMessage()

// State
const loading = ref(false)
const refreshLoading = ref(false)
const exportLoading = ref(false)
const tableData = ref<AuditLog[]>([])
const showDetailDrawer = ref(false)
const currentLog = ref<AuditLog | null>(null)

// 过滤器状态
const showAdvanced = ref(false)
const filters = ref({
  operationType: null as AuditOperationType | null,
  targetType: null as AuditTargetType | null,
  result: null as AuditResult | null,
  keyword: '',
  startTime: null as number | null,
  endTime: null as number | null
})

// Pagination
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  pageSizes: [10, 20, 50, 100]
})

const handlePageChange = (page: number) => {
  pagination.page = page
  loadData()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

// 导出表单
const showExportModal = ref(false)
const exportForm = reactive({
  format: 'CSV' as 'CSV' | 'EXCEL' | 'JSON',
  timeRange: null as [number, number] | null
})

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'operationType',
    label: '操作类型',
    type: 'select',
    placeholder: '请选择操作类型',
    options: [
      { label: '全部', value: null },
      { label: '批准插件', value: 'APPROVE_PLUGIN' },
      { label: '拒绝插件', value: 'REJECT_PLUGIN' },
      { label: '下架插件', value: 'DELIST_PLUGIN' },
      { label: '重新上架', value: 'RELIST_PLUGIN' },
      { label: '删除插件', value: 'DELETE_PLUGIN' },
      { label: '批准开发者', value: 'APPROVE_DEVELOPER' },
      { label: '暂停开发者', value: 'SUSPEND_DEVELOPER' },
      { label: '激活开发者', value: 'ACTIVATE_DEVELOPER' },
      { label: '创建规则', value: 'CREATE_RULE' },
      { label: '更新规则', value: 'UPDATE_RULE' },
      { label: '删除规则', value: 'DELETE_RULE' },
      { label: '批量批准', value: 'BATCH_APPROVE' },
      { label: '批量拒绝', value: 'BATCH_REJECT' },
      { label: '批量下架', value: 'BATCH_DELIST' }
    ]
  },
  {
    key: 'targetType',
    label: '目标类型',
    type: 'select',
    placeholder: '请选择目标类型',
    options: [
      { label: '全部', value: null },
      { label: '插件', value: 'PLUGIN' },
      { label: '开发者', value: 'DEVELOPER' },
      { label: '规则', value: 'RULE' },
      { label: '分类', value: 'CATEGORY' },
      { label: '标签', value: 'TAG' }
    ]
  },
  {
    key: 'result',
    label: '操作结果',
    type: 'select',
    placeholder: '请选择操作结果',
    options: [
      { label: '全部', value: null },
      { label: '成功', value: 'SUCCESS' },
      { label: '失败', value: 'FAILURE' }
    ]
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索操作人、目标名称'
  }
]

const advancedFields: FilterFieldConfig[] = [
  {
    key: 'timeRange',
    label: '操作时间',
    type: 'date-range',
    placeholder: '选择时间范围',
    span: 2
  }
]

// Table Columns
const columns: DataTableColumns<AuditLog> = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '操作类型',
    key: 'operationType',
    width: 120,
    ellipsis: {
      tooltip: true
    },
    render: (row: AuditLog) => {
      const text = getOperationTypeText(row.operationType)
      return h(
        'div',
        {
          style: {
            maxWidth: '100%',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap'
          }
        },
        [
          h(
            NTag,
            { 
              type: getOperationTypeColor(row.operationType), 
              size: 'small',
              style: { 
                maxWidth: '100%',
                display: 'inline-block',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                whiteSpace: 'nowrap'
              }
            },
            { default: () => text }
          )
        ]
      )
    }
  },
  {
    title: '操作名称',
    key: 'operationName',
    width: 150,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '目标类型',
    key: 'targetType',
    width: 100,
    render: (row: AuditLog) => row.targetType ? h(
      NTag,
      { size: 'small' },
      { default: () => getTargetTypeText(row.targetType!) }
    ) : '-'
  },
  {
    title: '目标名称',
    key: 'targetName',
    width: 150,
    ellipsis: {
      tooltip: true
    },
    render: (row: AuditLog) => row.targetName || '-'
  },
  {
    title: '操作人',
    key: 'operatorName',
    width: 120,
    ellipsis: {
      tooltip: true
    },
    render: (row: AuditLog) => row.operatorName || row.operatorId
  },
  {
    title: '操作IP',
    key: 'operatorIp',
    width: 130,
    render: (row: AuditLog) => row.operatorIp || '-'
  },
  {
    title: '结果',
    key: 'result',
    width: 80,
    render: (row: AuditLog) => h(
      NTag,
      { 
        type: row.result === 'SUCCESS' ? 'success' : 'error',
        size: 'small'
      },
      { 
        default: () => row.result === 'SUCCESS' ? '成功' : '失败',
        icon: () => h(NIcon, { 
          component: row.result === 'SUCCESS' ? CheckmarkCircle : CloseCircle 
        })
      }
    )
  },
  {
    title: '操作时间',
    key: 'operationTime',
    width: 180,
    render: (row: AuditLog) => formatDateTime(row.operationTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 100,
    fixed: 'right',
    render: (row: AuditLog) => h(
      NButton,
      {
        size: 'small',
        onClick: () => handleViewDetail(row)
      },
      {
        default: () => '查看详情',
        icon: () => h(NIcon, { component: Eye })
      }
    )
  }
]

// Methods
const loadData = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const params: any = {
      page: pagination.page,
      size: pagination.pageSize
    }
    
    // 只添加有效的过滤参数
    if (filters.value.operationType) {
      params.operationType = filters.value.operationType
    }
    if (filters.value.targetType) {
      params.targetType = filters.value.targetType
    }
    if (filters.value.result) {
      params.result = filters.value.result
    }
    if (filters.value.keyword && filters.value.keyword.trim()) {
      params.keyword = filters.value.keyword.trim()
    }
    if (filters.value.startTime) {
      // 如果 startTime 是时间戳（number），转换为毫秒时间戳
      const timestamp = typeof filters.value.startTime === 'number' 
        ? filters.value.startTime 
        : new Date(filters.value.startTime).getTime()
      params.startTime = timestamp
    }
    if (filters.value.endTime) {
      // 如果 endTime 是时间戳（number），转换为毫秒时间戳
      const timestamp = typeof filters.value.endTime === 'number' 
        ? filters.value.endTime 
        : new Date(filters.value.endTime).getTime()
      params.endTime = timestamp
    }

    const response = await auditLogApi.getList(params)
 
      tableData.value = response.items
      pagination.itemCount = response.total
  

  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  filters.value.operationType = null
  filters.value.targetType = null
  filters.value.result = null
  filters.value.keyword = ''
  filters.value.startTime = null
  filters.value.endTime = null
  pagination.page = 1
  loadData()
}

const handleViewDetail = async (log: AuditLog) => {

    const response = await auditLogApi.getDetail(log.id)
    

      currentLog.value = response
      showDetailDrawer.value = true


}

const handleExport = () => {
  exportForm.format = 'CSV'
  exportForm.timeRange = null
  showExportModal.value = true
}

const confirmExport = async () => {

    const request: AuditLogExportRequest = {
      format: exportForm.format,
      operationType: filters.value.operationType || undefined,
      targetType: filters.value.targetType || undefined,
      result: filters.value.result || undefined,
      startTime: exportForm.timeRange ? new Date(exportForm.timeRange[0]).toISOString() : undefined,
      endTime: exportForm.timeRange ? new Date(exportForm.timeRange[1]).toISOString() : undefined
    }

    const response = await auditLogApi.export(request)
    
   
      // 下载文件
      const link = document.createElement('a')
      link.href = response
      link.download = `audit-logs-${Date.now()}.${exportForm.format.toLowerCase()}`
      link.click()
      
      message.success('导出成功')
      showExportModal.value = false

}

// Helper Functions
const getOperationTypeText = (type: AuditOperationType): string => {
  const textMap: Record<AuditOperationType, string> = {
    APPROVE_PLUGIN: '批准插件',
    REJECT_PLUGIN: '拒绝插件',
    DELIST_PLUGIN: '下架插件',
    RELIST_PLUGIN: '重新上架',
    DELETE_PLUGIN: '删除插件',
    APPROVE_DEVELOPER: '批准开发者',
    SUSPEND_DEVELOPER: '暂停开发者',
    ACTIVATE_DEVELOPER: '激活开发者',
    CREATE_RULE: '创建规则',
    UPDATE_RULE: '更新规则',
    DELETE_RULE: '删除规则',
    BATCH_APPROVE: '批量批准',
    BATCH_REJECT: '批量拒绝',
    BATCH_DELIST: '批量下架',
    OTHER: '其他'
  }
  return textMap[type] || type
}

const getOperationTypeColor = (type: AuditOperationType): 'default' | 'success' | 'warning' | 'error' | 'info' => {
  if (type.includes('APPROVE') || type.includes('ACTIVATE') || type.includes('RELIST')) {
    return 'success'
  }
  if (type.includes('REJECT') || type.includes('DELETE') || type.includes('DELIST') || type.includes('SUSPEND')) {
    return 'error'
  }
  if (type.includes('CREATE') || type.includes('UPDATE')) {
    return 'info'
  }
  return 'default'
}

const getTargetTypeText = (type: AuditTargetType): string => {
  const textMap: Record<AuditTargetType, string> = {
    PLUGIN: '插件',
    DEVELOPER: '开发者',
    RULE: '规则',
    CATEGORY: '分类',
    TAG: '标签',
    OTHER: '其他'
  }
  return textMap[type] || type
}

const formatDateTime = (dateTime: string): string => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// Lifecycle
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.audit-log-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.page-header-wrapper {
  background: white;
  border-bottom: 1px solid #e8e8e8;
}

.page-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  overflow: auto;
}

.table-container {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 16px;
  overflow: auto;
  display: flex;
  flex-direction: column;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.log-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-section {
  margin-bottom: 16px;
}
</style>
