<template>
  <div class="login-container">
    <!-- 背景特效 -->
    <div class="login-background">
      <div class="quantum-network"></div>
      <div class="hologram-grid"></div>
      <div class="particle-field"></div>
    </div>

    <!-- 登录表单 -->
    <div class="login-form-wrapper">
      <div class="login-card">
        <!-- 头部 -->
        <div class="login-header">
          <div class="logo-section">
            <div class="logo-icon">
              <div class="quantum-logo"></div>
            </div>
            <div class="logo-text">
              <h1 class="system-name">Auto U2A POLIT</h1>
              <p class="system-desc">全协议认证中心</p>
            </div>
          </div>
        </div>

        <!-- 协议选择 -->
        <div class="protocol-section" v-if="!selectedProtocol">
          <h3 class="protocol-title">选择认证方式</h3>
          <div class="protocol-grid">
            <div 
              v-for="protocol in supportedProtocols" 
              :key="protocol.code"
              class="protocol-card"
              @click="selectProtocol(protocol)"
            >
              <div class="protocol-icon">
                <n-icon>
                  <component :is="getProtocolIcon(protocol.code)" />
                </n-icon>
              </div>
              <div class="protocol-info">
                <div class="protocol-name">{{ protocol.name }}</div>
                <div class="protocol-desc">{{ protocol.description }}</div>
              </div>
              <div class="protocol-badge" :class="protocol.category">
                {{ protocol.category }}
              </div>
            </div>
          </div>
        </div>

        <!-- 表单区域 -->
        <div class="form-section" v-else>
          <div class="protocol-header">
            <n-button text @click="selectedProtocol = null">
              <n-icon>
                <ArrowBackIcon />
              </n-icon>
              返回
            </n-button>
            <h3>{{ selectedProtocol.name }} 认证</h3>
          </div>

          <n-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            size="large"
            class="login-form"
          >
            <!-- 用户名密码认证 -->
            <template v-if="selectedProtocol.code === 'PASSWORD'">
              <n-form-item path="tenantId">
                <n-input
                  v-model:value="formData.tenantId"
                  placeholder="请输入租户ID"
                  :input-props="{ autocomplete: 'organization' }"
                >
                  <template #prefix>
                    <n-icon>
                      <BuildingIcon />
                    </n-icon>
                  </template>
                </n-input>
              </n-form-item>

              <n-form-item path="username">
                <n-input
                  v-model:value="formData.username"
                  placeholder="请输入用户名"
                  :input-props="{ autocomplete: 'username' }"
                >
                  <template #prefix>
                    <n-icon>
                      <UserIcon />
                    </n-icon>
                  </template>
                </n-input>
              </n-form-item>

              <n-form-item path="password">
                <n-input
                  v-model:value="formData.password"
                  type="password"
                  placeholder="请输入密码"
                  show-password-on="click"
                  :input-props="{ autocomplete: 'current-password' }"
                  @keyup.enter="handleLogin"
                >
                  <template #prefix>
                    <n-icon>
                      <LockIcon />
                    </n-icon>
                  </template>
                </n-input>
              </n-form-item>
            </template>

            <!-- 短信验证码认证 -->
            <template v-else-if="selectedProtocol.code === 'SMS'">
              <n-form-item path="phone">
                <n-input
                  v-model:value="formData.phone"
                  placeholder="请输入手机号"
                  :input-props="{ autocomplete: 'tel' }"
                >
                  <template #prefix>
                    <n-icon>
                      <PhoneIcon />
                    </n-icon>
                  </template>
                </n-input>
              </n-form-item>

              <n-form-item path="code">
                <div class="sms-code-wrapper">
                  <n-input
                    v-model:value="formData.code"
                    placeholder="请输入验证码"
                    class="sms-code-input"
                    @keyup.enter="handleLogin"
                  />
                  <n-button 
                    type="primary" 
                    :disabled="!canSendSms"
                    @click="sendSmsCode"
                    class="sms-send-btn"
                  >
                    {{ smsCountdown > 0 ? `${smsCountdown}s` : '发送验证码' }}
                  </n-button>
                </div>
              </n-form-item>
            </template>

            <!-- OAuth2认证 -->
            <template v-else-if="selectedProtocol.code === 'OAUTH2'">
              <div class="oauth2-providers">
                <n-button 
                  v-for="provider in oauth2Providers" 
                  :key="provider.id"
                  type="primary"
                  ghost
                  @click="handleOAuth2Login(provider)"
                  class="oauth2-btn"
                >
                  <n-icon>
                    <component :is="provider.icon" />
                  </n-icon>
                  {{ provider.name }}
                </n-button>
              </div>
            </template>

            <!-- 验证码 -->
            <n-form-item v-if="showCaptcha" path="captcha">
              <div class="captcha-wrapper">
                <n-input
                  v-model:value="formData.captcha"
                  placeholder="请输入验证码"
                  class="captcha-input"
                  @keyup.enter="handleLogin"
                />
                <div class="captcha-image" @click="refreshCaptcha">
                  <img :src="captchaUrl" alt="验证码" />
                </div>
              </div>
            </n-form-item>

            <!-- 记住我 -->
            <div class="form-options">
              <n-checkbox v-model:checked="formData.rememberMe">
                记住我
              </n-checkbox>
              <a class="forgot-password" @click="handleForgotPassword">
                忘记密码？
              </a>
            </div>

            <!-- 登录按钮 -->
            <n-button
              type="primary"
              size="large"
              :loading="loading"
              :disabled="!canLogin"
              @click="handleLogin"
              class="login-btn"
            >
              {{ loading ? '登录中...' : '登录' }}
            </n-button>
          </n-form>
        </div>

        <!-- 底部信息 -->
        <div class="login-footer">
          <div class="protocol-links">
            <a @click="handleProtocol('privacy')">隐私政策</a>
            <span class="separator">|</span>
            <a @click="handleProtocol('terms')">服务条款</a>
          </div>
          <div class="version-info">
            Version {{ version }}
          </div>
        </div>
      </div>
    </div>

    <!-- 系统状态指示器 -->
    <div class="system-status">
      <n-badge :value="systemStatus" :type="systemStatusType" />
      <span class="status-text">{{ systemStatusText }}</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { 
  UserIcon, 
  LockIcon, 
  PhoneIcon, 
  ArrowBackIcon,
  WechatIcon,
  DingtalkIcon,
  GithubIcon,
  GoogleIcon,
  QrcodeIcon,
  FingerprintIcon,
  MailIcon,
  KeyIcon,
  BuildingIcon
} from '@vicons/fa'
import { useAuthStore } from '@/stores/auth'
import { LoginParams } from '@/types/auth'

