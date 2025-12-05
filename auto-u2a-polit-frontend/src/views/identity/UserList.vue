<template>
  <div class="user-list-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <n-icon size="28" class="title-icon">
            <UserGroupIcon />
          </n-icon>
          用户管理
        </h1>
        <p class="page-desc">管理系统所有用户账户和权限</p>
      </div>
      <div class="header-right">
        <n-space>
          <n-button type="primary" @click="handleCreateUser">
            <template #icon>
              <n-icon><PlusIcon /></n-icon>
            </template>
            新建用户
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

    <!-- 搜索和筛选区域 -->
    <n-card class="filter-card">
      <n-form
        ref="filterFormRef"
        :model="filterForm"
        inline
        label-placement="left"
        label-width="auto"
      >
        <n-form-item label="用户名" path="username">
          <n-input
            v-model:value="filterForm.username"
            placeholder="请输入用户名"
            clearable
          />
        </n-form-item>
        
        <n-form-item label="邮箱" path="email">
          <n-input
            v-model:value="filterForm.email"
            placeholder="请输入邮箱"
            clearable
          />
        </n-form-item>
        
        <n-form-item label="状态" path="status">
          <n-select
            v-model:value="filterForm.status"
            placeholder="请选择状态"
            :options="statusOptions"
            clearable
          />
        </n-form-item>
        
        <n-form-item label="用户类型" path="type">
          <n-select
            v-model:value="filterForm.type"
            placeholder="请选择类型"
            :options="typeOptions"
            clearable
          />
        </n-form-item>
        
        <n-form-item>
          <n-space>
            <n-button type="primary" @click="handleSearch">
              <template #icon>
                <n-icon><SearchIcon /></n-icon>
              </template>
              搜索
            </n-button>
            <n-button @click="handleReset">
              <template #icon>
                <n-icon><RefreshIcon /></n-icon>
              </template>
              重置
            </n-button>
          </n-space>
        </n-form-item>
      </n-form>
    </n-card>

    <!-- 数据表格 -->
    <n-card class="table-card">
      <n-data-table
        :columns="columns"
        :data="userList"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row) => row.id"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </n-card>

    <!-- 新建/编辑用户对话框 -->
    <n-modal
      v-model:show="showUserModal"
      :mask-closable="false"
      preset="dialog"
      :title="modalTitle"
      positive-text="确认"
      negative-text="取消"
      @positive-click="handleModalConfirm"
      @negative-click="handleModalCancel"
    >
      <n-form
        ref="userFormRef"
        :model="userForm"
        :rules="userFormRules"
        label-placement="left"
        label-width="100px"
        require-mark-placement="right-hanging"
      >
        <n-form-item label="用户名" path="username">
          <n-input
            v-model:value="userForm.username"
            placeholder="请输入用户名"
            :disabled="!isCreate"
          />
        </n-form-item>
        
        <n-form-item label="邮箱" path="email">
          <n-input
            v-model:value="userForm.email"
            placeholder="请输入邮箱"
          />
        </n-form-item>
        
        <n-form-item label="手机号" path="phone">
          <n-input
            v-model:value="userForm.phone"
            placeholder="请输入手机号"
          />
        </n-form-item>
        
        <n-form-item label="真实姓名" path="realName">
          <n-input
            v-model:value="userForm.realName"
            placeholder="请输入真实姓名"
          />
        </n-form-item>
        
        <n-form-item v-if="isCreate" label="密码" path="password">
          <n-input
            v-model:value="userForm.password"
            type="password"
            placeholder="请输入密码"
            show-password-on="click"
          />
        </n-form-item>
      </n-form>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, h } from 'vue'
import { useMessage, useDialog, NButton, NSpace, NTag, NIcon } from 'naive-ui'
import {
  UserGroupIcon,
  PlusIcon,
  DownloadIcon,
  SearchIcon,
  RefreshIcon,
  EditIcon,
  TrashIcon,
  LockIcon,
  UnlockIcon,
  KeyIcon
} from '@vicons/fa'
import { userApi } from '@/api'
import type { User, UserCreateRequest, UserUpdateRequest, UserStatus } from '@/types/user'

