/**
 * 用户状态枚举
 */
export enum UserStatus {
  ACTIVE = 'ACTIVE',
  DISABLED = 'DISABLED',
  LOCKED = 'LOCKED'
}

/**
 * 用户信息
 */
export interface User {
  id: number
  username: string
  email: string
  phone?: string
  realName: string
  status: UserStatus
  tenantId: string
  createdAt: string
  updatedAt: string
}

/**
 * 用户创建请求
 */
export interface UserCreateRequest {
  username: string
  password: string
  email: string
  phone?: string
  realName: string
  tenantId?: string
}

/**
 * 用户更新请求
 */
export interface UserUpdateRequest {
  email?: string
  phone?: string
  realName?: string
}

/**
 * 用户列表响应
 */
export interface UserListResponse {
  code: number
  message: string
  data: {
    items: User[]
    total: number
    page: number
    size: number
    totalPages: number
  }
}

/**
 * 用户详情响应
 */
export interface UserDetailResponse {
  code: number
  message: string
  data: User
}

/**
 * 分页参数
 */
export interface PaginationParams {
  page: number
  size: number
  sort?: string
  direction?: string
}

/**
 * 查询参数
 */
export interface UserQueryParams extends PaginationParams {
  keyword?: string
}