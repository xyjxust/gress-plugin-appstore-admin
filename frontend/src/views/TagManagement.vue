<template>
  <div class="tag-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="标签管理" subtitle="管理插件标签，维护标签信息">
        <template #actions>
          <n-button type="primary" @click="handleCreate">
            <template #icon>
              <n-icon><component :is="Add" /></n-icon>
            </template>
            新建标签
          </n-button>
          <n-button @click="loadTags" :loading="refreshLoading">
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
          :data="tags"
          :loading="loading"
          :row-key="(row: Tag) => row.id"
          striped
        />
      </div>
    </div>

    <!-- 创建/编辑模态框 -->
    <n-modal
      v-model:show="showFormModal"
      :mask-closable="false"
      preset="card"
      :title="isEditing ? '编辑标签' : '新建标签'"
      style="width: 600px"
    >
      <n-form
        ref="formRef"
        :model="tagForm"
        :rules="formRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="标签名称" path="tagName">
          <n-input
            v-model:value="tagForm.tagName"
            placeholder="请输入标签名称"
            maxlength="64"
            show-count
          />
        </n-form-item>

        <n-form-item label="标签标识" path="tagKey">
          <n-input
            v-model:value="tagForm.tagKey"
            placeholder="请输入标签标识（英文，用于代码中引用）"
            maxlength="64"
            show-count
          />
        </n-form-item>

        <n-form-item label="标签描述" path="description">
          <n-input
            v-model:value="tagForm.description"
            type="textarea"
            placeholder="请输入标签描述"
            :rows="3"
            maxlength="500"
            show-count
          />
        </n-form-item>

        <n-form-item label="标签颜色" path="color">
          <n-color-picker
            v-model:value="tagForm.color"
            :show-alpha="false"
            :modes="['hex']"
            placeholder="选择标签颜色"
          />
        </n-form-item>

        <n-form-item label="启用状态" path="enabled">
          <n-switch v-model:value="tagForm.enabled" />
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
      <n-drawer-content :title="`标签详情 - ${currentTag?.tagName || ''}`">
        <div v-if="currentTag" class="tag-detail">
          <n-card title="基本信息" :bordered="false" class="detail-section">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="ID">
                {{ currentTag.id }}
              </n-descriptions-item>
              <n-descriptions-item label="标签名称">
                {{ currentTag.tagName }}
              </n-descriptions-item>
              <n-descriptions-item label="标签标识">
                {{ currentTag.tagKey }}
              </n-descriptions-item>
              <n-descriptions-item label="标签颜色">
                <n-space :size="8">
                  <div
                    v-if="currentTag.color"
                    :style="{
                      width: '20px',
                      height: '20px',
                      borderRadius: '4px',
                      backgroundColor: currentTag.color,
                      border: '1px solid #ddd'
                    }"
                  />
                  <span>{{ currentTag.color || '-' }}</span>
                </n-space>
              </n-descriptions-item>
              <n-descriptions-item label="启用状态">
                <n-tag :type="currentTag.enabled ? 'success' : 'default'">
                  {{ currentTag.enabled ? '已启用' : '已禁用' }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="使用次数">
                {{ currentTag.usageCount }}
              </n-descriptions-item>
            </n-descriptions>

            <n-divider />

            <n-descriptions :column="1" label-placement="left">
              <n-descriptions-item label="标签描述">
                {{ currentTag.description || '-' }}
              </n-descriptions-item>
              <n-descriptions-item label="创建时间">
                {{ formatDateTime(currentTag.createTime) }}
              </n-descriptions-item>
              <n-descriptions-item label="更新时间">
                {{ formatDateTime(currentTag.updateTime) }}
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
import { tagApi } from '../api'
import type { Tag } from '../types'

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
const Eye = useIcon('EyeOutline')

// Message and Dialog
const message = useMessage()
const dialog = useDialog()

// State
const loading = ref(false)
const refreshLoading = ref(false)
const submitting = ref(false)
const tags = ref<Tag[]>([])
const showFormModal = ref(false)
const showDetailDrawer = ref(false)
const isEditing = ref(false)
const currentTag = ref<Tag | null>(null)
const formRef = ref<any>(null)

// Filters
const showAdvanced = ref(false)
const filters = ref({
  keyword: '',
  enabled: null as boolean | null
})

// 标签表单
const tagForm = reactive({
  tagName: '',
  tagKey: '',
  description: '',
  color: '#18a058',
  enabled: true
})

// 表单验证规则
const formRules = {
  tagName: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { min: 2, max: 64, message: '标签名称长度为 2-64 个字符', trigger: 'blur' }
  ],
  tagKey: [
    { required: true, message: '请输入标签标识', trigger: 'blur' },
    { pattern: /^[a-z0-9_-]+$/, message: '标签标识只能包含小写字母、数字、下划线和连字符', trigger: 'blur' }
  ]
}

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '请输入标签名称或标识',
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
    title: '标签名称',
    key: 'tagName',
    width: 150
  },
  {
    title: '标签标识',
    key: 'tagKey',
    width: 150
  },
  {
    title: '标签颜色',
    key: 'color',
    width: 120,
    render: (row: Tag) => {
      if (!row.color) return '-'
      return h('div', {
        style: {
          display: 'flex',
          alignItems: 'center',
          gap: '8px'
        }
      }, [
        h('div', {
          style: {
            width: '20px',
            height: '20px',
            borderRadius: '4px',
            backgroundColor: row.color,
            border: '1px solid #ddd'
          }
        }),
        h('span', row.color)
      ])
    }
  },
  {
    title: '启用状态',
    key: 'enabled',
    width: 100,
    render: (row: Tag) => h(
      'n-tag',
      { type: row.enabled ? 'success' : 'default' },
      { default: () => row.enabled ? '已启用' : '已禁用' }
    )
  },
  {
    title: '使用次数',
    key: 'usageCount',
    width: 100
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 180,
    render: (row: Tag) => formatDateTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row: Tag) => {
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

// Methods
const loadTags = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const response = await tagApi.getAll()
    

      let filteredTags = response

      // 客户端过滤
      if (filters.value.keyword) {
        const keyword = filters.value.keyword.toLowerCase()
        filteredTags = filteredTags.filter(tag =>
          tag.tagName.toLowerCase().includes(keyword) ||
          tag.tagKey.toLowerCase().includes(keyword)
        )
      }

      if (filters.value.enabled !== null) {
        filteredTags = filteredTags.filter(tag => tag.enabled === filters.value.enabled)
      }

      tags.value = filteredTags


  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  loadTags()
}