// 响应式数据
const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const userList = ref<User[]>([])
const showUserModal = ref(false)
const isCreate = ref(true)
const editingUserId = ref<number | null>(null)

// 分页配置
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50, 100],
  onChange: (page: number) => {
    pagination.page = page
    loadUserList()
  },
  onUpdatePageSize: (pageSize: number) => {
    pagination.pageSize = pageSize
    pagination.page = 1
    loadUserList()
  }
})

// 筛选表单
const filterForm = reactive({
  username: '',
  email: '',
  status: null as UserStatus | null,
  type: null as string | null
})

// 用户表单
const userForm = reactive<UserCreateRequest & UserUpdateRequest>({
  username: '',
  password: '',
  email: '',
  phone: '',
  realName: ''
})

// 状态选项
const statusOptions = [
  { label: '启用', value: UserStatus.ACTIVE },
  { label: '禁用', value: UserStatus.DISABLED },
  { label: '锁定', value: UserStatus.LOCKED }
]

// 用户类型选项
const typeOptions = [
  { label: '普通用户', value: 'normal' },
  { label: '管理员', value: 'admin' },
  { label: '超级管理员', value: 'super_admin' }
]

// 表格列定义
const columns = [
  {
    title: 'ID',
    key: 'id',
    width: 80
  },
  {
    title: '用户名',
    key: 'username'
  },
  {
    title: '邮箱',
    key: 'email'
  },
  {
    title: '手机号',
    key: 'phone'
  },
  {
    title: '真实姓名',
    key: 'realName'
  },
  {
    title: '状态',
    key: 'status',
    render: (row: User) => {
      const statusMap = {
        [UserStatus.ACTIVE]: { type: 'success', text: '启用' },
        [UserStatus.DISABLED]: { type: 'error', text: '禁用' },
        [UserStatus.LOCKED]: { type: 'warning', text: '锁定' }
      }
      const status = statusMap[row.status]
      return h(NTag, { type: status.type }, { default: () => status.text })
    }
  },
  {
    title: '创建时间',
    key: 'createdAt',
    render: (row: User) => formatDate(row.createdAt)
  },
  {
    title: '操作',
    key: 'actions',
    render: (row: User) => {
      return h(NSpace, {}, {
        default: () => [
          h(NButton, {
            size: 'small',
            type: 'primary',
            onClick: () => handleEditUser(row)
          }, {
            icon: () => h(NIcon, {}, { default: () => h(EditIcon) }),
            default: () => '编辑'
          }),
          h(NButton, {
            size: 'small',
            type: row.status === UserStatus.ACTIVE ? 'warning' : 'success',
            onClick: () => handleToggleStatus(row)
          }, {
            icon: () => h(NIcon, {}, { default: () => h(row.status === UserStatus.ACTIVE ? LockIcon : UnlockIcon) }),
            default: () => row.status === UserStatus.ACTIVE ? '禁用' : '启用'
          }),
          h(NButton, {
            size: 'small',
            type: 'info',
            onClick: () => handleResetPassword(row)
          }, {
            icon: () => h(NIcon, {}, { default: () => h(KeyIcon) }),
            default: () => '重置密码'
          }),
          h(NButton, {
            size: 'small',
            type: 'error',
            onClick: () => handleDeleteUser(row)
          }, {
            icon: () => h(NIcon, {}, { default: () => h(TrashIcon) }),
            default: () => '删除'
          })
        ]
      })
    }
  }
]

