<template>
  <n-modal
    v-model:show="show"
    :mask-closable="false"
    preset="dialog"
    title="分配权限"
    style="width: 800px"
    positive-text="确认分配"
    negative-text="取消"
    @positive-click="handleSubmit"
    @negative-click="handleCancel"
  >
    <n-space vertical size="large">
      <n-alert type="info">
        为角色分配权限，支持选择菜单权限、按钮权限、接口权限和数据权限
      </n-alert>
      
      <n-tree
        ref="treeRef"
        :data="permissionTree"
        :default-expand-all="false"
        :default-expanded-keys="defaultExpandedKeys"
        :checked-keys="checkedKeys"
        checkable
        cascade
        virtual-scroll
        style="max-height: 400px; overflow: auto"
        @update:checked-keys="handleCheckedKeysChange"
      />
    </n-space>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue';
import { useMessage } from 'naive-ui';
import type { TreeOption } from 'naive-ui';
import type { Permission } from '@/types/permission';
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

// 权限树数据
const permissionTree = ref<TreeOption[]>([]);
const defaultExpandedKeys = ref<(string | number)[]>([]);
const checkedKeys = ref<(string | number)[]>([]);

// 加载权限树
const loadPermissionTree = async () => {
  try {
    const result = await permissionStore.getPermissionTree({});
    
    // 转换权限树格式
    const convertPermissionTree = (permissions: Permission[]): TreeOption[] => {
      return permissions.map(permission => ({
        key: permission.id!,
        label: `${permission.permissionName} (${permission.permissionCode})`,
        children: permission.children ? convertPermissionTree(permission.children) : undefined
      }));
    };
    
    permissionTree.value = convertPermissionTree(result || []);
    
    // 默认展开第一级
    if (permissionTree.value.length > 0) {
      defaultExpandedKeys.value = permissionTree.value.map(item => item.key as string | number);
    }
  } catch (error) {
    console.error('加载权限树失败:', error);
  }
};

// 加载角色已有权限
const loadRolePermissions = async () => {
  if (!props.roleId) return;
  
  try {
    const result = await permissionStore.getRolePermissions(props.roleId);
    checkedKeys.value = result?.map(permission => permission.id!) || [];
  } catch (error) {
    console.error('加载角色权限失败:', error);
  }
};

// 处理选中权限变化
const handleCheckedKeysChange = (keys: (string | number)[]) => {
  checkedKeys.value = keys;
};

// 提交分配
const handleSubmit = async () => {
  if (!props.roleId) {
    message.error('请选择角色');
    return;
  }
  
  try {
    await permissionStore.assignRolePermissions(props.roleId, checkedKeys.value as number[]);
    message.success('分配权限成功');
    emit('success');
    emit('update:show', false);
  } catch (error) {
    message.error('分配权限失败');
    console.error(error);
  }
};

// 取消
const handleCancel = () => {
  emit('update:show', false);
  checkedKeys.value = [];
};

// 监听props变化
watch(
  () => props.show,
  (newVal) => {
    if (newVal && props.roleId) {
      loadRolePermissions();
    } else {
      checkedKeys.value = [];
    }
  }
);

// 监听角色ID变化
watch(
  () => props.roleId,
  (newRoleId) => {
    if (newRoleId && props.show) {
      loadRolePermissions();
    }
  }
);

// 生命周期
onMounted(() => {
  loadPermissionTree();
});
</script>

<style scoped>
.n-tree {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 10px;
}
</style>