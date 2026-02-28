<template>
  <div class="plugin-table-permission-management-page">
    <!-- 页面头部 -->
    <div class="page-header-wrapper">
      <PageHeader title="插件表权限管理" subtitle="配置插件对系统表的特殊访问权限">
        <template #actions>
          <n-space>
            <n-button type="primary" @click="handleCreate">
              <template #icon>
                <n-icon><component :is="Add" /></n-icon>
              </template>
              新增权限
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

    <!-- 内容区域 -->
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
          :row-key="(row: PluginTablePermission) => row.id"
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

    <!-- 新增/编辑对话框 -->
    <n-modal
      v-model:show="showFormModal"
      :title="formMode === 'create' ? '新增权限配置' : '编辑权限配置'"
      preset="dialog"
      style="width: 600px"
      :positive-button-props="{ loading: submitting }"
      @positive-click="handleFormSubmit"
      @negative-click="showFormModal = false"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-placement="left"
        label-width="120px"
      >
        <n-form-item label="插件ID" path="pluginId">
          <n-input 
            v-model:value="formData.pluginId" 
            placeholder="请输入插件ID"
            :disabled="formMode === 'edit'"
          />
        </n-form-item>
        
        <n-form-item label="表名" path="tableName">
          <n-input 
            v-model:value="formData.tableName" 
            placeholder="请输入表名（小写）"
          />
        </n-form-item>
        
        <n-form-item label="允许的操作" path="allowedOperations">
          <n-select
            v-model:value="selectedOperations"
            multiple
            placeholder="选择允许的操作（不选则允许全部）"
            :options="operationOptions"
          />
        </n-form-item>
        
        <n-form-item label="只读模式" path="isReadonly">
          <n-switch v-model:value="formData.isReadonly" />
          <span style="margin-left: 8px; color: #666">
            开启后只允许 SELECT 操作
          </span>
        </n-form-item>
        
        <n-form-item label="描述" path="description">
          <n-input
            v-model:value="formData.description"
            type="textarea"
            placeholder="请输入描述"
            :rows="3"
          />
        </n-form-item>
        
        <n-form-item label="启用" path="enabled">
          <n-switch v-model:value="formData.enabled" />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, h, onMounted, watch, resolveComponent } from 'vue'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'
import { tablePermissionApi, type PluginTablePermission } from '../api'
import type { FormInst, FormRules } from 'naive-ui'

// 图标
const Add = useIcon('AddOutline')
const Refresh = useIcon('RefreshOutline')
const Edit = useIcon('CreateOutline')
const Delete = useIcon('TrashOutline')
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const CloseCircle = useIcon('CloseCircleOutline')

const message = useMessage()

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const submitting = ref(false)
const tableData = ref<PluginTablePermission[]>([])
const showFormModal = ref(false)
const formMode = ref<'create' | 'edit'>('create')
const formRef = ref<FormInst | null>(null)

// 表单数据
const formData = ref<Partial<PluginTablePermission>>({
  enabled: true,
  isReadonly: true
})
const selectedOperations = ref<string[]>([])

// 操作选项
const operationOptions = [
  { label: 'SELECT', value: 'SELECT' },
  { label: 'INSERT', value: 'INSERT' },
  { label: 'UPDATE', value: 'UPDATE' },
  { label: 'DELETE', value: 'DELETE' }
]

// 表单验证规则
const formRules: FormRules = {
  pluginId: {
    required: true,
    message: '请输入插件ID',
    trigger: 'blur'
  },
  tableName: {
    required: true,
    message: '请输入表名',
    trigger: 'blur'
  }
}

// 筛选条件
const showAdvanced = ref(false)
const filters = ref({
  pluginId: '',
  tableName: '',
  enabled: null as boolean | null
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  pageSizes: [10, 20, 50, 100]
})

// 筛选字段配置
const basicFields = [
  {
    key: 'pluginId',
    label: '插件ID',
    type: 'input' as const,
    placeholder: '请输入插件ID'
  },
  {
    key: 'tableName',
    label: '表名',
    type: 'input' as const,
    placeholder: '请输入表名'
  }
]

const advancedFields = [
  {
    key: 'enabled',
    label: '状态',
    type: 'select' as const,
    placeholder: '全部',
    options: [
      { label: '启用', value: true },
      { label: '禁用', value: false }
    ]
  }
]

