<template>
  <div class="role-management">
    <n-card title="角色管理" :bordered="false" size="small">
      <template #header-extra>
        <n-button type="primary" @click="$router.push('/permission/roles')">
          <template #icon>
            <n-icon><SettingOutlined /></n-icon>
          </template>
          管理角色
        </n-button>
      </template>

      <n-space vertical size="large">
        <n-alert type="info">
          角色管理功能已迁移到权限管理模块，点击上方按钮进入详细管理页面
        </n-alert>
        
        <n-descriptions title="角色管理功能" :column="2" bordered>
          <n-descriptions-item label="角色列表">
            查看和管理所有角色信息
          </n-descriptions-item>
          <n-descriptions-item label="角色分配">
            为用户分配和移除角色
          </n-descriptions-item>
          <n-descriptions-item label="权限分配">
            为角色分配和移除权限
          </n-descriptions-item>
          <n-descriptions-item label="角色继承">
            支持角色之间的继承关系
          </n-descriptions-item>
        </n-descriptions>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { SettingOutlined } from '@vicons/antd';
</script>

<style scoped>
.role-management {
  padding: 20px;
}

.role-management :deep(.n-card) {
  border-radius: 8px;
}
</style>