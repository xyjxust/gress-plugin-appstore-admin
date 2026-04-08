<template>
  <div class="plugin-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="插件列表管理" subtitle="管理已上架的插件">
        <template #actions>
          <n-space>
            <n-button type="primary" @click="showUploadModal = true">
              <template #icon>
                <n-icon><component :is="CloudUploadOutline" /></n-icon>
              </template>
              上传新插件
            </n-button>
            <n-button type="success" @click="handleBatchRelist" :disabled="!hasDelistedSelection" :loading="batchRelistLoading">
              <template #icon>
                <n-icon><component :is="CheckmarkCircle" /></n-icon>
              </template>
              批量上架
            </n-button>
            <n-button type="error" @click="handleBatchDelist" :disabled="!hasOnlineSelection" :loading="batchDelistLoading">
              <template #icon>
                <n-icon><component :is="BanOutline" /></n-icon>
              </template>
              批量下架
            </n-button>
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
          :row-key="(row: Plugin) => row.id"
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
        <PluginDetail
          v-if="currentPlugin"
          :plugin="currentPlugin"
          @delist="handleDelist"
          @relist="handleRelist"
          @edit="handleEdit"
          @delete="(id: string) => confirmDeletePlugin(id)"
          @close="showDetailDrawer = false"
        />
      </n-drawer-content>
    </n-drawer>

    <!-- 下架对话框 -->
    <n-modal
      v-model:show="showDelistModal"
      preset="dialog"
      title="下架插件"
      positive-text="确认下架"
      negative-text="取消"
      @positive-click="confirmDelist"
    >
      <n-form ref="delistFormRef" :model="delistForm" :rules="delistRules">
        <n-form-item label="下架原因" path="reason">
          <n-input
            v-model:value="delistForm.reason"
            type="textarea"
            placeholder="请输入下架原因（必填）"
            :rows="4"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 编辑对话框 -->
    <n-modal
      v-model:show="showEditModal"
      preset="dialog"
      title="编辑插件信息"
      positive-text="保存"
      negative-text="取消"
      @positive-click="confirmEdit"
    >
      <n-form ref="editFormRef" :model="editForm" :rules="editRules">
        <n-form-item label="插件名称" path="pluginName">
          <n-input
            v-model:value="editForm.pluginName"
            placeholder="请输入插件名称"
          />
        </n-form-item>
        <n-form-item label="插件描述" path="description">
          <n-input
            v-model:value="editForm.description"
            type="textarea"
            placeholder="请输入插件描述"
            :rows="4"
          />
        </n-form-item>
        <n-form-item label="分类" path="category">
          <n-input
            v-model:value="editForm.category"
            placeholder="请输入分类"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 上传插件对话框 -->
    <n-modal
      v-model:show="showUploadModal"
      preset="dialog"
      title="上传新插件"
      positive-text="开始上传"
      negative-text="取消"
      :positive-button-props="{ disabled: !uploadForm.file }"
      @positive-click="confirmUpload"
    >
      <n-form ref="uploadFormRef" :model="uploadForm" :rules="uploadRules">
        <n-form-item label="插件包文件" path="file">
          <n-upload
            :max="1"
            :default-upload="false"
            accept=".jar"
            @change="handleFileChange"
            :file-list="fileList"
          >
            <n-upload-dragger>
              <div style="margin-bottom: 12px">
                <n-icon size="48" :depth="3">
                  <component :is="CloudUploadOutline" />
                </n-icon>
              </div>
              <n-text style="font-size: 16px">
                点击或拖拽文件到此区域上传
              </n-text>
              <n-p depth="3" style="margin: 8px 0 0 0">
                仅支持 .jar 格式的插件包文件
              </n-p>
            </n-upload-dragger>
          </n-upload>
        </n-form-item>

        <n-form-item label="插件类型" path="pluginType">
          <n-select
            v-model:value="uploadForm.pluginType"
            placeholder="请选择插件类型"
            :options="pluginTypeOptions"
          />
        </n-form-item>

        <n-form-item label="插件描述" path="description">
          <n-input
            v-model:value="uploadForm.description"
            type="textarea"
            placeholder="请输入插件描述（可选）"
            :rows="3"
          />
        </n-form-item>

        <n-form-item label="是否立即上架" path="autoList">
          <n-switch v-model:value="uploadForm.autoList" />
          <n-text depth="3" style="margin-left: 12px; font-size: 14px">
            开启后插件将自动上架到应用商店
          </n-text>
        </n-form-item>
      </n-form>

      <!-- 上传进度 -->
      <n-progress
        v-if="uploading"
        type="line"
        :percentage="uploadProgress"
        :indicator-placement="'inside'"
        processing
      />
    </n-modal>

    <!-- 升级插件对话框 -->
    <n-modal
      v-model:show="showUpgradeModal"
      preset="dialog"
      title="升级插件版本"
      positive-text="确认升级"
      negative-text="取消"
      @positive-click="confirmUpgrade"
    >
      <n-alert type="info" style="margin-bottom: 16px">
        <template #header>升级插件</template>
        正在为插件 <strong>{{ upgradeTargetPlugin?.pluginName }}</strong> ({{ upgradeTargetPlugin?.pluginId }}) 升级新版本
        <br />
        当前版本：<strong>{{ upgradeTargetPlugin?.currentVersion }}</strong>
        <span v-if="upgradeForm.parsedVersion">
          <br />
          检测到的新版本：<strong style="color: #18a058">{{ upgradeForm.parsedVersion }}</strong>
        </span>
      </n-alert>

      <n-form ref="upgradeFormRef" :model="upgradeForm" :rules="upgradeRules">
        <n-form-item label="插件包文件" path="file">
          <n-upload
            :max="1"
            :default-upload="false"
            accept=".jar"
            @change="handleUpgradeFileChange"
            :file-list="upgradeFileList"
          >
            <n-upload-dragger>
              <div style="margin-bottom: 12px">
                <n-icon size="48" :depth="3">
                  <component :is="CloudUploadOutline" />
                </n-icon>
              </div>
              <n-text style="font-size: 16px">
                点击或拖拽文件到此区域上传
              </n-text>
              <n-p depth="3" style="margin: 8px 0 0 0">
                仅支持 .jar 格式的插件包文件，版本号将自动从包中解析
              </n-p>
            </n-upload-dragger>
          </n-upload>
        </n-form-item>

        <n-form-item label="更新说明" path="updateNotes">
          <n-input
            v-model:value="upgradeForm.updateNotes"
            type="textarea"
            placeholder="请输入本次更新的内容说明"
            :rows="4"
          />
        </n-form-item>

        <n-form-item label="是否立即上架" path="autoList">
          <n-switch v-model:value="upgradeForm.autoList" />
          <n-text depth="3" style="margin-left: 12px; font-size: 14px">
            开启后新版本将自动上架到应用商店
          </n-text>
        </n-form-item>
      </n-form>

      <!-- 上传进度 -->
      <n-progress
        v-if="upgrading"
        type="line"
        :percentage="upgradeProgress"
        :indicator-placement="'inside'"
        processing
      />
    </n-modal>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, h, onMounted, resolveComponent } from 'vue'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'
