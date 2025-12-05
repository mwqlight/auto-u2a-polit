<template>
  <div class="permission-list">
    <n-card title="权限管理" :bordered="false" size="small">
      <template #header-extra>
        <n-button type="primary" @click="handleCreate">
          <template #icon>
            <n-icon><PlusOutlined /></n-icon>
          </template>
          新增权限
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
          <n-form-item label="权限编码" path="permissionCode">
            <n-input
              v-model:value="searchForm.permissionCode"
              placeholder="请输入权限编码"
              clearable
            />
          </n-form-item>
          <n-form-item label="权限名称" path="permissionName">
            <n-input
              v-model:value="searchForm.permissionName"
              placeholder="请输入权限名称"
              clearable
            />
          </n-form-item>
          <n-form-item label="权限类型" path="permissionType">
            <n-select
              v-model:value="searchForm.permissionType"
              placeholder="请选择权限类型"
              :options="permissionTypeOptions"
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

        <!-- 权限树表格 -->
        <n-data-table
          :columns="columns"
          :data="permissionList"
          :loading="loading"
          :pagination="pagination"
          :row-key="(row) => row.id"
          :default-expand-all="false"
          :expandable="expandable"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
        />
      </n-space>
    </n-card>

    <!-- 新增/编辑权限对话框 -->
    <PermissionModal
      v-model:show="showModal"
      :type="modalType"
      :data="currentPermission"
      @success="handleModalSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useMessage, useDialog } from 'naive-ui';
import { PlusOutlined, EditOutlined, DeleteOutlined, EyeOutlined } from '@vicons/antd';
import type { DataTableColumns } from 'naive-ui';
import type { Permission, PermissionQueryParams } from '@/types/permission';
import { usePermissionStore } from '@/stores/permission';
import PermissionModal from './components/PermissionModal.vue';

const message = useMessage();
const dialog = useDialog();
const permissionStore = usePermissionStore();

// 搜索表单
const searchForm = reactive<PermissionQueryParams>({
  permissionCode: '',
  permissionName: '',
  permissionType: undefined,
  status: undefined,
  page: 1,
  size: 10
});

// 权限列表数据
const permissionList = ref<Permission[]>([]);
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

// 权限类型选项
const permissionTypeOptions = [
  { label: '菜单权限', value: 'MENU' },
  { label: '按钮权限', value: 'BUTTON' },
  { label: '接口权限', value: 'API' },
  { label: '数据权限', value: 'DATA' }
];

// 状态选项
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
];

// 表格列配置
const columns: DataTableColumns<Permission> = [
  {
    type: 'expand',
    expandable: 'children'
  },
  {
    title: '权限编码',
    key: 'permissionCode',
    width: 150
  },
  {
    title: '权限名称',
    key: 'permissionName',
    width: 150
  },
  {
    title: '权限类型',
    key: 'permissionType',
    width: 100,
    render: (row) => {
      const typeMap = {
        MENU: '菜单权限',
        BUTTON: '按钮权限',
        API: '接口权限',
        DATA: '数据权限'
      };
      return typeMap[row.permissionType] || row.permissionType;
    }
  },
  {
    title: '权限路径',
    key: 'permissionPath',
    width: 200
  },
  {
    title: '权限描述',
    key: 'permissionDesc',
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
    title: '创建时间',
    key: 'createTime',
    width: 180
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
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
                type: 'error',
                onClick: () => handleDelete(row)
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

// 可展开配置
const expandable = {
  children: 'children'
};

// 模态框相关
const showModal = ref(false);
const modalType = ref<'create' | 'edit'>('create');
const currentPermission = ref<Permission | null>(null);

// 生命周期
onMounted(() => {
  loadPermissionList();
});

// 加载权限列表
const loadPermissionList = async () => {
  try {
    loading.value = true;
    const result = await permissionStore.getPermissionTree(searchForm);
    permissionList.value = result || [];
    
    // 更新分页信息
    if (result && result.length > 0) {
      pagination.itemCount = result.length;
    }
  } catch (error) {
    message.error('加载权限列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  searchForm.page = pagination.page;
  searchForm.size = pagination.pageSize;
  loadPermissionList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    permissionCode: '',
    permissionName: '',
    permissionType: undefined,
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

// 新增权限
const handleCreate = () => {
  modalType.value = 'create';
  currentPermission.value = null;
  showModal.value = true;
};

// 编辑权限
const handleEdit = (permission: Permission) => {
  modalType.value = 'edit';
  currentPermission.value = { ...permission };
  showModal.value = true;
};

// 删除权限
const handleDelete = (permission: Permission) => {
  dialog.warning({
    title: '确认删除',
    content: `确定要删除权限 "${permission.permissionName}" 吗？此操作不可恢复。`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await permissionStore.deletePermission(permission.id!);
        message.success('删除权限成功');
        loadPermissionList();
      } catch (error) {
        message.error('删除权限失败');
        console.error(error);
      }
    }
  });
};

// 模态框成功回调
const handleModalSuccess = () => {
  showModal.value = false;
  loadPermissionList();
};
</script>

<style scoped>
.permission-list {
  padding: 20px;
}

.permission-list :deep(.n-card) {
  border-radius: 8px;
}
</style>