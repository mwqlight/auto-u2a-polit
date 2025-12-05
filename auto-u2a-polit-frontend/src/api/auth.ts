import request from './index'
import type { LoginParams, UserInfo, LoginResponse } from '@/types/auth'

// 认证相关API
export const authApi = {
  // 用户登录（支持多种协议）
  login: (params: any): Promise<LoginResponse> => {
    return request.post('/auth/login', params)
  },
  
  // 用户登出
  logout: (): Promise<any> => {
    return request.post('/auth/logout')
  },
  
  // 获取用户信息
  getUserInfo: (): Promise<{ code: number; data: UserInfo }> => {
    return request.get('/auth/me')
  },
  
  // 刷新token
  refreshToken: (): Promise<LoginResponse> => {
    return request.post('/auth/refresh')
  },
  
  // 修改密码
  changePassword: (params: { oldPassword: string; newPassword: string }): Promise<any> => {
    return request.put('/auth/password', params)
  },
  
  // 重置密码
  resetPassword: (params: { username: string; email: string }): Promise<any> => {
    return request.post('/auth/reset-password', params)
  },
  
  // 获取支持的认证协议
  getSupportedProtocols: (): Promise<any> => {
    return request.get('/auth/protocols')
  },
  
  // 检查协议是否需要验证码
  requiresCaptcha: (protocol: string): Promise<any> => {
    return request.get(`/auth/protocols/${protocol}/captcha`)
  },
  
  // 检查协议是否需要多因素认证
  requiresMFA: (protocol: string): Promise<any> => {
    return request.get(`/auth/protocols/${protocol}/mfa`)
  },
  
  // 发送短信验证码
  sendSmsCode: (phone: string): Promise<any> => {
    return request.post('/auth/sms/code', { phone })
  },
  
  // 发送邮箱验证码
  sendEmailCode: (email: string): Promise<any> => {
    return request.post('/auth/email/code', { email })
  },
  
  // OAuth2登录
  oauth2Login: (provider: string, params?: any): Promise<any> => {
    return request.post(`/auth/oauth2/${provider}`, params)
  },
  
  // OAuth2回调
  oauth2Callback: (provider: string, code: string): Promise<any> => {
    return request.get(`/auth/oauth2/${provider}/callback?code=${code}`)
  },
  
  // 用户注册
  register: (params: { username: string; email: string; phone: string; password: string; smsCode: string; displayName: string }): Promise<any> => {
    return request.post('/auth/register', params)
  }
}