// 表格列
const columns = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '插件ID',
    key: 'pluginId',
    width: 200
  },
  {
    title: '表名',
    key: 'tableName',
    width: 200
  },
  {
    title: '允许的操作',
    key: 'allowedOperations',
    width: 200,
    render: (row: PluginTablePermission) => {
      return row.allowedOperations || '全部'
    }
  },
  {
    title: '只读',
    key: 'isReadonly',
    width: 100,
    render: (row: PluginTablePermission) => {
      return h('n-tag', {
        type: row.isReadonly ? 'warning' : 'success',
        size: 'small'
      }, { default: () => row.isReadonly ? '是' : '否' })
    }
  },
  {
    title: '状态',
    key: 'enabled',
    width: 100,
    render: (row: PluginTablePermission) => {
      return h('n-tag', {
        type: row.enabled ? 'success' : 'default',
        size: 'small'
      }, { default: () => row.enabled ? '启用' : '禁用' })
    }
  },
  {
    title: '描述',
    key: 'description',
    ellipsis: true
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right',
    render: (row: PluginTablePermission) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const NPopconfirm = resolveComponent('NPopconfirm') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')
      const Eye = useIcon('EyeOutline')

      // 更多菜单选项
      const moreOptions = [
        {
          label: '编辑',
          key: 'edit',
          icon: () => h(NIcon, { component: Edit })
        },
        {
          label: row.enabled ? '禁用' : '启用',
          key: 'toggle',
          icon: () => h(NIcon, { component: row.enabled ? CloseCircle : CheckmarkCircle })
        },
        {
          label: '删除',
          key: 'delete',
          icon: () => h(NIcon, { component: Delete })
        }
      ]

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'edit':
            handleEdit(row)
            break
          case 'toggle':
            handleToggleEnabled(row)
            break
          case 'delete':
            handleDelete(row)
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

// 监听表单数据变化，同步操作选项
watch(() => formData.value.allowedOperations, (val) => {
  if (val) {
    selectedOperations.value = val.split(',').map(s => s.trim())
  } else {
    selectedOperations.value = []
  }
}, { immediate: true })

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const params: any = {}
    if (filters.value.pluginId) {
      params.pluginId = filters.value.pluginId
    }
    if (filters.value.tableName) {
      params.tableName = filters.value.tableName
    }
    
    const response = await tablePermissionApi.getList(params)
 
      let data = response
      
      // 客户端筛选状态
      if (filters.value.enabled !== null) {
        data = data.filter(item => item.enabled === filters.value.enabled)
      }
      
      tableData.value = data
      pagination.itemCount = data.length


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
  filters.value = {
    pluginId: '',
    tableName: '',
    enabled: null
  }
  pagination.page = 1
  loadData()
}

const handleCreate = () => {
  formMode.value = 'create'
  formData.value = {
    enabled: true,
    isReadonly: true
  }
  selectedOperations.value = []
  showFormModal.value = true
}

const handleEdit = (row: PluginTablePermission) => {
  formMode.value = 'edit'
  formData.value = { ...row }
  if (row.allowedOperations) {
    selectedOperations.value = row.allowedOperations.split(',').map(s => s.trim())
  } else {
    selectedOperations.value = []
  }
  showFormModal.value = true
}

const handleDelete = async (row: PluginTablePermission) => {
  if (!row.id) return

    const response = await tablePermissionApi.delete(row.id)
 
      message.success('删除成功')
      loadData()

}

const handleToggleEnabled = async (row: PluginTablePermission) => {
  if (!row.id) return

    const response = await tablePermissionApi.setEnabled(row.id, !row.enabled)
 
      message.success(`${row.enabled ? '禁用' : '启用'}成功`)
      loadData()

}

const handleFormSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (errors) => {
    if (!errors) {
      submitting.value = true
      try {
        const data: PluginTablePermission = {
          ...formData.value,
          allowedOperations: selectedOperations.value.length > 0 
            ? selectedOperations.value.join(',') 
            : undefined,
          tableName: formData.value.tableName?.toLowerCase()
        } as PluginTablePermission
        
        let response
        if (formMode.value === 'create') {
          response = await tablePermissionApi.create(data)
        } else {
          if (data.id) {
            response = await tablePermissionApi.update(data.id, data)
          }
        }
        
  
          message.success(formMode.value === 'create' ? '创建成功' : '更新成功')
          showFormModal.value = false
          loadData()
 
  
      } finally {
        submitting.value = false
      }
    }
  })
}

const handlePageChange = (page: number) => {
  pagination.page = page
  loadData()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.plugin-table-permission-management-page {
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

