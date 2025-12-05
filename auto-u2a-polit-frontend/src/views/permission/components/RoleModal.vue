<template>
  <n-modal
    v-model:show="show"
    :mask-closable="false"
    preset="dialog"
    :title="type === 'create' ? '新增角色' : '编辑角色'"
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
      <n-form-item label="父级角色" path="parentId">
        <n-tree-select
          v-model:value="formData.parentId"
          :options="roleTree"
          label-field="roleName"
          key-field="id"
          children-field="children"
          placeholder="请选择父级角色"
          clearable
          filterable
        />
      </n-form-item>

      <n-form-item label="角色编码" path="roleCode">
        <n-input
          v-model:value="formData.roleCode"
          placeholder="请输入角色编码"
          maxlength="50"
          show-count
        />
      </n-form-item>

      <n-form-item label="角色名称" path="roleName">
        <n-input
          v-model:value="formData.roleName"
          placeholder="请输入角色名称"
          maxlength="50"
          show-count
        />
      </n-form-item>

      <n-form-item label="角色类型" path="roleType">
        <n-select
          v-model:value="formData.roleType"
          placeholder="请选择角色类型"
          :options="roleTypeOptions"
        />
      </n-form-item>

      <n-form-item label="角色描述" path="roleDesc">
        <n-input
          v-model:value="formData.roleDesc"
          type="textarea"
          placeholder="请输入角色描述"
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
import { ref, reactive, watch, onMounted } from 'vue';
import { useMessage, type FormInst } from 'naive-ui';
import type { Role } from '@/types/permission';
import { usePermissionStore } from '@/stores/permission';

interface Props {
  show: boolean;
  type: 'create' | 'edit';
  data?: Role | null;
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

// 角色树数据
const roleTree = ref<Role[]>([]);

// 表单数据
const formData = reactive<Partial<Role>>({
  parentId: 0,
  roleCode: '',
  roleName: '',
  roleType: 'BUSINESS',
  roleDesc: '',
  sortOrder: 0,
  status: 1,
  isSystem: false
});

// 角色类型选项
const roleTypeOptions = [
  { label: '系统角色', value: 'SYSTEM' },
  { label: '业务角色', value: 'BUSINESS' },
  { label: '自定义角色', value: 'CUSTOM' }
];

// 表单验证规则
const rules = {
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '角色编码只能包含字母、数字、下划线和连字符', trigger: 'blur' }
  ],
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleType: [
    { required: true, message: '请选择角色类型', trigger: 'change' }
  ],
  sortOrder: [
    { required: true, type: 'number', message: '请输入排序', trigger: 'blur' }
  ]
};

// 加载角色树
const loadRoleTree = async () => {
  try {
    const result = await permissionStore.getRoleTree({});
    roleTree.value = result || [];
  } catch (error) {
    console.error('加载角色树失败:', error);
  }
};

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    parentId: 0,
    roleCode: '',
    roleName: '',
    roleType: 'BUSINESS',
    roleDesc: '',
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
      await permissionStore.createRole(formData as Role);
      message.success('新增角色成功');
    } else {
      await permissionStore.updateRole(formData as Role);
      message.success('编辑角色成功');
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
  loadRoleTree();
});
</script>

<style scoped>
.n-form {
  padding: 20px 0;
}
</style>