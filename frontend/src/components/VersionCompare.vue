<template>
  <div class="version-compare">
    <n-grid :cols="2" :x-gap="16">
      <!-- 版本1 -->
      <n-gi>
        <n-card :title="`版本 ${version1.version}`" :bordered="false">
          <n-descriptions :column="1" label-placement="left" size="small">
            <n-descriptions-item label="状态">
              <n-tag :type="getStatusTag(version1.status).type" size="small">
                {{ getStatusTag(version1.status).label }}
              </n-tag>
            </n-descriptions-item>
            <n-descriptions-item label="文件大小">
              {{ formatFileSize(version1.fileSize) }}
            </n-descriptions-item>
            <n-descriptions-item label="下载次数">
              {{ version1.downloadCount }}
            </n-descriptions-item>
            <n-descriptions-item label="创建时间">
              {{ formatDateTime(version1.createTime) }}
            </n-descriptions-item>
          </n-descriptions>

          <n-divider />

          <div class="section-title">版本描述</div>
          <div class="description-content">
            {{ version1.description || '暂无描述' }}
          </div>

          <template v-if="version1.releaseNotes">
            <n-divider />
            <div class="section-title">发布说明</div>
            <div class="release-notes-content">
              {{ version1.releaseNotes }}
            </div>
          </template>
        </n-card>
      </n-gi>

      <!-- 版本2 -->
      <n-gi>
        <n-card :title="`版本 ${version2.version}`" :bordered="false">
          <n-descriptions :column="1" label-placement="left" size="small">
            <n-descriptions-item label="状态">
              <n-tag :type="getStatusTag(version2.status).type" size="small">
                {{ getStatusTag(version2.status).label }}
              </n-tag>
            </n-descriptions-item>
            <n-descriptions-item label="文件大小">
              {{ formatFileSize(version2.fileSize) }}
              <n-tag 
                v-if="fileSizeDiff !== 0" 
                :type="fileSizeDiff > 0 ? 'error' : 'success'" 
                size="small"
                style="margin-left: 8px;"
              >
                {{ fileSizeDiff > 0 ? '+' : '' }}{{ formatFileSize(Math.abs(fileSizeDiff)) }}
              </n-tag>
            </n-descriptions-item>
            <n-descriptions-item label="下载次数">
              {{ version2.downloadCount }}
              <n-tag 
                v-if="downloadDiff !== 0" 
                :type="downloadDiff > 0 ? 'success' : 'default'" 
                size="small"
                style="margin-left: 8px;"
              >
                {{ downloadDiff > 0 ? '+' : '' }}{{ downloadDiff }}
              </n-tag>
            </n-descriptions-item>
            <n-descriptions-item label="创建时间">
              {{ formatDateTime(version2.createTime) }}
            </n-descriptions-item>
          </n-descriptions>

          <n-divider />

          <div class="section-title">版本描述</div>
          <div class="description-content">
            {{ version2.description || '暂无描述' }}
          </div>

          <template v-if="version2.releaseNotes">
            <n-divider />
            <div class="section-title">发布说明</div>
            <div class="release-notes-content">
              {{ version2.releaseNotes }}
            </div>
          </template>
        </n-card>
      </n-gi>
    </n-grid>

    <!-- 差异摘要 -->
    <n-card title="差异摘要" :bordered="false" style="margin-top: 16px;">
      <n-space vertical>
        <div>
          <n-text strong>文件大小变化：</n-text>
          <n-text :type="fileSizeDiff > 0 ? 'error' : fileSizeDiff < 0 ? 'success' : 'default'">
            {{ fileSizeDiff > 0 ? '增加' : fileSizeDiff < 0 ? '减少' : '无变化' }}
            {{ fileSizeDiff !== 0 ? formatFileSize(Math.abs(fileSizeDiff)) : '' }}
          </n-text>
        </div>
        <div>
          <n-text strong>下载次数变化：</n-text>
          <n-text :type="downloadDiff > 0 ? 'success' : 'default'">
            {{ downloadDiff > 0 ? '+' : '' }}{{ downloadDiff }}
          </n-text>
        </div>
        <div>
          <n-text strong>时间间隔：</n-text>
          <n-text>{{ timeDiff }}</n-text>
        </div>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { NCard, NGrid, NGi, NDescriptions, NDescriptionsItem, NTag, NSpace, NText, NDivider } from 'naive-ui'
import type { PluginVersion, SubmissionStatus } from '../types'

// 定义Props
interface Props {
  version1: PluginVersion
  version2: PluginVersion
}

const props = defineProps<Props>()

// 计算差异
const fileSizeDiff = computed(() => props.version2.fileSize - props.version1.fileSize)
const downloadDiff = computed(() => props.version2.downloadCount - props.version1.downloadCount)

const timeDiff = computed(() => {
  const date1 = new Date(props.version1.createTime)
  const date2 = new Date(props.version2.createTime)
  const diff = Math.abs(date2.getTime() - date1.getTime())
  
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60))
  
  if (days > 0) {
    return `${days}天${hours}小时`
  } else if (hours > 0) {
    return `${hours}小时`
  } else {
    const minutes = Math.floor(diff / (1000 * 60))
    return `${minutes}分钟`
  }
})

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
    minute: '2-digit'
  })
}
</script>

<style scoped>
.version-compare .section-title {
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.version-compare .description-content,
.version-compare .release-notes-content {
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
  font-size: 14px;
}
</style>
