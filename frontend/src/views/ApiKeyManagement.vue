<template>
  <div class="api-key-management-page">
    <div class="page-header-wrapper">
      <PageHeader title="API Key 管理" subtitle="管理 appstore 客户端访问凭证（每用户一个）">
        <template #actions>
          <n-space>
            <n-button type="primary" @click="showCreateModal = true">创建/重置 Key</n-button>
            <n-button @click="loadKeys" :loading="loading">刷新</n-button>
          </n-space>
        </template>
      </PageHeader>
    </div>

    <div class="page-content">
      <div class="table-container">
        <n-data-table
          :columns="columns"
          :data="keys"
          :loading="loading"
          :pagination="false"
          :row-key="(row: ApiKey) => row.keyId"
          striped
        />
      </div>
    </div>

    <n-modal v-model:show="showCreateModal" preset="card" title="创建/重置 API Key" style="width: 600px">
      <n-form ref="createFormRef" :model="createForm" label-placement="left" label-width="130px">
        <n-form-item label="开发者" path="userId">
          <n-select
            v-model:value="createForm.userId"
            filterable
            remote
            :options="developerSelectOptions"
            :loading="developerSearchLoading"
            clearable
            placeholder="输入姓名、用户名或邮箱搜索并选择"
            @search="handleDeveloperSearch"
          />
        </n-form-item>
        <n-form-item label="Scopes" path="scopes">
          <n-input
            v-model:value="createForm.scopes"
            placeholder="例如 appstore:read,appstore:download"
          />
        </n-form-item>
        <n-form-item label="过期秒数" path="expireInSeconds">
          <n-input-number v-model:value="createForm.expireInSeconds" :min="0" style="width: 100%" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showCreateModal = false">取消</n-button>
          <n-button type="primary" @click="handleCreateOrReset">确认</n-button>
        </n-space>
      </template>
    </n-modal>

    <n-modal v-model:show="showSecretModal" preset="card" title="请保存 Secret" style="width: 680px">
      <n-alert type="warning" style="margin-bottom: 12px">
        请妥善保管。创建后也可在列表中通过「复制密钥」再次从服务端解密并复制。
      </n-alert>
      <n-input type="textarea" :value="lastSecret" :rows="5" readonly />
      <template #footer>
        <n-space justify="end">
          <n-button :disabled="!lastSecret" @click="copyLastSecret">复制密钥</n-button>
          <n-button type="primary" @click="showSecretModal = false">我已保存</n-button>
        </n-space>
      </template>
    </n-modal>

    <n-drawer v-model:show="showDetail" :width="900" placement="right">
      <n-drawer-content :title="`Key 详情 - ${currentKey?.keyId || ''}`" closable>
        <n-tabs v-model:value="activeTab" type="line">
          <n-tab-pane name="basic" tab="基本信息">
            <n-descriptions :column="2" label-placement="left">
              <n-descriptions-item label="用户ID">{{ currentKey?.userId }}</n-descriptions-item>
              <n-descriptions-item label="KeyId">{{ currentKey?.keyId }}</n-descriptions-item>
              <n-descriptions-item label="状态">
                <n-tag :type="currentKey?.enabled ? 'success' : 'default'">{{ currentKey?.enabled ? '启用' : '禁用' }}</n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="Scopes">{{ currentKey?.scopes || '-' }}</n-descriptions-item>
              <n-descriptions-item label="过期时间">{{ currentKey?.expireAt || '-' }}</n-descriptions-item>
              <n-descriptions-item label="最近使用">{{ currentKey?.lastUsedAt || '-' }}</n-descriptions-item>
            </n-descriptions>
          </n-tab-pane>
          <n-tab-pane name="downloads" tab="下载明细">
            <div style="margin-bottom: 12px;">
              下载总次数：<strong>{{ logsPagination.itemCount }}</strong>
            </div>
            <n-data-table
              :columns="logColumns"
              :data="downloadLogs"
              :loading="logLoading"
              :pagination="false"
              :row-key="(row: ApiKeyDownloadLog) => row.id"
              striped
            />
            <div style="margin-top: 12px; display: flex; justify-content: flex-end;">
              <n-pagination
                v-model:page="logsPagination.page"
                v-model:page-size="logsPagination.pageSize"
                :item-count="logsPagination.itemCount"
                :page-sizes="logsPagination.pageSizes"
                show-size-picker
                @update:page="loadDownloadLogs"
                @update:page-size="handleLogPageSizeChange"
              />
            </div>
          </n-tab-pane>
        </n-tabs>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, watch, resolveComponent, computed } from 'vue'
