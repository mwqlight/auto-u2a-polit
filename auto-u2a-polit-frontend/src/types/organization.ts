/**
 * 组织类型
 */
export type OrganizationType = 'COMPANY' | 'DEPARTMENT' | 'TEAM' | 'GROUP'

/**
 * 组织状态
 */
export type OrganizationStatus = 'ACTIVE' | 'INACTIVE'

/**
 * 组织
 */
export interface Organization {
  /**
   * 组织ID
   */
  id: string

  /**
   * 组织名称
   */
  name: string

  /**
   * 组织编码
   */
  code: string

  /**
   * 组织类型
   */
  type: OrganizationType

  /**
   * 父组织ID
   */
  parentId?: string

  /**
   * 父组织名称
   */
  parentName?: string

  /**
   * 描述
   */
  description?: string

  /**
   * 状态
   */
  status: OrganizationStatus

  /**
   * 排序
   */
  sort: number

  /**
   * 创建时间
   */
  createTime: string

  /**
   * 更新时间
   */
  updateTime: string

  /**
   * 子组织
   */
  children?: Organization[]
}

/**
 * 组织创建请求
 */
export interface OrganizationCreateRequest {
  /**
   * 组织名称
   */
  name: string

  /**
   * 组织编码
   */
  code: string

  /**
   * 组织类型
   */
  type: OrganizationType

  /**
   * 父组织ID
   */
  parentId?: string

  /**
   * 描述
   */
  description?: string

  /**
   * 状态
   */
  status?: OrganizationStatus

  /**
   * 排序
   */
  sort?: number
}

/**
 * 组织更新请求
 */
export interface OrganizationUpdateRequest {
  /**
   * 组织名称
   */
  name?: string

  /**
   * 组织编码
   */
  code?: string

  /**
   * 组织类型
   */
  type?: OrganizationType

  /**
   * 父组织ID
   */
  parentId?: string

  /**
   * 描述
   */
  description?: string

  /**
   * 状态
   */
  status?: OrganizationStatus

  /**
   * 排序
   */
  sort?: number
}

/**
 * 组织查询参数
 */
export interface OrganizationQueryParams {
  /**
   * 组织名称
   */
  name?: string

  /**
   * 组织编码
   */
  code?: string

  /**
   * 组织类型
   */
  type?: OrganizationType

  /**
   * 父组织ID
   */
  parentId?: string

  /**
   * 状态
   */
  status?: OrganizationStatus

  /**
   * 页码
   */
  page?: number

  /**
   * 每页数量
   */
  size?: number
}
