<template>
  <div class="review-rules-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="审核规则配置" subtitle="配置自动化审核规则，提高审核效率">
        <template #actions>
          <n-space>
            <n-button type="primary" @click="handleCreate">
              <template #icon>
                <n-icon><component :is="Add" /></n-icon>
              </template>
              创建规则
            </n-button>
            <n-button @click="loadRules" :loading="refreshLoading">
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
        @search="handleSearch"
        @reset="handleReset"
      />

      <!-- 数据表格 -->
      <div class="table-container">
        <n-data-table
          :columns="columns"
          :data="rules"
          :loading="loading"
          :pagination="false"
          :row-key="(row: ReviewRule) => row.id"
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

    <!-- 规则创建/编辑模态框 -->
    <n-modal
      v-model:show="showRuleModal"
      :mask-closable="false"
      preset="card"
      :title="isEditing ? '编辑规则' : '创建规则'"
      style="width: 800px"
    >
      <n-form
        ref="formRef"
        :model="ruleForm"
        :rules="formRules"
        label-placement="left"
        label-width="120px"
      >
        <n-form-item label="规则名称" path="ruleName">
          <n-input
            v-model:value="ruleForm.ruleName"
            placeholder="请输入规则名称"
            maxlength="128"
            show-count
          />
        </n-form-item>

        <n-form-item label="规则描述" path="description">
          <n-input
            v-model:value="ruleForm.description"
            type="textarea"
            placeholder="请输入规则描述"
            :rows="3"
            maxlength="500"
            show-count
          />
        </n-form-item>

        <n-form-item label="规则类型" path="ruleType">
          <n-select
            v-model:value="ruleForm.ruleType"
            :options="ruleTypeOptions"
            placeholder="请选择规则类型"
          />
        </n-form-item>

        <n-form-item label="优先级" path="priority">
          <n-input-number
            v-model:value="ruleForm.priority"
            :min="0"
            :max="100"
            placeholder="数字越大优先级越高"
            style="width: 100%"
          />
        </n-form-item>

        <n-form-item label="启用状态" path="enabled">
          <n-switch v-model:value="ruleForm.enabled">
            <template #checked>启用</template>
            <template #unchecked>禁用</template>
          </n-switch>
        </n-form-item>

        <n-divider />

        <n-form-item label="条件逻辑" path="conditions.logic">
          <n-radio-group v-model:value="ruleForm.conditions.logic">
            <n-radio value="AND">所有条件都满足（AND）</n-radio>
            <n-radio value="OR">任一条件满足（OR）</n-radio>
          </n-radio-group>
        </n-form-item>

        <n-form-item label="规则条件" path="conditions.conditions">
          <div class="conditions-container">
            <div
              v-for="(condition, index) in ruleForm.conditions.conditions"
              :key="index"
              class="condition-item"
            >
              <n-space :size="8" align="center">
                <n-select
                  v-model:value="condition.field"
                  :options="fieldOptions"
                  placeholder="字段"
                  style="width: 150px"
                />
                <n-select
                  v-model:value="condition.operator"
                  :options="operatorOptions"
                  placeholder="操作符"
                  style="width: 120px"
                />
                <n-input
                  v-model:value="condition.value"
                  placeholder="值"
                  style="width: 200px"
                />
                <n-button
                  text
                  type="error"
                  @click="removeCondition(index)"
                >
                  <template #icon>
                    <n-icon><component :is="TrashOutline" /></n-icon>
                  </template>
                </n-button>
              </n-space>
            </div>
            <n-button
              dashed
              block
              @click="addCondition"
              style="margin-top: 8px"
            >
              <template #icon>
                <n-icon><component :is="Add" /></n-icon>
              </template>
              添加条件
            </n-button>
          </div>
        </n-form-item>

        <n-divider />

        <n-form-item label="规则动作" path="actions">
          <div class="actions-container">
            <div
              v-for="(action, index) in ruleForm.actions"
              :key="index"
              class="action-item"
            >
              <n-space :size="8" align="center">
                <n-select
                  v-model:value="action.type"
                  :options="actionTypeOptions"
                  placeholder="动作类型"
                  style="width: 150px"
                />
                <n-input
                  v-if="action.type === 'notify' && action.params"
                  v-model:value="action.params.message"
                  placeholder="通知消息"
                  style="width: 300px"
                />
                <n-input
                  v-if="action.type === 'reject' && action.params"
                  v-model:value="action.params.reason"
                  placeholder="拒绝原因"
                  style="width: 300px"
                />
                <n-button
                  text
                  type="error"
                  @click="removeAction(index)"
                >
                  <template #icon>
                    <n-icon><component :is="TrashOutline" /></n-icon>
                  </template>
                </n-button>
              </n-space>
            </div>
            <n-button
              dashed
              block
              @click="addAction"
              style="margin-top: 8px"
            >
              <template #icon>
                <n-icon><component :is="Add" /></n-icon>
              </template>
              添加动作
            </n-button>
          </div>
        </n-form-item>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showRuleModal = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit" :loading="submitting">
            {{ isEditing ? '更新' : '创建' }}
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, resolveComponent } from 'vue'
import type { FormInst, FormRules, DataTableColumns } from 'naive-ui'
import { NTag, useDialog } from 'naive-ui'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'