// 路由和状态管理
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()

// 响应式数据
const formRef = ref()
const loading = ref(false)
const showCaptcha = ref(false)
const captchaUrl = ref('')
const version = ref('1.0.0')
const systemStatus = ref('online')
const selectedProtocol = ref<any>(null)
const smsCountdown = ref(0)

// 支持的认证协议
const supportedProtocols = ref([
  { code: 'PASSWORD', name: '密码登录', description: '使用用户名和密码登录', category: 'basic', icon: 'KeyIcon' },
  { code: 'SMS', name: '短信验证码', description: '通过手机短信验证码登录', category: 'modern', icon: 'PhoneIcon' },
  { code: 'OAUTH2', name: '第三方登录', description: '使用微信、钉钉等第三方账号登录', category: 'social', icon: 'WechatIcon' },
  { code: 'WECHAT', name: '微信登录', description: '使用微信扫码登录', category: 'social', icon: 'WechatIcon' },
  { code: 'DINGTALK', name: '钉钉登录', description: '使用钉钉扫码登录', category: 'social', icon: 'DingtalkIcon' },
  { code: 'EMAIL', name: '邮箱验证', description: '通过邮箱验证码登录', category: 'modern', icon: 'MailIcon' },
  { code: 'BIOMETRIC', name: '生物识别', description: '使用指纹或面部识别登录', category: 'advanced', icon: 'FingerprintIcon' }
])

