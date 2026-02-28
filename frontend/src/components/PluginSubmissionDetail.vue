<template>
  <div class="plugin-submission-detail">
    <!-- 基本信息 -->
    <n-card title="基本信息" :bordered="false" class="detail-card">
      <n-descriptions :column="2" label-placement="left">
        <n-descriptions-item label="插件名称">
          {{ submission.pluginName }}
        </n-descriptions-item>
        <n-descriptions-item label="插件ID">
          {{ submission.pluginId }}
        </n-descriptions-item>
        <n-descriptions-item label="版本号">
          {{ submission.version }}
        </n-descriptions-item>
        <n-descriptions-item label="插件类型">
          <n-tag :type="getPluginTypeTag(submission.pluginType).type" size="small">
            {{ getPluginTypeTag(submission.pluginType).label }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="开发者">
          {{ submission.developerName || submission.developerId }}
        </n-descriptions-item>
        <n-descriptions-item label="审核状态">
          <n-tag :type="getStatusTag(submission.status).type" size="small">
            {{ getStatusTag(submission.status).label }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="提交时间" :span="2">
          {{ formatDateTime(submission.submitTime) }}
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 插件描述 -->
    <n-card title="插件描述" :bordered="false" class="detail-card">
      <div class="description-content">
        {{ submission.description || '暂无描述' }}
      </div>
    </n-card>

    <!-- 插件图标 -->
    <n-card v-if="submission.icon" title="插件图标" :bordered="false" class="detail-card">
      <div class="icon-preview">
        <img :src="submission.icon" alt="插件图标" />
      </div>
    </n-card>

    <!-- 分类和标签 -->
    <n-card title="分类和标签" :bordered="false" class="detail-card">
      <n-space>
        <n-tag v-if="submission.category" type="info">
          分类: {{ submission.category }}
        </n-tag>
        <n-tag v-for="tag in submission.tags" :key="tag" type="default">
          {{ tag }}
        </n-tag>
        <span v-if="!submission.category && (!submission.tags || submission.tags.length === 0)" class="empty-text">
          暂无分类和标签
        </span>
      </n-space>
    </n-card>

    <!-- 审核信息 -->
    <n-card v-if="submission.reviewTime" title="审核信息" :bordered="false" class="detail-card">
      <n-descriptions :column="1" label-placement="left">
        <n-descriptions-item label="审核人">
          {{ submission.reviewerName || submission.reviewerId }}
        </n-descriptions-item>
        <n-descriptions-item label="审核时间">
          {{ formatDateTime(submission.reviewTime) }}
        </n-descriptions-item>
        <n-descriptions-item label="审核意见">
          {{ submission.reviewComment || '无' }}
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 操作按钮 -->
    <div v-if="submission.status === 'PENDING'" class="action-buttons">
      <n-space justify="end">
        <n-button @click="$emit('close')">
          取消
        </n-button>
        <n-button type="error" @click="$emit('reject', submission.id)">
          <template #icon>
            <n-icon><component :is="Close" /></n-icon>
          </template>
          拒绝
        </n-button>
        <n-button type="success" @click="$emit('approve', submission.id)">
          <template #icon>
            <n-icon><component :is="CheckmarkDone" /></n-icon>
          </template>
          批准
        </n-button>
      </n-space>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useIcon } from '@keqi.gress/plugin-bridge'
import type { PluginSubmission, SubmissionStatus, PluginType } from '../types'

// 图标
const Close = useIcon('CloseOutline')
const CheckmarkDone = useIcon('CheckmarkDoneOutline')

// 定义Props
interface Props {
  submission: PluginSubmission
}

defineProps<Props>()

// 定义Emits
defineEmits<{
  (e: 'approve', id: number): void
  (e: 'reject', id: number): void
  (e: 'close'): void
}>()

// 获取插件类型标签配置
function getPluginTypeTag(type: PluginType) {
  const typeMap: Record<PluginType, { label: string; type: 'info' | 'success' | 'warning' }> = {
    TASK: { label: '任务节点', type: 'info' },
    TRIGGER: { label: '触发器', type: 'success' },
    APPLICATION: { label: '应用插件', type: 'warning' }
  }
  return typeMap[type]
}

// 获取状态标签配置
function getStatusTag(status: SubmissionStatus) {
  const statusMap: Record<SubmissionStatus, { label: string; type: 'default' | 'success' | 'error' }> = {
    PENDING: { label: '待审核', type: 'default' },
    APPROVED: { label: '已批准', type: 'success' },
    REJECTED: { label: '已拒绝', type: 'error' }
  }
  return statusMap[status]
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
.plugin-submission-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.detail-card {
  margin-bottom: 0;
}

.description-content {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.icon-preview {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  
  img {
    max-width: 200px;
    max-height: 200px;
    object-fit: contain;
  }
}

.empty-text {
  color: #999;
  font-size: 14px;
}

.action-buttons {
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}
</style>
