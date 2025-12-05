import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import type { UserInfo, LoginParams } from '@/types/auth'
import { authApi } from '@/api/auth'
import { setToken, getToken, removeToken } from '@/utils/auth'

export const useAuthStore = defineStore('auth', () => {
  const router = useRouter()
  const message = useMessage()
  
  // 状态
  const token = ref<string>(getToken() || '')
  const userInfo = ref<UserInfo | null>(null)
  const permissions = ref<string[]>([])
  const roles = ref<string[]>([])
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isTokenExpired = computed(() => {
    if (!token.value) return true
    try {
      const payload = JSON.parse(atob(token.value.split('.')[1]))
      return payload.exp * 1000 < Date.now()
    } catch {
      return true
    }
  })
  
  // 登录（支持多种认证协议）
  const login = async (params: any) => {
    try {
      const response = await authApi.login(params)
      
      if (response.code === 200) {
        token.value = response.data.token || response.data.access_token
        userInfo.value = response.data.user || response.data
        permissions.value = response.data.permissions || []
        roles.value = response.data.roles || []
        
        // 保存token
        setToken(token.value)
        
        message.success('登录成功')
        
        // 跳转到目标页面或首页
        const redirect = router.currentRoute.value.query.redirect as string
        router.push(redirect || '/dashboard')
        
        return true
      } else {
        message.error(response.message || '登录失败')
        return false
      }
    } catch (error: any) {
      message.error(error.message || '登录失败')
      return false
    }
  }
  
  // 登出
  const logout = async () => {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('登出失败:', error)
    } finally {
      // 清除状态
      token.value = ''
      userInfo.value = null
      permissions.value = []
      roles.value = []
      
      // 清除token
      removeToken()
      
      // 跳转到登录页
      router.push('/login')
    }
  }
  
  // 获取用户信息
  const getUserInfo = async () => {
    try {
      const response = await authApi.getUserInfo()
      if (response.code === 200) {
        userInfo.value = response.data
        permissions.value = response.data.permissions || []
        roles.value = response.data.roles || []
        return true
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
    return false
  }
  
  // 检查权限
  const hasPermission = (permission: string) => {
    return permissions.value.includes(permission)
  }
  
  const hasPermissions = (requiredPermissions: string[]) => {
    return requiredPermissions.every(permission => permissions.value.includes(permission))
  }
  
  // 检查角色
  const hasRole = (role: string) => {
    return roles.value.includes(role)
  }
  
  const hasRoles = (requiredRoles: string[]) => {
    return requiredRoles.every(role => roles.value.includes(role))
  }
  
  // 刷新token
  const refreshToken = async () => {
    try {
      const response = await authApi.refreshToken()
      if (response.code === 200) {
        token.value = response.data.access_token
        setToken(token.value)
        return true
      }
    } catch (error) {
      console.error('刷新token失败:', error)
    }
    return false
  }
  
  return {
    token,
    userInfo,
    permissions,
    roles,
    isLoggedIn,
    isTokenExpired,
    login,
    logout,
    getUserInfo,
    hasPermission,
    hasPermissions,
    hasRole,
    hasRoles,
    refreshToken
  }
})