import { useMessage } from '@keqi.gress/plugin-bridge'
import { apiKeyApi, developerApi } from '../api'
import type { ApiKey, ApiKeyDownloadLog, Developer } from '../types'

const message = useMessage()
const loading = ref(false)
const keys = ref<ApiKey[]>([])

const showCreateModal = ref(false)
const createFormRef = ref<any>(null)
const createForm = reactive({
  userId: null as string | null,
  scopes: 'appstore:read,appstore:download',
  expireInSeconds: 0 as number | null
})

const developerSelectOptions = ref<{ label: string; value: string }[]>([])
const developerSearchLoading = ref(false)
let developerSearchTimer: ReturnType<typeof setTimeout> | null = null

function formatDeveloperOptionLabel(d: Developer): string {
  const name = (d.displayName && d.displayName.trim()) || d.username
  const bits = [name, d.username !== name ? `@${d.username}` : null, d.email].filter(Boolean)
  return `${bits.join(' · ')}（${d.userId}）`
}

async function searchDevelopers(keyword: string) {
  developerSearchLoading.value = true
  try {
    const params: Record<string, string | number> = { page: 1, size: 50 }
    const q = keyword?.trim()
    if (q) params.keyword = q
    const res: any = await developerApi.getList(params)
    const page = res?.data ?? res
    const items: Developer[] = page?.items || []
    developerSelectOptions.value = items.map((d) => ({
      label: formatDeveloperOptionLabel(d),
      value: d.userId
    }))
  } catch {
    developerSelectOptions.value = []
  } finally {
    developerSearchLoading.value = false
  }
}

function handleDeveloperSearch(query: string) {
  if (developerSearchTimer) clearTimeout(developerSearchTimer)
  developerSearchTimer = setTimeout(() => {
    void searchDevelopers(query)
  }, 300)
}

watch(showCreateModal, (open) => {
  if (open) {
    createForm.userId = null
    developerSelectOptions.value = []
    void searchDevelopers('')
  }
})

const showSecretModal = ref(false)
const lastSecret = ref('')
const copyingKeyId = ref<string | null>(null)

const showDetail = ref(false)
const activeTab = ref('basic')
const currentKey = ref<ApiKey | null>(null)
const logLoading = ref(false)
const downloadLogs = ref<ApiKeyDownloadLog[]>([])
const logsPagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  pageSizes: [10, 20, 50, 100]
})

const NTag = resolveComponent('NTag') as any
const NButton = resolveComponent('NButton') as any

const columns = computed(() => [
  { title: '用户ID', key: 'userId', width: 140, ellipsis: { tooltip: true } },
  { title: 'KeyId', key: 'keyId', width: 220, ellipsis: { tooltip: true } },
  {
    title: '状态',
    key: 'enabled',
    width: 100,
    render: (row: ApiKey) => h(NTag, { type: row.enabled ? 'success' : 'default', size: 'small' }, { default: () => row.enabled ? '启用' : '禁用' })
  },
  { title: 'Scopes', key: 'scopes', width: 220, ellipsis: { tooltip: true } },
  { title: '过期时间', key: 'expireAt', width: 170, render: (row: ApiKey) => row.expireAt || '-' },
  { title: '最近使用', key: 'lastUsedAt', width: 170, render: (row: ApiKey) => row.lastUsedAt || '-' },
  {
    title: '操作',
    key: 'actions',
    width: 330,
    render: (row: ApiKey) =>
      h('div', [
        h(
          NButton,
          { size: 'small', type: 'info', onClick: () => openDetail(row) },
          { default: () => '详情' }
        ),
        h(
          NButton,
          {
            size: 'small',
            style: 'margin-left: 8px',
            loading: copyingKeyId.value === row.keyId,
            onClick: () => copyKeySecret(row)
          },
          { default: () => '复制密钥' }
        ),
        h(
          NButton,
          {
            size: 'small',
            style: 'margin-left: 8px',
            type: row.enabled ? 'warning' : 'success',
            onClick: () => toggleEnabled(row)
          },
          { default: () => row.enabled ? '禁用' : '启用' }
        )
      ])
  }
])

