import type { Router } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useMessage } from 'naive-ui'

export function setupRouterGuard(router: Router) {
  // 路由前置守卫
  router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore()
    const message = useMessage()
    
    // 检查是否需要认证
    if (to.meta.requiresAuth) {
      // 检查token是否存在
      if (!authStore.token) {
        message.warning('请先登录')
        next({
          name: 'Login',
          query: { redirect: to.fullPath }
        })
        return
      }
      
      // 检查token是否过期
      if (authStore.isTokenExpired) {
        message.warning('登录已过期，请重新登录')
        authStore.logout()
        next({
          name: 'Login',
          query: { redirect: to.fullPath }
        })
        return
      }
      
      // 检查用户权限
      if (to.meta.permissions) {
        const hasPermission = authStore.hasPermissions(to.meta.permissions as string[])
        if (!hasPermission) {
          message.error('权限不足')
          next(from.fullPath === '/' ? '/dashboard' : from.fullPath)
          return
        }
      }
    }
    
    // 设置页面标题
    if (to.meta.title) {
      document.title = `${to.meta.title} - Auto U2A Polit`
    }
    
    next()
  })
  
  // 路由后置守卫
  router.afterEach((to, from) => {
    // 可以在这里添加页面访问统计等
  })
  
  // 路由错误处理
  router.onError((error) => {
    console.error('路由错误:', error)
    const message = useMessage()
    message.error('页面加载失败，请刷新重试')
  })
}