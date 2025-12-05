<template>
  <n-config-provider :theme="theme" :theme-overrides="themeOverrides" :locale="zhCN" :date-locale="dateZhCN">
    <n-loading-bar-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-message-provider>
            <router-view />
            <global-loading />
          </n-message-provider>
        </n-notification-provider>
      </n-dialog-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { NConfigProvider, NLoadingBarProvider, NDialogProvider, NNotificationProvider, NMessageProvider } from 'naive-ui'
import { zhCN, dateZhCN, darkTheme } from 'naive-ui'
import { useThemeStore } from '@/stores/theme'
import GlobalLoading from '@/components/global/GlobalLoading.vue'

// 主题配置
const themeStore = useThemeStore()
const theme = computed(() => themeStore.isDark ? darkTheme : null)

// 主题覆盖配置
const themeOverrides = computed(() => ({
  common: {
    primaryColor: '#00f3ff',
    primaryColorHover: '#00c4cc',
    primaryColorPressed: '#009599',
    primaryColorSuppl: '#00f3ff'
  },
  Layout: {
    siderColor: '#0d0f19',
    headerColor: '#0d0f19'
  }
}))
</script>

<style lang="scss">
#app {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
  overflow: hidden;
}

// 全局样式重置
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
  background: #0d0f19;
  color: #ffffff;
}

// 滚动条样式
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #1a1d2e;
}

::-webkit-scrollbar-thumb {
  background: #00f3ff;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #00c4cc;
}
</style>