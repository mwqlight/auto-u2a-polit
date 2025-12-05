<template>
  <div class="role-list">
    <n-card title="角色管理" :bordered="false" size="small">
      <template #header-extra>
        <n-button type="primary" @click="handleCreate">
          <template #icon>
            <n-icon><PlusOutlined /></n-icon>
          </template>
          新增角色
        </n-button>
      </template>

      <!-- 搜索区域 -->
      <n-space vertical size="large">
        <n-form
          ref="searchFormRef"
          inline
          :label-width="80"
          :model="searchForm"
          label-placement="left"
        >
          <n-form-item label="角色编码" path="roleCode">
            <n-input
              v-model:value="searchForm.roleCode"
              placeholder="请输入角色编码"
              clearable
            />
          </n-form-item>
          <n-form-item label="角色名称" path="roleName">
            <n-input
              v-model:value="searchForm.roleName"
              placeholder="请输入角色名称"
              clearable
            />
          </n-form-item>
          <n-form-item label="角色类型" path="roleType">
            <n-select
              v-model:value="searchForm.roleType"
              placeholder="请选择角色类型"
              :options="roleTypeOptions"
              clearable
            />
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-select
              v-model:value="searchForm.status"
              placeholder="请选择状态"
              :options="statusOptions"
              clearable
            />
          </n-form-item>
          <n-form-item>
            <n-space>
              <n-button type="primary" @click="handleSearch">查询</n-button>
              <n-button @click="handleReset">重置</n-button>
            </n-space>
          </n-form-item>
        </n-form>

        <!-- 角色表格 -->
        <n-data-table
          :columns="columns"
          :data="roleList"
          :loading="loading"
          :pagination="pagination"
          :row-key="(row) => row.id"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
        />
      </n-space>
    </n-card>

    <!-- 新增/编辑角色对话框 -->
    <RoleModal
      v-model:show="showModal"
      :type="modalType"
      :data="currentRole"
      @success="handleModalSuccess"
    />

    <!-- 分配权限对话框 -->
    <AssignPermissionModal
      v-model:show="showAssignPermissionModal"
      :role-id="currentRoleId"
      @success="handleAssignPermissionSuccess"
    />

    <!-- 分配用户对话框 -->
    <AssignUserModal
      v-model:show="showAssignUserModal"
      :role-id="currentRoleId"
      @success="handleAssignUserSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, h } from 'vue';
import { useMessage, useDialog } from 'naive-ui';
import { PlusOutlined, EditOutlined, DeleteOutlined, UserOutlined, SafetyOutlined } from '@vicons/antd';
import type { DataTableColumns } from 'naive-ui';
import type { Role, RoleQueryParams } from '@/types/permission';
import { usePermissionStore } from '@/stores/permission';
import RoleModal from './components/RoleModal.vue';
import AssignPermissionModal from './components/AssignPermissionModal.vue';
import AssignUserModal from './components/AssignUserModal.vue';

const message = useMessage();
const dialog = useDialog();
const permissionStore = usePermissionStore();

// 搜索表单
const searchForm = reactive<RoleQueryParams>({
  roleCode: '',
  roleName: '',
  roleType: undefined,
  status: undefined,
  page: 1,
  size: 10
});

// 角色列表数据
const roleList = ref<Role[]>([]);
const loading = ref(false);

// 分页配置
const pagination = reactive({
  page: 1,
  pageSize: 10,
  pageCount: 0,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50, 100],
  onChange: (page: number) => {
    pagination.page = page;
    handleSearch();
  },
  onUpdatePageSize: (pageSize: number) => {
    pagination.pageSize = pageSize;
    pagination.page = 1;
    handleSearch();
  }
});

// 角色类型选项
const roleTypeOptions = [
  { label: '系统角色', value: 'SYSTEM' },
  { label: '业务角色', value: 'BUSINESS' },
  { label: '自定义角色', value: 'CUSTOM' }
];

// 状态选项
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];