// 加载用户列表
const loadUserList = async () => {
  try {
    loading.value = true
    const response = await userApi.getUsers({
      page: pagination.page - 1,
      size: pagination.pageSize,
      keyword: filterForm.username || filterForm.email || undefined
    })
    
    if (response.code === 200) {
      userList.value = response.data.items
      pagination.itemCount = response.data.total
    }
  } catch (error) {
    message.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const handleSearch = () => {
  pagination.page = 1
  loadUserList()
}

// 重置搜索
const handleReset = () => {
  Object.assign(filterForm, {
    username: '',
    email: '',
    status: null,
    type: null
  })
  pagination.page = 1
  loadUserList()
}

// 创建用户
const handleCreateUser = () => {
  isCreate.value = true
  editingUserId.value = null
  Object.assign(userForm, {
    username: '',
    password: '',
    email: '',
    phone: '',
    realName: ''
  })
  showUserModal.value = true
}

// 编辑用户
const handleEditUser = (user: User) => {
  isCreate.value = false
  editingUserId.value = user.id
  Object.assign(userForm, {
    username: user.username,
    email: user.email,
    phone: user.phone || '',
    realName: user.realName
  })
  showUserModal.value = true
}

// 切换用户状态
const handleToggleStatus = (user: User) => {
  const action = user.status === UserStatus.ACTIVE ? '禁用' : '启用'
  dialog.warning({
    title: '确认操作',
    content: `确定要${action}用户 "${user.username}" 吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        if (user.status === UserStatus.ACTIVE) {
          await userApi.disableUser(user.id)
        } else {
          await userApi.enableUser(user.id)
        }
        message.success(`${action}用户成功`)
        loadUserList()
      } catch (error) {
        message.error(`${action}用户失败`)
      }
    }
  })
}

// 重置密码
const handleResetPassword = (user: User) => {
  dialog.warning({
    title: '确认重置密码',
    content: `确定要重置用户 "${user.username}" 的密码吗？重置后密码将变为"123456"`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.resetPassword(user.id)
        message.success('重置密码成功')
      } catch (error) {
        message.error('重置密码失败')
      }
    }
  })
}

// 删除用户
const handleDeleteUser = (user: User) => {
  dialog.error({
    title: '确认删除',
    content: `确定要删除用户 "${user.username}" 吗？此操作不可恢复。`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.deleteUser(user.id)
        message.success('删除用户成功')
        loadUserList()
      } catch (error) {
        message.error('删除用户失败')
      }
    }
  })
}

// 模态框确认
const handleModalConfirm = async () => {
  try {
    if (isCreate.value) {
      await userApi.createUser(userForm)
      message.success('创建用户成功')
    } else {
      await userApi.updateUser(editingUserId.value!, userForm)
      message.success('更新用户成功')
    }
    showUserModal.value = false
    loadUserList()
  } catch (error) {
    message.error(isCreate.value ? '创建用户失败' : '更新用户失败')
  }
}

// 模态框取消
const handleModalCancel = () => {
  showUserModal.value = false
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page
  loadUserList()
}

// 页面大小变化
const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadUserList()
}

// 格式化日期
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 页面标题
const modalTitle = computed(() => {
  return isCreate.value ? '新建用户' : '编辑用户'
})

// 组件挂载时加载数据
onMounted(() => {
  loadUserList()
})

// 表单验证规则
const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '真实姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ]
}

// 导出函数
const handleExport = () => {
  message.info('导出功能开发中...')
}

// 生命周期
onMounted(() => {
  loadUserList()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables';
@import '@/styles/mixins';

.user-list-container {
  padding: $spacing-lg;
  height: 100%;
  overflow-y: auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-xl;
  
  .header-left {
    .page-title {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: $text-color;
      
      .title-icon {
        color: $primary-color;
      }
    }
    
    .page-desc {
      margin: $spacing-xs 0 0 0;
      color: $text-color-secondary;
      font-size: $font-size-sm;
    }
  }
}

.filter-card {
  margin-bottom: $spacing-lg;
  
  .n-form {
    display: flex;
    flex-wrap: wrap;
    gap: $spacing-md;
    align-items: flex-end;
    
    .n-form-item {
      margin-bottom: 0;
    }
  }
}

.table-card {
  .user-cell {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
    
    .username {
      font-weight: 500;
    }
  }
  
  .action-buttons {
    display: flex;
    gap: $spacing-xs;
    
    .n-button {
      flex: 1;
    }
  }
}

// 响应式设计
@include respond-to('md') {
  .filter-card .n-form {
    flex-direction: column;
    align-items: stretch;
  }
}

@include respond-to('sm') {
  .user-list-container {
    padding: $spacing-md;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-md;
  }
  
  .action-buttons {
    flex-direction: column;
  }
}
</style>