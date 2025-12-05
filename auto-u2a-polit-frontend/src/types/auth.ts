// 认证相关类型定义

export interface LoginParams {
  username: string
  password: string
  tenantId: string
  captcha?: string
  rememberMe?: boolean
}

export interface UserInfo {
  id: string
  username: string
  email: string
  phone?: string
  displayName: string
  avatarUrl?: string
  status: 'ACTIVE' | 'INACTIVE' | 'LOCKED' | 'SUSPENDED'
  type: 'INTERNAL' | 'EXTERNAL' | 'SYSTEM'
  tenantId: string
  roles: string[]
  permissions: string[]
  lastLoginAt?: string
  lastLoginIp?: string
  createdAt: string
  updatedAt?: string
}

export interface LoginResponse {
  code: number
  message: string
  data: {
    access_token: string
    refresh_token: string
    expires_in: number
    token_type: string
    user: UserInfo
    permissions: string[]
    roles: string[]
  }
}

export interface Permission {
  id: string
  name: string
  code: string
  type: 'MENU' | 'BUTTON' | 'API'
  parentId?: string
  path?: string
  component?: string
  icon?: string
  sort: number
  status: 'ACTIVE' | 'INACTIVE'
  description?: string
}

export interface Role {
  id: string
  name: string
  code: string
  type: 'SYSTEM' | 'CUSTOM'
  permissions: string[]
  status: 'ACTIVE' | 'INACTIVE'
  description?: string
  createdAt: string
  updatedAt?: string
}