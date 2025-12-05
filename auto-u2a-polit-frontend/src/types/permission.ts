/**
 * 权限类型定义
 */

/** 权限实体 */
export interface Permission {
  id: number;
  code: string;
  name: string;
  description?: string;
  type: 'MENU' | 'BUTTON' | 'API' | 'DATA';
  path?: string;
  method?: string;
  parentId?: number;
  sortOrder: number;
  status: 0 | 1;
  createTime: string;
  updateTime: string;
  createBy?: string;
  updateBy?: string;
  children?: Permission[];
}

/** 角色实体 */
export interface Role {
  id: number;
  code: string;
  name: string;
  description?: string;
  type: 'SYSTEM' | 'CUSTOM';
  parentId?: number;
  dataScope: 'ALL' | 'DEPT' | 'SELF' | 'CUSTOM';
  deptIds?: string;
  status: 0 | 1;
  sortOrder: number;
  createTime: string;
  updateTime: string;
  createBy?: string;
  updateBy?: string;
  permissions?: Permission[];
  children?: Role[];
}

/** 用户角色关联 */
export interface UserRole {
  id: number;
  userId: number;
  roleId: number;
  status: 0 | 1;
  createTime: string;
  createBy?: string;
}

/** 权限查询参数 */
export interface PermissionQueryParams {
  type?: string;
  parentId?: number;
  status?: number;
  keyword?: string;
  page?: number;
  size?: number;
}

/** 角色查询参数 */
export interface RoleQueryParams {
  type?: string;
  status?: number;
  keyword?: string;
  page?: number;
  size?: number;
}

/** 权限分配参数 */
export interface PermissionAssignParams {
  roleId: number;
  permissionIds: number[];
}

/** 角色分配参数 */
export interface RoleAssignParams {
  userId: number;
  roleIds: number[];
}

/** 权限检查参数 */
export interface PermissionCheckParams {
  userId: number;
  permissionCode: string;
}

/** 角色检查参数 */
export interface RoleCheckParams {
  userId: number;
  roleCode: string;
}

/** 接口权限检查参数 */
export interface ApiPermissionCheckParams {
  userId: number;
  path: string;
  method: string;
}