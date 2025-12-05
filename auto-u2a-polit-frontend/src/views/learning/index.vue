<template>
  <div class="learning-container">
    <n-layout has-sider class="h-full">
      <!-- 侧边栏 -->
      <n-layout-sider
        bordered
        collapse-mode="width"
        :collapsed-width="64"
        :width="240"
        :collapsed="collapsed"
        show-trigger
        @collapse="collapsed = true"
        @expand="collapsed = false"
      >
        <n-menu
          :collapsed="collapsed"
          :collapsed-width="64"
          :collapsed-icon-size="22"
          :options="menuOptions"
          :value="activeMenu"
          @update:value="handleMenuChange"
        />
      </n-layout-sider>

      <!-- 主内容区 -->
      <n-layout-content class="p-6">
        <router-view />
      </n-layout-content>
    </n-layout>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { NIcon } from 'naive-ui'
import {
  BookOutline,
  CodeSlashOutline,
  GitBranchOutline,
  CubeOutline,
  FlashOutline,
  SettingsOutline
} from '@vicons/ionicons5'

const route = useRoute()
const router = useRouter()
const collapsed = ref(false)

// 菜单配置
const menuOptions = [
  {
    label: '基础语法',
    key: '/learning/basic',
    icon: renderIcon(BookOutline)
  },
  {
    label: '函数与方法',
    key: '/learning/functions',
    icon: renderIcon(CodeSlashOutline)
  },
  {
    label: '数据结构',
    key: '/learning/data-structures',
    icon: renderIcon(CubeOutline)
  },
  {
    label: '并发编程',
    key: '/learning/concurrent',
    icon: renderIcon(GitBranchOutline)
  },
  {
    label: '高级特性',
    key: '/learning/advanced',
    icon: renderIcon(FlashOutline)
  },
  {
    label: '代码沙盒',
    key: '/learning/sandbox',
    icon: renderIcon(SettingsOutline)
  }
]

// 渲染图标函数
function renderIcon(icon: any) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

// 当前激活菜单
const activeMenu = computed(() => route.path)

// 菜单切换处理
const handleMenuChange = (key: string) => {
  router.push(key)
}
</script>

<style scoped>
.learning-container {
  height: calc(100vh - 64px);
}

.h-full {
  height: 100%;
}

.p-6 {
  padding: 24px;
}
</style>