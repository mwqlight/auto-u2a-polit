<template>
  <div class="organization-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <n-icon size="28" class="title-icon">
            <BuildingIcon />
          </n-icon>
          组织架构管理
        </h1>
        <p class="page-desc">管理系统组织架构和部门</p>
      </div>
      <div class="header-right">
        <n-space>
          <n-button type="primary" @click="handleCreateOrganization">
            <template #icon>
              <n-icon><PlusIcon /></n-icon>
            </template>
            新建组织
          </n-button>
          <n-button @click="handleExport">
            <template #icon>
              <n-icon><DownloadIcon /></n-icon>
            </template>
            导出
          </n-button>
        </n-space>
      </div>
    </div>

    <!-- 组织架构树 -->
    <n-card class="tree-card">
      <div class="tree-container">
        <n-tree
          ref="treeRef"
          :data="organizationTree"
          :loading="loading"
          :expanded-keys="expandedKeys"
          :selected-keys="selectedKeys"
          :checkable="false"
          :render-label="renderLabel"
          @update:expanded-keys="handleExpandedKeysChange"
          @update:selected-keys="handleSelectedKeysChange"
        />
      </div>
    </n-card>

    <!-- 组织详情 -->
    <n-card class="detail-card" v-if="selectedOrganization">
      <div class="detail-header">
        <h2 class="detail-title">组织详情</h2>
        <n-space>
          <n-button type="primary" @click="handleEditOrganization">
            <template #icon>
              <n-icon><EditIcon /></n-icon>
            </template>
            编辑
          </n-button>
          <n-button type="danger" @click="handleDeleteOrganization">
            <template #icon>
              <n-icon><DeleteIcon /></n-icon>
            </template>
            删除
          </n-button>
        </n-space>
      </div>

      <n-form
        ref="detailFormRef"
        :model="detailForm"
        label-placement="top"
        label-width="120px"
      >
        <n-form-item label="组织名称" path="name">
          <n-input
            v-model:value="detailForm.name"
            placeholder="请输入组织名称"
            disabled
          />
        </n-form-item>

        <n-form-item label="组织编码" path="code">
          <n-input
            v-model:value="detailForm.code"
            placeholder="请输入组织编码"
            disabled
          />
        </n-form-item>

        <n-form-item label="组织类型" path="type">
          <n-select
            v-model:value="detailForm.type"
            placeholder="请选择组织类型"
            :options="typeOptions"
            disabled
          />
        </n-form-item>

        <n-form-item label="父组织" path="parentId">
          <n-input
            v-model:value="detailForm.parentName"
            placeholder="请选择父组织"
            disabled
          />
        </n-form-item>

        <n-form-item label="描述" path="description">
          <n-input
            v-model:value="detailForm.description"
            placeholder="请输入组织描述"
            disabled
            type="textarea"
            :rows="3"
          />
        </n-form-item>

        <n-form-item label="状态" path="status">
          <n-select
            v-model:value="detailForm.status"
            placeholder="请选择状态"
            :options="statusOptions"
            disabled
          />
        </n-form-item>
      </n-form>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useMessage } from 'naive-ui'
import type { FormInstance } from 'naive-ui'
import { BuildingIcon, PlusIcon, DownloadIcon, EditIcon, DeleteIcon } from '@vicons/ionicons5'
import { organizationApi } from '@/api/modules/organization'
import type { Organization } from '@/types/organization'

// 状态
const loading = ref(false)
const organizationTree = ref<any[]>([])
const expandedKeys = ref<string[]>([])
const selectedKeys = ref<string[]>([])
const selectedOrganization = ref<Organization | null>(null)

// 表单引用
const treeRef = ref<any>(null)
const detailFormRef = ref<FormInstance | null>(null)

// 表单数据
const detailForm = ref({
  name: '',
  code: '',
  type: '',
  parentId: '',
  parentName: '',
  description: '',
  status: ''
})

