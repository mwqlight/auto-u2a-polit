import request from './index'
import type { User, UserCreateRequest, UserUpdateRequest, UserListResponse } from '@/types/user'

/**
 * 用户管理API
 */
export const userApi = {
  /**
   * 获取用户列表
   */
  getUsers(params: {
    page?: number
    size?: number
    sort?: string
    direction?: string
    keyword?: string
  }) {
    return request.get<UserListResponse>('/api/v1/identity/users', { params })
  },

  /**
   * 获取用户详情
   */
  getUser(id: string) {
    return request.get<User>(`/api/v1/identity/users/${id}`)
  },

  /**
   * 创建用户
   */
  createUser(data: UserCreateRequest) {
    return request.post<User>('/api/v1/identity/users', data)
  },

  /**
   * 更新用户
   */
  updateUser(id: string, data: UserUpdateRequest) {
    return request.put<User>(`/api/v1/identity/users/${id}`, data)
  },

  /**
   * 删除用户
   */
  deleteUser(id: string) {
    return request.delete(`/api/v1/identity/users/${id}`)
  },

  /**
   * 启用用户
   */
  enableUser(id: string) {
    return request.put(`/api/v1/identity/users/${id}/enable`)
  },

  /**
   * 禁用用户
   */
  disableUser(id: string) {
    return request.put(`/api/v1/identity/users/${id}/disable`)
  },

  /**
   * 重置密码
   */
  resetPassword(id: string) {
    return request.put(`/api/v1/identity/users/${id}/reset-password`)
  }
}