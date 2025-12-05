<template>
  <div class="permission-management">
    <n-card title="权限管理" :bordered="false" size="small">
      <template #header-extra>
        <n-button type="primary" @click="$router.push('/permission/permissions')">
          <template #icon>
            <n-icon><SafetyOutlined /></n-icon>
          </template>
          管理权限
        </n-button>
      </template>

      <n-space vertical size="large">
        <n-alert type="info">
          权限管理功能已迁移到权限管理模块，点击上方按钮进入详细管理页面
        </n-alert>
        
        <n-descriptions title="权限管理功能" :column="2" bordered>
          <n-descriptions-item label="权限列表">
            查看和管理所有权限信息
          </n-descriptions-item>
          <n-descriptions-item label="权限树">
            以树形结构展示权限层级关系
          </n-descriptions-item>
          <n-descriptions-item label="权限类型">
            支持菜单、按钮、接口、数据权限
          </n-descriptions-item>
          <n-descriptions-item label="权限检查">
            提供细粒度的权限验证功能
          </n-descriptions-item>
        </n-descriptions>
      </n-space>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { SafetyOutlined } from '@vicons/antd';
</script>

<style scoped>
.permission-management {
  padding: 20px;
}

.permission-management :deep(.n-card) {
  border-radius: 8px;
}
</style>