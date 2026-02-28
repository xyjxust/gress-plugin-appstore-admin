<template>
  <div class="category-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="分类管理" subtitle="管理插件分类，维护分类信息">
        <template #actions>
          <n-button type="primary" @click="handleCreate">
            <template #icon>
              <n-icon><component :is="Add" /></n-icon>
            </template>
            新建分类
          </n-button>
          <n-button @click="loadCategories" :loading="refreshLoading">
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
          :data="categories"
          :loading="loading"
          :row-key="(row: Category) => row.id"
          striped
        />
      </div>
    </div>

    <!-- 创建/编辑模态框 -->
    <n-modal
      v-model:show="showFormModal"
      :mask-closable="false"
      preset="card"
      :title="isEditing ? '编辑分类' : '新建分类'"
      style="width: 600px"
    >
      <n-form
        ref="formRef"
        :model="categoryForm"
        :rules="formRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="分类名称" path="categoryName">
          <n-input
            v-model:value="categoryForm.categoryName"
            placeholder="请输入分类名称"
            maxlength="64"
            show-count
          />
        </n-form-item>

        <n-form-item label="分类标识" path="categoryKey">
          <n-input
            v-model:value="categoryForm.categoryKey"
            placeholder="请输入分类标识（英文，用于代码中引用）"
            maxlength="64"
            show-count
          />
        </n-form-item>

        <n-form-item label="分类描述" path="description">
          <n-input
            v-model:value="categoryForm.description"
            type="textarea"
            placeholder="请输入分类描述"
            :rows="3"
            maxlength="500"
            show-count
          />
        </n-form-item>

        <n-form-item label="图标" path="icon">
          <n-input
            v-model:value="categoryForm.icon"
            placeholder="请输入图标名称（如：folder-outline）"
            maxlength="128"
          />
        </n-form-item>

        <n-form-item label="排序顺序" path="displayOrder">
          <n-input-number
            v-model:value="categoryForm.displayOrder"
            :min="0"
            :max="9999"
            placeholder="数字越小越靠前"
            style="width: 100%"
          />
        </n-form-item>

        <n-form-item label="启用状态" path="enabled">
          <n-switch v-model:value="categoryForm.enabled" />
        </n-form-item>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showFormModal = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEditing ? '更新' : '创建' }}
          </n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 详情抽屉 -->
    <n-drawer
      v-model:show="showDetailDrawer"
      :width="600"
      placement="right"
    >
      <n-drawer-content :title="`分类详情 - ${currentCategory?.categoryName || ''}`">
        <div v-if="currentCategory" class="category-detail">
          <n-card title="基本信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="ID">
                {{ currentCategory.id }}
              </n-descriptions-item>
              <n-descriptions-item label="分类名称">
                {{ currentCategory.categoryName }}
              </n-descriptions-item>
              <n-descriptions-item label="分类标识">
                {{ currentCategory.categoryKey }}
              </n-descriptions-item>
              <n-descriptions-item label="图标">
                {{ currentCategory.icon || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="排序顺序">
                {{ currentCategory.displayOrder }}
              </n-descriptions-item>
              <n-descriptions-item label="启用状态">
                <n-tag :type="currentCategory.enabled ? 'success' : 'default'">
                  {{ currentCategory.enabled ? '已启用' : '已禁用' }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="插件数量">
                {{ currentCategory.pluginCount }}
              </n-descriptions-item>
            </n-descriptions>

            <n-divider />

            <n-descriptions :column="1" label-placement="left">
              <n-descriptions-item label="分类描述">
                {{ currentCategory.description || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="创建时间">
                {{ formatDateTime(currentCategory.createTime) }}
              </n-descriptions-item>
              <n-descriptions-item label="更新时间">
                {{ formatDateTime(currentCategory.updateTime) }}
              </n-descriptions-item>
            </n-descriptions>
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
import { categoryApi } from '../api'
import type { Category } from '../types'

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

// 图标
const Add = useIcon('AddOutline')
const Refresh = useIcon('RefreshOutline')
const Edit = useIcon('CreateOutline')
const Delete = useIcon('TrashOutline')

// Message and Dialog
const message = useMessage()
const dialog = useDialog()

// State
const loading = ref(false)
const refreshLoading = ref(false)
const submitting = ref(false)
const categories = ref<Category[]>([])
const showFormModal = ref(false)
const showDetailDrawer = ref(false)
const isEditing = ref(false)
const currentCategory = ref<Category | null>(null)
const formRef = ref<any>(null)

// Filters
const showAdvanced = ref(false)
const filters = ref({
  keyword: '',
  enabled: null as boolean | null
})

// 分类表单
const categoryForm = reactive({
  categoryName: '',
  categoryKey: '',
  description: '',
  icon: '',
  displayOrder: 0,
  enabled: true
})

// 表单验证规则
const formRules = {
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 64, message: '分类名称长度为 2-64 个字符', trigger: 'blur' }
  ],
  categoryKey: [
    { required: true, message: '请输入分类标识', trigger: 'blur' },
    { pattern: /^[a-z0-9_-]+$/, message: '分类标识只能包含小写字母、数字、下划线和连字符', trigger: 'blur' }
  ],
  displayOrder: [
    { required: true, type: 'number', message: '请输入排序顺序', trigger: 'blur' }
  ]
}

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '请输入分类名称或标识',
    span: 12
  },
  {
    key: 'enabled',
    label: '启用状态',
    type: 'select',
    placeholder: '请选择启用状态',
    options: [
      { label: '全部', value: null },
      { label: '已启用', value: true },
      { label: '已禁用', value: false }
    ],
    span: 12
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
    title: '分类名称',
    key: 'categoryName',
    width: 150
  },
  {
    title: '分类标识',
    key: 'categoryKey',
    width: 150
  },
  {
    title: '图标',
    key: 'icon',
    width: 120,
    render: (row: Category) => row.icon || '-'
  },
  {
    title: '排序顺序',
    key: 'displayOrder',
    width: 100
  },
  {
    title: '启用状态',
    key: 'enabled',
    width: 100,
    render: (row: Category) => h(
      'n-tag',
      { type: row.enabled ? 'success' : 'default' },
      { default: () => row.enabled ? '已启用' : '已禁用' }
    )
  },
  {
    title: '插件数量',
    key: 'pluginCount',
    width: 100
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 180,
    render: (row: Category) => formatDateTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row: Category) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions = [
        {
          label: '编辑',
          key: 'edit',
          icon: () => h(NIcon, { component: Edit })
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
                onClick: () => handleViewDetail(row.id)
              },
              {
                icon: () => h(NIcon, { component: useIcon('EyeOutline') }),
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

// Methods
const loadCategories = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const response = await categoryApi.getAll()
    
   
      let filteredCategories = response

      // 客户端过滤
      if (filters.value.keyword) {
        const keyword = filters.value.keyword.toLowerCase()
        filteredCategories = filteredCategories.filter(cat =>
          cat.categoryName.toLowerCase().includes(keyword) ||
          cat.categoryKey.toLowerCase().includes(keyword)
        )
      }

      if (filters.value.enabled !== null) {
        filteredCategories = filteredCategories.filter(cat => cat.enabled === filters.value.enabled)
      }

      // 按排序顺序排序
      filteredCategories.sort((a, b) => a.displayOrder - b.displayOrder)

      categories.value = filteredCategories


  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  loadCategories()
}

const handleReset = () => {
  filters.value.keyword = ''
  filters.value.enabled = null
  loadCategories()
}

const handleCreate = () => {
  isEditing.value = false
  currentCategory.value = null
  resetForm()
  showFormModal.value = true
}

const handleEdit = (category: Category) => {
  isEditing.value = true
  currentCategory.value = category
  categoryForm.categoryName = category.categoryName
  categoryForm.categoryKey = category.categoryKey
  categoryForm.description = category.description || ''
  categoryForm.icon = category.icon || ''
  categoryForm.displayOrder = category.displayOrder
  categoryForm.enabled = category.enabled
  showFormModal.value = true
}

const handleViewDetail = async (id: number) => {

    const response = await categoryApi.getById(id)
    
   
      currentCategory.value = response
      showDetailDrawer.value = true

}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
  } catch (error) {
    return
  }

  submitting.value = true
  try {
    let response
    if (isEditing.value && currentCategory.value) {
      response = await categoryApi.update(currentCategory.value.id, categoryForm)
    } else {
      response = await categoryApi.create(categoryForm)
    }

 
      message.success(isEditing.value ? '更新成功' : '创建成功')
      showFormModal.value = false
      loadCategories()


  } finally {
    submitting.value = false
  }
}

const handleDelete = (category: Category) => {
  dialog.warning({
    title: '删除分类',
    content: `确定要删除分类 "${category.categoryName}" 吗？\n\n删除后无法恢复，且该分类下的插件将失去分类信息。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
   
        const response = await categoryApi.delete(category.id)

     
          message.success('删除成功')
          loadCategories()
   
    }
  })
}

const resetForm = () => {
  categoryForm.categoryName = ''
  categoryForm.categoryKey = ''
  categoryForm.description = ''
  categoryForm.icon = ''
  categoryForm.displayOrder = 0
  categoryForm.enabled = true
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
  loadCategories()
})
</script>

<style scoped>
.category-management-page {
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
}

.table-container {
  margin-top: 16px;
}

.category-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-section {
  margin-bottom: 16px;
}
</style>