// 表格列配置
const columns: DataTableColumns<Role> = [
  {
    title: '角色编码',
    key: 'roleCode',
    width: 120
  },
  {
    title: '角色名称',
    key: 'roleName',
    width: 120
  },
  {
    title: '角色类型',
    key: 'roleType',
    width: 100,
    render: (row) => {
      const typeMap = {
        SYSTEM: '系统角色',
        BUSINESS: '业务角色',
        CUSTOM: '自定义角色'
      };
      return typeMap[row.roleType] || row.roleType;
    }
  },
  {
    title: '角色描述',
    key: 'roleDesc',
    width: 200
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => {
      return h(
        'n-tag',
        {
          type: row.status === 1 ? 'success' : 'error'
        },
        {
          default: () => (row.status === 1 ? '启用' : '禁用')
        }
      );
    }
  },
  {
    title: '是否系统内置',
    key: 'isSystem',
    width: 100,
    render: (row) => {
      return h(
        'n-tag',
        {
          type: row.isSystem ? 'warning' : 'default'
        },
        {
          default: () => (row.isSystem ? '是' : '否')
        }
      );
    }
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'actions',
    width: 300,
    render: (row) => {
      return h(
        'n-space',
        {
          size: 'small'
        },
        {
          default: () => [
            h(
              'n-button',
              {
                size: 'small',
                type: 'primary',
                onClick: () => handleEdit(row)
              },
              {
                default: () => '编辑',
                icon: () => h(EditOutlined)
              }
            ),
            h(
              'n-button',
              {
                size: 'small',
                type: 'info',
                onClick: () => handleAssignPermission(row)
              },
              {
                default: () => '分配权限',
                icon: () => h(SafetyOutlined)
              }
            ),
            h(
              'n-button',
              {
                size: 'small',
                type: 'warning',
                onClick: () => handleAssignUser(row)
              },
              {
                default: () => '分配用户',
                icon: () => h(UserOutlined)
              }
            ),
            h(
              'n-button',
              {
                size: 'small',
                type: 'error',
                onClick: () => handleDelete(row),
                disabled: row.isSystem
              },
              {
                default: () => '删除',
                icon: () => h(DeleteOutlined)
              }
            )
          ]
        }
      );
    }
  }
];

// 模态框相关
const showModal = ref(false);
const modalType = ref<'create' | 'edit'>('create');
const currentRole = ref<Role | null>(null);

// 分配权限相关
const showAssignPermissionModal = ref(false);
const showAssignUserModal = ref(false);
const currentRoleId = ref<number | null>(null);

// 生命周期
onMounted(() => {
  loadRoleList();
});

// 加载角色列表
const loadRoleList = async () => {
  try {
    loading.value = true;
    const result = await permissionStore.getRoleList(searchForm);
    roleList.value = result?.items || [];
    
    // 更新分页信息
    if (result) {
      pagination.itemCount = result.total;
      pagination.pageCount = Math.ceil(result.total / pagination.pageSize);
    }
  } catch (error) {
    message.error('加载角色列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  searchForm.page = pagination.page;
  searchForm.size = pagination.pageSize;
  loadRoleList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    roleCode: '',
    roleName: '',
    roleType: undefined,
    status: undefined,
    page: 1,
    size: 10
  });
  pagination.page = 1;
  handleSearch();
};

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page;
  handleSearch();
};

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize;
  pagination.page = 1;
  handleSearch();
};

// 新增角色
const handleCreate = () => {
  modalType.value = 'create';
  currentRole.value = null;
  showModal.value = true;
};

// 编辑角色
const handleEdit = (role: Role) => {
  modalType.value = 'edit';
  currentRole.value = { ...role };
  showModal.value = true;
};

// 删除角色
const handleDelete = (role: Role) => {
  dialog.warning({
    title: '确认删除',
    content: `确定要删除角色 "${role.roleName}" 吗？此操作不可恢复。`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await permissionStore.deleteRole(role.id!);
        message.success('删除角色成功');
        loadRoleList();
      } catch (error) {
        message.error('删除角色失败');
        console.error(error);
      }
    }
  });
};

// 分配权限
const handleAssignPermission = (role: Role) => {
  currentRoleId.value = role.id!;
  showAssignPermissionModal.value = true;
};

// 分配用户
const handleAssignUser = (role: Role) => {
  currentRoleId.value = role.id!;
  showAssignUserModal.value = true;
};

// 模态框成功回调
const handleModalSuccess = () => {
  showModal.value = false;
  loadRoleList();
};

// 分配权限成功回调
const handleAssignPermissionSuccess = () => {
  showAssignPermissionModal.value = false;
  message.success('分配权限成功');
};

// 分配用户成功回调
const handleAssignUserSuccess = () => {
  showAssignUserModal.value = false;
  message.success('分配用户成功');
};
</script>

<style scoped>
.role-list {
  padding: 20px;
}

.role-list :deep(.n-card) {
  border-radius: 8px;
}
</style>