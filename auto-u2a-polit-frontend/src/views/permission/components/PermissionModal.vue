<template>
  <n-modal
    v-model:show="show"
    :mask-closable="false"
    preset="dialog"
    :title="type === 'create' ? '新增权限' : '编辑权限'"
    positive-text="确认"
    negative-text="取消"
    @positive-click="handleSubmit"
    @negative-click="handleCancel"
  >
    <n-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-placement="left"
      :label-width="100"
      size="medium"
    >
      <n-form-item label="父级权限" path="parentId">
        <n-tree-select
          v-model:value="formData.parentId"
          :options="permissionTree"
          label-field="permissionName"
          key-field="id"
          children-field="children"
          placeholder="请选择父级权限"
          clearable
          filterable
        />
      </n-form-item>

      <n-form-item label="权限编码" path="permissionCode">
        <n-input
          v-model:value="formData.permissionCode"
          placeholder="请输入权限编码"
          maxlength="50"
          show-count
        />
      </n-form-item>

      <n-form-item label="权限名称" path="permissionName">
        <n-input
          v-model:value="formData.permissionName"
          placeholder="请输入权限名称"
          maxlength="50"
          show-count
        />
      </n-form-item>

      <n-form-item label="权限类型" path="permissionType">
        <n-select
          v-model:value="formData.permissionType"
          placeholder="请选择权限类型"
          :options="permissionTypeOptions"
          @update:value="handlePermissionTypeChange"
        />
      </n-form-item>

      <n-form-item v-if="showPathField" label="权限路径" path="permissionPath">
        <n-input
          v-model:value="formData.permissionPath"
          placeholder="请输入权限路径"
          maxlength="200"
          show-count
        />
      </n-form-item>

      <n-form-item v-if="showMethodField" label="请求方法" path="permissionMethod">
        <n-select
          v-model:value="formData.permissionMethod"
          placeholder="请选择请求方法"
          :options="methodOptions"
          multiple
        />
      </n-form-item>

      <n-form-item label="权限描述" path="permissionDesc">
        <n-input
          v-model:value="formData.permissionDesc"
          type="textarea"
          placeholder="请输入权限描述"
          :rows="3"
          maxlength="200"
          show-count
        />
      </n-form-item>

      <n-form-item label="排序" path="sortOrder">
        <n-input-number
          v-model:value="formData.sortOrder"
          placeholder="请输入排序"
          :min="0"
          :max="999"
        />
      </n-form-item>

      <n-form-item label="状态" path="status">
        <n-radio-group v-model:value="formData.status">
          <n-space>
            <n-radio :value="1">启用</n-radio>
            <n-radio :value="0">禁用</n-radio>
          </n-space>
        </n-radio-group>
      </n-form-item>

      <n-form-item label="是否系统内置" path="isSystem">
        <n-switch v-model:value="formData.isSystem" />
      </n-form-item>
    </n-form>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted } from 'vue';
import { useMessage, type FormInst } from 'naive-ui';
import type { Permission } from '@/types/permission';
import { usePermissionStore } from '@/stores/permission';

interface Props {
  show: boolean;
  type: 'create' | 'edit';
  data?: Permission | null;
}

interface Emits {
  (e: 'update:show', value: boolean): void;
  (e: 'success'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

const message = useMessage();
const permissionStore = usePermissionStore();
const formRef = ref<FormInst | null>(null);

// 权限树数据
const permissionTree = ref<Permission[]>([]);

// 表单数据
const formData = reactive<Partial<Permission>>({
  parentId: 0,
  permissionCode: '',
  permissionName: '',
  permissionType: 'MENU',
  permissionPath: '',
  permissionMethod: [],
  permissionDesc: '',
  sortOrder: 0,
  status: 1,
  isSystem: false
});

// 权限类型选项
const permissionTypeOptions = [
  { label: '菜单权限', value: 'MENU' },
  { label: '按钮权限', value: 'BUTTON' },
  { label: '接口权限', value: 'API' },
  { label: '数据权限', value: 'DATA' }
];

// 请求方法选项
const methodOptions = [
  { label: 'GET', value: 'GET' },
  { label: 'POST', value: 'POST' },
  { label: 'PUT', value: 'PUT' },
  { label: 'DELETE', value: 'DELETE' },
  { label: 'PATCH', value: 'PATCH' }
];

// 表单验证规则
const rules = {
  permissionCode: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '权限编码只能包含字母、数字、下划线和连字符', trigger: 'blur' }
  ],
  permissionName: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  permissionType: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  permissionPath: [
    { required: true, message: '请输入权限路径', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, type: 'number', message: '请输入排序', trigger: 'blur' }
  ]
};

// 计算属性
const showPathField = computed(() => {
  return ['MENU', 'API'].includes(formData.permissionType!);
});

const showMethodField = computed(() => {
  return formData.permissionType === 'API';
});

// 监听权限类型变化
const handlePermissionTypeChange = (value: string) => {
  // 重置相关字段
  if (value !== 'API') {
    formData.permissionMethod = [];
  }
  if (value === 'BUTTON') {
    formData.permissionPath = '';
  }
};

// 加载权限树
const loadPermissionTree = async () => {
  try {
    const result = await permissionStore.getPermissionTree({});
    permissionTree.value = result || [];
  } catch (error) {
    console.error('加载权限树失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    parentId: 0,
    permissionCode: '',
    permissionName: '',
    permissionType: 'MENU',
    permissionPath: '',
    permissionMethod: [],
    permissionDesc: '',
    sortOrder: 0,
    status: 1,
    isSystem: false
  });
};

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value?.validate();
    
    if (props.type === 'create') {
      await permissionStore.createPermission(formData as Permission);
      message.success('新增权限成功');
    } else {
      await permissionStore.updatePermission(formData as Permission);
      message.success('编辑权限成功');
    }
    
    emit('success');
    emit('update:show', false);
    resetForm();
  } catch (error) {
    console.error('提交表单失败:', error);
  }
};

// 取消
const handleCancel = () => {
  emit('update:show', false);
  resetForm();
};

// 监听props变化
watch(
  () => props.show,
  (newVal) => {
    if (newVal) {
      if (props.type === 'edit' && props.data) {
        Object.assign(formData, props.data);
      } else {
        resetForm();
      }
    }
  }
);

// 监听数据变化
watch(
  () => props.data,
  (newData) => {
    if (props.type === 'edit' && newData) {
      Object.assign(formData, newData);
    }
  },
  { immediate: true }
);

// 生命周期
onMounted(() => {
  loadPermissionTree();
});
</script>

<style scoped>
.n-form {
  padding: 20px 0;
}
</style>