// 图标
const Add = useIcon('AddOutline')
const Refresh = useIcon('RefreshOutline')
const TrashOutline = useIcon('TrashOutline')
const CreateOutline = useIcon('CreateOutline')
const BanOutline = useIcon('BanOutline')
const CheckmarkCircleOutline = useIcon('CheckmarkCircleOutline')
import { reviewRuleApi } from '../api'
import type { ReviewRule, RuleType, RuleCondition, RuleAction } from '../types'

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

// Message & Dialog
const message = useMessage()
const dialog = useDialog()

// State
const loading = ref(false)
const refreshLoading = ref(false)
const submitting = ref(false)
const rules = ref<ReviewRule[]>([])
const showRuleModal = ref(false)
const isEditing = ref(false)
const currentRuleId = ref<number | null>(null)
const formRef = ref<FormInst | null>(null)

// 过滤器状态
const showAdvanced = ref(false)
const filters = ref({
  keyword: '',
  ruleType: null as RuleType | null,
  enabled: null as boolean | null
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
  loadRules()
}

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadRules()
}

// 规则表单
const ruleForm = reactive({
  ruleName: '',
  description: '',
  ruleType: 'AUTO_APPROVE' as RuleType,
  priority: 0,
  enabled: true,
  conditions: {
    logic: 'AND' as 'AND' | 'OR',
    conditions: [] as RuleCondition[]
  },
  actions: [] as RuleAction[]
})

// 表单验证规则
const formRules: FormRules = {
  ruleName: [
    { required: true, message: '请输入规则名称', trigger: 'blur' },
    { min: 2, max: 128, message: '规则名称长度为 2-128 个字符', trigger: 'blur' }
  ],
  ruleType: [
    { required: true, message: '请选择规则类型', trigger: 'change' }
  ],
  priority: [
    { required: true, type: 'number', message: '请输入优先级', trigger: 'blur' }
  ],
  'conditions.logic': [
    { required: true, message: '请选择条件逻辑', trigger: 'change' }
  ]
}

// 过滤字段配置
const basicFields: FilterFieldConfig[] = [
  {
    key: 'ruleType',
    label: '规则类型',
    type: 'select',
    placeholder: '请选择规则类型',
    options: [
      { label: '全部', value: null },
      { label: '自动批准', value: 'AUTO_APPROVE' },
      { label: '自动拒绝', value: 'AUTO_REJECT' },
      { label: '标记', value: 'FLAG' }
    ]
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
    ]
  },
  {
    key: 'keyword',
    label: '关键词',
    type: 'input',
    placeholder: '搜索规则名称、描述'
  }
]

// 规则类型选项
const ruleTypeOptions = [
  { label: '自动批准', value: 'AUTO_APPROVE' },
  { label: '自动拒绝', value: 'AUTO_REJECT' },
  { label: '标记', value: 'FLAG' }
]