const logColumns: any[] = [
  { title: '插件ID', key: 'pluginId', width: 170, ellipsis: { tooltip: true } },
  { title: '版本', key: 'version', width: 120, render: (row: ApiKeyDownloadLog) => row.version || '当前版本' },
  { title: '签发IP', key: 'issuedIp', width: 140, ellipsis: { tooltip: true } },
  { title: '消费IP', key: 'consumedIp', width: 140, ellipsis: { tooltip: true } },
  { title: '签发时间', key: 'createTime', width: 170 },
  { title: '消费时间', key: 'usedAt', width: 170, render: (row: ApiKeyDownloadLog) => row.usedAt || '-' },
  {
    title: '状态',
    key: 'used',
    width: 90,
    render: (row: ApiKeyDownloadLog) => h(NTag, { type: row.used ? 'success' : 'default', size: 'small' }, { default: () => row.used ? '已消费' : '未消费' })
  }
]

async function copyToClipboard(text: string) {
  if (navigator.clipboard?.writeText) {
    await navigator.clipboard.writeText(text)
    return
  }
  const ta = document.createElement('textarea')
  ta.value = text
  ta.setAttribute('readonly', '')
  ta.style.position = 'fixed'
  ta.style.left = '-9999px'
  document.body.appendChild(ta)
  ta.select()
  try {
    const ok = document.execCommand('copy')
    if (!ok) throw new Error('execCommand copy failed')
  } finally {
    document.body.removeChild(ta)
  }
}

async function copyLastSecret() {
  const s = lastSecret.value?.trim()
  if (!s) {
    message.warning('无可复制的密钥')
    return
  }
  try {
    await copyToClipboard(s)
    message.success('密钥已复制到剪贴板')
  } catch {
    message.error('复制失败，请手动选择文本复制')
  }
}

async function copyKeySecret(row: ApiKey) {
  if (copyingKeyId.value) return
  copyingKeyId.value = row.keyId
  try {
    const res: any = await apiKeyApi.revealSecret(row.keyId)
    const data = res?.data ?? res
    const secret = typeof data?.secret === 'string' ? data.secret : ''
    if (!secret) {
      message.error('未获取到密钥')
      return
    }
    await copyToClipboard(secret)
    message.success('密钥已复制到剪贴板')
  } catch (e: any) {
    message.error(e?.message || '复制失败')
  } finally {
    copyingKeyId.value = null
  }
}

async function loadKeys() {
  loading.value = true
  try {
    const res: any = await apiKeyApi.list()
    keys.value = Array.isArray(res) ? res : res?.data || []
  } catch (e: any) {
    message.error(e?.message || '加载 API Key 列表失败')
  } finally {
    loading.value = false
  }
}

async function handleCreateOrReset() {
  await createFormRef.value?.validate?.()
  const uid = typeof createForm.userId === 'string' ? createForm.userId.trim() : ''
  if (!uid) {
    message.error('请选择开发者')
    return
  }
  try {
    const payload = {
      userId: uid,
      scopes: createForm.scopes?.trim() || undefined,
      expireInSeconds: createForm.expireInSeconds && createForm.expireInSeconds > 0 ? createForm.expireInSeconds : undefined
    }
    const res: any = await apiKeyApi.createOrReset(payload)
    const data = res?.data ?? res
    lastSecret.value = data?.secret || ''
    showCreateModal.value = false
    showSecretModal.value = true
    message.success('创建/重置成功')
    await loadKeys()
  } catch (e: any) {
    message.error(e?.message || '创建/重置失败')
  }
}

async function toggleEnabled(row: ApiKey) {
  try {
    if (row.enabled) {
      await apiKeyApi.disable(row.keyId)
      message.success('已禁用')
    } else {
      await apiKeyApi.enable(row.keyId)
      message.success('已启用')
    }
    await loadKeys()
    if (currentKey.value?.keyId === row.keyId) {
      currentKey.value.enabled = !row.enabled
    }
  } catch (e: any) {
    message.error(e?.message || '操作失败')
  }
}

function openDetail(row: ApiKey) {
  currentKey.value = row
  showDetail.value = true
  activeTab.value = 'basic'
  logsPagination.page = 1
  loadDownloadLogs()
}

async function loadDownloadLogs() {
  if (!currentKey.value) return
  logLoading.value = true
  try {
    const res: any = await apiKeyApi.getDownloadLogs(currentKey.value.keyId, logsPagination.page, logsPagination.pageSize)
    const page = res?.data ?? res
    downloadLogs.value = page?.items || []
    logsPagination.itemCount = page?.total || 0
  } catch (e: any) {
    message.error(e?.message || '加载下载明细失败')
  } finally {
    logLoading.value = false
  }
}

function handleLogPageSizeChange(size: number) {
  logsPagination.pageSize = size
  logsPagination.page = 1
  loadDownloadLogs()
}

onMounted(() => {
  loadKeys()
})
</script>

<style scoped>
.api-key-management-page {
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
</style>

