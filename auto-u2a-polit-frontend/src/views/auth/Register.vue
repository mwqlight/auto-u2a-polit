<template>
  <div class="register-container">
    <!-- 背景特效 -->
    <div class="register-background">
      <div class="quantum-network"></div>
      <div class="hologram-grid"></div>
      <div class="particle-field"></div>
    </div>

    <!-- 注册表单 -->
    <div class="register-form-wrapper">
      <div class="register-card">
        <!-- 头部 -->
        <div class="register-header">
          <div class="logo-section">
            <div class="logo-icon">
              <div class="quantum-logo"></div>
            </div>
            <div class="logo-text">
              <h1 class="system-name">Auto U2A POLIT</h1>
              <p class="system-desc">用户注册中心</p>
            </div>
          </div>
        </div>

        <!-- 表单区域 -->
        <div class="form-section">
          <n-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            size="large"
            class="register-form"
          >
            <!-- 用户名 -->
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

            <!-- 邮箱 -->
            <n-form-item path="email">
              <n-input
                v-model:value="formData.email"
                placeholder="请输入邮箱地址"
                :input-props="{ autocomplete: 'email' }"
              >
                <template #prefix>
                  <n-icon>
                    <MailIcon />
                  </n-icon>
                </template>
              </n-input>
            </n-form-item>

            <!-- 手机号 -->
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

            <!-- 显示名称 -->
            <n-form-item path="displayName">
              <n-input
                v-model:value="formData.displayName"
                placeholder="请输入显示名称"
                :input-props="{ autocomplete: 'name' }"
              >
                <template #prefix>
                  <n-icon>
                    <UserIcon />
                  </n-icon>
                </template>
              </n-input>
            </n-form-item>

            <!-- 密码 -->
            <n-form-item path="password">
              <n-input
                v-model:value="formData.password"
                type="password"
                placeholder="请输入密码"
                show-password-on="click"
                :input-props="{ autocomplete: 'new-password' }"
              >
                <template #prefix>
                  <n-icon>
                    <LockIcon />
                  </n-icon>
                </template>
              </n-input>
            </n-form-item>

            <!-- 确认密码 -->
            <n-form-item path="confirmPassword">
              <n-input
                v-model:value="formData.confirmPassword"
                type="password"
                placeholder="请确认密码"
                show-password-on="click"
                :input-props="{ autocomplete: 'new-password' }"
              >
                <template #prefix>
                  <n-icon>
                    <LockIcon />
                  </n-icon>
                </template>
              </n-input>
            </n-form-item>

            <!-- 验证码 -->
            <n-form-item v-if="showCaptcha" path="captcha">
              <div class="captcha-wrapper">
                <n-input
                  v-model:value="formData.captcha"
                  placeholder="请输入验证码"
                  class="captcha-input"
                />
                <div class="captcha-image" @click="refreshCaptcha">
                  <img :src="captchaUrl" alt="验证码" />
                </div>
              </div>
            </n-form-item>

            <!-- 短信验证码 -->
            <n-form-item path="smsCode">
              <div class="sms-code-wrapper">
                <n-input
                  v-model:value="formData.smsCode"
                  placeholder="请输入短信验证码"
                  class="sms-code-input"
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

            <!-- 同意协议 -->
            <div class="form-options">
              <n-checkbox v-model:checked="formData.agreeTerms">
                我同意
                <a class="terms-link" @click="handleTerms">《用户协议》</a>
                和
                <a class="terms-link" @click="handlePrivacy">《隐私政策》</a>
              </n-checkbox>
            </div>

            <!-- 注册按钮 -->
            <n-button
              type="primary"
              size="large"
              :loading="loading"
              :disabled="!canRegister"
              @click="handleRegister"
              class="register-btn"
            >
              {{ loading ? '注册中...' : '注册' }}
            </n-button>
          </n-form>
        </div>

        <!-- 底部信息 -->
        <div class="register-footer">
          <p>已有账户？
            <a class="login-link" @click="handleLoginRedirect">立即登录</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { UserIcon, LockIcon, MailIcon, PhoneIcon } from '@vicons/ionicons5'
import { register } from '@/api/auth'

const router = useRouter()
const message = useMessage()
const formRef = ref()
const loading = ref(false)
const showCaptcha = ref(false)
const captchaUrl = ref('')
const smsCountdown = ref(0)
const canSendSms = computed(() => smsCountdown.value === 0)

// 表单数据
const formData = reactive({
  username: '',
  email: '',
  phone: '',
  displayName: '',
  password: '',
  confirmPassword: '',
  captcha: '',
  smsCode: '',
  agreeTerms: false
})

// 表单规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: '用户名只能包含字母、数字、下划线和减号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  displayName: [
    { required: true, message: '请输入显示名称', trigger: 'blur' },
    { min: 2, max: 20, message: '显示名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{6,20}$/, message: '密码必须包含大小写字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule: any, value: string) => {
      if (value !== formData.password) {
        return new Error('两次输入的密码不一致')
      }
      return true
    }, trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ],
  smsCode: [
    { required: true, message: '请输入短信验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '短信验证码长度为 6 个字符', trigger: 'blur' }
  ],
  agreeTerms: [
    { validator: (rule: any, value: boolean) => {
      if (!value) {
        return new Error('请同意用户协议和隐私政策')
      }
      return true
    }, trigger: 'change' }
  ]
}

