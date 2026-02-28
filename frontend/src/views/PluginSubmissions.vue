<template>
  <div class="plugin-submissions-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="插件审核管理" subtitle="审核开发者提交的插件申请">
        <template #actions>
          <n-button type="primary" @click="handleBatchApprove" :disabled="!hasSelection" :loading="batchApproveLoading">
            <template #icon>
              <n-icon><component :is="CheckmarkCircle" /></n-icon>
            </template>
            批量批准
          </n-button>
          <n-button @click="handleBatchReject" :disabled="!hasSelection" :loading="batchRejectLoading">
            <template #icon>
              <n-icon><component :is="CloseCircle" /></n-icon>
            </template>
            批量拒绝
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
          :row-key="(row: PluginSubmission) => row.id"
          :checked-row-keys="checkedRowKeys"
          @update:checked-row-keys="handleCheck"
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
      <n-drawer-content title="插件详情" closable>
        <PluginSubmissionDetail
          v-if="currentSubmission"
          :submission="currentSubmission"
          @approve="handleApprove"
          @reject="handleReject"
          @close="showDetailDrawer = false"
        />
      </n-drawer-content>
    </n-drawer>

    <!-- 批准对话框 -->
    <n-modal
      v-model:show="showApproveModal"
      preset="dialog"
      title="批准插件"
      positive-text="确认批准"
      negative-text="取消"
      :positive-button-props="{ loading: approveLoading }"
      @positive-click="confirmApprove"
    >
      <n-form :model="approveForm" :rules="approveRules">
        <n-form-item label="审核意见" path="comment">
          <n-input
            v-model:value="approveForm.comment"
            type="textarea"
            placeholder="请输入审核意见（可选）"
            :rows="4"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 拒绝对话框 -->
    <n-modal
      v-model:show="showRejectModal"
      preset="dialog"
      title="拒绝插件"
      positive-text="确认拒绝"
      negative-text="取消"
      :positive-button-props="{ loading: rejectLoading }"
      @positive-click="confirmReject"
    >
      <n-form ref="rejectFormRef" :model="rejectForm" :rules="rejectRules">
        <n-form-item label="拒绝原因" path="reason">
          <n-input
            v-model:value="rejectForm.reason"
            type="textarea"
            placeholder="请输入拒绝原因（必填）"
            :rows="4"
          />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, h, onMounted, resolveComponent } from 'vue'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'

// 图标
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const CloseCircle = useIcon('CloseCircleOutline')
const Refresh = useIcon('RefreshOutline')
const Eye = useIcon('EyeOutline')
const CheckmarkDone = useIcon('CheckmarkDoneOutline')
const Close = useIcon('CloseOutline')
import PluginSubmissionDetail from '../components/PluginSubmissionDetail.vue'
import { submissionApi } from '../api'
import type { PluginSubmission, SubmissionStatus, PluginType } from '../types'

// PageHeader 和 FilterPanel 是宿主机全局注册的组件，无需导入
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

// 消息提示
const message = useMessage()

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const batchApproveLoading = ref(false)
const batchRejectLoading = ref(false)
const approveLoading = ref(false)
const rejectLoading = ref(false)
const tableData = ref<PluginSubmission[]>([])
const checkedRowKeys = ref<number[]>([])
const showDetailDrawer = ref(false)
const currentSubmission = ref<PluginSubmission | null>(null)

// 过滤器状态
const showAdvanced = ref(false)
const filters = ref({
  status: 'PENDING' as string | null,
  pluginType: null as string | null,
  keyword: '',
  startTime: null as string | null,
  endTime: null as string | null
})

// 分页状态
const pagination = reactive({
  page: 1,
  pageSize: 20,
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

// 批准表单
const showApproveModal = ref(false)
const approveForm = reactive({
  comment: ''
})
const approveRules: FormRules = {}
const approveTargetIds = ref<number[]>([])

// 拒绝表单
const showRejectModal = ref(false)
const rejectFormRef = ref<FormInst | null>(null)
const rejectForm = reactive({
  reason: ''
})
const rejectRules: FormRules = {
  reason: [
    { required: true, message: '请输入拒绝原因', trigger: 'blur' }
  ]
}
const rejectTargetIds = ref<number[]>([])

// 计算属性
const hasSelection = computed(() => checkedRowKeys.value.length > 0)

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'status',
    label: '审核状态',
    type: 'select',
    placeholder: '请选择状态',
    options: [
      { label: '全部', value: null },
      { label: '待审核', value: 'PENDING' },
      { label: '已批准', value: 'APPROVED' },
      { label: '已拒绝', value: 'REJECTED' }
    ]
  },
  {
    key: 'pluginType',
    label: '插件类型',
    type: 'select',
    placeholder: '请选择类型',
    options: [
      { label: '全部', value: null },
      { label: '任务节点', value: 'TASK' },
      { label: '触发器', value: 'TRIGGER' },
      { label: '应用插件', value: 'APPLICATION' }
    ]
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索插件名称、开发者'
  }
]

const advancedFields: FilterFieldConfig[] = [
  {
    key: 'timeRange',
    label: '提交时间',
    type: 'date-range',
    placeholder: '选择时间范围',
    span: 2
  }
]

