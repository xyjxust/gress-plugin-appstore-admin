<template>
  <div class="statistics-analysis-page">
    <!-- 页面头部 - 撑满宽度 -->
    <div class="page-header-wrapper">
      <PageHeader title="统计分析" subtitle="查看插件商店的统计数据和趋势分析">
        <template #actions>
          <n-button @click="handleExport" :loading="exportLoading">
            <template #icon>
              <n-icon><component :is="DownloadOutline" /></n-icon>
            </template>
            导出数据
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
      <!-- 时间范围选择 -->
      <n-card class="filter-card" :bordered="false">
        <n-space align="center">
          <span class="filter-label">时间范围：</span>
          <n-date-picker
            v-model:value="dateRange"
            type="daterange"
            clearable
            @update:value="handleDateRangeChange"
          />
          <n-button-group>
            <n-button
              :type="quickRange === '7d' ? 'primary' : 'default'"
              @click="setQuickRange('7d')"
            >
              最近7天
            </n-button>
            <n-button
              :type="quickRange === '30d' ? 'primary' : 'default'"
              @click="setQuickRange('30d')"
            >
              最近30天
            </n-button>
            <n-button
              :type="quickRange === '90d' ? 'primary' : 'default'"
              @click="setQuickRange('90d')"
            >
              最近90天
            </n-button>
          </n-button-group>
        </n-space>
      </n-card>

      <!-- 统计概览卡片 -->
      <div class="overview-cards">
        <n-card class="stat-card" :bordered="false">
          <div class="stat-content">
            <div class="stat-icon" style="background: #18a058;">
              <n-icon :size="32"><component :is="AppsOutline" /></n-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">插件总数</div>
              <div class="stat-value">{{ overview.totalPlugins }}</div>
            </div>
          </div>
        </n-card>

        <n-card class="stat-card" :bordered="false">
          <div class="stat-content">
            <div class="stat-icon" style="background: #2080f0;">
              <n-icon :size="32"><component :is="DownloadOutline" /></n-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">总安装次数</div>
              <div class="stat-value">{{ formatNumber(overview.totalInstalls) }}</div>
            </div>
          </div>
        </n-card>

        <n-card class="stat-card" :bordered="false">
        <div class="stat-content">
          <div class="stat-icon" style="background: #f0a020;">
            <n-icon :size="32"><component :is="PeopleOutline" /></n-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">活跃用户数</div>
            <div class="stat-value">{{ formatNumber(overview.totalActiveUsers) }}</div>
          </div>
        </div>
      </n-card>

      <n-card class="stat-card" :bordered="false">
        <div class="stat-content">
          <div class="stat-icon" style="background: #d03050;">
            <n-icon :size="32"><component :is="TimeOutline" /></n-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">待审核</div>
            <div class="stat-value">{{ overview.pendingReviews }}</div>
          </div>
        </div>
      </n-card>
    </div>

    <!-- 今日数据 -->
    <n-card title="今日数据" class="today-stats" :bordered="false">
      <n-space>
        <n-statistic label="新提交" :value="overview.newSubmissionsToday">
          <template #suffix>
            <span class="stat-unit">个</span>
          </template>
        </n-statistic>
        <n-divider vertical />
        <n-statistic label="已批准" :value="overview.approvedToday">
          <template #suffix>
            <span class="stat-unit">个</span>
          </template>
        </n-statistic>
        <n-divider vertical />
        <n-statistic label="已拒绝" :value="overview.rejectedToday">
          <template #suffix>
            <span class="stat-unit">个</span>
          </template>
        </n-statistic>
      </n-space>
    </n-card>

    <!-- 趋势图表 -->
    <div class="trend-charts">
      <n-card title="安装趋势" class="chart-card" :bordered="false">
        <n-spin :show="loading">
          <div class="chart-container">
            <div v-if="trendData.installTrend && trendData.installTrend.length > 0" class="simple-chart">
              <div
                v-for="(point, index) in trendData.installTrend"
                :key="index"
                class="chart-bar"
                :style="{ height: getBarHeight(point.value, trendData.installTrend) }"
                :title="`${point.date}: ${point.value}`"
              >
                <div class="bar-value">{{ formatNumber(point.value) }}</div>
              </div>
            </div>
            <div v-else class="empty-chart">暂无数据</div>
          </div>
          <div v-if="trendData.installTrend && trendData.installTrend.length > 0" class="chart-labels">
            <span
              v-for="(point, index) in trendData.installTrend"
              :key="index"
              class="chart-label"
            >
              {{ formatDate(point.date) }}
            </span>
          </div>
        </n-spin>
      </n-card>

      <n-card title="活跃用户趋势" class="chart-card" :bordered="false">
        <n-spin :show="loading">
          <div class="chart-container">
            <div v-if="trendData.activeTrend && trendData.activeTrend.length > 0" class="simple-chart">
              <div
                v-for="(point, index) in trendData.activeTrend"
                :key="index"
                class="chart-bar"
                :style="{ height: getBarHeight(point.value, trendData.activeTrend) }"
                :title="`${point.date}: ${point.value}`"
              >
                <div class="bar-value">{{ formatNumber(point.value) }}</div>
              </div>
            </div>
            <div v-else class="empty-chart">暂无数据</div>
          </div>
          <div v-if="trendData.activeTrend && trendData.activeTrend.length > 0" class="chart-labels">
            <span
              v-for="(point, index) in trendData.activeTrend"
              :key="index"
              class="chart-label"
            >
              {{ formatDate(point.date) }}
            </span>
          </div>
        </n-spin>
      </n-card>
    </div>

    <!-- 热门插件排行 -->
    <n-card title="热门插件 TOP 10" class="top-plugins" :bordered="false">
      <n-data-table
        :columns="topPluginsColumns"
        :data="overview.topPlugins || []"
        :pagination="false"
        :loading="loading"
        striped
      />
    </n-card>

      <!-- 导出对话框 -->
      <n-modal
        v-model:show="showExportModal"
        preset="dialog"
        title="导出统计数据"
        positive-text="导出"
        negative-text="取消"
        :positive-button-props="{ loading: exportLoading }"
        @positive-click="confirmExport"
      >
        <n-form ref="exportFormRef" :model="exportForm" :rules="exportRules">
          <n-form-item label="导出格式" path="format">
            <n-select
              v-model:value="exportForm.format"
              :options="formatOptions"
              placeholder="请选择导出格式"
            />
          </n-form-item>
          <n-form-item label="包含详细数据">
            <n-switch v-model:value="exportForm.includeDetails" />
          </n-form-item>
        </n-form>
      </n-modal>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { useMessage, useIcon } from '@keqi.gress/plugin-bridge'
