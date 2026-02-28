<template>
  <div class="icon-test">
    <h3>SVG 图标测试</h3>
    
    <!-- 方式1: 直接使用 SVG 组件 -->
    <div class="test-item">
      <span>直接使用:</span>
      <TestIcon class="icon" />
    </div>
    
    <!-- 方式2: 在 NIcon 中使用 -->
    <div class="test-item">
      <span>NIcon 包装:</span>
      <n-icon :size="24">
        <TestIcon />
      </n-icon>
    </div>
    
    <!-- 方式3: 动态颜色 -->
    <div class="test-item">
      <span>动态颜色:</span>
      <TestIcon class="icon" style="color: #18a058;" />
      <TestIcon class="icon" style="color: #d03050;" />
      <TestIcon class="icon" style="color: #2080f0;" />
    </div>
  </div>
</template>

<script setup lang="ts">
import TestIcon from '@/assets/icons/test-icon.svg'
import { NIcon } from 'naive-ui'
</script>

<style scoped>
.icon-test {
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background: #fafafa;
}

.test-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin: 12px 0;
}

.icon {
  width: 24px;
  height: 24px;
}
</style>
