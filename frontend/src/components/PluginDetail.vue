<template>
  <div class="plugin-detail">
    <!-- 基本信息 -->
    <n-card title="基本信息" :bordered="false" class="detail-card">
      <n-descriptions :column="2" label-placement="left">
        <n-descriptions-item label="插件名称">
          {{ plugin.pluginName }}
        </n-descriptions-item>
        <n-descriptions-item label="插件ID">
          {{ plugin.pluginId }}
        </n-descriptions-item>
        <n-descriptions-item label="当前版本">
          {{ plugin.currentVersion }}
        </n-descriptions-item>
        <n-descriptions-item label="插件类型">
          <n-tag :type="getPluginTypeTag(plugin.pluginType).type" size="small">
            {{ getPluginTypeTag(plugin.pluginType).label }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="开发者">
          {{ plugin.developerName || plugin.developerId }}
        </n-descriptions-item>
        <n-descriptions-item label="插件状态">
          <n-tag :type="getStatusTag(plugin.status).type" size="small">
            {{ getStatusTag(plugin.status).label }}
          </n-tag>
        </n-descriptions-item>
        <n-descriptions-item label="上架时间" :span="2">
          {{ formatDateTime(plugin.listingTime) }}
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 插件描述 -->
    <n-card title="插件描述" :bordered="false" class="detail-card">
      <div class="description-content">
        {{ plugin.description || '暂无描述' }}
      </div>
    </n-card>

    <!-- 历史版本 -->
    <n-card title="历史版本" :bordered="false" class="detail-card">
      <template #header-extra>
        <n-button text @click="loadVersions" :loading="loadingVersions">
          <template #icon>
            <n-icon><component :is="Refresh" /></n-icon>
          </template>
          刷新
        </n-button>
      </template>
      
      <n-spin :show="loadingVersions">
        <div v-if="versions.length > 0" class="version-list">
          <div v-for="version in versions" :key="version.version" class="version-item">
            <div class="version-header">
              <div class="version-info">
                <span class="version-number">{{ version.version }}</span>
                <n-tag v-if="version.isCurrent" type="success" size="small" style="margin-left: 8px">
                  当前版本
                </n-tag>
                <n-tag :type="getVersionStatusTag(version.status).type" size="small" style="margin-left: 8px">
                  {{ getVersionStatusTag(version.status).label }}
                </n-tag>
              </div>
              <span class="version-time">{{ formatDateTime(version.uploadTime) }}</span>
            </div>
            <div v-if="version.releaseNotes" class="version-notes">
              {{ version.releaseNotes }}
            </div>
            <div class="version-meta">
              <span>文件大小: {{ formatFileSize(version.fileSize) }}</span>
              <span style="margin-left: 16px">下载次数: {{ version.downloadCount || 0 }}</span>
            </div>
          </div>
        </div>
        <n-empty v-else description="暂无版本记录" size="small" />
      </n-spin>
    </n-card>

    <!-- 插件图标 -->
    <n-card v-if="plugin.icon" title="插件图标" :bordered="false" class="detail-card">
      <div class="icon-preview">
        <img :src="plugin.icon" alt="插件图标" />
      </div>
    </n-card>

    <!-- 分类和标签 -->
    <n-card title="分类和标签" :bordered="false" class="detail-card">
      <n-space>
        <n-tag v-if="plugin.category" type="info">
          分类: {{ plugin.category }}
        </n-tag>
        <n-tag v-for="tag in plugin.tags" :key="tag" type="default">
          {{ tag }}
        </n-tag>
        <span v-if="!plugin.category && (!plugin.tags || plugin.tags.length === 0)" class="empty-text">
          暂无分类和标签
        </span>
      </n-space>
    </n-card>

    <!-- 使用统计 -->
    <n-card title="使用统计" :bordered="false" class="detail-card">
      <n-descriptions :column="2" label-placement="left">
        <n-descriptions-item label="安装次数">
          {{ plugin.installCount }}
        </n-descriptions-item>
        <n-descriptions-item label="活跃用户">
          {{ plugin.activeUsers }}
        </n-descriptions-item>
        <n-descriptions-item label="评分" v-if="plugin.rating">
          <n-rate :value="plugin.rating" readonly size="small" />
          <span class="rating-text">{{ plugin.rating.toFixed(1) }}</span>
        </n-descriptions-item>
      </n-descriptions>
    </n-card>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <n-space justify="end">
        <n-button @click="$emit('close')">
          关闭
        </n-button>
        <n-button @click="$emit('edit', plugin.pluginId)">
          <template #icon>
            <n-icon><component :is="CreateOutline" /></n-icon>
          </template>
          编辑
        </n-button>
        <n-button v-if="plugin.status === 'ONLINE'" type="error" @click="$emit('delist', plugin.pluginId)">
          <template #icon>
            <n-icon><component :is="BanOutline" /></n-icon>
          </template>
          下架
        </n-button>
        <n-button v-if="plugin.status === 'DELISTED' || plugin.status === 'OFFLINE'" type="error" secondary @click="$emit('delete', plugin.pluginId)">
          <template #icon>
            <n-icon><component :is="TrashOutline" /></n-icon>
          </template>
          删除
        </n-button>
        <n-button v-if="plugin.status === 'DELISTED'" type="success" @click="$emit('relist', plugin.pluginId)">
          <template #icon>
            <n-icon><component :is="CheckmarkCircle" /></n-icon>
          </template>
          重新上架
        </n-button>
      </n-space>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useIcon } from '@keqi.gress/plugin-bridge'
