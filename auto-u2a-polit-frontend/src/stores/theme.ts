import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useStorage } from '@vueuse/core'

export const useThemeStore = defineStore('theme', () => {
  // 主题模式：light | dark | auto
  const themeMode = useStorage<'light' | 'dark' | 'auto'>('theme-mode', 'dark')
  
  // 是否暗色主题
  const isDark = computed(() => {
    if (themeMode.value === 'auto') {
      return window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    return themeMode.value === 'dark'
  })
  
  // 切换主题
  const toggleTheme = () => {
    if (themeMode.value === 'light') {
      themeMode.value = 'dark'
    } else if (themeMode.value === 'dark') {
      themeMode.value = 'auto'
    } else {
      themeMode.value = 'light'
    }
  }
  
  // 设置主题
  const setTheme = (mode: 'light' | 'dark' | 'auto') => {
    themeMode.value = mode
  }
  
  return {
    themeMode,
    isDark,
    toggleTheme,
    setTheme
  }
})