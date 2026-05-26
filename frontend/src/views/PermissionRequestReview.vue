<template>
  <div class="permission-request-review-page">
    <!-- 页面头部 -->
    <div class="page-header-wrapper">
      <PageHeader title="权限申请审核" subtitle="审核插件对系统表的访问权限申请">
        <template #actions>
          <n-space>
            <n-button @click="loadData" :loading="refreshLoading">
              <template #icon>
                <n-icon><component :is="Refresh" /></n-icon>
              </template>
              刷新
            </n-button>
          </n-space>
        </template>
      </PageHeader>
    </div>

    <!-- 内容区域 -->
    <div class="page-content">
      <!-- 过滤面板 -->
      <FilterPanel
        :filters="filters"
        :show-advanced="showAdvanced"
        @update:filters="Object.assign(filters, $event)"
        @update:show-advanced="showAdvanced = $event"
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
          :row-key="(row: PluginTablePermissionRequest) => row.id"
          striped
        />
        
        <!-- 独立分页器 -->
        <div class="pagination-container">
          <n-pagination
            v-model:page="pagination.page"
            v-model:page-size="pagination.pageSize"
            :item-count="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            show-size-picker
            show-quick-jumper
            @update:page="handlePageChange"
            @update:page-size="handlePageSizeChange"
          />
        </div>
      </div>
    </div>

    <!-- 批准对话框 -->
    <n-modal
      v-model:show="showApproveModal"
      preset="dialog"
      title="批准权限申请"
      positive-text="确认批准"
      negative-text="取消"
      @positive-click="handleApproveSubmit"
    >
      <n-form
        ref="approveFormRef"
        :model="approveForm"
        :rules="approveRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="审核意见" path="comment">
          <n-input
            v-model:value="approveForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见（可选）"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 拒绝对话框 -->
    <n-modal
      v-model:show="showRejectModal"
      preset="dialog"
      title="拒绝权限申请"
      positive-text="确认拒绝"
      negative-text="取消"
      @positive-click="handleRejectSubmit"
    >
      <n-form
        ref="rejectFormRef"
        :model="rejectForm"
        :rules="rejectRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="拒绝原因" path="reason">
          <n-input
            v-model:value="rejectForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入拒绝原因（必填）"
          />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, h, onMounted, resolveComponent } from 'vue'
import { NButton, NTag, NIcon, NSpace, NPopconfirm, useMessage } from 'naive-ui'
import { useIcon } from '@keqi.gress/plugin-bridge'
import { permissionRequestApi, type PluginTablePermissionRequest, type ApprovePermissionRequest, type RejectPermissionRequest } from '../api'
import type { FormInst, FormRules } from 'naive-ui'

// 图标
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const CloseCircle = useIcon('CloseCircleOutline')
const Refresh = useIcon('RefreshOutline')

// PageHeader 和 FilterPanel 是宿主机全局注册的组件，FilterPanel 无需导入

const message = useMessage()

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const tableData = ref<PluginTablePermissionRequest[]>([])
const currentRequest = ref<PluginTablePermissionRequest | null>(null)

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

// 过滤
const filters = reactive({
  pluginId: '',
  status: ''
})
const showAdvanced = ref(false)

// 批准表单
const showApproveModal = ref(false)
const approveFormRef = ref<FormInst | null>(null)
const approveForm = reactive({
  comment: ''
})
const approveRules: FormRules = {}

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

// 过滤字段配置
const basicFields = [
  {
    key: 'pluginId',
    label: '插件ID',
    type: 'input',
    placeholder: '请输入插件ID'
  },
  {
    key: 'status',
    label: '审核状态',
    type: 'select',
    placeholder: '请选择状态',
    options: [
      { label: '全部', value: '' },
      { label: '待审核', value: 'PENDING' },
      { label: '已批准', value: 'APPROVED' },
      { label: '已拒绝', value: 'REJECTED' },
      { label: '已取消', value: 'CANCELLED' }
    ]
  }
]

const advancedFields: any[] = []

