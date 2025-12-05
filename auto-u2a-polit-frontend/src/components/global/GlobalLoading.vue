<template>
  <div v-if="loading" class="global-loading-overlay">
    <div class="global-loading-content">
      <n-spin size="large" :show-description="false" />
      <p class="loading-text">{{ loadingText }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useLoadingStore } from '@/stores/loading'
import { NSpin } from 'naive-ui'

// 获取加载状态
const loadingStore = useLoadingStore()
const loading = computed(() => loadingStore.loading)
const loadingText = computed(() => loadingStore.loadingText)
</script>

<style lang="scss" scoped>
.global-loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
  backdrop-filter: blur(2px);
}

.global-loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  padding: 32px;
  background-color: var(--n-color-card);
  border-radius: 8px;
  box-shadow: var(--n-shadow-popover);
}

.loading-text {
  margin: 0;
  font-size: 14px;
  color: var(--n-color-text-2);
  font-weight: 500;
}
</style>