import type { FormInst, FormRules, DataTableColumns } from 'naive-ui'
import { NIcon, NTag, NButtonGroup } from 'naive-ui'

// 图标
const DownloadOutline = useIcon('DownloadOutline')
const Refresh = useIcon('RefreshOutline')
const AppsOutline = useIcon('AppsOutline')
const PeopleOutline = useIcon('PeopleOutline')
const TimeOutline = useIcon('TimeOutline')
const TrophyOutline = useIcon('TrophyOutline')

import { statisticsApi } from '../api'
import type {
  StatisticsOverview,
  TrendData,
  TrendPoint,
  TopPlugin,
  ExportRequest
} from '../types'

// 消息提示
const message = useMessage()

// 数据状态
const loading = ref(false)
const refreshLoading = ref(false)
const exportLoading = ref(false)
const dateRange = ref<[number, number] | null>(null)
const quickRange = ref('30d')

// 统计概览数据
const overview = ref<StatisticsOverview>({
  totalPlugins: 0,
  totalInstalls: 0,
  totalActiveUsers: 0,
  totalDownloads: 0,
  pendingReviews: 0,
  approvedToday: 0,
  rejectedToday: 0,
  newSubmissionsToday: 0,
  topPlugins: []
})

// 趋势数据
const trendData = ref<TrendData>({
  installTrend: [],
  activeTrend: [],
  downloadTrend: [],
  submissionTrend: []
})

// 导出对话框
const showExportModal = ref(false)
const exportFormRef = ref<FormInst | null>(null)
const exportForm = reactive<ExportRequest>({
  format: 'CSV',
  includeDetails: false
})

const exportRules: FormRules = {
  format: {
    required: true,
    message: '请选择导出格式',
    trigger: 'change'
  }
}

const formatOptions = [
  { label: 'CSV', value: 'CSV' },
  { label: 'Excel', value: 'EXCEL' },
  { label: 'JSON', value: 'JSON' }
]

// 热门插件表格列配置
const topPluginsColumns: DataTableColumns<TopPlugin> = [
  {
    title: '排名',
    key: 'rank',
    width: 80,
    render: (_row: TopPlugin, index: number) => {
      return h('div', { class: 'rank-badge' }, [
        h(NIcon, {
          component: TrophyOutline,
          size: 16,
          color: index < 3 ? '#f0a020' : '#999'
        }),
        h('span', { style: 'margin-left: 4px;' }, index + 1)
      ])
    }
  },
  {
    title: '插件名称',
    key: 'pluginName',
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '安装次数',
    key: 'installCount',
    width: 120,
    render: (row: TopPlugin) => formatNumber(row.installCount)
  },
  {
    title: '活跃用户',
    key: 'activeUsers',
    width: 120,
    render: (row: TopPlugin) => formatNumber(row.activeUsers)
  },
  {
    title: '评分',
    key: 'rating',
    width: 100,
    render: (row: TopPlugin) => row.rating ? `${row.rating.toFixed(1)} ⭐` : '-'
  }
]

/**
 * 加载数据
 */