const handleReset = () => {
  filters.value.keyword = ''
  filters.value.enabled = null
  loadTags()
}

const handleCreate = () => {
  isEditing.value = false
  currentTag.value = null
  resetForm()
  showFormModal.value = true
}

const handleEdit = (tag: Tag) => {
  isEditing.value = true
  currentTag.value = tag
  tagForm.tagName = tag.tagName
  tagForm.tagKey = tag.tagKey
  tagForm.description = tag.description || ''
  tagForm.color = tag.color || '#18a058'
  tagForm.enabled = tag.enabled
  showFormModal.value = true
}

const handleViewDetail = async (id: number) => {

    const response = await tagApi.getById(id)
 
      currentTag.value = response
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
    if (isEditing.value && currentTag.value) {
      response = await tagApi.update(currentTag.value.id, tagForm)
    } else {
      response = await tagApi.create(tagForm)
    }
      message.success(isEditing.value ? '更新成功' : '创建成功')
      showFormModal.value = false
      loadTags()


  } finally {
    submitting.value = false
  }
}

const handleDelete = (tag: Tag) => {
  dialog.warning({
    title: '删除标签',
    content: `确定要删除标签 "${tag.tagName}" 吗？\n\n删除后无法恢复，且该标签将从所有插件中移除。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {

        const response = await tagApi.delete(tag.id)


          message.success('删除成功')
          loadTags()
    
    }
  })
}

const resetForm = () => {
  tagForm.tagName = ''
  tagForm.tagKey = ''
  tagForm.description = ''
  tagForm.color = '#18a058'
  tagForm.enabled = true
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
  loadTags()
})
</script>

<style scoped>
.tag-management-page {
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

.tag-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-section {
  margin-bottom: 16px;
}
</style>
