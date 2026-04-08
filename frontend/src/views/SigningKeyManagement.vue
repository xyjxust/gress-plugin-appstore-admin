<template>
  <div class="signing-key-management-page">
    <div class="page-header-wrapper">
      <PageHeader title="签名密钥管理" subtitle="生成与轮换 AppStore 签名密钥（self-signed）">
        <template #actions>
          <n-space>
            <n-button type="primary" @click="showGenerateModal = true">
              一键生成密钥
            </n-button>
            <n-button @click="loadKeys" :loading="refreshLoading">
              刷新
            </n-button>
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
          :row-key="(row: SigningKeyDTO) => row.keyId"
          striped
        />
      </div>
    </div>

    <!-- Generate modal -->
    <n-modal
      v-model:show="showGenerateModal"
      preset="card"
      title="生成新的签名密钥"
      :mask-closable="false"
      style="width: 600px"
    >
      <n-form
        ref="generateFormRef"
        :model="generateForm"
        :rules="generateRules"
        label-placement="left"
        label-width="140px"
      >
        <n-form-item label="alias（可选）" path="alias">
          <n-input v-model:value="generateForm.alias" placeholder="不填则自动生成" />
        </n-form-item>

        <n-form-item label="有效期（天）" path="validityDays">
          <n-input-number
            v-model:value="generateForm.validityDays"
            :min="1"
            :max="36500"
            style="width: 100%"
          />
        </n-form-item>

        <n-form-item label="是否立即激活" path="makeActive">
          <n-switch v-model:value="generateForm.makeActive" />
        </n-form-item>

        <n-form-item label="可信窗口（天）" path="trustedWindowDays">
          <n-input-number
            v-model:value="generateForm.trustedWindowDays"
            :min="1"
            :max="3650"
            style="width: 100%"
          />
          <div style="margin-top: 6px; font-size: 12px; color: #888">
            激活后，新旧密钥会同时作为客户端 trusted roots 在窗口期内被接受。
          </div>
        </n-form-item>

        <n-alert type="warning" title="安全提醒" style="margin-top: 12px">
          服务器端需要环境变量 <code>APPSTORE_MASTER_ENCRYPTION_KEY</code> 才能安全加密 keystore 密码。
        </n-alert>
      </n-form>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showGenerateModal = false">取消</n-button>
          <n-button type="primary" @click="handleGenerate" :loading="refreshLoading">
            生成
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted, resolveComponent } from 'vue'
import { useMessage, useDialog } from '@keqi.gress/plugin-bridge'
import type { SigningKeyDTO, GenerateSigningKeyRequest, ActivateSigningKeyRequest } from '../types'
import { signingKeyApi } from '../api'

const message = useMessage()
const dialog = useDialog()

const loading = ref(false)
const refreshLoading = ref(false)
const keys = ref<SigningKeyDTO[]>([])

const showGenerateModal = ref(false)
const generateFormRef = ref<any>(null)
const generateForm = reactive({
  alias: '',
  validityDays: 3650,
  makeActive: true,
  trustedWindowDays: 30
})

const generateRules = {
  validityDays: [
    { required: true, type: 'number', message: '请输入有效期（天）', trigger: 'blur' }
  ],
  trustedWindowDays: [
    { required: true, type: 'number', message: '请输入可信窗口（天）', trigger: 'blur' }
  ]
}

// Naive UI components (render function usage)
const NTag = resolveComponent('NTag') as any
const NButton = resolveComponent('NButton') as any

const columns: any[] = [
  { title: '密钥ID', key: 'keyId', width: 180, ellipsis: { tooltip: true } },
  { title: '别名', key: 'alias', width: 140, ellipsis: { tooltip: true } },
  {
    title: '状态',
    key: 'active',
    width: 100,
    render: (row: SigningKeyDTO) =>
      h(
        NTag,
        { type: row.active ? 'success' : 'default' },
        { default: () => (row.active ? 'ACTIVE' : 'TRUSTED') }
      )
  },
  { title: '公钥指纹（SHA-256）', key: 'fingerprintSha256', width: 240, ellipsis: { tooltip: true } },
  { title: '可信至', key: 'trustedUntil', width: 160 },
  {
    title: '吊销时间',
    key: 'revokedAt',
    width: 160,
    render: (row: SigningKeyDTO) => (row.revokedAt ? row.revokedAt : '-')
  },
  { title: '创建时间', key: 'createTime', width: 160 },
  {
    title: '操作',
    key: 'actions',
    width: 220,
    fixed: 'right',
    render: (row: SigningKeyDTO) => {
      return h('div', [
        h(
          NButton,
          {
            size: 'small',
            type: row.active ? 'default' : 'primary',
            disabled: row.active,
            onClick: () => handleActivate(row.keyId)
          },
          { default: () => '设为激活' }
        ),
        h(
          NButton,
          {
            size: 'small',
            style: 'margin-left: 8px',
            onClick: () => handleDownloadPem(row)
          },
          { default: () => '下载公钥' }
        ),
      ])
    }
  }
]

async function loadKeys() {
  loading.value = true
  try {
    const res = await signingKeyApi.list()
    keys.value = Array.isArray(res) ? res : res?.data || []
  } catch (e: any) {
    message.error(e?.message || '加载签名密钥失败')
  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

async function handleGenerate() {
  await generateFormRef.value?.validate()

  const payload: GenerateSigningKeyRequest = {
    alias: generateForm.alias || undefined,
    validityDays: generateForm.validityDays,
    makeActive: generateForm.makeActive,
    trustedWindowDays: generateForm.trustedWindowDays
  }

  showGenerateModal.value = false
  refreshLoading.value = true
  try {
    await signingKeyApi.generate(payload)
    message.success('密钥生成成功')
  } catch (e: any) {
    message.error(e?.message || '密钥生成失败')
  } finally {
    refreshLoading.value = false
    await loadKeys()
  }
}

function handleActivate(keyId: string) {
  dialog.warning({
    title: '切换为该密钥的 ACTIVE',
    content: '将把当前密钥切换为 ACTIVE，并让新旧密钥在可信窗口期内都被客户端接受。',
    positiveText: '确认切换',
    negativeText: '取消',
    onPositiveClick: async () => {
      refreshLoading.value = true
      try {
        const payload: ActivateSigningKeyRequest = { trustedWindowDays: generateForm.trustedWindowDays }
        await signingKeyApi.activate(keyId, payload)
        message.success('切换成功')
        await loadKeys()
      } catch (e: any) {
        message.error(e?.message || '切换失败')
      } finally {
        refreshLoading.value = false
      }
    }
  })
}

async function handleDownloadPem(row: SigningKeyDTO) {
  try {
    const res = await signingKeyApi.getPublicKeyPem(row.keyId)
    const pem = (res as any)?.data ?? res
    if (!pem || typeof pem !== 'string') {
      message.error('下载失败：未获取到 PEM 内容')
      return
    }

    const blob = new Blob([pem], { type: 'application/x-pem-file;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `appstore-signing-public-key-${row.keyId}.pem`
    document.body.appendChild(a)
    a.click()
    a.remove()
    URL.revokeObjectURL(url)
  } catch (e: any) {
    message.error(e?.message || '下载公钥失败')
  }
}

onMounted(() => {
  loadKeys()
})
</script>

<style scoped>
.signing-key-management-page {
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
  gap: 16px;
}

.table-container {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 16px;
  overflow: auto;
}
</style>

