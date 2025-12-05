import { usePermissionStore } from '../stores/permission';
import { useAuthStore } from '../stores/auth';

/**
 * 权限管理组合式API
 */
export function usePermission() {
  const permissionStore = usePermissionStore();
  const authStore = useAuthStore();
  
  // 获取当前用户ID
  const getCurrentUserId = (): number | null => {
    return authStore.userInfo?.id || null;
  };
  
  // 检查当前用户是否有权限
  const hasPermission = async (permissionCode: string): Promise<boolean> => {
    const userId = getCurrentUserId();
    if (!userId) return false;
    
    return await permissionStore.checkPermission(userId, permissionCode);
  };
  
  // 检查当前用户是否有角色
  const hasRole = async (roleCode: string): Promise<boolean> => {
    const userId = getCurrentUserId();
    if (!userId) return false;
    
    return await permissionStore.checkRole(userId, roleCode);
  };
  
  // 检查当前用户是否有接口权限
  const hasApiPermission = async (path: string, method: string): Promise<boolean> => {
    const userId = getCurrentUserId();
    if (!userId) return false;
    
    return await permissionStore.checkApiPermission(userId, path, method);
  };
  
  // 获取当前用户权限列表
  const getCurrentUserPermissions = async (): Promise<string[]> => {
    const userId = getCurrentUserId();
    if (!userId) return [];
    
    const permissions = await permissionStore.fetchCurrentUserPermissions(userId);
    return permissions.map(p => p.code);
  };
  
  // 获取当前用户角色列表
  const getCurrentUserRoles = async (): Promise<string[]> => {
    const userId = getCurrentUserId();
    if (!userId) return [];
    
    const roles = await permissionStore.fetchCurrentUserRoles(userId);
    return roles.map(r => r.code);
  };
  
  // 检查权限（同步版本，基于本地缓存）
  const hasPermissionSync = (permissionCode: string): boolean => {
    const permissions = permissionStore.currentUserPermissions;
    return permissions.some(p => p.code === permissionCode && p.status === 1);
  };
  
  // 检查角色（同步版本，基于本地缓存）
  const hasRoleSync = (roleCode: string): boolean => {
    const roles = permissionStore.currentUserRoles;
    return roles.some(r => r.code === roleCode && r.status === 1);
  };
  
  // 检查是否有任意权限
  const hasAnyPermission = (permissionCodes: string[]): boolean => {
    return permissionCodes.some(code => hasPermissionSync(code));
  };
  
  // 检查是否有所有权限
  const hasAllPermissions = (permissionCodes: string[]): boolean => {
    return permissionCodes.every(code => hasPermissionSync(code));
  };
  
  // 检查是否有任意角色
  const hasAnyRole = (roleCodes: string[]): boolean => {
    return roleCodes.some(code => hasRoleSync(code));
  };
  
  // 检查是否有所有角色
  const hasAllRoles = (roleCodes: string[]): boolean => {
    return roleCodes.every(code => hasRoleSync(code));
  };
  
  // 初始化用户权限数据
  const initUserPermissionData = async (): Promise<void> => {
    const userId = getCurrentUserId();
    if (!userId) return;
    
    try {
      await Promise.all([
        permissionStore.fetchCurrentUserPermissions(userId),
        permissionStore.fetchCurrentUserRoles(userId)
      ]);
    } catch (error) {
      console.error('初始化用户权限数据失败:', error);
    }
  };
  
  // 清除权限数据
  const clearPermissionData = (): void => {
    permissionStore.clearPermissionData();
  };
  
  return {
    // 权限检查
    hasPermission,
    hasRole,
    hasApiPermission,
    hasPermissionSync,
    hasRoleSync,
    hasAnyPermission,
    hasAllPermissions,
    hasAnyRole,
    hasAllRoles,
    
    // 数据获取
    getCurrentUserPermissions,
    getCurrentUserRoles,
    
    // 数据管理
    initUserPermissionData,
    clearPermissionData
  };
}