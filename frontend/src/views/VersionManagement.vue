<template>
  <div class="version-management-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="版本管理" :subtitle="`插件: ${pluginName || pluginId}`">
        <template #actions>
          <n-button @click="goBack">
            <template #icon>
              <n-icon><component :is="ArrowBack" /></n-icon>
            </template>
            返回
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
      <!-- 版本列表 -->
      <div class="version-list-container">
        <n-card title="版本列表" :bordered="false">
          <n-data-table
            :columns="columns"
            :data="tableData"
            :loading="loading"
            :pagination="false"
            striped
          />
        </n-card>
      </div>
    </div>

    <!-- 版本详情抽屉 -->
    <n-drawer
      v-model:show="showDetailDrawer"
      :width="720"
      placement="right"
    >
      <n-drawer-content title="版本详情" closable>
        <VersionDetail
          v-if="currentVersion"
          :version="currentVersion"
          @set-current="handleSetCurrent"
          @rollback="handleRollback"
          @delete="handleDelete"
          @close="showDetailDrawer = false"
        />
      </n-drawer-content>
    </n-drawer>

    <!-- 版本比较对话框 -->
    <n-modal
      v-model:show="showCompareModal"
      preset="card"
      title="版本比较"
      style="width: 900px"
    >
      <VersionCompare
        v-if="compareVersions.length === 2"
        :version1="compareVersions[0]"
        :version2="compareVersions[1]"
      />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, h, onMounted, resolveComponent } from 'vue'
import { useRoute, useRouter, useMessage, useIcon } from '@keqi.gress/plugin-bridge'
import { useDialog, NTag } from 'naive-ui'
import type { DataTableColumns } from 'naive-ui'

// 图标
const ArrowBack = useIcon('ArrowBackOutline')
const Refresh = useIcon('RefreshOutline')
const Eye = useIcon('EyeOutline')
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const RefreshCircle = useIcon('RefreshOutline')
const TrashOutline = useIcon('TrashOutline')
import VersionDetail from '../components/VersionDetail.vue'
import VersionCompare from '../components/VersionCompare.vue'
import { versionApi } from '../api'
import type { PluginVersion, SubmissionStatus } from '../types'

// 路由
const route = useRoute()
const router = useRouter()

// 消息提示
const message = useMessage()
const dialog = useDialog()

// 插件信息
const pluginId = ref(route.value.params.pluginId as string)
const pluginName = ref('')

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const tableData = ref<PluginVersion[]>([])
const showDetailDrawer = ref(false)
const currentVersion = ref<PluginVersion | null>(null)

// 版本比较
const showCompareModal = ref(false)
const compareVersions = ref<PluginVersion[]>([])
const selectedVersions = ref<number[]>([])

// 表格列配置
const columns: DataTableColumns<PluginVersion> = [
  {
    type: 'selection',
    disabled: (row: PluginVersion) => row.isCurrent,
    multiple: false
  },
  {
    title: '版本号',
    key: 'version',
    width: 120,
    render: (row: PluginVersion) => {
      return h('div', { style: 'display: flex; align-items: center; gap: 8px;' }, [
        h('span', row.version),
        row.isCurrent && h(NTag, { type: 'success', size: 'small' }, { default: () => '当前版本' })
      ])
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: PluginVersion) => {
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
    title: '描述',
    key: 'description',
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '文件大小',
    key: 'fileSize',
    width: 120,
    render: (row: PluginVersion) => formatFileSize(row.fileSize)
  },
  {
    title: '下载次数',
    key: 'downloadCount',
    width: 100
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 160,
    render: (row: PluginVersion) => formatDateTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row: PluginVersion) => {
      const NButton = resolveComponent('NButton') as any
      const NSpace = resolveComponent('NSpace') as any
      const NIcon = resolveComponent('NIcon') as any
      const NDropdown = resolveComponent('NDropdown') as any
      const MoreHorizontal = useIcon('EllipsisHorizontal')

      // 更多菜单选项
      const moreOptions: Array<{ label: string; key: string; icon: () => any }> = []
      
      if (!row.isCurrent && row.status === 'APPROVED') {
        moreOptions.push({
          label: '设为当前',
          key: 'setCurrent',
          icon: () => h(NIcon, { component: CheckmarkCircle })
        })
        moreOptions.push({
          label: '回滚',
          key: 'rollback',
          icon: () => h(NIcon, { component: RefreshCircle })
        })
      }
      
      if (!row.isCurrent) {
        moreOptions.push({
          label: '删除',
          key: 'delete',
          icon: () => h(NIcon, { component: TrashOutline })
        })
      }

      const handleMoreSelect = (key: string) => {
        switch (key) {
          case 'setCurrent':
            handleSetCurrent(row.version)
            break
          case 'rollback':
            handleRollback(row.version)
            break
          case 'delete':
            handleDelete(row.version)
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
    const response = await versionApi.getList(pluginId.value)
    
 
      tableData.value = response
      // 获取插件名称（从第一个版本中获取）
      if (response.length > 0) {
        // 这里假设版本数据中包含插件名称，如果没有则需要额外调用API获取
        pluginName.value = response[0].pluginId
      }


  } finally {
    loading.value = false
  }
}

// 查看详情
function handleViewDetail(version: PluginVersion) {
  currentVersion.value = version
  showDetailDrawer.value = true
}

// 设置当前版本
function handleSetCurrent(version: string) {
  dialog.info({
    title: '设置当前版本',
    content: `确定要将版本 ${version} 设置为当前版本吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {

        await versionApi.setCurrent(pluginId.value, version)
        message.success('当前版本已更新')
        await loadData()
  
    }
  })
}

// 回滚版本
function handleRollback(version: string) {
  dialog.warning({
    title: '回滚版本',
    content: `确定要回滚到版本 ${version} 吗？\n\n此操作将替换当前运行的版本。`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {

        await versionApi.rollback(pluginId.value, version)
        message.success('版本回滚成功')
        await loadData()

    }
  })
}

// 删除版本
function handleDelete(version: string) {
  dialog.warning({
    title: '删除版本',
    content: `确定要删除版本 ${version} 吗？\n\n此操作不可恢复。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {

        await versionApi.delete(pluginId.value, version)
        message.success('版本已删除')
        await loadData()

    }
  })
}

// 版本比较
function handleCompare() {
  if (selectedVersions.value.length !== 2) {
    message.warning('请选择两个版本进行比较')
    return
  }
  
  compareVersions.value = tableData.value.filter(v => 
    selectedVersions.value.includes(v.id)
  )
  showCompareModal.value = true
}

// 返回
function goBack() {
  router.back()
}

// 格式化文件大小
function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
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
.version-management-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 16px;
  padding: 16px;
  background: #f5f5f5;
}

.version-list-container {
  flex: 1;
  overflow: auto;
}
</style>
