import axios from 'axios'
import { useMessage } from 'naive-ui'
import { getToken, removeToken } from '@/utils/auth'

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 添加token到请求头
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 添加租户ID到请求头
    const tenantId = localStorage.getItem('tenant-id')
    if (tenantId) {
      config.headers['X-Tenant-ID'] = tenantId
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const { data } = response
    
    // 处理业务错误
    if (data.code !== 200) {
      const message = useMessage()
      
      // 认证失败
      if (data.code === 401) {
        message.error('认证失败，请重新登录')
        removeToken()
        window.location.href = '/login'
        return Promise.reject(new Error('认证失败'))
      }
      
      // 权限不足
      if (data.code === 403) {
        message.error('权限不足')
        return Promise.reject(new Error('权限不足'))
      }
      
      // 其他业务错误
      message.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    
    return data
  },
  (error) => {
    const message = useMessage()
    
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          message.error('认证失败，请重新登录')
          removeToken()
          window.location.href = '/login'
          break
        case 403:
          message.error('权限不足')
          break
        case 404:
          message.error('请求的资源不存在')
          break
        case 500:
          message.error('服务器内部错误')
          break
        case 502:
          message.error('网关错误')
          break
        case 503:
          message.error('服务不可用')
          break
        default:
          message.error(data?.message || '请求失败')
      }
    } else if (error.request) {
      // 请求未收到响应
      message.error('网络错误，请检查网络连接')
    } else {
      // 请求配置错误
      message.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

// 导出所有API模块
export * from './auth'
export * from './user'
export * from './modules/organization'

export default request