import request from '@/utils/request'
import type { Organization, OrganizationCreateRequest, OrganizationUpdateRequest } from '@/types/organization'

/**
 * 组织架构相关API
 */
export const organizationApi = {
  /**
   * 获取组织架构树
   * @returns 组织架构树
   */
  getOrganizationTree: () => {
    return request.get<Organization[]>('/api/v1/organizations/tree')
  },

  /**
   * 获取组织列表
   * @param params 查询参数
   * @returns 组织列表
   */
  getOrganizationList: (params?: any) => {
    return request.get('/api/v1/organizations', { params })
  },

  /**
   * 获取组织详情
   * @param id 组织ID
   * @returns 组织详情
   */
  getOrganizationById: (id: string) => {
    return request.get<Organization>(`/api/v1/organizations/${id}`)
  },

  /**
   * 创建组织
   * @param data 组织创建数据
   * @returns 创建结果
   */
  createOrganization: (data: OrganizationCreateRequest) => {
    return request.post('/api/v1/organizations', data)
  },

  /**
   * 更新组织
   * @param id 组织ID
   * @param data 组织更新数据
   * @returns 更新结果
   */
  updateOrganization: (id: string, data: OrganizationUpdateRequest) => {
    return request.put(`/api/v1/organizations/${id}`, data)
  },

  /**
   * 删除组织
   * @param id 组织ID
   * @returns 删除结果
   */
  deleteOrganization: (id: string) => {
    return request.delete(`/api/v1/organizations/${id}`)
  },

  /**
   * 移动组织
   * @param id 组织ID
   * @param parentId 新的父组织ID
   * @returns 移动结果
   */
  moveOrganization: (id: string, parentId: string) => {
    return request.patch(`/api/v1/organizations/${id}/move`, { parentId })
  },

  /**
   * 获取组织下的用户
   * @param id 组织ID
   * @param params 查询参数
   * @returns 用户列表
   */
  getOrganizationUsers: (id: string, params?: any) => {
    return request.get(`/api/v1/organizations/${id}/users`, { params })
  },

  /**
   * 为组织添加用户
   * @param id 组织ID
   * @param userIds 用户ID列表
   * @returns 添加结果
   */
  addUsersToOrganization: (id: string, userIds: string[]) => {
    return request.post(`/api/v1/organizations/${id}/users`, { userIds })
  },

  /**
   * 从组织移除用户
   * @param id 组织ID
   * @param userId 用户ID
   * @returns 移除结果
   */
  removeUserFromOrganization: (id: string, userId: string) => {
    return request.delete(`/api/v1/organizations/${id}/users/${userId}`)
  }
}