// OAuth2提供商
const oauth2Providers = ref([
  { id: 'wechat', name: '微信', icon: 'WechatIcon' },
  { id: 'dingtalk', name: '钉钉', icon: 'DingtalkIcon' },
  { id: 'github', name: 'GitHub', icon: 'GithubIcon' },
  { id: 'google', name: 'Google', icon: 'GoogleIcon' }
])

const formData = reactive({
  username: '',
  password: '',
  tenantId: '',
  phone: '',
  code: '',
  captcha: '',
  rememberMe: false,
  protocol: ''
})

// 表单验证规则
const rules = {
  tenantId: [
    { required: true, message: '请输入租户ID', trigger: 'blur' },
    { min: 3, max: 20, message: '租户ID长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 4, message: '验证码长度为4位', trigger: 'blur' }
  ]
}

// 计算属性
const canLogin = computed(() => {
  if (!selectedProtocol.value) return false
  
  switch (selectedProtocol.value.code) {
    case 'PASSWORD':
      return formData.username && formData.password && (!showCaptcha.value || formData.captcha)
    case 'SMS':
      return formData.phone && formData.code && (!showCaptcha.value || formData.captcha)
    case 'OAUTH2':
      return true
    default:
      return false
  }
})

const canSendSms = computed(() => {
  return formData.phone && smsCountdown.value === 0
})

const systemStatusType = computed(() => {
  return systemStatus.value === 'online' ? 'success' : 'error'
})

const systemStatusText = computed(() => {
  return systemStatus.value === 'online' ? '系统正常' : '系统维护中'
})

// 方法
const selectProtocol = (protocol: any) => {
  selectedProtocol.value = protocol
  formData.protocol = protocol.code
  
  // 重置表单数据
  Object.keys(formData).forEach(key => {
    if (key !== 'rememberMe' && key !== 'protocol') {
      formData[key] = ''
    }
  })
}

const getProtocolIcon = (code: string) => {
  const iconMap: Record<string, string> = {
    'PASSWORD': 'KeyIcon',
    'SMS': 'PhoneIcon',
    'OAUTH2': 'WechatIcon',
    'WECHAT': 'WechatIcon',
    'DINGTALK': 'DingtalkIcon',
    'EMAIL': 'MailIcon',
    'BIOMETRIC': 'FingerprintIcon'
  }
  return iconMap[code] || 'KeyIcon'
}

const handleLogin = async () => {
  try {
    await formRef.value?.validate()
    
    loading.value = true
    
    // 根据协议类型构建登录参数
    const loginParams: any = {
      protocol: selectedProtocol.value.code,
      rememberMe: formData.rememberMe
    }
    
    switch (selectedProtocol.value.code) {
      case 'PASSWORD':
        loginParams.username = formData.username
        loginParams.password = formData.password
        loginParams.tenantId = formData.tenantId
        if (showCaptcha.value) loginParams.captcha = formData.captcha
        break
      case 'SMS':
        loginParams.phone = formData.phone
        loginParams.code = formData.code
        if (showCaptcha.value) loginParams.captcha = formData.captcha
        break
    }
    
    await authStore.login(loginParams)
    
    message.success('登录成功')
    
    // 跳转到首页
    router.push('/dashboard')
  } catch (error: any) {
    console.error('登录失败:', error)
    
    if (error.response?.status === 401) {
      message.error('认证失败，请检查输入信息')
      showCaptcha.value = true
      refreshCaptcha()
    } else if (error.response?.status === 429) {
      message.error('登录尝试次数过多，请稍后再试')
    } else {
      message.error(error.message || '登录失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

const sendSmsCode = async () => {
  if (!formData.phone) {
    message.error('请输入手机号')
    return
  }
  
  try {
    // 调用发送短信验证码的API
    // await authApi.sendSmsCode(formData.phone)
    message.success('验证码已发送')
    
    // 开始倒计时
    smsCountdown.value = 60
    const timer = setInterval(() => {
      smsCountdown.value--
      if (smsCountdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    message.error('发送验证码失败，请重试')
  }
}

const handleOAuth2Login = (provider: any) => {
  message.info(`${provider.name}登录功能开发中...`)
  // 这里可以跳转到OAuth2授权页面
  // window.location.href = `/api/auth/oauth2/${provider.id}`
}

const refreshCaptcha = () => {
  captchaUrl.value = `/api/captcha?t=${Date.now()}`
}

const handleForgotPassword = () => {
  message.info('忘记密码功能开发中...')
}

const handleProtocol = (type: 'privacy' | 'terms') => {
  const urls = {
    privacy: '/protocol/privacy',
    terms: '/protocol/terms'
  }
  window.open(urls[type], '_blank')
}

// 生命周期
onMounted(() => {
  // 检查是否已登录
  if (authStore.isAuthenticated) {
    router.push('/dashboard')
  }
  
  // 检查是否需要验证码
  // 这里可以调用API检查登录失败次数
  
  // 加载支持的认证协议
  loadSupportedProtocols()
})

const loadSupportedProtocols = async () => {
  try {
    // 调用后端API获取支持的认证协议
    // const response = await authApi.getSupportedProtocols()
    // supportedProtocols.value = response.data.protocols
  } catch (error) {
    console.error('加载认证协议失败:', error)
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables';
@import '@/styles/mixins';

.login-container {
  height: 100vh;
  position: relative;
  overflow: hidden;
  @include particle-background;
}

.login-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  
  .quantum-network {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: 
      radial-gradient(circle at 20% 50%, rgba($hologram-blue, 0.1) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba($hologram-purple, 0.1) 0%, transparent 50%);
    animation: network-rotate 20s infinite linear;
  }
  
  .hologram-grid {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: 
      linear-gradient(rgba($hologram-blue, 0.1) 1px, transparent 1px),
      linear-gradient(90deg, rgba($hologram-blue, 0.1) 1px, transparent 1px);
    background-size: 50px 50px;
    animation: grid-move 30s infinite linear;
  }
  
  .particle-field {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(ellipse at center, rgba($hologram-pink, 0.05) 0%, transparent 70%);
  }
}

.login-form-wrapper {
  position: relative;
  z-index: 1;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 400px;
  padding: $spacing-xl;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: $border-radius-lg;
  box-shadow: $shadow-lg;
  @include glass-morphism;
  @include hologram-border;
}

.login-header {
  text-align: center;
  margin-bottom: $spacing-xl;
  
  .logo-section {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: $spacing-md;
    
    .logo-icon {
      .quantum-logo {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, $primary-color, $accent-color);
        border-radius: $border-radius-base;
        @include neon-glow($primary-color);
        animation: logo-rotate 10s infinite linear;
      }
    }
    
    .logo-text {
      .system-name {
        font-size: 24px;
        font-weight: 600;
        margin: 0;
        background: linear-gradient(135deg, $primary-color, $accent-color);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
      
      .system-desc {
        margin: $spacing-xs 0 0 0;
        color: $text-color-secondary;
        font-size: $font-size-sm;
      }
    }
  }
}

.protocol-section {
  margin-bottom: $spacing-xl;
  
  .protocol-title {
    text-align: center;
    margin-bottom: $spacing-lg;
    color: $text-color;
    font-size: $font-size-lg;
    font-weight: 600;
  }
  
  .protocol-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: $spacing-md;
    
    .protocol-card {
      padding: $spacing-md;
      background: rgba(255, 255, 255, 0.8);
      border: 1px solid $border-color;
      border-radius: $border-radius-lg;
      cursor: pointer;
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: $shadow-md;
        border-color: $primary-color;
        
        .protocol-icon {
          transform: scale(1.1);
        }
      }
      
      .protocol-icon {
        width: 48px;
        height: 48px;
        margin-bottom: $spacing-sm;
        transition: transform 0.3s ease;
        
        .n-icon {
          width: 100%;
          height: 100%;
          color: $primary-color;
        }
      }
      
      .protocol-info {
        .protocol-name {
          font-size: $font-size-md;
          font-weight: 600;
          color: $text-color;
          margin-bottom: $spacing-xs;
        }
        
        .protocol-desc {
          font-size: $font-size-sm;
          color: $text-color-secondary;
          line-height: 1.4;
        }
      }
      
      .protocol-badge {
        position: absolute;
        top: $spacing-sm;
        right: $spacing-sm;
        padding: 2px 8px;
        font-size: $font-size-xs;
        border-radius: $border-radius-sm;
        text-transform: uppercase;
        
        &.basic {
          background: rgba($primary-color, 0.1);
          color: $primary-color;
        }
        
        &.modern {
          background: rgba($accent-color, 0.1);
          color: $accent-color;
        }
        
        &.social {
          background: rgba($success-color, 0.1);
          color: $success-color;
        }
        
        &.advanced {
          background: rgba($warning-color, 0.1);
          color: $warning-color;
        }
      }
    }
  }
}

.protocol-header {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  margin-bottom: $spacing-lg;
  
  h3 {
    margin: 0;
    color: $text-color;
    font-size: $font-size-lg;
    font-weight: 600;
  }
}

.form-section {
  .login-form {
    .sms-code-wrapper {
      display: flex;
      gap: $spacing-sm;
      
      .sms-code-input {
        flex: 1;
      }
      
      .sms-send-btn {
        min-width: 120px;
      }
    }
    
    .oauth2-providers {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: $spacing-md;
      margin-bottom: $spacing-lg;
      
      .oauth2-btn {
        width: 100%;
        justify-content: center;
        
        .n-icon {
          margin-right: $spacing-xs;
        }
      }
    }
    
    .captcha-wrapper {
      display: flex;
      gap: $spacing-sm;
      
      .captcha-input {
        flex: 1;
      }
      
      .captcha-image {
        width: 100px;
        height: 40px;
        border: 1px solid $border-color;
        border-radius: $border-radius-base;
        cursor: pointer;
        overflow: hidden;
        
        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        &:hover {
          border-color: $primary-color;
        }
      }
    }
    
    .form-options {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $spacing-lg;
      
      .forgot-password {
        color: $primary-color;
        cursor: pointer;
        
        &:hover {
          color: $primary-color-hover;
        }
      }
    }
    
    .login-btn {
      width: 100%;
      @include quantum-pulse($primary-color);
    }
  }
}

.login-footer {
  margin-top: $spacing-lg;
  text-align: center;
  
  .protocol-links {
    margin-bottom: $spacing-sm;
    
    a {
      color: $text-color-secondary;
      font-size: $font-size-sm;
      cursor: pointer;
      
      &:hover {
        color: $primary-color;
      }
    }
    
    .separator {
      margin: 0 $spacing-sm;
      color: $text-color-disabled;
    }
  }
  
  .version-info {
    color: $text-color-disabled;
    font-size: $font-size-sm;
  }
}

.system-status {
  position: absolute;
  top: $spacing-md;
  right: $spacing-md;
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  .status-text {
    color: white;
    font-size: $font-size-sm;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  }
}

// 动画定义
@keyframes network-rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes grid-move {
  0% { background-position: 0 0; }
  100% { background-position: 50px 50px; }
}

@keyframes logo-rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

// 响应式设计
@include respond-to('sm') {
  .login-card {
    width: 90vw;
    margin: 0 auto;
  }
  
  .login-header .logo-section {
    flex-direction: column;
    text-align: center;
  }
}
</style>