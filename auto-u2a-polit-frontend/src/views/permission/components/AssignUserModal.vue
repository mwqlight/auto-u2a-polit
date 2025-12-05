<template>
  <n-modal
    v-model:show="show"
    :mask-closable="false"
    preset="dialog"
    title="分配用户"
    style="width: 800px"
    positive-text="确认分配"
    negative-text="取消"
    @positive-click="handleSubmit"
    @negative-click="handleCancel"
  >
    <n-space vertical size="large">
      <n-alert type="info">
        为角色分配用户，支持多选用户进行批量分配
      </n-alert>
      
      <!-- 用户搜索和选择 -->
      <n-space vertical>
        <n-input
          v-model:value="searchKeyword"
          placeholder="搜索用户（用户名、姓名、邮箱）"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <n-icon><SearchOutlined /></n-icon>
          </template>
        </n-input>
        
        <n-data-table
          :columns="columns"
          :data="userList"
          :loading="loading"
          :pagination="pagination"
          :row-key="(row) => row.id"
          :checked-row-keys="checkedUserIds"
          @update:checked-row-keys="handleCheckedRowKeysChange"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
        />
      </n-space>
    </n-space>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted, h } from 'vue';
import { useMessage } from 'naive-ui';
import { SearchOutlined } from '@vicons/antd';
import type { DataTableColumns } from 'naive-ui';
import type { User } from '@/types/permission';
import { usePermissionStore } from '@/stores/permission';

interface Props {
  show: boolean;
  roleId?: number | null;
}

interface Emits {
  (e: 'update:show', value: boolean): void;
  (e: 'success'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const message = useMessage();
const permissionStore = usePermissionStore();

// 搜索关键词
const searchKeyword = ref('');

// 用户列表数据
const userList = ref<User[]>([]);
const loading = ref(false);
const checkedUserIds = ref<number[]>([]);

// 分页配置
const pagination = reactive({
  page: 1,
  pageSize: 10,
  pageCount: 0,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  onChange: (page: number) => {
    pagination.page = page;
    loadUserList();
  },
  onUpdatePageSize: (pageSize: number) => {
    pagination.pageSize = pageSize;
    pagination.page = 1;
    loadUserList();
  }
});

// 表格列配置
const columns: DataTableColumns<User> = [
  {
    type: 'selection'
  },
  {
    title: '用户名',
    key: 'username',
    width: 120
  },
  {
    title: '姓名',
    key: 'realName',
    width: 100
  },
  {
    title: '邮箱',
    key: 'email',
    width: 150
  },
  {
    title: '手机号',
    key: 'phone',
    width: 120
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
  }
];

// 加载用户列表
const loadUserList = async () => {
  try {
    loading.value = true;
    
    // 这里需要调用用户服务获取用户列表
    // 暂时使用模拟数据
    const mockUsers: User[] = [
      {
        id: 1,
        username: 'admin',
        realName: '管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        status: 1,
        createTime: '2023-01-01 10:00:00'
      },
      {
        id: 2,
        username: 'user1',
        realName: '用户1',
        email: 'user1@example.com',
        phone: '13800138001',
        status: 1,
        createTime: '2023-01-01 10:00:00'
      },
      {
        id: 3,
        username: 'user2',
        realName: '用户2',
        email: 'user2@example.com',
        phone: '13800138002',
        status: 1,
        createTime: '2023-01-01 10:00:00'
      }
    ];
    
    // 过滤搜索结果
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase();
      userList.value = mockUsers.filter(user => 
        user.username.toLowerCase().includes(keyword) ||
        user.realName?.toLowerCase().includes(keyword) ||
        user.email?.toLowerCase().includes(keyword)
      );
    } else {
      userList.value = mockUsers;
    }
    
    pagination.itemCount = userList.value.length;
    pagination.pageCount = Math.ceil(userList.value.length / pagination.pageSize);
    
  } catch (error) {
    console.error('加载用户列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 加载角色已有用户
const loadRoleUsers = async () => {
  if (!props.roleId) return;
  
  try {
    const result = await permissionStore.getRoleUsers(props.roleId);
    checkedUserIds.value = result?.map(user => user.id!) || [];
  } catch (error) {
    console.error('加载角色用户失败:', error);
  }
};

// 处理搜索
const handleSearch = () => {
  pagination.page = 1;
  loadUserList();
};

// 处理选中用户变化
const handleCheckedRowKeysChange = (keys: number[]) => {
  checkedUserIds.value = keys;
};

// 分页变化
const handlePageChange = (page: number) => {
  pagination.page = page;
  loadUserList();
};

const handlePageSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize;
  pagination.page = 1;
  loadUserList();
};

// 提交分配
const handleSubmit = async () => {
  if (!props.roleId) {
    message.error('请选择角色');
    return;
  }
  
  if (checkedUserIds.value.length === 0) {
    message.error('请选择要分配的用户');
    return;
  }
  
  try {
    await permissionStore.assignUserRoles(checkedUserIds.value, [props.roleId]);
    message.success('分配用户成功');
    emit('success');
    emit('update:show', false);
  } catch (error) {
    message.error('分配用户失败');
    console.error(error);
  }
};

// 取消
const handleCancel = () => {
  emit('update:show', false);
  checkedUserIds.value = [];
  searchKeyword.value = '';
};

// 监听props变化
watch(
  () => props.show,
  (newVal) => {
    if (newVal && props.roleId) {
      loadUserList();
      loadRoleUsers();
    } else {
      checkedUserIds.value = [];
      searchKeyword.value = '';
    }
  }
);

// 监听角色ID变化
watch(
  () => props.roleId,
  (newRoleId) => {
    if (newRoleId && props.show) {
      loadRoleUsers();
    }
  }
);

// 生命周期
onMounted(() => {
  // 初始化时加载用户列表
});
</script>

<style scoped>
.n-data-table {
  max-height: 400px;
  overflow: auto;
}
</style>