// 字段选项
const fieldOptions = [
  { label: '插件类型', value: 'pluginType' },
  { label: '开发者ID', value: 'developerId' },
  { label: '插件名称', value: 'pluginName' },
  { label: '文件大小', value: 'fileSize' },
  { label: '标签', value: 'tags' },
  { label: '分类', value: 'category' }
]

// 操作符选项
const operatorOptions = [
  { label: '等于', value: 'equals' },
  { label: '包含', value: 'contains' },
  { label: '大于', value: 'greater_than' },
  { label: '小于', value: 'less_than' },
  { label: '在列表中', value: 'in' },
  { label: '不在列表中', value: 'not_in' }
]

// 动作类型选项
const actionTypeOptions = [
  { label: '批准', value: 'approve' },
  { label: '拒绝', value: 'reject' },
  { label: '标记', value: 'flag' },
  { label: '通知', value: 'notify' }
]

// Table Columns
const columns: DataTableColumns<ReviewRule> = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '规则名称',
    key: 'ruleName',
    width: 200
  },
  {
    title: '描述',
    key: 'description',
    width: 250,
    ellipsis: {
      tooltip: true
    },
    render: (row: ReviewRule) => row.description || '-'
  },
  {
    title: '规则类型',
    key: 'ruleType',
    width: 120,
    render: (row: ReviewRule) => h(
      NTag,
      { type: getRuleTypeTagType(row.ruleType) },
      { default: () => getRuleTypeText(row.ruleType) }
    )
  },
  {
    title: '优先级',
    key: 'priority',
    width: 100
  },
  {
    title: '状态',
    key: 'enabled',
    width: 100,
    render: (row: ReviewRule) => h(
      NTag,
      { type: row.enabled ? 'success' : 'default' },
      { default: () => row.enabled ? '已启用' : '已禁用' }
    )
  },
  {
    title: '匹配次数',
    key: 'matchCount',
    width: 100
  },
  {
    title: '最后匹配',
    key: 'lastMatchTime',
    width: 180,
    render: (row: ReviewRule) => row.lastMatchTime ? formatDateTime(row.lastMatchTime) : '-'
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 180,
    render: (row: ReviewRule) => formatDateTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    fixed: 'right',
    render: (row: ReviewRule) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项（不包含编辑）
      const moreOptions = [
        {
          label: row.enabled ? '禁用' : '启用',
          key: row.enabled ? 'disable' : 'enable',
          icon: () => h(NIcon, { component: row.enabled ? BanOutline : CheckmarkCircleOutline })
        },
        {
          label: '删除',
          key: 'delete',
          icon: () => h(NIcon, { component: TrashOutline })
        }
      ]

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'enable':
            handleEnable(row)
            break
          case 'disable':
            handleDisable(row)
            break
          case 'delete':
            handleDelete(row)
            break
        }
      }

      return h(
        NSpace,
        { size: 4, wrap: false },
        {
          default: () => [
            // 编辑按钮 - 始终显示
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                text: true,
                onClick: () => handleEdit(row)
              },
              {
                icon: () => h(NIcon, { component: CreateOutline }),
                default: () => '编辑'
              }
            ),
            // 更多操作下拉菜单
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
const loadRules = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    const params: any = {
      page: pagination.page,
      size: pagination.pageSize,
      keyword: filters.value.keyword || undefined,
      ruleType: filters.value.ruleType || undefined,
      enabled: filters.value.enabled !== null ? filters.value.enabled : undefined
    }

    const response = await reviewRuleApi.getList(params)
    
   
      // 处理两种可能的返回格式
      if (Array.isArray(response)) {
        // 直接返回数组
        rules.value = response
        pagination.itemCount = response.length
      } else { 
        // 分页格式
        rules.value = response.items || []
        pagination.itemCount = response.total || 0
      }
  

  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadRules()
}

const handleReset = () => {
  filters.value.keyword = ''
  filters.value.ruleType = null
  filters.value.enabled = null
  pagination.page = 1
  loadRules()
}

const handleCreate = () => {
  isEditing.value = false
  currentRuleId.value = null
  resetForm()
  showRuleModal.value = true
}

