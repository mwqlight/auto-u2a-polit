import request from '../index';
import type {
  Permission,
  Role,
  PermissionQueryParams,
  RoleQueryParams,
  PermissionAssignParams,
  RoleAssignParams,
  PermissionCheckParams,
  RoleCheckParams,
  ApiPermissionCheckParams
} from '../../types/permission';

/**
 * 权限管理API
 */

export const permissionApi = {
  
  /**
   * 权限相关API
   */
  
  // 创建权限
  createPermission: (data: Partial<Permission>): Promise<Permission> =>
    request.post('/api/v1/permissions', data),
  
  // 更新权限
  updatePermission: (id: number, data: Partial<Permission>): Promise<Permission> =>
    request.put(`/api/v1/permissions/${id}`, data),
  
  // 删除权限
  deletePermission: (id: number): Promise<void> =>
    request.delete(`/api/v1/permissions/${id}`),
  
  // 根据ID获取权限
  getPermissionById: (id: number): Promise<Permission> =>
    request.get(`/api/v1/permissions/${id}`),
  
  // 根据编码获取权限
  getPermissionByCode: (code: string): Promise<Permission> =>
    request.get(`/api/v1/permissions/code/${code}`),
  
  // 获取所有权限
  getAllPermissions: (): Promise<Permission[]> =>
    request.get('/api/v1/permissions'),
  
  // 根据类型获取权限
  getPermissionsByType: (type: string): Promise<Permission[]> =>
    request.get(`/api/v1/permissions/type/${type}`),
  
  // 根据父权限ID获取子权限
  getPermissionsByParentId: (parentId: number): Promise<Permission[]> =>
    request.get(`/api/v1/permissions/parent/${parentId}`),
  
  // 根据用户ID获取权限
  getPermissionsByUserId: (userId: number): Promise<Permission[]> =>
    request.get(`/api/v1/permissions/user/${userId}`),
  
  // 根据角色ID获取权限
  getPermissionsByRoleId: (roleId: number): Promise<Permission[]> =>
    request.get(`/api/v1/permissions/role/${roleId}`),
  
  // 构建权限树
  buildPermissionTree: (): Promise<Permission[]> =>
    request.get('/api/v1/permissions/tree'),
  
  // 检查用户是否有权限
  hasPermission: (params: PermissionCheckParams): Promise<boolean> =>
    request.get('/api/v1/permissions/check', { params }),
  
  // 检查用户是否有接口权限
  hasApiPermission: (params: ApiPermissionCheckParams): Promise<boolean> =>
    request.get('/api/v1/permissions/check-api', { params }),
  
  /**
   * 角色相关API
   */
  
  // 创建角色
  createRole: (data: Partial<Role>): Promise<Role> =>
    request.post('/api/v1/roles', data),
  
  // 更新角色
  updateRole: (id: number, data: Partial<Role>): Promise<Role> =>
    request.put(`/api/v1/roles/${id}`, data),
  
  // 删除角色
  deleteRole: (id: number): Promise<void> =>
    request.delete(`/api/v1/roles/${id}`),
  
  // 根据ID获取角色
  getRoleById: (id: number): Promise<Role> =>
    request.get(`/api/v1/roles/${id}`),
  
  // 根据编码获取角色
  getRoleByCode: (code: string): Promise<Role> =>
    request.get(`/api/v1/roles/code/${code}`),
  
  // 获取所有角色
  getAllRoles: (): Promise<Role[]> =>
    request.get('/api/v1/roles'),
  
  // 根据类型获取角色
  getRolesByType: (type: string): Promise<Role[]> =>
    request.get(`/api/v1/roles/type/${type}`),
  
  // 根据用户ID获取角色
  getRolesByUserId: (userId: number): Promise<Role[]> =>
    request.get(`/api/v1/roles/user/${userId}`),
  
  // 构建角色树
  buildRoleTree: (): Promise<Role[]> =>
    request.get('/api/v1/roles/tree'),
  
  // 为用户分配角色
  assignRolesToUser: (userId: number, roleIds: number[]): Promise<void> =>
    request.post(`/api/v1/roles/user/${userId}/assign`, roleIds),
  
  // 移除用户的角色
  removeRolesFromUser: (userId: number, roleIds: number[]): Promise<void> =>
    request.post(`/api/v1/roles/user/${userId}/remove`, roleIds),
  
  // 检查用户是否有角色
  hasRole: (params: RoleCheckParams): Promise<boolean> =>
    request.get('/api/v1/roles/check', { params }),
  
  // 为角色分配权限
  assignPermissionsToRole: (roleId: number, permissionIds: number[]): Promise<void> =>
    request.post(`/api/v1/roles/${roleId}/permissions/assign`, permissionIds),
  
  // 移除角色的权限
  removePermissionsFromRole: (roleId: number, permissionIds: number[]): Promise<void> =>
    request.post(`/api/v1/roles/${roleId}/permissions/remove`, permissionIds)
};