import { useMessage } from '@keqi.gress/plugin-bridge'
import { versionApi } from '../api'
import type { Plugin, PluginStatus, PluginType, PluginVersion } from '../types'

// 图标
const CreateOutline = useIcon('CreateOutline')
const BanOutline = useIcon('BanOutline')
const CheckmarkCircle = useIcon('CheckmarkCircleOutline')
const TrashOutline = useIcon('TrashOutline')
const Refresh = useIcon('RefreshOutline')

// 消息提示
const message = useMessage()

// 定义Props
interface Props {
  plugin: Plugin
}

const props = defineProps<Props>()

// 定义Emits
defineEmits<{
  (e: 'delist', pluginId: string): void
  (e: 'relist', pluginId: string): void
  (e: 'edit', pluginId: string): void
  (e: 'delete', pluginId: string): void
  (e: 'close'): void
}>()

// 版本列表状态
const versions = ref<PluginVersion[]>([])
const loadingVersions = ref(false)

// 加载版本列表
async function loadVersions() {
  if (!props.plugin?.pluginId) return
  
  loadingVersions.value = true
  try {
    const response = await versionApi.getList(props.plugin.pluginId)

      versions.value = response

  } finally {
    loadingVersions.value = false
  }
}

// 监听插件变化，自动加载版本
watch(() => props.plugin?.pluginId, (newPluginId) => {
  if (newPluginId) {
    loadVersions()
  }
}, { immediate: true })

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
function getStatusTag(status: PluginStatus) {
  const statusMap: Record<PluginStatus, { label: string; type: 'success' | 'warning' | 'error' }> = {
    ONLINE: { label: '在线', type: 'success' },
    OFFLINE: { label: '离线', type: 'warning' },
    DELISTED: { label: '已下架', type: 'error' }
  }
  return statusMap[status]
}

// 获取版本状态标签配置
function getVersionStatusTag(status: string) {
  const statusMap: Record<string, { label: string; type: 'success' | 'warning' | 'error' | 'default' }> = {
    ONLINE: { label: '在线', type: 'success' },
    OFFLINE: { label: '离线', type: 'warning' },
    DELISTED: { label: '已下架', type: 'error' }
  }
  return statusMap[status] || { label: status, type: 'default' }
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

// 格式化文件大小
function formatFileSize(bytes: number): string {
  if (!bytes || bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}
</script>

<style scoped>
.plugin-detail {
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

.version-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.version-item {
  padding: 12px;
  background: #fafafa;
  border-radius: 4px;
  border: 1px solid #f0f0f0;
}

.version-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.version-info {
  display: flex;
  align-items: center;
}

.version-number {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.version-time {
  font-size: 13px;
  color: #999;
}

.version-notes {
  margin-bottom: 8px;
  padding: 8px;
  background: white;
  border-radius: 4px;
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

.version-meta {
  font-size: 13px;
  color: #999;
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

.rating-text {
  margin-left: 8px;
  color: #666;
  font-size: 14px;
}

.action-buttons {
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}
</style>