import { useDialog } from 'naive-ui'
import { NUploadDragger } from 'naive-ui'

// 图标
const BanOutline = useIcon('BanOutline')
const Refresh = useIcon('RefreshOutline')
const Eye = useIcon('EyeOutline')
const CreateOutline = useIcon('CreateOutline')
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const CloudUploadOutline = useIcon('CloudUploadOutline')
const CloudUpload = useIcon('CloudUpload')
const TrashOutline = useIcon('TrashOutline')
import PluginDetail from '../components/PluginDetail.vue'
import { pluginApi, type PluginTypeInfo } from '../api'
import type { Plugin, PluginStatus, PluginType } from '../types'

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
const dialog = useDialog()

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const batchRelistLoading = ref(false)
const batchDelistLoading = ref(false)
const tableData = ref<Plugin[]>([])
const checkedRowKeys = ref<number[]>([])
const showDetailDrawer = ref(false)
const currentPlugin = ref<Plugin | null>(null)

// 过滤器状态
const showAdvanced = ref(false)
const filters = ref({
  status: '' as string,
  pluginType: '' as string,
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

// 下架表单
const showDelistModal = ref(false)
const delistFormRef = ref<any>(null)
const delistForm = reactive({
  reason: ''
})
const delistRules = {
  reason: [
    { required: true, message: '请输入下架原因', trigger: 'blur' }
  ]
}
const delistTargetIds = ref<string[]>([])

// 编辑表单
const showEditModal = ref(false)
const editFormRef = ref<any>(null)
const editForm = reactive({
  pluginId: '',
  pluginName: '',
  description: '',
  category: ''
})
const editRules = {
  pluginName: [
    { required: true, message: '请输入插件名称', trigger: 'blur' }
  ]
}

// 上传表单
const showUploadModal = ref(false)
const uploadFormRef = ref<any>(null)
const uploading = ref(false)
const uploadProgress = ref(0)
const fileList = ref<any[]>([])
const uploadForm = reactive({
  file: null as File | null,
  pluginType: null as PluginType | null,
  description: '',
  autoList: false
})
const uploadRules = {
  file: [
    { 
      required: true, 
      message: '请选择插件包文件',
      validator: (rule: any, value: any) => {
        return !!uploadForm.file
      },
      trigger: ['change', 'blur']
    }
  ],
  pluginType: [
    { required: true, message: '请选择插件类型', trigger: 'change' }
  ]
}

// 升级表单
const showUpgradeModal = ref(false)
const upgradeFormRef = ref<any>(null)
const upgrading = ref(false)
const upgradeProgress = ref(0)
const upgradeFileList = ref<any[]>([])
const upgradeTargetPlugin = ref<Plugin | null>(null)
const upgradeForm = reactive({
  parsedVersion: '', // 从 JAR 包解析的版本号
  file: null as File | null,
  updateNotes: '',
  autoList: false
})
const upgradeRules = {
  file: [
    { 
      required: true, 
      message: '请选择插件包文件',
      validator: (rule: any, value: any) => {
        return !!upgradeForm.file
      },
      trigger: ['change', 'blur']
    }
  ],
  updateNotes: [
    { required: true, message: '请输入更新说明', trigger: 'blur' }
  ]
}

// 插件类型选项（从后台获取）
const pluginTypeOptions = ref<Array<{ label: string; value: string }>>([])
const pluginTypeMap = ref<Record<string, PluginTypeInfo>>({})

// 加载插件类型列表
const loadPluginTypes = async () => {

    const response = await pluginApi.getTypes()

      pluginTypeOptions.value = [
        { label: '全部', value: '' },
        ...response.map(type => ({
          label: type.label,
          value: type.code
        }))
      ]
      
      // 构建类型映射
      const map: Record<string, PluginTypeInfo> = {}
      response.forEach(type => {
        map[type.code] = type
      })
      pluginTypeMap.value = map

}

// 计算属性
const hasSelection = computed(() => checkedRowKeys.value.length > 0)
const hasOnlineSelection = computed(() => {
  const selectedPlugins = tableData.value.filter(p => checkedRowKeys.value.includes(p.id))
  return selectedPlugins.some(p => p.status === 'ONLINE')
})
const hasDelistedSelection = computed(() => {
  const selectedPlugins = tableData.value.filter(p => checkedRowKeys.value.includes(p.id))
  return selectedPlugins.some(p => p.status === 'DELISTED' || p.status === 'OFFLINE')
})

// 过滤字段配置
const basicFields = computed<FilterFieldConfig[]>(() => [
  {
    key: 'status',
    label: '插件状态',
    type: 'select',
    placeholder: '请选择状态',
    clearable: true,
    options: [
      { label: '全部', value: '' },
      { label: '上架', value: 'ONLINE' },
      { label: '下架', value: 'OFFLINE' },
      { label: '已下架', value: 'DELISTED' }
    ]
  },
  {
    key: 'pluginType',
    label: '插件类型',
    type: 'select',
    placeholder: '请选择类型',
    clearable: true,
    options: pluginTypeOptions.value
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索插件名称、开发者'
  }
])

const advancedFields: FilterFieldConfig[] = [
  {
    key: 'timeRange',
    label: '上架时间',
    type: 'date-range',
    placeholder: '选择时间范围',
    span: 2
  }
]

// 表格列配置
const columns = [
  {
    type: 'selection'
  },
  {
    title: '插件名称',
    key: 'pluginName',
    width: 180,
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
    title: '当前版本',
    key: 'currentVersion',
    width: 100
  },
  {
    title: '类型',
    key: 'pluginType',
    width: 100,
    render: (row: Plugin) => {
      const NTag = resolveComponent('NTag') as any
      const typeInfo = pluginTypeMap.value[row.pluginType]
      if (typeInfo) {
        return h(NTag, { 
          type: (typeInfo.tagType || 'info') as 'info' | 'success' | 'warning' | 'error', 
          size: 'small' 
        }, { default: () => typeInfo.label })
      }
      // 回退到默认映射
      const defaultMap: Record<PluginType, { label: string; type: 'info' | 'success' | 'warning' | 'error' }> = {
        TASK: { label: '任务节点', type: 'info' },
        TRIGGER: { label: '触发器', type: 'success' },
        APPLICATION: { label: '应用插件', type: 'warning' },
        MIDDLEWARE: { label: '中间件', type: 'error' }
      }
      const config = defaultMap[row.pluginType] || { label: row.pluginType, type: 'info' }
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
    render: (row: Plugin) => {
      const NTag = resolveComponent('NTag') as any
      const statusMap: Record<PluginStatus, { label: string; type: 'success' | 'warning' | 'error' }> = {
        ONLINE: { label: '上架', type: 'success' },
        OFFLINE: { label: '下架', type: 'warning' },
        DELISTED: { label: '已下架', type: 'error' }
      }
      const config = statusMap[row.status]
      return h(NTag, { type: config.type, size: 'small' }, { default: () => config.label })
    }
  },
  {
    title: '安装次数',
    key: 'installCount',
    width: 100
  },
  {
    title: '活跃用户',
    key: 'activeUsers',
    width: 100
  },
  {
    title: '上架时间',
    key: 'listingTime',
    width: 160,
    render: (row: Plugin) => formatDateTime(row.listingTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 220,
    fixed: 'right' as const,
    render: (row: Plugin) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions = [
        {
          label: '升级版本',
          key: 'upgrade',
          icon: () => h(NIcon, { component: CloudUpload })
        }
      ]

      if (row.status === 'ONLINE') {
        moreOptions.push({
          label: '下架',
          key: 'delist',
          icon: () => h(NIcon, { component: BanOutline })
        })
      } else if (row.status === 'DELISTED') {
        moreOptions.push({
          label: '重新上架',
          key: 'relist',
          icon: () => h(NIcon, { component: CheckmarkCircle })
        })
      }

      if (row.status === 'DELISTED' || row.status === 'OFFLINE') {
        moreOptions.push({
          label: '删除',
          key: 'delete',
          icon: () => h(NIcon, { component: TrashOutline })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'upgrade':
            handleUpgradeVersion(row.pluginId)
            break
          case 'delist':
            handleDelist(row.pluginId)
            break
          case 'relist':
            handleRelist(row.pluginId)
            break
          case 'delete':
            confirmDeletePlugin(row.pluginId, row.pluginName)
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
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                onClick: () => handleEdit(row.pluginId)
              },
              {
                icon: () => h(NIcon, { component: CreateOutline }),
                default: () => '编辑'
              }
            ),
            h(
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

    const response = await pluginApi.getList(params)
     tableData.value = response.items || []
          pagination.itemCount = response.total || 0

  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

// 查看详情
function handleViewDetail(plugin: Plugin) {
  currentPlugin.value = plugin
  showDetailDrawer.value = true
}

// 下架插件
function handleDelist(pluginId: string | string[]) {
  delistTargetIds.value = Array.isArray(pluginId) ? pluginId : [pluginId]
  delistForm.reason = ''
  showDelistModal.value = true
}

// 确认下架
async function confirmDelist() {
  await delistFormRef.value?.validate()
  
 
    let successCount = 0
    let failedCount = 0
    const errors: string[] = []
    
    for (const pluginId of delistTargetIds.value) {
      try {
        await pluginApi.delist(pluginId, { reason: delistForm.reason })
   
          successCount++

      } catch (error: any) {
        failedCount++
        const errorMsg = error.response?.data?.errorMessage || error.message || '未知错误'
        errors.push(`${pluginId}: ${errorMsg}`)
      }
    }
    
    if (successCount > 0) {
      message.success(`成功下架 ${successCount} 个插件`)
    }
    
    if (failedCount > 0) {
      message.error(`${failedCount} 个插件下架失败：\n${errors.join('\n')}`)
    }
    
    showDelistModal.value = false
    checkedRowKeys.value = []
    await loadData()

}

// 重新上架
async function handleRelist(pluginId: string) {

    await pluginApi.relist(pluginId)
    message.success('插件已重新上架')
    await loadData()

}

/** 永久删除（已下架 / 离线）：后端会清理应用信息、版本与存储中的 JAR */
function confirmDeletePlugin(pluginId: string, pluginName?: string) {
  dialog.warning({
    title: '永久删除插件',
    content: `将删除插件「${pluginName || pluginId}」的应用信息、全部版本记录，并移除已存储的插件包（JAR），不可恢复。确定继续？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await pluginApi.delete(pluginId)
        message.success('插件已删除')
        showDetailDrawer.value = false
        currentPlugin.value = null
        checkedRowKeys.value = []
        await loadData()
        return true
      } catch (error: any) {
        const msg =
          error.response?.data?.errorMessage ||
          error.response?.data?.message ||
          error.message ||
          '删除失败'
        message.error(msg)
        return false
      }
    }
  })
}

// 编辑插件
function handleEdit(pluginId: string) {
  const plugin = tableData.value.find(p => p.pluginId === pluginId)
  if (plugin) {
    editForm.pluginId = plugin.pluginId
    editForm.pluginName = plugin.pluginName
    editForm.description = plugin.description || ''
    editForm.category = plugin.category || ''
    showEditModal.value = true
  }
}

// 确认编辑
async function confirmEdit() {
  await editFormRef.value?.validate()
  

    await pluginApi.update(editForm.pluginId, {
      pluginName: editForm.pluginName,
      description: editForm.description,
      category: editForm.category
    })
    
    message.success('插件信息已更新')
    showEditModal.value = false
    await loadData()

}

// 批量下架
async function handleBatchDelist() {
  const selectedPlugins = tableData.value.filter(p => checkedRowKeys.value.includes(p.id))
  const onlinePlugins = selectedPlugins.filter(p => p.status === 'ONLINE')
  if (onlinePlugins.length === 0) {
    message.warning('请选择在线状态的插件')
    return
  }
  batchDelistLoading.value = true
  try {
    const pluginIds = onlinePlugins.map(p => p.pluginId)
    handleDelist(pluginIds)
  } finally {
    batchDelistLoading.value = false
  }
}

// 批量上架
async function handleBatchRelist() {
  const selectedPlugins = tableData.value.filter(p => checkedRowKeys.value.includes(p.id))
  const delistedPlugins = selectedPlugins.filter(p => p.status === 'DELISTED' || p.status === 'OFFLINE')
  
  if (delistedPlugins.length === 0) {
    message.warning('请选择已下架状态的插件')
    return
  }

  batchRelistLoading.value = true
  try {
    let successCount = 0
    let failedCount = 0
    const errors: string[] = []
    
    for (const plugin of delistedPlugins) {
      try {
        const response = await pluginApi.relist(plugin.pluginId)
       
          successCount++
   
      } catch (error: any) {
        failedCount++
        const errorMsg = error.response?.data?.errorMessage || error.message || '未知错误'
        errors.push(`${plugin.pluginName}: ${errorMsg}`)
      }
    }
    
    if (successCount > 0) {
      message.success(`成功上架 ${successCount} 个插件`)
    }
    
    if (failedCount > 0) {
      message.error(`${failedCount} 个插件上架失败：\n${errors.join('\n')}`)
    }
    
    checkedRowKeys.value = []
    await loadData()

  } finally {
    batchRelistLoading.value = false
  }
}

// 升级版本
function handleUpgradeVersion(pluginId: string) {
  const plugin = tableData.value.find(p => p.pluginId === pluginId)
  if (plugin) {
    upgradeTargetPlugin.value = plugin
    upgradeForm.parsedVersion = ''
    upgradeForm.file = null
    upgradeForm.updateNotes = ''
    upgradeForm.autoList = false
    upgradeFileList.value = []
    showUpgradeModal.value = true
  }
}

// 处理升级文件选择
function handleUpgradeFileChange(options: { fileList: any[] }) {
  upgradeFileList.value = options.fileList
  if (options.fileList.length > 0) {
    const file = options.fileList[0].file
    // 验证文件格式
    if (!file.name.endsWith('.jar')) {
      message.error('只能上传 .jar 格式的插件包文件')
      upgradeFileList.value = []
      upgradeForm.file = null
      upgradeForm.parsedVersion = ''
      return
    }
    upgradeForm.file = file
    upgradeForm.parsedVersion = '' // 清空之前解析的版本号
    
    // 手动触发表单验证，清除错误提示
    upgradeFormRef.value?.validate(
      () => {
        // 验证完成
      },
      (rule: any) => {
        // 只验证 file 字段
        return rule?.key === 'file'
      }
    )
  } else {
    upgradeForm.file = null
    upgradeForm.parsedVersion = ''
  }
}

// 确认升级
async function confirmUpgrade() {
  await upgradeFormRef.value?.validate()
  
  if (!upgradeForm.file || !upgradeTargetPlugin.value) {
    message.error('请选择插件包文件')
    return false
  }

  upgrading.value = true
  upgradeProgress.value = 0

  try {
    const formData = new FormData()
    formData.append('file', upgradeForm.file)
    formData.append('pluginId', upgradeTargetPlugin.value.pluginId)
    // 不再传递 version，由后端从 JAR 包解析
    formData.append('updateNotes', upgradeForm.updateNotes)
    formData.append('autoList', String(upgradeForm.autoList))

    // 模拟上传进度
    const progressInterval = setInterval(() => {
      if (upgradeProgress.value < 90) {
        upgradeProgress.value += 10
      }
    }, 200)

    const response = await pluginApi.upgrade(formData, (progress: number) => {
      upgradeProgress.value = Math.min(progress, 95)
    })

    clearInterval(progressInterval)
    upgradeProgress.value = 100

  
      message.success('插件升级成功！')
      showUpgradeModal.value = false
      resetUpgradeForm()
      await loadData()

  } catch (error: any) {

    return false
  } finally {
    upgrading.value = false
    upgradeProgress.value = 0
  }
}

// 重置升级表单
function resetUpgradeForm() {
  upgradeForm.parsedVersion = ''
  upgradeForm.file = null
  upgradeForm.updateNotes = ''
  upgradeForm.autoList = false
  upgradeFileList.value = []
  upgradeProgress.value = 0
  upgradeTargetPlugin.value = null
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  filters.value.status = ''
  filters.value.pluginType = ''
  filters.value.keyword = ''
  filters.value.startTime = null
  filters.value.endTime = null
  pagination.page = 1
  checkedRowKeys.value = []
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

// 文件选择处理
function handleFileChange(options: { fileList: any[] }) {
  fileList.value = options.fileList
  if (options.fileList.length > 0) {
    const file = options.fileList[0].file
    // 验证文件格式
    if (!file.name.endsWith('.jar')) {
      message.error('只能上传 .jar 格式的插件包文件')
      fileList.value = []
      uploadForm.file = null
      return
    }
    uploadForm.file = file
    // 手动触发表单验证，清除错误提示
    uploadFormRef.value?.validate(
      () => {
        // 验证完成
      },
      (rule: any) => {
        // 只验证 file 字段
        return rule?.key === 'file'
      }
    )
  } else {
    uploadForm.file = null
  }
}

// 确认上传
async function confirmUpload() {
  await uploadFormRef.value?.validate()
  
  if (!uploadForm.file) {
    message.error('请选择插件包文件')
    return false
  }

  uploading.value = true
  uploadProgress.value = 0

  try {
    const formData = new FormData()
    formData.append('file', uploadForm.file)
    formData.append('pluginType', uploadForm.pluginType || '')
    formData.append('description', uploadForm.description)
    formData.append('autoList', String(uploadForm.autoList))

    // 模拟上传进度
    const progressInterval = setInterval(() => {
      if (uploadProgress.value < 90) {
        uploadProgress.value += 10
      }
    }, 200)

    const response = await pluginApi.upload(formData, (progress: number) => {
      uploadProgress.value = Math.min(progress, 95)
    })

    clearInterval(progressInterval)
    uploadProgress.value = 100


      message.success('插件上传成功！')
      showUploadModal.value = false
      resetUploadForm()
      await loadData()

  } catch (error: any) {

    return false
  } finally {
    uploading.value = false
    uploadProgress.value = 0
  }
}

// 重置上传表单
function resetUploadForm() {
  uploadForm.file = null
  uploadForm.pluginType = null
  uploadForm.description = ''
  uploadForm.autoList = false
  fileList.value = []
  uploadProgress.value = 0
}

// 初始化
onMounted(() => {
  loadPluginTypes()
  loadData()
})
</script>

<style scoped>
.plugin-management-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f5f5;
}

.page-header-wrapper {
  /* 页面头部撑满宽度，无 padding */
  background: white;
  border-bottom: 1px solid #e8e8e8;
}

.page-content {
  /* 内容区域有 padding */
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