// 计算属性：是否可以注册
const canRegister = computed(() => {
  return (
    formData.username &&
    formData.email &&
    formData.phone &&
    formData.displayName &&
    formData.password &&
    formData.confirmPassword &&
    formData.smsCode &&
    formData.agreeTerms &&
    !loading.value
  )
})

// 刷新验证码
const refreshCaptcha = () => {
  // TODO: 实现验证码刷新逻辑
  captchaUrl.value = `/api/captcha?timestamp=${Date.now()}`
}

// 发送短信验证码
const sendSmsCode = async () => {
  if (!formData.phone) {
    message.warning('请先输入手机号')
    return
  }

  try {
    // TODO: 实现发送短信验证码逻辑
    message.success('短信验证码已发送，请注意查收')
    
    // 开始倒计时
    smsCountdown.value = 60
    const timer = setInterval(() => {
      smsCountdown.value--
      if (smsCountdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    message.error('发送短信验证码失败，请稍后重试')
  }
}

// 处理注册
const handleRegister = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true
    
    // 调用注册接口
    await register({
      username: formData.username,
      email: formData.email,
      phone: formData.phone,
      displayName: formData.displayName,
      password: formData.password,
      smsCode: formData.smsCode
    })

    message.success('注册成功，即将跳转到登录页面')
    
    // 延迟跳转，让用户看到成功提示
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error: any) {
    message.error(error.message || '注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 处理用户协议
const handleTerms = () => {
  message.info('用户协议内容...')
}

// 处理隐私政策
const handlePrivacy = () => {
  message.info('隐私政策内容...')
}

// 跳转到登录页面
const handleLoginRedirect = () => {
  router.push('/login')
}

// 页面加载时刷新验证码
onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.register-container {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
}

.register-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.quantum-network {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(64, 158, 255, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(64, 158, 255, 0.1) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: quantumFlow 20s linear infinite;
}

.hologram-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: 
    radial-gradient(circle at 25% 25%, rgba(64, 158, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 75% 75%, rgba(64, 158, 255, 0.1) 0%, transparent 50%);
  animation: hologramPulse 8s ease-in-out infinite;
}

.particle-field {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    radial-gradient(2px 2px at 20% 30%, white, transparent),
    radial-gradient(2px 2px at 60% 70%, white, transparent),
    radial-gradient(1px 1px at 50% 50%, white, transparent),
    radial-gradient(1px 1px at 80% 10%, white, transparent),
    radial-gradient(2px 2px at 90% 60%, white, transparent),
    radial-gradient(1px 1px at 33% 80%, white, transparent),
    radial-gradient(1px 1px at 15% 60%, white, transparent);
  background-size: 200% 200%;
  animation: particleDrift 25s linear infinite;
}

@keyframes quantumFlow {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

@keyframes hologramPulse {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 0.6; }
}

@keyframes particleDrift {
  0% { background-position: 0% 0%; }
  100% { background-position: 100% 100%; }
}

.register-form-wrapper {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 480px;
  padding: 20px;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 
    0 20px 60px rgba(0, 0, 0, 0.3),
    0 0 1px rgba(255, 255, 255, 0.5) inset;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.register-card:hover {
  transform: translateY(-5px);
  box-shadow: 
    0 25px 70px rgba(0, 0, 0, 0.4),
    0 0 1px rgba(255, 255, 255, 0.5) inset;
}

.register-header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 16px;
}

.logo-icon {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.quantum-logo {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border-radius: 16px;
  position: relative;
  animation: quantumRotate 3s linear infinite;
}

.quantum-logo::before {
  content: '';
  position: absolute;
  top: 10px;
  left: 10px;
  right: 10px;
  bottom: 10px;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border-radius: 12px;
  opacity: 0.8;
}

@keyframes quantumRotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.logo-text {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.system-name {
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0;
}

.system-desc {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

.form-section {
  margin-bottom: 30px;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.captcha-wrapper {
  display: flex;
  gap: 12px;
}

.captcha-input {
  flex: 1;
}

.captcha-image {
  width: 120px;
  height: 40px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sms-code-wrapper {
  display: flex;
  gap: 12px;
}

.sms-code-input {
  flex: 1;
}

.sms-send-btn {
  min-width: 120px;
}

.form-options {
  margin-bottom: 20px;
}

.terms-link {
  color: #409EFF;
  cursor: pointer;
  text-decoration: underline;
}

.terms-link:hover {
  color: #66b1ff;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(64, 158, 255, 0.3);
}

.register-footer {
  text-align: center;
  color: #606266;
}

.login-link {
  color: #409EFF;
  cursor: pointer;
  text-decoration: none;
}

.login-link:hover {
  color: #66b1ff;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-form-wrapper {
    padding: 15px;
  }
  
  .register-card {
    padding: 30px 20px;
  }
  
  .logo-section {
    flex-direction: column;
    gap: 12px;
  }
  
  .system-name {
    font-size: 24px;
  }
  
  .system-desc {
    font-size: 12px;
  }
  
  .register-form {
    gap: 16px;
  }
  
  .captcha-wrapper {
    flex-direction: column;
  }
  
  .captcha-image {
    width: 100%;
    height: 48px;
  }
  
  .sms-code-wrapper {
    flex-direction: column;
  }
  
  .sms-send-btn {
    width: 100%;
    min-width: auto;
  }
}

@media (max-width: 480px) {
  .register-card {
    padding: 25px 15px;
    border-radius: 16px;
  }
  
  .system-name {
    font-size: 22px;
  }
  
  .register-btn {
    height: 44px;
    font-size: 15px;
  }
}
</style>