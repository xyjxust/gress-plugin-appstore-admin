<template>
  <div class="developer-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="开发者管理" subtitle="管理开发者账户，审核开发者资格申请">
        <template #actions>
          <n-button @click="loadDevelopers" :loading="refreshLoading">
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
          :data="developers"
          :loading="loading"
          :pagination="false"
          :row-key="(row: Developer) => row.id"
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

    <!-- Developer Detail Drawer -->
    <n-drawer
      v-model:show="showDetailDrawer"
      :width="800"
      placement="right"
    >
      <n-drawer-content :title="`开发者详情 - ${currentDeveloper?.username || ''}`">
        <div v-if="currentDeveloper" class="developer-detail">
          <!-- Basic Info -->
          <n-card title="基本信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="用户ID">
                {{ currentDeveloper.userId }}
              </n-descriptions-item>
              <n-descriptions-item label="用户名">
                {{ currentDeveloper.username }}
              </n-descriptions-item>
              <n-descriptions-item label="邮箱">
                {{ currentDeveloper.email }}
              </n-descriptions-item>
              <n-descriptions-item label="显示名称">
                {{ currentDeveloper.displayName || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="公司">
                {{ currentDeveloper.company || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="网站">
                <a v-if="currentDeveloper.website" :href="currentDeveloper.website" target="_blank">
                  {{ currentDeveloper.website }}
                </a>
                <span v-else>-</span>
              </n-descriptions-item>
              <n-descriptions-item label="状态" :span="2">
                <n-tag :type="getStatusType(currentDeveloper.status)">
                  {{ getStatusText(currentDeveloper.status) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="验证状态" :span="2">
                <n-tag :type="currentDeveloper.verified ? 'success' : 'default'">
                  {{ currentDeveloper.verified ? '已验证' : '未验证' }}
                </n-tag>
              </n-descriptions-item>
            </n-descriptions>

            <n-divider />

            <n-descriptions :column="1" label-placement="left">
              <n-descriptions-item label="个人简介">
                {{ currentDeveloper.bio || '-' }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- Statistics -->
          <n-card title="统计信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="插件数量">
                {{ currentDeveloper.pluginCount }}
              </n-descriptions-item>
              <n-descriptions-item label="总下载量">
                {{ currentDeveloper.totalDownloads }}
              </n-descriptions-item>
              <n-descriptions-item label="申请时间">
                {{ formatDateTime(currentDeveloper.applyTime) }}
              </n-descriptions-item>
              <n-descriptions-item label="审核时间">
                {{ currentDeveloper.reviewTime ? formatDateTime(currentDeveloper.reviewTime) : '-' }}
              </n-descriptions-item>
            </n-descriptions>

            <template v-if="currentDeveloper.reviewComment">
              <n-divider />
              <n-descriptions :column="1" label-placement="left">
                <n-descriptions-item label="审核意见">
                  {{ currentDeveloper.reviewComment }}
                </n-descriptions-item>
              </n-descriptions>
            </template>
          </n-card>

          <!-- Plugin List -->
          <n-card title="提交的插件" :bordered="false" class="detail-section">
            <n-list v-if="currentDeveloper.plugins && currentDeveloper.plugins.length > 0" bordered>
              <n-list-item v-for="plugin in currentDeveloper.plugins" :key="plugin.id">
                <n-thing>
                  <template #header>
                    {{ plugin.pluginName }}
                    <n-tag size="small" style="margin-left: 8px">{{ plugin.version }}</n-tag>
                  </template>
                  <template #description>
                    <n-space :size="8">
                      <n-tag size="small" :type="getSubmissionStatusType(plugin.status)">
                        {{ getSubmissionStatusText(plugin.status) }}
                      </n-tag>
                      <span class="text-secondary">{{ formatDateTime(plugin.submitTime) }}</span>
                    </n-space>
                  </template>
                  {{ plugin.description || '暂无描述' }}
                </n-thing>
              </n-list-item>
            </n-list>
            <n-empty v-else description="暂无插件" />
          </n-card>
        </div>

        <template #footer>
          <n-space justify="end">
            <n-button @click="showDetailDrawer = false">关闭</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, resolveComponent } from 'vue'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'
import { useDialog } from 'naive-ui'
import { developerApi } from '../api'

// 图标
const Refresh = useIcon('RefreshOutline')
const Eye = useIcon('EyeOutline')
const CheckmarkCircleOutline = useIcon('CheckmarkCircleOutline')
const BanOutline = useIcon('BanOutline')
const PlayCircleOutline = useIcon('PlayCircleOutline')
import type { Developer, DeveloperDetail, DeveloperStatus, SubmissionStatus } from '../types'

// Message and Dialog
const message = useMessage()
const dialog = useDialog()

// State
const loading = ref(false)
const refreshLoading = ref(false)
const developers = ref<Developer[]>([])
const showDetailDrawer = ref(false)
const currentDeveloper = ref<DeveloperDetail | null>(null)

// Filters
const showAdvanced = ref(false)
const filters = ref({
  keyword: '',
  status: null as DeveloperStatus | null,
  verified: null as boolean | null
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
  loadDevelopers()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadDevelopers()
}

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

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索用户名、邮箱',
    span: 12
  },
  {
    key: 'status',
    label: '状态',
    type: 'select',
    placeholder: '请选择状态',
    options: [
      { label: '全部', value: null },
      { label: '待审核', value: 'PENDING' },
      { label: '已激活', value: 'ACTIVE' },
      { label: '已暂停', value: 'SUSPENDED' }
    ],
    span: 6
  },
  {
    key: 'verified',
    label: '验证状态',
    type: 'select',
    placeholder: '请选择验证状态',
    options: [
      { label: '全部', value: null },
      { label: '已验证', value: true },
      { label: '未验证', value: false }
    ],
    span: 6
  }
]

// Table Columns
const columns: any[] = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '用户名',
    key: 'username',
    width: 150
  },
  {
    title: '邮箱',
    key: 'email',
    width: 200
  },
  {
    title: '显示名称',
    key: 'displayName',
    width: 150,
    render: (row: Developer) => row.displayName || '-'
  },
  {
    title: '公司',
    key: 'company',
    width: 150,
    render: (row: Developer) => row.company || '-'
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: Developer) => {
      const NTag = resolveComponent('NTag') as any
      return h(
        NTag,
        { type: getStatusType(row.status) },
        { default: () => getStatusText(row.status) }
      )
    }
  },
  {
    title: '验证',
    key: 'verified',
    width: 80,
    render: (row: Developer) => {
      const NTag = resolveComponent('NTag') as any
      return h(
        NTag,
        { type: row.verified ? 'success' : 'default', size: 'small' },
        { default: () => row.verified ? '已验证' : '未验证' }
      )
    }
  },
  {
    title: '插件数',
    key: 'pluginCount',
    width: 80
  },
  {
    title: '下载量',
    key: 'totalDownloads',
    width: 100
  },
  {
    title: '申请时间',
    key: 'applyTime',
    width: 180,
    render: (row: Developer) => formatDateTime(row.applyTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row: Developer) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions: Array<{ label: string; key: string; icon: () => any }> = []
      
      if (row.status === 'PENDING') {
        moreOptions.push({
          label: '批准',
          key: 'approve',
          icon: () => h(NIcon, { component: CheckmarkCircleOutline })
        })
      }
      
      if (row.status === 'ACTIVE') {
        moreOptions.push({
          label: '暂停',
          key: 'suspend',
          icon: () => h(NIcon, { component: BanOutline })
        })
      }
      
      if (row.status === 'SUSPENDED') {
        moreOptions.push({
          label: '激活',
          key: 'activate',
          icon: () => h(NIcon, { component: PlayCircleOutline })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'approve':
            handleApprove(row)
            break
          case 'suspend':
            handleSuspend(row)
            break
          case 'activate':
            handleActivate(row)
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
const loadDevelopers = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const params: any = {
      page: pagination.page,
      size: pagination.pageSize
    }

    if (filters.value.keyword) {
      params.keyword = filters.value.keyword
    }
    if (filters.value.status) {
      params.status = filters.value.status
    }
    if (filters.value.verified !== null) {
      params.verified = filters.value.verified
    }

    const response = await developerApi.getList(params)
    

      developers.value = response.items
      pagination.itemCount = response.total


  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadDevelopers()
}

const handleReset = () => {
  filters.value.keyword = ''
  filters.value.status = null
  filters.value.verified = null
  pagination.page = 1
  loadDevelopers()
}

const handleViewDetail = async (id: number) => {

    const response = await developerApi.getDetail(id)

      currentDeveloper.value = response
      showDetailDrawer.value = true


}

const handleApprove = (developer: Developer) => {
  dialog.warning({
    title: '批准开发者资格',
    content: `确定要批准开发者 "${developer.username}" 的资格申请吗？`,
    positiveText: '批准',
    negativeText: '取消',
    onPositiveClick: async () => {
    
        const response = await developerApi.approve(developer.id, {
          comment: '资格审核通过'
        })

     
          message.success('批准成功')
          loadDevelopers()

    }
  })
}

const handleSuspend = (developer: Developer) => {
  dialog.warning({
    title: '暂停开发者账户',
    content: `确定要暂停开发者 "${developer.username}" 的账户吗？\n\n暂停后该开发者将无法提交新插件`,
    positiveText: '暂停',
    negativeText: '取消',
    onPositiveClick: async () => {
  
        const response = await developerApi.suspend(developer.id, {
          reason: '违反平台规则'
        })

          message.success('暂停成功')
          loadDevelopers()
   
    }
  })
}

const handleActivate = (developer: Developer) => {
  dialog.info({
    title: '激活开发者账户',
    content: `确定要激活开发者 "${developer.username}" 的账户吗？`,
    positiveText: '激活',
    negativeText: '取消',
    onPositiveClick: async () => {
    
        const response = await developerApi.activate(developer.id, {
          comment: '账户已恢复正常'
        })


          message.success('激活成功')
          loadDevelopers()
  
    }
  })
}

// Helper Functions
const getStatusType = (status: DeveloperStatus): 'default' | 'success' | 'warning' | 'error' => {
  const typeMap: Record<DeveloperStatus, 'default' | 'success' | 'warning' | 'error'> = {
    PENDING: 'warning',
    ACTIVE: 'success',
    SUSPENDED: 'error'
  }
  return typeMap[status] || 'default'
}

const getStatusText = (status: DeveloperStatus): string => {
  const textMap: Record<DeveloperStatus, string> = {
    PENDING: '待审核',
    ACTIVE: '已激活',
    SUSPENDED: '已暂停'
  }
  return textMap[status] || status
}

const getSubmissionStatusType = (status: SubmissionStatus): 'default' | 'success' | 'warning' | 'error' => {
  const typeMap: Record<SubmissionStatus, 'default' | 'success' | 'warning' | 'error'> = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'error'
  }
  return typeMap[status] || 'default'
}

const getSubmissionStatusText = (status: SubmissionStatus): string => {
  const textMap: Record<SubmissionStatus, string> = {
    PENDING: '待审核',
    APPROVED: '已批准',
    REJECTED: '已拒绝'
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
  loadDevelopers()
})
</script>

<style scoped>
.developer-management-page {
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

.developer-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-section {
  margin-bottom: 16px;
}

.text-secondary {
  color: #999;
  font-size: 12px;
}
</style>