const loadData = async () => {
  loading.value = true
  refreshLoading.value = true
  try {
    // 加载统计概览
    const overviewRes = await statisticsApi.getOverview()
    if (overviewRes.success && overviewRes.data) {
      overview.value = overviewRes.data
    } else {
      console.warn('获取统计概览失败:', overviewRes.message)
    }

    // 加载趋势数据
    const params = getDateRangeParams()
    const trendRes = await statisticsApi.getTrendData(params)

      trendData.value = trendRes


    message.success('数据加载成功')
  } catch (error: any) {
    console.error('加载数据失败:', error)
    message.error(error.message || '加载数据失败')
    // 确保即使出错也有默认值
    trendData.value = {
      installTrend: [],
      activeTrend: [],
      downloadTrend: [],
      submissionTrend: []
    }
  } finally {
    loading.value = false
    refreshLoading.value = false
  }
}

/**
 * 获取日期范围参数
 */
const getDateRangeParams = () => {
  if (dateRange.value) {
    return {
      startDate: formatDateToString(new Date(dateRange.value[0])),
      endDate: formatDateToString(new Date(dateRange.value[1]))
    }
  }
  return {}
}

/**
 * 处理日期范围变化
 */
const handleDateRangeChange = () => {
  quickRange.value = ''
  loadData()
}

/**
 * 设置快捷日期范围
 */
const setQuickRange = (range: string) => {
  quickRange.value = range
  const now = new Date()
  const start = new Date()

  switch (range) {
    case '7d':
      start.setDate(now.getDate() - 7)
      break
    case '30d':
      start.setDate(now.getDate() - 30)
      break
    case '90d':
      start.setDate(now.getDate() - 90)
      break
  }

  dateRange.value = [start.getTime(), now.getTime()]
  loadData()
}

/**
 * 处理导出
 */
const handleExport = () => {
  showExportModal.value = true
}

/**
 * 确认导出
 */
const confirmExport = async () => {
  try {
    await exportFormRef.value?.validate()

    exportLoading.value = true
    const params = {
      ...exportForm,
      ...getDateRangeParams()
    }

    const res = await statisticsApi.exportStatistics(params)

      // 创建下载链接
      const blob = new Blob([res], { type: 'text/plain' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `statistics_${Date.now()}.${exportForm.format.toLowerCase()}`
      link.click()
      window.URL.revokeObjectURL(url)

      message.success('导出成功')
      showExportModal.value = false
      return true

  } catch (error: any) {
    if (error.errors) {
      // 表单验证错误
      return false
    }
    message.error(error.message || '导出失败')
    return false
  } finally {
    exportLoading.value = false
  }
}

/**
 * 格式化数字
 */
const formatNumber = (num: number): string => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

/**
 * 格式化日期
 */
const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

/**
 * 格式化日期为字符串
 */
const formatDateToString = (date: Date): string => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

/**
 * 计算柱状图高度
 */
const getBarHeight = (value: number, data: TrendPoint[]): string => {
  const max = Math.max(...data.map(d => d.value))
  if (max === 0) return '0%'
  return `${(value / max) * 100}%`
}

// 组件挂载时加载数据
onMounted(() => {
  setQuickRange('30d')
})
</script>

<style scoped>
.statistics-analysis-page {
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

.filter-card {
  margin-bottom: 24px;

  .filter-label {
    font-weight: 500;
    color: var(--n-text-color);
  }
}

.overview-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;

  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 16px;

      .stat-icon {
        width: 64px;
        height: 64px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
      }

      .stat-info {
        flex: 1;

        .stat-label {
          font-size: 14px;
          color: var(--n-text-color-3);
          margin-bottom: 8px;
        }

        .stat-value {
          font-size: 28px;
          font-weight: 600;
          color: var(--n-text-color);
        }
      }
    }
  }
}

.today-stats {
  margin-bottom: 24px;

  .stat-unit {
    font-size: 14px;
    color: var(--n-text-color-3);
    margin-left: 4px;
  }
}

.trend-charts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 16px;
  margin-bottom: 24px;

  .chart-card {
    .chart-container {
      height: 300px;
      padding: 20px 0;
      position: relative;

      .simple-chart {
        display: flex;
        align-items: flex-end;
        justify-content: space-around;
        height: 100%;
        gap: 8px;

        .chart-bar {
          flex: 1;
          background: linear-gradient(180deg, #2080f0 0%, #4098fc 100%);
          border-radius: 4px 4px 0 0;
          min-height: 20px;
          position: relative;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            opacity: 0.8;
            transform: translateY(-2px);
          }

          .bar-value {
            position: absolute;
            top: -24px;
            left: 50%;
            transform: translateX(-50%);
            font-size: 12px;
            color: var(--n-text-color);
            white-space: nowrap;
          }
        }
      }

      .empty-chart {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        color: var(--n-text-color-3);
        font-size: 14px;
      }
    }

    .chart-labels {
      display: flex;
      justify-content: space-around;
      margin-top: 12px;
      padding: 0 4px;

      .chart-label {
        font-size: 12px;
        color: var(--n-text-color-3);
        text-align: center;
        flex: 1;
      }
    }
  }
}

.top-plugins {
  .rank-badge {
    display: flex;
    align-items: center;
  }
}
</style>
