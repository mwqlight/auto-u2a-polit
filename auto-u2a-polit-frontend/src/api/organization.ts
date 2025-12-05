import request from '@/api/index'

// 组织架构相关API接口

export interface Organization {
  id: string
  name: string
  code: string
  type: 'DEPARTMENT' | 'TEAM' | 'COMPANY' | 'GROUP' | 'DIVISION' | 'UNIT'
  status: 'ACTIVE' | 'INACTIVE'
  parentId?: string
  path: string
  description?: string
  managerId?: string
  managerName?: string
  memberCount: number
  children?: Organization[]
  createdAt: string
  updatedAt: string
}

export interface OrganizationCreateRequest {
  name: string
  code: string
  type: 'DEPARTMENT' | 'TEAM' | 'COMPANY' | 'GROUP' | 'DIVISION' | 'UNIT'
  parentId?: string
  description?: string
  managerId?: string
}

export interface OrganizationUpdateRequest {
  name?: string
  code?: string
  type?: 'DEPARTMENT' | 'TEAM' | 'COMPANY' | 'GROUP' | 'DIVISION' | 'UNIT'
  description?: string
  managerId?: string
}

export interface OrganizationQueryParams {
  tenantId?: string
  parentId?: string
  keyword?: string
  status?: 'ACTIVE' | 'INACTIVE'
  type?: string
  page?: number
  pageSize?: number
}

/**
 * 获取组织架构树
 * @param params 查询参数
 * @returns 组织架构树
 */
export const getOrganizationTree = (params?: OrganizationQueryParams) => {
  return request.get<Organization[]>('/api/v1/identity/organizations', { params })
}

/**
 * 获取组织详情
 * @param orgId 组织ID
 * @returns 组织详情
 */
export const getOrganization = (orgId: string) => {
  return request.get<Organization>(`/api/v1/identity/organizations/${orgId}`)
}

/**
 * 创建组织
 * @param data 组织创建数据
 * @returns 创建后的组织
 */
export const createOrganization = (data: OrganizationCreateRequest) => {
  return request.post<Organization>('/api/v1/identity/organizations', data)
}

/**
 * 更新组织
 * @param orgId 组织ID
 * @param data 组织更新数据
 * @returns 更新后的组织
 */
export const updateOrganization = (orgId: string, data: OrganizationUpdateRequest) => {
  return request.put<Organization>(`/api/v1/identity/organizations/${orgId}`, data)
}

/**
 * 移动组织
 * @param orgId 组织ID
 * @param newParentId 新的父组织ID
 * @returns 移动后的组织
 */
export const moveOrganization = (orgId: string, newParentId: string) => {
  return request.put<Organization>(`/api/v1/identity/organizations/${orgId}/move`, { newParentId })
}

/**
 * 删除组织
 * @param orgId 组织ID
 * @returns 操作结果
 */
export const deleteOrganization = (orgId: string) => {
  return request.delete(`/api/v1/identity/organizations/${orgId}`)
}

/**
 * 获取组织成员
 * @param orgId 组织ID
 * @param includeChildren 是否包含子组织成员
 * @param params 查询参数
 * @returns 组织成员列表
 */
export const getOrganizationMembers = (orgId: string, includeChildren: boolean = false, params?: any) => {
  return request.get(`/api/v1/identity/organizations/${orgId}/members`, { 
    params: { includeChildren, ...params } 
  })
}

/**
 * 添加组织成员
 * @param orgId 组织ID
 * @param userIds 用户ID列表
 * @returns 操作结果
 */
export const addOrganizationMembers = (orgId: string, userIds: string[]) => {
  return request.post(`/api/v1/identity/organizations/${orgId}/members`, { userIds })
}

/**
 * 移除组织成员
 * @param orgId 组织ID
 * @param userIds 用户ID列表
 * @returns 操作结果
 */
export const removeOrganizationMembers = (orgId: string, userIds: string[]) => {
  return request.delete(`/api/v1/identity/organizations/${orgId}/members`, { data: { userIds } })
}

/**
 * 获取组织编制信息
 * @param orgId 组织ID
 * @returns 编制信息
 */
export const getOrganizationQuota = (orgId: string) => {
  return request.get(`/api/v1/identity/organizations/${orgId}/quota`)
}

/**
 * 更新组织编制
 * @param orgId 组织ID
 * @param quota 编制数量
 * @returns 操作结果
 */
export const updateOrganizationQuota = (orgId: string, quota: number) => {
  return request.put(`/api/v1/identity/organizations/${orgId}/quota`, { quota })
}
