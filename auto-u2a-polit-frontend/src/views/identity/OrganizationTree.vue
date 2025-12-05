<template>
  <div class="organization-tree-container">
    <div class="page-header">
      <div class="header-info">
        <h1>组织架构管理</h1>
        <p>管理企业组织架构，支持无限层级组织树和多维度部门视图</p>
      </div>
      <div class="header-actions">
        <n-button 
          type="primary" 
          @click="showCreateModal = true"
          :disabled="!selectedOrg"
        >
          <template #icon>
            <n-icon><PlusOutlined /></n-icon>
          </template>
          新建子部门
        </n-button>
        <n-button 
          type="primary" 
          @click="showCreateModal = true"
        >
          <template #icon>
            <n-icon><PlusOutlined /></n-icon>
          </template>
          新建根部门
        </n-button>
        <n-button 
          @click="handleRefresh"
        >
          <template #icon>
            <n-icon><ReloadOutlined /></n-icon>
          </template>
          刷新
        </n-button>
        <n-button 
          @click="handleExport"
        >
          <template #icon>
            <n-icon><ExportOutlined /></n-icon>
          </template>
          导出
        </n-button>
      </div>
    </div>

    <div class="content-wrapper">
      <!-- 左侧组织树 -->
      <div class="tree-panel">
        <div class="search-box">
          <n-input
            v-model:value="searchKeyword"
            placeholder="搜索组织名称或编码"
            size="small"
            @input="handleSearch"
          >
            <template #prefix>
              <n-icon><SearchOutlined /></n-icon>
            </template>
          </n-input>
        </div>
        <div class="tree-container">
          <n-tree
            ref="treeRef"
            :data="organizationTree"
            :default-expand-all="true"
            :filter-node-method="filterNode"
            :render-label="renderTreeNode"
            @update:selected-keys="handleNodeSelect"
          />
        </div>
      </div>

      <!-- 右侧组织详情 -->
      <div class="detail-panel">
        <div v-if="selectedOrg" class="detail-content">
          <div class="detail-header">
            <h2>{{ selectedOrg.name }}</h2>
            <div class="detail-actions">
              <n-button 
                size="small" 
                @click="showEditModal = true"
              >
                <template #icon>
                  <n-icon><EditOutlined /></n-icon>
                </template>
                编辑
              </n-button>
              <n-button 
                size="small" 
                @click="handleMoveOrg"
              >
                <template #icon>
                  <n-icon><SwapOutlined /></n-icon>
                </template>
                移动
              </n-button>
              <n-button 
                size="small" 
                type="primary"
                @click="showAddMemberModal = true"
              >
                <template #icon>
                  <n-icon><UserAddOutlined /></n-icon>
                </template>
                添加成员
              </n-button>
              <n-button 
                size="small" 
                type="danger"
                @click="handleDeleteOrg"
              >
                <template #icon>
                  <n-icon><DeleteOutlined /></n-icon>
                </template>
                删除
              </n-button>
            </div>
          </div>

          <!-- 组织基本信息 -->
          <n-card title="基本信息" size="small" class="detail-card">
            <n-descriptions :column="2" bordered size="small">
              <n-descriptions-item label="组织编码">
                {{ selectedOrg.code }}
              </n-descriptions-item>
              <n-descriptions-item label="组织类型">
                <n-tag :type="getOrgTypeTagType(selectedOrg.type)">
                  {{ getOrgTypeLabel(selectedOrg.type) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="状态">
                <n-tag :type="selectedOrg.status === 'ACTIVE' ? 'success' : 'default'">
                  {{ selectedOrg.status === 'ACTIVE' ? '启用' : '禁用' }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="成员数量">
                {{ selectedOrg.memberCount }}
              </n-descriptions-item>
              <n-descriptions-item label="负责人">
                {{ selectedOrg.managerName || '未设置' }}
              </n-descriptions-item>
              <n-descriptions-item label="创建时间">
                {{ formatDate(selectedOrg.createdAt) }}
              </n-descriptions-item>
              <n-descriptions-item label="更新时间" :span="2">
                {{ formatDate(selectedOrg.updatedAt) }}
              </n-descriptions-item>
              <n-descriptions-item label="描述" :span="2">
                {{ selectedOrg.description || '无' }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- 组织成员列表 -->
          <n-card title="组织成员" size="small" class="detail-card">
            <div class="member-table-header">
              <n-input
                v-model:value="memberSearchKeyword"
                placeholder="搜索成员姓名或邮箱"
                size="small"
                style="width: 200px"
                @input="handleMemberSearch"
              >
                <template #prefix>
                  <n-icon><SearchOutlined /></n-icon>
                </template>
              </n-input>
            </div>
            <n-data-table
              :data="filteredMembers"
              :columns="memberColumns"
              :pagination="memberPagination"
              size="small"
              bordered
              :loading="memberLoading"
            />
          </n-card>
        </div>

        <div v-else class="empty-state">
          <n-empty description="请选择一个组织查看详情" />
        </div>
      </div>
    </div>

    <!-- 新建/编辑组织对话框 -->
    <n-modal
      v-model:show="showCreateModal || showEditModal"
      :title="showEditModal ? '编辑组织' : '新建组织'"
      width="500px"
      @ok="handleModalConfirm"
      @cancel="handleModalCancel"
    >
      <n-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-placement="left"
        label-width="100px"
      >
        <n-form-item label="组织名称" path="name">
          <n-input v-model:value="formData.name" placeholder="请输入组织名称" />
        </n-form-item>
        <n-form-item label="组织编码" path="code">
          <n-input v-model:value="formData.code" placeholder="请输入组织编码" />
        </n-form-item>
        <n-form-item label="组织类型" path="type">
          <n-select v-model:value="formData.type" placeholder="请选择组织类型">
            <n-option label="公司" value="COMPANY" />
            <n-option label="部门" value="DEPARTMENT" />
            <n-option label="团队" value="TEAM" />
            <n-option label="群组" value="GROUP" />
            <n-option label="事业部" value="DIVISION" />
            <n-option label="单元" value="UNIT" />
          </n-select>
        </n-form-item>
        <n-form-item label="上级组织" path="parentId" v-if="!showEditModal">
          <n-tree-select
            v-model:value="formData.parentId"
            :data="organizationTree"
            placeholder="请选择上级组织"
            :filterable="true"
            :render-label="renderTreeNode"
          />
        </n-form-item>
        <n-form-item label="负责人" path="managerId">
          <n-select v-model:value="formData.managerId" placeholder="请选择负责人" :filterable="true">
            <n-option 
              v-for="user in userList" 
              :key="user.id" 
              :label="user.displayName || user.username" 
              :value="user.id"
            />
          </n-select>
        </n-form-item>
        <n-form-item label="描述" path="description">
          <n-input
            v-model:value="formData.description"
            placeholder="请输入组织描述"
            type="textarea"
            :rows="3"
          />
        </n-form-item>
      </n-form>
    </n-modal>

    <!-- 移动组织对话框 -->
    <n-modal
      v-model:show="showMoveModal"
      title="移动组织"
      width="500px"
      @ok="handleMoveConfirm"
      @cancel="handleMoveCancel"
    >
      <div>
        <p>请选择新的上级组织：</p>
        <n-tree-select
          v-model:value="moveTargetParentId"
          :data="organizationTree"
          placeholder="请选择上级组织"
          :filterable="true"
          :render-label="renderTreeNode"
          style="margin-top: 12px"
        />
      </div>
    </n-modal>

    <!-- 添加成员对话框 -->
    <n-modal
      v-model:show="showAddMemberModal"
      title="添加组织成员"
      width="600px"
      @ok="handleAddMemberConfirm"
      @cancel="handleAddMemberCancel"
    >
      <n-transfer
        v-model:target-keys="selectedMemberIds"
        :data="availableUsers"
        :titles="['可选用户', '已选用户']"
        :render="renderUserOption"
        :filterable="true"
        filter-placeholder="搜索用户姓名或邮箱"
      />
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { 
  PlusOutlined, 
  ReloadOutlined, 
  ExportOutlined, 
  SearchOutlined, 
  EditOutlined, 
  SwapOutlined, 
  UserAddOutlined, 
  DeleteOutlined 
} from '@ant-design/icons-vue'
import { 
  NTree, 
  NTreeSelect, 
  NDataTable, 
  NModal, 
  NForm, 
  NFormItem, 
  NInput, 
  NSelect, 
  NOption, 
  NCard, 
  NDescriptions, 
  NDescriptionsItem, 
  NTag, 
  NEmpty, 
  NTransfer 
} from 'naive-ui'
import { message, dialog } from 'naive-ui'
import type { TreeInst, TableColumns } from 'naive-ui'
import { 
  getOrganizationTree, 
  createOrganization, 
  updateOrganization, 
  moveOrganization, 
  deleteOrganization, 
  getOrganizationMembers, 
  addOrganizationMembers, 
  removeOrganizationMembers 
} from '@/api/organization'
import { getUsers } from '@/api/user'
import type { Organization, OrganizationCreateRequest, OrganizationUpdateRequest } from '@/api/organization'
import type { UserInfo } from '@/api/user'

// 响应式数据
const organizationTree = ref<Organization[]>([])
const selectedOrg = ref<Organization | null>(null)
const searchKeyword = ref('')
const treeRef = ref<TreeInst>()

// 成员相关
const members = ref<UserInfo[]>([])
const memberSearchKeyword = ref('')
const memberLoading = ref(false)
const memberPagination = ref({
  page: 1,
  pageSize: 10,
  itemCount: 0
})

// 对话框相关
const showCreateModal = ref(false)
const showEditModal = ref(false)
const showMoveModal = ref(false)
const showAddMemberModal = ref(false)
const formRef = ref()
const formData = ref({
  name: '',
  code: '',
  type: 'DEPARTMENT' as const,
  parentId: undefined as string | undefined,
  managerId: undefined as string | undefined,
  description: ''
})
const moveTargetParentId = ref<string | undefined>()

// 用户相关
const userList = ref<UserInfo[]>([])
const availableUsers = ref<UserInfo[]>([])
const selectedMemberIds = ref<string[]>([])

// 表单规则
const formRules = {
  name: [
    { required: true, message: '请输入组织名称', trigger: 'blur' },
    { min: 2, max: 50, message: '组织名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入组织编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '组织编码只能包含字母、数字、下划线和短横线', trigger: 'blur' },
    { min: 2, max: 30, message: '组织编码长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择组织类型', trigger: 'change' }
  ]
}

// 计算属性
const filteredMembers = computed(() => {
  if (!memberSearchKeyword.value) return members.value
  const keyword = memberSearchKeyword.value.toLowerCase()
  return members.value.filter(user => 
    user.username.toLowerCase().includes(keyword) ||
    user.displayName?.toLowerCase().includes(keyword) ||
    user.email?.toLowerCase().includes(keyword)
  )
})

// 成员表格列
const memberColumns: TableColumns<UserInfo> = [
  {
    title: '用户名',
    key: 'username',
    width: 120
  },
  {
    title: '姓名',
    key: 'displayName',
    width: 120
  },
  {
    title: '邮箱',
    key: 'email',
    ellipsis: true
  },
  {
    title: '手机号',
    key: 'phone',
    width: 130
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => (
      <n-tag type={row.status === 'ACTIVE' ? 'success' : 'default'}>
        {row.status === 'ACTIVE' ? '启用' : '禁用'}
      </n-tag>
    )
  },
  {
    title: '操作',
    key: 'actions',
    width: 80,
    render: (row) => (
      <n-button
        size="small"
        type="danger"
        text
        @click={() => handleRemoveMember(row.id)}
      >
        移除
      </n-button>
    )
  }
]

// 方法
const loadOrganizationTree = async () => {
  try {
    const response = await getOrganizationTree()
    organizationTree.value = response.data
  } catch (error) {
    message.error('加载组织架构失败')
  }
}

const loadUserList = async () => {
  try {
    const response = await getUsers({ pageSize: 1000 })
    userList.value = response.data.items
  } catch (error) {
    message.error('加载用户列表失败')
  }
}

const loadOrganizationMembers = async (orgId: string) => {
  if (!orgId) return
  
  memberLoading.value = true
  try {
    const response = await getOrganizationMembers(orgId, false, {
      page: memberPagination.value.page,
      pageSize: memberPagination.value.pageSize
    })
    members.value = response.data.items
    memberPagination.value.itemCount = response.data.total
  } catch (error) {
    message.error('加载组织成员失败')
  } finally {
    memberLoading.value = false
  }
}

const handleSearch = (value: string) => {
  treeRef.value?.filter(value)
}

const filterNode = (value: string, node: any) => {
  return (
    node.data.name.toLowerCase().includes(value.toLowerCase()) ||
    node.data.code.toLowerCase().includes(value.toLowerCase())
  )
}

const renderTreeNode = (node: any) => {
  const org = node.data as Organization
  return (
    <div class="tree-node">
      <span class="node-name">{org.name}</span>
      <span class="node-code">({org.code})</span>
      <span class="node-count">{org.memberCount}</span>
    </div>
  )
}

const handleNodeSelect = (keys: string[]) => {
  if (keys.length > 0) {
    const orgId = keys[0]
    const org = findOrgById(organizationTree.value, orgId)
    if (org) {
      selectedOrg.value = org
      loadOrganizationMembers(orgId)
    }
  } else {
    selectedOrg.value = null
  }
}

const findOrgById = (tree: Organization[], id: string): Organization | null => {
  for (const org of tree) {
    if (org.id === id) {
      return org
    }
    if (org.children && org.children.length > 0) {
      const found = findOrgById(org.children, id)
      if (found) {
        return found
      }
    }
  }
  return null
}

const getOrgTypeLabel = (type: string) => {
  const typeMap: Record<string, string> = {
    COMPANY: '公司',
    DEPARTMENT: '部门',
    TEAM: '团队',
    GROUP: '群组',
    DIVISION: '事业部',
    UNIT: '单元'
  }
  return typeMap[type] || type
}

const getOrgTypeTagType = (type: string) => {
  const typeMap: Record<string, string> = {
    COMPANY: 'success',
    DEPARTMENT: 'primary',
    TEAM: 'info',
    GROUP: 'warning',
    DIVISION: 'error',
    UNIT: 'default'
  }
  return typeMap[type] || 'default'
}

const formatDate = (dateString: string) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString()
}

const handleRefresh = () => {
  loadOrganizationTree()
  if (selectedOrg.value) {
    loadOrganizationMembers(selectedOrg.value.id)
  }
}

const handleExport = () => {
  message.info('导出功能开发中...')
}

const handleCreateOrg = () => {
  showCreateModal.value = true
  formData.value = {
    name: '',
    code: '',
    type: 'DEPARTMENT',
    parentId: selectedOrg.value?.id,
    managerId: undefined,
    description: ''
  }
}

const handleEditOrg = () => {
  if (!selectedOrg.value) return
  
  showEditModal.value = true
  formData.value = {
    name: selectedOrg.value.name,
    code: selectedOrg.value.code,
    type: selectedOrg.value.type,
    parentId: selectedOrg.value.parentId,
    managerId: selectedOrg.value.managerId,
    description: selectedOrg.value.description || ''
  }
}

const handleMoveOrg = () => {
  if (!selectedOrg.value) return
  
  showMoveModal.value = true
  moveTargetParentId.value = selectedOrg.value.parentId
}

const handleDeleteOrg = () => {
  if (!selectedOrg.value) return
  
  dialog.error({
    title: '确认删除',
    content: `确定要删除组织 "${selectedOrg.value.name}" 吗？此操作不可恢复。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteOrganization(selectedOrg.value!.id)
        message.success('删除组织成功')
        selectedOrg.value = null
        loadOrganizationTree()
      } catch (error) {
        message.error('删除组织失败')
      }
    }
  })
}

const handleModalConfirm = async () => {
  try {
    await formRef.value?.validate()
    
    if (showEditModal.value && selectedOrg.value) {
      const updateData: OrganizationUpdateRequest = {
        name: formData.value.name,
        code: formData.value.code,
        type: formData.value.type,
        managerId: formData.value.managerId,
        description: formData.value.description
      }
      await updateOrganization(selectedOrg.value.id, updateData)
      message.success('更新组织成功')
    } else {
      const createData: OrganizationCreateRequest = {
        name: formData.value.name,
        code: formData.value.code,
        type: formData.value.type,
        parentId: formData.value.parentId,
        managerId: formData.value.managerId,
        description: formData.value.description
      }
      await createOrganization(createData)
      message.success('创建组织成功')
    }
    
    showCreateModal.value = false
    showEditModal.value = false
    loadOrganizationTree()
  } catch (error) {
    message.error('表单验证失败')
  }
}

const handleModalCancel = () => {
  showCreateModal.value = false
  showEditModal.value = false
  formRef.value?.resetValidation()
}

const handleMoveConfirm = async () => {
  if (!selectedOrg.value || !moveTargetParentId.value) return
  
  try {
    await moveOrganization(selectedOrg.value.id, moveTargetParentId.value)
    message.success('移动组织成功')
    showMoveModal.value = false
    loadOrganizationTree()
  } catch (error) {
    message.error('移动组织失败')
  }
}

const handleMoveCancel = () => {
  showMoveModal.value = false
}

const handleAddMember = () => {
  if (!selectedOrg.value) return
  
  showAddMemberModal.value = true
  selectedMemberIds.value = []
  
  // 加载可用用户（排除已在组织中的用户）
  const existingMemberIds = members.value.map(m => m.id)
  availableUsers.value = userList.value.filter(user => 
    !existingMemberIds.includes(user.id)
  )
}

const handleAddMemberConfirm = async () => {
  if (!selectedOrg.value || selectedMemberIds.value.length === 0) return
  
  try {
    await addOrganizationMembers(selectedOrg.value.id, selectedMemberIds.value)
    message.success('添加成员成功')
    showAddMemberModal.value = false
    loadOrganizationMembers(selectedOrg.value.id)
  } catch (error) {
    message.error('添加成员失败')
  }
}

const handleAddMemberCancel = () => {
  showAddMemberModal.value = false
  selectedMemberIds.value = []
}

const handleRemoveMember = (userId: string) => {
  if (!selectedOrg.value) return
  
  dialog.warning({
    title: '确认移除',
    content: '确定要移除该成员吗？',
    positiveText: '移除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await removeOrganizationMembers(selectedOrg.value!.id, [userId])
        message.success('移除成员成功')
        loadOrganizationMembers(selectedOrg.value!.id)
      } catch (error) {
        message.error('移除成员失败')
      }
    }
  })
}

const handleMemberSearch = (value: string) => {
  // 搜索逻辑已在filteredMembers计算属性中处理
}

const renderUserOption = (user: UserInfo) => {
  return (
    <div class="user-option">
      <div class="user-info">
        <div class="user-name">{user.displayName || user.username}</div>
        <div class="user-email">{user.email}</div>
      </div>
    </div>
  )
}

// 生命周期
onMounted(() => {
  loadOrganizationTree()
  loadUserList()
})

// 监听selectedOrg变化
watch(selectedOrg, (newOrg) => {
  if (newOrg) {
    loadOrganizationMembers(newOrg.id)
  }
})
</script>

<style lang="scss" scoped>
.organization-tree-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

.header-info h1 {
  margin: 0 0 4px 0;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.header-info p {
  margin: 0;
  color: #8c8c8c;
  font-size: 14px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.content-wrapper {
  display: flex;
  flex: 1;
  overflow: hidden;
  padding: 24px;
  gap: 24px;
}

.tree-panel {
  width: 350px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.search-box {
  padding: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.tree-container {
  flex: 1;
  overflow: auto;
  padding: 8px;
}

.tree-node {
  display: flex;
  align-items: center;
  gap: 4px;
}

.node-name {
  font-weight: 500;
  color: #1a1a1a;
}

.node-code {
  font-size: 12px;
  color: #8c8c8c;
}

.node-count {
  margin-left: auto;
  font-size: 12px;
  color: #8c8c8c;
  background-color: #f5f5f5;
  padding: 2px 6px;
  border-radius: 10px;
}

.detail-panel {
  flex: 1;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  overflow: auto;
}

.detail-content {
  padding: 24px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.detail-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
}

.detail-actions {
  display: flex;
  gap: 8px;
}

.detail-card {
  margin-bottom: 24px;
}

.member-table-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-end;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.user-option {
  padding: 8px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-weight: 500;
  color: #1a1a1a;
}

.user-email {
  font-size: 12px;
  color: #8c8c8c;
}

@media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .tree-panel {
    width: 100%;
    height: 300px;
  }
}
</style>
