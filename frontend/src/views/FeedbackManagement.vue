<template>
  <div class="feedback-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="用户反馈管理" subtitle="查看和处理用户提交的反馈和举报">
        <template #actions>
          <n-button @click="loadFeedbacks" :loading="refreshLoading">
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
        @search="handleSearch"
        @reset="handleReset"
      />

      <!-- 数据表格 -->
      <div class="table-container">
        <n-data-table
          :columns="columns"
          :data="feedbacks"
          :loading="loading"
          :pagination="false"
          :row-key="(row: Feedback) => row.id"
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

    <!-- 反馈详情抽屉 -->
    <n-drawer
      v-model:show="showDetailDrawer"
      :width="720"
      placement="right"
    >
      <n-drawer-content title="反馈详情" closable>
        <div v-if="currentFeedback" class="feedback-detail">
          <!-- Basic Info -->
          <n-card title="基本信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="反馈ID">
                {{ currentFeedback.id }}
              </n-descriptions-item>
              <n-descriptions-item label="反馈类型">
                <n-tag :type="getFeedbackTypeColor(currentFeedback.feedbackType)">
                  {{ getFeedbackTypeText(currentFeedback.feedbackType) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="插件ID">
                {{ currentFeedback.pluginId }}
              </n-descriptions-item>
              <n-descriptions-item label="插件版本">
                {{ currentFeedback.pluginVersion || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="严重程度" v-if="currentFeedback.severity">
                <n-tag :type="getSeverityColor(currentFeedback.severity)">
                  {{ getSeverityText(currentFeedback.severity) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="状态">
                <n-tag :type="getStatusColor(currentFeedback.status)">
                  {{ getStatusText(currentFeedback.status) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="提交时间" :span="2">
                {{ formatDateTime(currentFeedback.submitTime) }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- User Info -->
          <n-card title="用户信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="用户ID">
                {{ currentFeedback.userId }}
              </n-descriptions-item>
              <n-descriptions-item label="用户名">
                {{ currentFeedback.username || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="联系方式" :span="2">
                {{ currentFeedback.contact || '-' }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- Feedback Content -->
          <n-card title="反馈内容" :bordered="false" class="detail-section">
            <n-descriptions :column="1" label-placement="left">
              <n-descriptions-item label="标题">
                {{ currentFeedback.title }}
              </n-descriptions-item>
              <n-descriptions-item label="详细描述">
                <div class="feedback-content">
                  {{ currentFeedback.content }}
                </div>
              </n-descriptions-item>
            </n-descriptions>

            <template v-if="currentFeedback.attachments && currentFeedback.attachments.length > 0">
              <n-divider />
              <n-descriptions :column="1" label-placement="left">
                <n-descriptions-item label="附件">
                  <n-space vertical>
                    <n-tag
                      v-for="(attachment, index) in currentFeedback.attachments"
                      :key="index"
                      size="small"
                    >
                      {{ attachment }}
                    </n-tag>
                  </n-space>
                </n-descriptions-item>
              </n-descriptions>
            </template>
          </n-card>

          <!-- Handle Info -->
          <n-card 
            v-if="currentFeedback.handlerId" 
            title="处理信息" 
            :bordered="false" 
            class="detail-section"
          >
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="处理人ID">
                {{ currentFeedback.handlerId }}
              </n-descriptions-item>
              <n-descriptions-item label="处理人">
                {{ currentFeedback.handlerName || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="处理时间" :span="2">
                {{ currentFeedback.handleTime ? formatDateTime(currentFeedback.handleTime) : '-' }}
              </n-descriptions-item>
            </n-descriptions>

            <template v-if="currentFeedback.handleComment">
              <n-divider />
              <n-descriptions :column="1" label-placement="left">
                <n-descriptions-item label="处理意见">
                  <div class="handle-comment">
                    {{ currentFeedback.handleComment }}
                  </div>
                </n-descriptions-item>
              </n-descriptions>
            </template>
          </n-card>
        </div>

        <template #footer>
          <n-space justify="end">
            <n-button @click="showDetailDrawer = false">关闭</n-button>
            <n-button
              v-if="currentFeedback && currentFeedback.status === 'OPEN'"
              type="primary"
              @click="handleProcess(currentFeedback)"
            >
              处理反馈
            </n-button>
            <n-button
              v-if="currentFeedback && (currentFeedback.status === 'OPEN' || currentFeedback.status === 'IN_PROGRESS')"
              type="success"
              @click="handleClose(currentFeedback)"
            >
              关闭反馈
            </n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>

    <!-- 处理反馈对话框 -->
    <n-modal
      v-model:show="showProcessModal"
      preset="dialog"
      title="处理反馈"
      positive-text="确定"
      negative-text="取消"
      :positive-button-props="{ loading: processLoading }"
      @positive-click="handleProcessSubmit"
    >
      <n-form
        ref="processFormRef"
        :model="processForm"
        :rules="processFormRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="处理状态" path="status">
          <n-select
            v-model:value="processForm.status"
            :options="processStatusOptions"
            placeholder="请选择处理状态"
          />
        </n-form-item>
        <n-form-item label="处理意见" path="handleComment">
          <n-input
            v-model:value="processForm.handleComment"
            type="textarea"
            :rows="4"
            placeholder="请输入处理意见"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 关闭反馈对话框 -->
    <n-modal
      v-model:show="showCloseModal"
      preset="dialog"
      title="关闭反馈"
      positive-text="确定"
      negative-text="取消"
      :positive-button-props="{ loading: closeLoading }"
      @positive-click="handleCloseSubmit"
    >
      <n-form
        :model="closeForm"
        :rules="closeFormRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="关闭说明" path="comment">
          <n-input
            v-model:value="closeForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入关闭说明（可选）"
          />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, resolveComponent } from 'vue'
import type { FormInst, FormRules, DataTableColumns } from 'naive-ui'
import { NTag } from 'naive-ui'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'

// 图标
const Refresh = useIcon('RefreshOutline')
const Eye = useIcon('EyeOutline')
const CheckmarkCircleOutline = useIcon('CheckmarkCircleOutline')
const CloseCircleOutline = useIcon('CloseCircleOutline')
import { feedbackApi } from '../api'
import type { 
  Feedback, 
  FeedbackType, 
  FeedbackStatus, 
  FeedbackSeverity,
  FeedbackProcessRequest
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
const processLoading = ref(false)
const closeLoading = ref(false)
const feedbacks = ref<Feedback[]>([])
const showDetailDrawer = ref(false)
const currentFeedback = ref<Feedback | null>(null)
const showProcessModal = ref(false)
const showCloseModal = ref(false)

// 过滤器状态
const showAdvanced = ref(false)
const filters = ref({
  keyword: '',
  feedbackType: null as FeedbackType | null,
  status: null as FeedbackStatus | null,
  severity: null as FeedbackSeverity | null,
  pluginId: ''
})

// Pagination
const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  pageSizes: [10, 20, 50, 100]
})

const handlePageChange = (page: number) => {
  pagination.page = page
  loadFeedbacks()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadFeedbacks()
}

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'feedbackType',
    label: '反馈类型',
    type: 'select',
    placeholder: '请选择反馈类型',
    options: [
      { label: '全部', value: null },
      { label: 'Bug反馈', value: 'BUG' },
      { label: '功能建议', value: 'FEATURE' },
      { label: '举报', value: 'REPORT' },
      { label: '其他', value: 'OTHER' }
    ]
  },
  {
    key: 'status',
    label: '处理状态',
    type: 'select',
    placeholder: '请选择状态',
    options: [
      { label: '全部', value: null },
      { label: '待处理', value: 'OPEN' },
      { label: '处理中', value: 'IN_PROGRESS' },
      { label: '已解决', value: 'RESOLVED' },
      { label: '已关闭', value: 'CLOSED' }
    ]
  },
  {
    key: 'severity',
    label: '严重程度',
    type: 'select',
    placeholder: '请选择严重程度',
    options: [
      { label: '全部', value: null },
      { label: '低', value: 'LOW' },
      { label: '中', value: 'MEDIUM' },
      { label: '高', value: 'HIGH' },
      { label: '严重', value: 'CRITICAL' }
    ]
  },
  {
    key: 'pluginId',
    label: '插件ID',
    type: 'input',
    placeholder: '请输入插件ID'
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索标题、内容'
  }
]

// 处理状态选项
const processStatusOptions = [
  { label: '处理中', value: 'IN_PROGRESS' },
  { label: '已解决', value: 'RESOLVED' }
]

// 处理表单
const processFormRef = ref<FormInst | null>(null)
const processForm = reactive({
  status: 'IN_PROGRESS' as FeedbackStatus,
  handleComment: ''
})

const processFormRules: FormRules = {
  status: {
    required: true,
    message: '请选择处理状态',
    trigger: 'change'
  },
  handleComment: {
    required: true,
    message: '请输入处理意见',
    trigger: 'blur'
  }
}

// 关闭表单
const closeForm = reactive({
  comment: ''
})

const closeFormRules: FormRules = {}

// Table Columns
const columns: DataTableColumns<Feedback> = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '反馈类型',
    key: 'feedbackType',
    width: 100,
    render: (row) => h(
      NTag,
      { type: getFeedbackTypeColor(row.feedbackType), size: 'small' },
      { default: () => getFeedbackTypeText(row.feedbackType) }
    )
  },
  {
    title: '标题',
    key: 'title',
    width: 200,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '插件ID',
    key: 'pluginId',
    width: 150,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '严重程度',
    key: 'severity',
    width: 100,
    render: (row) => row.severity ? h(
      NTag,
      { type: getSeverityColor(row.severity), size: 'small' },
      { default: () => getSeverityText(row.severity) }
    ) : '-'
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => h(
      NTag,
      { type: getStatusColor(row.status) },
      { default: () => getStatusText(row.status) }
    )
  },
  {
    title: '提交用户',
    key: 'username',
    width: 120,
    render: (row) => row.username || row.userId
  },
  {
    title: '提交时间',
    key: 'submitTime',
    width: 180,
    render: (row) => formatDateTime(row.submitTime)
  },
  {
    title: '处理人',
    key: 'handlerName',
    width: 120,
    render: (row) => row.handlerName || '-'
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions = []
      
      if (row.status === 'OPEN') {
        moreOptions.push({
          label: '处理',
          key: 'process',
          icon: () => h(NIcon, { component: CheckmarkCircleOutline })
        })
      }
      
      if (row.status === 'OPEN' || row.status === 'IN_PROGRESS') {
        moreOptions.push({
          label: '关闭',
          key: 'close',
          icon: () => h(NIcon, { component: CloseCircleOutline })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'process':
            handleProcess(row)
            break
          case 'close':
            handleClose(row)
            break
        }
      }

      return h(
        NSpace,
        { size: 2, wrap: false },
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                onClick: () => handleViewDetail(row.id)
              },
              {
                icon: () => h(NIcon, { component: Eye }),
                default: () => '查看'
              }
            ),
            moreOptions.length > 0 && h(
              NDropdown,
              {
                trigger: 'click',
                options: moreOptions,
                onSelect: handleMoreSelect
              },
              {
                default: () => h(
                  NButton,
                  {
                    size: 'small',
                    quaternary: true
                  },
                  {
                    icon: () => h(NIcon, { component: MoreHorizontal })
                  }
                )
              }
            )
          ]
        }
      )
    }
  }
]

// Methods
const loadFeedbacks = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const params: any = {
      page: pagination.page,
      size: pagination.pageSize,
      keyword: filters.value.keyword || undefined,
      feedbackType: filters.value.feedbackType || undefined,
      status: filters.value.status || undefined,
      severity: filters.value.severity || undefined,
      pluginId: filters.value.pluginId || undefined
    }

    const response = await feedbackApi.getList(params)
    

      feedbacks.value = response.items
      pagination.itemCount = response.total


  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadFeedbacks()
}

const handleReset = () => {
  filters.value.keyword = ''
  filters.value.feedbackType = null
  filters.value.status = null
  filters.value.severity = null
  filters.value.pluginId = ''
  pagination.page = 1
  loadFeedbacks()
}

const handleViewDetail = async (id: number) => {

    const response = await feedbackApi.getDetail(id)
    
  
      currentFeedback.value = response
      showDetailDrawer.value = true
 
}

const handleProcess = (feedback: Feedback) => {
  currentFeedback.value = feedback
  processForm.status = 'IN_PROGRESS'
  processForm.handleComment = ''
  showProcessModal.value = true
}

const handleProcessSubmit = async () => {
  if (!processFormRef.value || !currentFeedback.value) return false

  try {
    await processFormRef.value.validate()

    processLoading.value = true
    const request: FeedbackProcessRequest = {
      handleComment: processForm.handleComment,
      status: processForm.status
    }

    const response = await feedbackApi.process(currentFeedback.value.id, request)


      message.success('处理成功')
      showProcessModal.value = false
      showDetailDrawer.value = false
      loadFeedbacks()
      return true

  } catch (error: any) {

    return false
  } finally {
    processLoading.value = false
  }
}

const handleClose = (feedback: Feedback) => {
  currentFeedback.value = feedback
  closeForm.comment = ''
  showCloseModal.value = true
}

const handleCloseSubmit = async () => {
  if (!currentFeedback.value) return false

  closeLoading.value = true
  try {
    const response = await feedbackApi.close(currentFeedback.value.id, {
      comment: closeForm.comment || undefined
    })


      message.success('关闭成功')
      showCloseModal.value = false
      showDetailDrawer.value = false
      loadFeedbacks()
      return true

  } catch (error: any) {

    return false
  } finally {
    closeLoading.value = false
  }
}

// Helper Functions
const getFeedbackTypeColor = (type: FeedbackType): 'default' | 'success' | 'warning' | 'error' | 'info' => {
  const colorMap: Record<FeedbackType, 'default' | 'success' | 'warning' | 'error' | 'info'> = {
    BUG: 'error',
    FEATURE: 'info',
    REPORT: 'warning',
    OTHER: 'default'
  }
  return colorMap[type] || 'default'
}

const getFeedbackTypeText = (type: FeedbackType): string => {
  const textMap: Record<FeedbackType, string> = {
    BUG: 'Bug反馈',
    FEATURE: '功能建议',
    REPORT: '举报',
    OTHER: '其他'
  }
  return textMap[type] || type
}

const getSeverityColor = (severity: FeedbackSeverity): 'default' | 'success' | 'warning' | 'error' => {
  const colorMap: Record<FeedbackSeverity, 'default' | 'success' | 'warning' | 'error'> = {
    LOW: 'default',
    MEDIUM: 'warning',
    HIGH: 'error',
    CRITICAL: 'error'
  }
  return colorMap[severity] || 'default'
}

const getSeverityText = (severity: FeedbackSeverity): string => {
  const textMap: Record<FeedbackSeverity, string> = {
    LOW: '低',
    MEDIUM: '中',
    HIGH: '高',
    CRITICAL: '严重'
  }
  return textMap[severity] || severity
}

const getStatusColor = (status: FeedbackStatus): 'default' | 'success' | 'warning' | 'error' | 'info' => {
  const colorMap: Record<FeedbackStatus, 'default' | 'success' | 'warning' | 'error' | 'info'> = {
    OPEN: 'warning',
    IN_PROGRESS: 'info',
    RESOLVED: 'success',
    CLOSED: 'default'
  }
  return colorMap[status] || 'default'
}

const getStatusText = (status: FeedbackStatus): string => {
  const textMap: Record<FeedbackStatus, string> = {
    OPEN: '待处理',
    IN_PROGRESS: '处理中',
    RESOLVED: '已解决',
    CLOSED: '已关闭'
  }
  return textMap[status] || status
}

const formatDateTime = (dateTime: string): string => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// Lifecycle
onMounted(() => {
  loadFeedbacks()
})
</script>

<style scoped>
.feedback-management-page {
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

.feedback-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-section {
  margin-bottom: 16px;
}

.feedback-content,
.handle-comment {
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
  padding: 12px;
  background: #f5f5f5;
  border-radius: 4px;
}
</style>