// 表格列配置
const columns: any[] = [
  {
    type: 'selection'
  },
  {
    title: '插件名称',
    key: 'pluginName',
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
    title: '版本',
    key: 'version',
    width: 100
  },
  {
    title: '类型',
    key: 'pluginType',
    width: 100,
    render: (row: PluginSubmission) => {
      const NTag = resolveComponent('NTag') as any
      const typeMap: Record<PluginType, { label: string; type: 'info' | 'success' | 'warning' }> = {
        TASK: { label: '任务节点', type: 'info' },
        TRIGGER: { label: '触发器', type: 'success' },
        APPLICATION: { label: '应用插件', type: 'warning' }
      }
      const config = typeMap[row.pluginType]
      return h(NTag, { type: config.type, size: 'small' }, { default: () => config.label })
    }
  },
  {
    title: '开发者',
    key: 'developerName',
    width: 120,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: PluginSubmission) => {
      const NTag = resolveComponent('NTag') as any
      const statusMap: Record<SubmissionStatus, { label: string; type: 'default' | 'success' | 'error' }> = {
        PENDING: { label: '待审核', type: 'default' },
        APPROVED: { label: '已批准', type: 'success' },
        REJECTED: { label: '已拒绝', type: 'error' }
      }
      const config = statusMap[row.status]
      return h(NTag, { type: config.type, size: 'small' }, { default: () => config.label })
    }
  },
  {
    title: '提交时间',
    key: 'submitTime',
    width: 160,
    render: (row: PluginSubmission) => formatDateTime(row.submitTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row: PluginSubmission) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions = []
      
      if (row.status === 'PENDING') {
        moreOptions.push({
          label: '批准',
          key: 'approve',
          icon: () => h(NIcon, { component: CheckmarkDone })
        })
        moreOptions.push({
          label: '拒绝',
          key: 'reject',
          icon: () => h(NIcon, { component: Close })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'approve':
            handleApprove(row.id)
            break
          case 'reject':
            handleReject(row.id)
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
                onClick: () => handleViewDetail(row)
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

// 加载数据
async function loadData() {
  loading.value = true
  refreshLoading.value = true
  try {
    const params = {
      page: pagination.page,
      size: pagination.pageSize,
      status: filters.value.status || undefined,
      type: filters.value.pluginType || undefined,
      keyword: filters.value.keyword || undefined
    }

    const response = await submissionApi.getList(params)
    

      tableData.value = response.items
      pagination.itemCount = response.total


  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

// 查看详情
function handleViewDetail(submission: PluginSubmission) {
  currentSubmission.value = submission
  showDetailDrawer.value = true
}

// 批准插件
function handleApprove(id: number | number[]) {
  approveTargetIds.value = Array.isArray(id) ? id : [id]
  approveForm.comment = ''
  showApproveModal.value = true
}

// 确认批准
async function confirmApprove() {
  approveLoading.value = true
  try {
    for (const id of approveTargetIds.value) {
      await submissionApi.approve(id, { 
        comment: approveForm.comment,
        reviewerId: 'admin', // TODO: Get from current user
        reviewerName: '管理员' // TODO: Get from current user
      })
    }
    
    message.success(`成功批准 ${approveTargetIds.value.length} 个插件`)
    showApproveModal.value = false
    checkedRowKeys.value = []
    await loadData()
    return true
  } catch (error) {
  
    return false
  } finally {
    approveLoading.value = false
  }
}

// 拒绝插件
function handleReject(id: number | number[]) {
  rejectTargetIds.value = Array.isArray(id) ? id : [id]
  rejectForm.reason = ''
  showRejectModal.value = true
}

// 确认拒绝
async function confirmReject() {
  try {
    await rejectFormRef.value?.validate()
  } catch {
    return false
  }
  
  rejectLoading.value = true
  try {
    for (const id of rejectTargetIds.value) {
      await submissionApi.reject(id, { 
        reason: rejectForm.reason,
        reviewerId: 'admin', // TODO: Get from current user
        reviewerName: '管理员' // TODO: Get from current user
      })
    }
    
    message.success(`成功拒绝 ${rejectTargetIds.value.length} 个插件`)
    showRejectModal.value = false
    checkedRowKeys.value = []
    await loadData()
    return true
  } catch (error) {

    return false
  } finally {
    rejectLoading.value = false
  }
}

// 批量批准
async function handleBatchApprove() {
  batchApproveLoading.value = true
  try {
    handleApprove(checkedRowKeys.value)
  } finally {
    batchApproveLoading.value = false
  }
}

// 批量拒绝
async function handleBatchReject() {
  batchRejectLoading.value = true
  try {
    handleReject(checkedRowKeys.value)
  } finally {
    batchRejectLoading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  filters.value.status = 'PENDING'
  filters.value.pluginType = null
  filters.value.keyword = ''
  filters.value.startTime = null
  filters.value.endTime = null
  pagination.page = 1
  loadData()
}

// 选择行
function handleCheck(keys: number[]) {
  checkedRowKeys.value = keys
}

// 格式化日期时间
function formatDateTime(dateStr: string): string {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 初始化
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.plugin-submissions-page {
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
</style>