// 选项
const typeOptions = ref([
  { label: '公司', value: 'COMPANY' },
  { label: '部门', value: 'DEPARTMENT' },
  { label: '团队', value: 'TEAM' },
  { label: '小组', value: 'GROUP' }
])

const statusOptions = ref([
  { label: '启用', value: 'ACTIVE' },
  { label: '禁用', value: 'INACTIVE' }
])

// 消息提示
const message = useMessage()

// 渲染组织标签
const renderLabel = ({ node }: any) => {
  return (
    <div class="tree-node-label">
      <span class="node-name">{node.label}</span>
      <span class="node-code">{node.code}</span>
      <n-tag type={node.status === 'ACTIVE' ? 'success' : 'warning'} size="small">
        {node.status === 'ACTIVE' ? '启用' : '禁用'}
      </n-tag>
    </div>
  )
}

// 加载组织架构树
const loadOrganizationTree = async () => {
  loading.value = true
  try {
    const response = await organizationApi.getOrganizationTree()
    if (response.code === 200) {
      organizationTree.value = response.data
      // 展开所有节点
      expandedKeys.value = getAllKeys(response.data)
    } else {
      message.error(response.message || '加载组织架构失败')
    }
  } catch (error: any) {
    message.error(error.message || '加载组织架构失败')
  } finally {
    loading.value = false
  }
}

// 获取所有节点的key
const getAllKeys = (nodes: any[]): string[] => {
  const keys: string[] = []
  for (const node of nodes) {
    keys.push(node.key)
    if (node.children && node.children.length > 0) {
      keys.push(...getAllKeys(node.children))
    }
  }
  return keys
}

// 展开节点变化
const handleExpandedKeysChange = (keys: string[]) => {
  expandedKeys.value = keys
}

// 选中节点变化
const handleSelectedKeysChange = (keys: string[]) => {
  selectedKeys.value = keys
  if (keys.length > 0) {
    findOrganization(keys[0])
  } else {
    selectedOrganization.value = null
  }
}

// 查找组织
const findOrganization = (key: string) => {
  const findNode = (nodes: any[]): any => {
    for (const node of nodes) {
      if (node.key === key) {
        return node
      }
      if (node.children && node.children.length > 0) {
        const found = findNode(node.children)
        if (found) return found
      }
    }
    return null
  }

  const node = findNode(organizationTree.value)
  if (node) {
    selectedOrganization.value = node
    detailForm.value = {
      name: node.label,
      code: node.code,
      type: node.type,
      parentId: node.parentId,
      parentName: node.parentName || '无',
      description: node.description || '',
      status: node.status
    }
  }
}

// 新建组织
const handleCreateOrganization = () => {
  // TODO: 打开新建组织对话框
  message.info('新建组织功能开发中')
}

// 编辑组织
const handleEditOrganization = () => {
  // TODO: 打开编辑组织对话框
  message.info('编辑组织功能开发中')
}

// 删除组织
const handleDeleteOrganization = () => {
  // TODO: 确认删除组织
  message.info('删除组织功能开发中')
}

// 导出组织架构
const handleExport = () => {
  // TODO: 导出组织架构
  message.info('导出组织架构功能开发中')
}

// 页面挂载时加载数据
onMounted(() => {
  loadOrganizationTree()
})
</script>

<style lang="scss" scoped>
.organization-management-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  .page-title {
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 10px;

    .title-icon {
      color: #1890ff;
    }
  }

  .page-desc {
    font-size: 14px;
    color: #8c8c8c;
    margin: 5px 0 0 0;
  }
}

.tree-card {
  margin-bottom: 20px;
}

.tree-container {
  height: 500px;
  overflow-y: auto;
}

.tree-node-label {
  display: flex;
  align-items: center;
  gap: 10px;

  .node-name {
    flex: 1;
  }

  .node-code {
    font-size: 12px;
    color: #8c8c8c;
  }
}

.detail-card {
  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .detail-title {
      font-size: 18px;
      font-weight: 600;
      margin: 0;
    }
  }
}
</style>
