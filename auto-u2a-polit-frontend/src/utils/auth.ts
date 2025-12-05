import { jwtDecode } from 'jwt-decode'

// Token管理
export const getToken = (): string | null => {
  return localStorage.getItem('access_token')
}

export const setToken = (token: string): void => {
  localStorage.setItem('access_token', token)
}

export const removeToken = (): void => {
  localStorage.removeItem('access_token')
  localStorage.removeItem('refresh_token')
}

// Token验证
export const isTokenValid = (token: string): boolean => {
  try {
    const decoded = jwtDecode(token)
    return decoded.exp! > Date.now() / 1000
  } catch {
    return false
  }
}

// Token解码
export const decodeToken = (token: string): any => {
  try {
    return jwtDecode(token)
  } catch {
    return null
  }
}

// 权限检查
export const hasPermission = (requiredPermissions: string[], userPermissions: string[]): boolean => {
  return requiredPermissions.every(permission => userPermissions.includes(permission))
}

// 角色检查
export const hasRole = (requiredRoles: string[], userRoles: string[]): boolean => {
  return requiredRoles.every(role => userRoles.includes(role))
}

// 密码强度检查
export const checkPasswordStrength = (password: string): number => {
  let strength = 0
  
  if (password.length >= 8) strength++
  if (/[a-z]/.test(password)) strength++
  if (/[A-Z]/.test(password)) strength++
  if (/[0-9]/.test(password)) strength++
  if (/[^a-zA-Z0-9]/.test(password)) strength++
  
  return strength
}

// 生成随机字符串
export const generateRandomString = (length: number): string => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  let result = ''
  for (let i = 0; i < length; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}