// 表格列配置
const columns = [
  {
    title: '插件ID',
    key: 'pluginId',
    width: 150,
    ellipsis: { tooltip: true }
  },
  {
    title: '表名',
    key: 'tableName',
    width: 200,
    ellipsis: { tooltip: true }
  },
  {
    title: '申请操作',
    key: 'requestedOperations',
    width: 200,
    ellipsis: { tooltip: true }
  },
  {
    title: '只读',
    key: 'isReadonly',
    width: 80,
    render: (row: PluginTablePermissionRequest) => {
      return row.isReadonly ? '是' : '否'
    }
  },
  {
    title: '申请原因',
    key: 'reason',
    width: 200,
    ellipsis: { tooltip: true }
  },
  {
    title: '申请人',
    key: 'applicantName',
    width: 120
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: PluginTablePermissionRequest) => {
      const statusMap: Record<string, { label: string; type: 'default' | 'success' | 'error' | 'warning' }> = {
        PENDING: { label: '待审核', type: 'warning' },
        APPROVED: { label: '已批准', type: 'success' },
        REJECTED: { label: '已拒绝', type: 'error' },
        CANCELLED: { label: '已取消', type: 'default' }
      }
      const config = statusMap[row.status || 'PENDING']
      return h(NTag, { type: config.type, size: 'small' }, { default: () => config.label })
    }
  },
  {
    title: '审核人',
    key: 'reviewerName',
    width: 120
  },
  {
    title: '审核时间',
    key: 'reviewTime',
    width: 160,
    render: (row: PluginTablePermissionRequest) => row.reviewTime ? formatDateTime(row.reviewTime) : '-'
  },
  {
    title: '申请时间',
    key: 'createTime',
    width: 160,
    render: (row: PluginTablePermissionRequest) => formatDateTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right',
    render: (row: PluginTablePermissionRequest) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')
      const Eye = useIcon('EyeOutline')
      
      // 更多菜单选项
      const moreOptions = []
      
      if (row.status === 'PENDING') {
        moreOptions.push({
          label: '批准',
          key: 'approve',
          icon: () => h(NIcon, { component: CheckmarkCircle })
        })
        moreOptions.push({
          label: '拒绝',
          key: 'reject',
          icon: () => h(NIcon, { component: CloseCircle })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'approve':
            handleApprove(row)
            break
          case 'reject':
            handleReject(row)
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
                onClick: () => {} // TODO: 实现查看详情
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
const loadData = async () => {
  loading.value = true
  try {
    const response = await permissionRequestApi.getList({
      pluginId: filters.pluginId || undefined,
      status: filters.status || undefined
    })
    

      tableData.value = response
      pagination.total = response.length
 

  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  filters.pluginId = ''
  filters.status = ''
  pagination.page = 1
  loadData()
}

// 分页
const handlePageChange = (page: number) => {
  pagination.page = page
  loadData()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

// 批准
const handleApprove = (row: PluginTablePermissionRequest) => {
  currentRequest.value = row
  approveForm.comment = ''
  showApproveModal.value = true
}

const handleApproveSubmit = async () => {
  if (!currentRequest.value || !currentRequest.value.id) return
  

    const request: ApprovePermissionRequest = {
      comment: approveForm.comment
    }
    
    const response = await permissionRequestApi.approve(currentRequest.value.id, request)
    

      message.success('批准成功')
      showApproveModal.value = false
      loadData()


}

// 拒绝
const handleReject = (row: PluginTablePermissionRequest) => {
  currentRequest.value = row
  rejectForm.reason = ''
  showRejectModal.value = true
}

const handleRejectSubmit = async () => {
  if (!rejectFormRef.value) return
  
  await rejectFormRef.value.validate(async (errors) => {
    if (!errors && currentRequest.value && currentRequest.value.id) {
 
        const request: RejectPermissionRequest = {
          reason: rejectForm.reason
        }
        
        const response = await permissionRequestApi.reject(currentRequest.value.id, request)
        
      
          message.success('拒绝成功')
          showRejectModal.value = false
          loadData()

    }
  })
}

// 初始化
onMounted(() => {
  loadData()
})

// 格式化日期时间
const formatDateTime = (dateTime: string): string => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
</script>

<style scoped>
.permission-request-review-page {
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
