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
    return request.get<UserListResponse>('/v1/users', { params })
  },

  /**
   * 获取用户详情
   */
  getUser(id: number) {
    return request.get<User>(`/v1/users/${id}`)
  },

  /**
   * 创建用户
   */
  createUser(data: UserCreateRequest) {
    return request.post<User>('/v1/users', data)
  },

  /**
   * 更新用户
   */
  updateUser(id: number, data: UserUpdateRequest) {
    return request.put<User>(`/v1/users/${id}`, data)
  },

  /**
   * 删除用户
   */
  deleteUser(id: number) {
    return request.delete(`/v1/users/${id}`)
  },

  /**
   * 启用用户
   */
  enableUser(id: number) {
    return request.put(`/v1/users/${id}/enable`)
  },

  /**
   * 禁用用户
   */
  disableUser(id: number) {
    return request.put(`/v1/users/${id}/disable`)
  },

  /**
   * 重置密码
   */
  resetPassword(id: number) {
    return request.put(`/v1/users/${id}/reset-password`)
  }
}