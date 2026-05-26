<template>
  <div class="version-detail">
    <!-- 基本信息 -->
    <n-card title="基本信息" :bordered="false" class="detail-card">
      <n-descriptions :column="2" label-placement="left">
        <n-descriptions-item label="版本号">
          <div style="display: flex; align-items: center; gap: 8px;">
            <span>{{ version.version }}</span>
            <n-tag v-if="version.isCurrent" type="success" size="small">当前版本</n-tag>
          </div>
        </n-descriptions-item>
        <n-descriptions-item label="状态">
          <n-tag :type="getStatusTag(version.status).type" size="small">
            {{ getStatusTag(version.status).label }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="文件大小">
          {{ formatFileSize(version.fileSize) }}
        </n-descriptions-item>
        <n-descriptions-item label="下载次数">
          {{ version.downloadCount }}
        </n-descriptions-item>
        <n-descriptions-item label="文件哈希" :span="2">
          <n-text code>{{ version.fileHash }}</n-text>
        </n-descriptions-item>
        <n-descriptions-item label="创建时间" :span="2">
          {{ formatDateTime(version.createTime) }}
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 版本描述 -->
    <n-card title="版本描述" :bordered="false" class="detail-card">
      <div v-if="version.description" class="description-content richtext" v-html="sanitize(version.description)" />
      <div v-else class="description-content">暂无描述</div>
    </n-card>

    <!-- 发布说明 -->
    <n-card v-if="version.releaseNotes" title="发布说明" :bordered="false" class="detail-card">
      <div class="release-notes-content richtext" v-html="sanitize(version.releaseNotes)" />
    </n-card>

    <!-- 文件信息 -->
    <n-card title="文件信息" :bordered="false" class="detail-card">
      <n-descriptions :column="1" label-placement="left">
        <n-descriptions-item label="文件路径">
          <n-text code>{{ version.filePath }}</n-text>
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <n-space justify="end">
        <n-button @click="$emit('close')">
          关闭
        </n-button>
        <n-button 
          v-if="!version.isCurrent && version.status === 'APPROVED'" 
          type="success" 
          @click="$emit('set-current', version.version)"
        >
          <template #icon>
            <n-icon><component :is="CheckmarkCircle" /></n-icon>
          </template>
          设为当前版本
        </n-button>
        <n-button 
          v-if="!version.isCurrent && version.status === 'APPROVED'" 
          type="primary" 
          @click="$emit('rollback', version.version)"
        >
          <template #icon>
            <n-icon><component :is="RefreshCircle" /></n-icon>
          </template>
          回滚到此版本
        </n-button>
        <n-button 
          v-if="!version.isCurrent" 
          type="error" 
          @click="$emit('delete', version.version)"
        >
          <template #icon>
            <n-icon><component :is="TrashOutline" /></n-icon>
          </template>
          删除版本
        </n-button>
      </n-space>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useIcon } from '@keqi.gress/plugin-bridge'
import type { PluginVersion, SubmissionStatus } from '../types'
import { sanitizeHtml } from '@keqi.gress/plugin-ui'

// 图标
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const RefreshCircle = useIcon('RefreshCircleOutline')
const TrashOutline = useIcon('TrashOutline')

// 定义Props
interface Props {
  version: PluginVersion
}

defineProps<Props>()
const sanitize = sanitizeHtml

// 定义Emits
defineEmits<{
  (e: 'set-current', version: string): void
  (e: 'rollback', version: string): void
  (e: 'delete', version: string): void
  (e: 'close'): void
}>()

// 获取状态标签配置
function getStatusTag(status: SubmissionStatus) {
  const statusMap: Record<SubmissionStatus, { label: string; type: 'default' | 'success' | 'error' }> = {
    PENDING: { label: '待审核', type: 'default' },
    APPROVED: { label: '已批准', type: 'success' },
    REJECTED: { label: '已拒绝', type: 'error' }
  }
  return statusMap[status]
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
    minute: '2-digit',
    second: '2-digit'
  })
}
</script>

<style scoped>
.version-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-card {
  margin-bottom: 0;
}

.description-content,
.release-notes-content {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.richtext :deep(p) {
  margin: 0 0 10px;
}
.richtext :deep(ul),
.richtext :deep(ol) {
  padding-left: 22px;
  margin: 0 0 10px;
}
.richtext :deep(a) {
  color: #1677ff;
  text-decoration: underline;
}

.action-buttons {
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}
</style>