const handleEdit = (rule: ReviewRule) => {
  isEditing.value = true
  currentRuleId.value = rule.id
  
  // 填充表单
  ruleForm.ruleName = rule.ruleName
  ruleForm.description = rule.description || ''
  ruleForm.ruleType = rule.ruleType
  ruleForm.priority = rule.priority
  ruleForm.enabled = rule.enabled
  ruleForm.conditions = JSON.parse(JSON.stringify(rule.conditions))
  ruleForm.actions = JSON.parse(JSON.stringify(rule.actions))
  
  showRuleModal.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // 验证至少有一个条件
    if (ruleForm.conditions.conditions.length === 0) {
      message.error('请至少添加一个规则条件')
      return
    }
    
    // 验证至少有一个动作
    if (ruleForm.actions.length === 0) {
      message.error('请至少添加一个规则动作')
      return
    }
    
    submitting.value = true
    
    const requestData = {
      ruleName: ruleForm.ruleName,
      description: ruleForm.description,
      ruleType: ruleForm.ruleType,
      priority: ruleForm.priority,
      enabled: ruleForm.enabled,
      conditions: ruleForm.conditions,
      actions: ruleForm.actions
    }
    
    let response
    if (isEditing.value && currentRuleId.value) {
      response = await reviewRuleApi.update(currentRuleId.value, requestData)
    } else {
      response = await reviewRuleApi.create(requestData)
    }
    

      message.success(isEditing.value ? '更新成功' : '创建成功')
      showRuleModal.value = false
      loadRules()
  
  } catch (error: any) {
    if (error.errors) {
      // 表单验证错误
      return
    }
    console.error('提交失败:', error)
   
  } finally {
    submitting.value = false
  }
}

const handleEnable = async (rule: ReviewRule) => {

    const response = await reviewRuleApi.enable(rule.id)
    

      message.success('启用成功')
      loadRules()


}

const handleDisable = async (rule: ReviewRule) => {

    const response = await reviewRuleApi.disable(rule.id)
    

      message.success('禁用成功')
      loadRules()

}

const handleDelete = (rule: ReviewRule) => {
  dialog.warning({
    title: '删除规则',
    content: `确定要删除规则 "${rule.ruleName}" 吗？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {

        const response = await reviewRuleApi.delete(rule.id)
        
       
          message.success('删除成功')
          loadRules()
   
    }
  })
}

const addCondition = () => {
  ruleForm.conditions.conditions.push({
    field: 'pluginType',
    operator: 'equals',
    value: ''
  })
}

const removeCondition = (index: number) => {
  ruleForm.conditions.conditions.splice(index, 1)
}

const addAction = () => {
  ruleForm.actions.push({
    type: 'approve',
    params: {}
  })
}

const removeAction = (index: number) => {
  ruleForm.actions.splice(index, 1)
}

const resetForm = () => {
  ruleForm.ruleName = ''
  ruleForm.description = ''
  ruleForm.ruleType = 'AUTO_APPROVE'
  ruleForm.priority = 0
  ruleForm.enabled = true
  ruleForm.conditions = {
    logic: 'AND',
    conditions: []
  }
  ruleForm.actions = []
}

// Helper Functions
const getRuleTypeTagType = (ruleType: RuleType): 'default' | 'success' | 'warning' | 'error' => {
  const typeMap: Record<RuleType, 'default' | 'success' | 'warning' | 'error'> = {
    AUTO_APPROVE: 'success',
    AUTO_REJECT: 'error',
    FLAG: 'warning'
  }
  return typeMap[ruleType] || 'default'
}

const getRuleTypeText = (ruleType: RuleType): string => {
  const textMap: Record<RuleType, string> = {
    AUTO_APPROVE: '自动批准',
    AUTO_REJECT: '自动拒绝',
    FLAG: '标记'
  }
  return textMap[ruleType] || ruleType
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
  loadRules()
})
</script>

<style scoped>
.review-rules-page {
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

.conditions-container,
.actions-container {
  width: 100%;
}

.condition-item,
.action-item {
  margin-bottom: 8px;
}
</style>
