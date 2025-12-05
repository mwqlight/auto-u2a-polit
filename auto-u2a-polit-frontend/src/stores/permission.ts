import { defineStore } from 'pinia';
import { permissionApi } from '../api/modules/permission';
import type { Permission, Role } from '../types/permission';

/**
 * 权限管理存储
 */
export const usePermissionStore = defineStore('permission', () => {
  
  // 状态
  const permissions = ref<Permission[]>([]);
  const roles = ref<Role[]>([]);
  const permissionTree = ref<Permission[]>([]);
  const roleTree = ref<Role[]>([]);
  const currentUserPermissions = ref<Permission[]>([]);
  const currentUserRoles = ref<Role[]>([]);
  
  // 获取所有权限
  const fetchPermissions = async () => {
    try {
      const data = await permissionApi.getAllPermissions();
      permissions.value = data;
      return data;
    } catch (error) {
      console.error('获取权限列表失败:', error);
      throw error;
    }
  };
  
  // 获取所有角色
  const fetchRoles = async () => {
    try {
      const data = await permissionApi.getAllRoles();
      roles.value = data;
      return data;
    } catch (error) {
      console.error('获取角色列表失败:', error);
      throw error;
    }
  };
  
  // 构建权限树
  const buildPermissionTree = async () => {
    try {
      const data = await permissionApi.buildPermissionTree();
      permissionTree.value = data;
      return data;
    } catch (error) {
      console.error('构建权限树失败:', error);
      throw error;
    }
  };
  
  // 构建角色树
  const buildRoleTree = async () => {
    try {
      const data = await permissionApi.buildRoleTree();
      roleTree.value = data;
      return data;
    } catch (error) {
      console.error('构建角色树失败:', error);
      throw error;
    }
  };
  
  // 获取当前用户权限
  const fetchCurrentUserPermissions = async (userId: number) => {
    try {
      const data = await permissionApi.getPermissionsByUserId(userId);
      currentUserPermissions.value = data;
      return data;
    } catch (error) {
      console.error('获取用户权限失败:', error);
      throw error;
    }
  };
  
  // 获取当前用户角色
  const fetchCurrentUserRoles = async (userId: number) => {
    try {
      const data = await permissionApi.getRolesByUserId(userId);
      currentUserRoles.value = data;
      return data;
    } catch (error) {
      console.error('获取用户角色失败:', error);
      throw error;
    }
  };
  
  // 检查用户是否有权限
  const checkPermission = async (userId: number, permissionCode: string): Promise<boolean> => {
    try {
      return await permissionApi.hasPermission({ userId, permissionCode });
    } catch (error) {
      console.error('检查权限失败:', error);
      return false;
    }
  };
  
  // 检查用户是否有角色
  const checkRole = async (userId: number, roleCode: string): Promise<boolean> => {
    try {
      return await permissionApi.hasRole({ userId, roleCode });
    } catch (error) {
      console.error('检查角色失败:', error);
      return false;
    }
  };
  
  // 检查用户是否有接口权限
  const checkApiPermission = async (userId: number, path: string, method: string): Promise<boolean> => {
    try {
      return await permissionApi.hasApiPermission({ userId, path, method });
    } catch (error) {
      console.error('检查接口权限失败:', error);
      return false;
    }
  };
  
  // 创建权限
  const createPermission = async (permission: Partial<Permission>) => {
    try {
      const data = await permissionApi.createPermission(permission);
      permissions.value.push(data);
      return data;
    } catch (error) {
      console.error('创建权限失败:', error);
      throw error;
    }
  };
  
  // 创建角色
  const createRole = async (role: Partial<Role>) => {
    try {
      const data = await permissionApi.createRole(role);
      roles.value.push(data);
      return data;
    } catch (error) {
      console.error('创建角色失败:', error);
      throw error;
    }
  };
  
  // 为用户分配角色
  const assignRolesToUser = async (userId: number, roleIds: number[]) => {
    try {
      await permissionApi.assignRolesToUser(userId, roleIds);
      // 重新获取用户角色
      await fetchCurrentUserRoles(userId);
    } catch (error) {
      console.error('分配角色失败:', error);
      throw error;
    }
  };
  
  // 为角色分配权限
  const assignPermissionsToRole = async (roleId: number, permissionIds: number[]) => {
    try {
      await permissionApi.assignPermissionsToRole(roleId, permissionIds);
    } catch (error) {
      console.error('分配权限失败:', error);
      throw error;
    }
  };
  
  // 清除权限数据
  const clearPermissionData = () => {
    permissions.value = [];
    roles.value = [];
    permissionTree.value = [];
    roleTree.value = [];
    currentUserPermissions.value = [];
    currentUserRoles.value = [];
  };
  
  return {
    // 状态
    permissions: readonly(permissions),
    roles: readonly(roles),
    permissionTree: readonly(permissionTree),
    roleTree: readonly(roleTree),
    currentUserPermissions: readonly(currentUserPermissions),
    currentUserRoles: readonly(currentUserRoles),
    
    // 方法
    fetchPermissions,
    fetchRoles,
    buildPermissionTree,
    buildRoleTree,
    fetchCurrentUserPermissions,
    fetchCurrentUserRoles,
    checkPermission,
    checkRole,
    checkApiPermission,
    createPermission,
    createRole,
    assignRolesToUser,
    assignPermissionsToRole,
    clearPermissionData
  };
});