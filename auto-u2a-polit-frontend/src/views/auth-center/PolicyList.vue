<template>
  <div class="policy-list-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <n-icon size="28" class="title-icon">
            <ShieldIcon />
          </n-icon>
          认证策略管理
        </h1>
        <p class="page-desc">配置和管理多种认证协议和策略</p>
      </div>
      <div class="header-right">
        <n-space>
          <n-button type="primary" @click="handleCreatePolicy">
            <template #icon>
              <n-icon><PlusIcon /></n-icon>
            </template>
            新建策略
          </n-button>
          <n-button @click="handleImport">
            <template #icon>
              <n-icon><UploadIcon /></n-icon>
            </template>
            导入配置
          </n-button>
        </n-space>
      </div>
    </div>

    <!-- 协议类型筛选 -->
    <n-card class="protocol-filter-card">
      <div class="protocol-tabs">
        <n-radio-group v-model:value="activeProtocol" size="large">
          <n-space>
            <n-radio-button value="all">全部协议</n-radio-button>
            <n-radio-button value="oauth2">OAuth2</n-radio-button>
            <n-radio-button value="oidc">OIDC</n-radio-button>
            <n-radio-button value="saml">SAML</n-radio-button>
            <n-radio-button value="ldap">LDAP</n-radio-button>
            <n-radio-button value="local">本地认证</n-radio-button>
          </n-space>
        </n-radio-group>
      </div>
    </n-card>

    <!-- 策略卡片网格 -->
    <div class="policy-grid">
      <n-card
        v-for="policy in filteredPolicies"
        :key="policy.id"
        class="policy-card"
        hoverable
        :class="{ 'policy-active': policy.status === 'ACTIVE' }"
      >
        <template #header>
          <div class="policy-header">
            <div class="policy-icon">
              <n-icon size="24" :color="getProtocolColor(policy.protocol)">
                <component :is="getProtocolIcon(policy.protocol)" />
              </n-icon>
            </div>
            <div class="policy-info">
              <h3 class="policy-name">{{ policy.name }}</h3>
              <n-tag :type="getStatusType(policy.status)" size="small">
                {{ getStatusText(policy.status) }}
              </n-tag>
            </div>
            <div class="policy-priority">
              <n-tag type="info" size="small">
                优先级: {{ policy.priority }}
              </n-tag>
            </div>
          </div>
        </template>

        <div class="policy-content">
          <div class="policy-desc">
            {{ policy.description || '暂无描述' }}
          </div>
          
          <div class="policy-meta">
            <div class="meta-item">
              <n-icon size="16">
                <ClockIcon />
              </n-icon>
              <span>创建: {{ formatTime(policy.createdAt) }}</span>
            </div>
            <div class="meta-item">
              <n-icon size="16">
                <UserIcon />
              </n-icon>
              <span>创建者: {{ policy.createdBy }}</span>
            </div>
          </div>

          <div class="policy-scopes">
            <n-tag
              v-for="scope in policy.scopes.slice(0, 3)"
              :key="scope"
              size="small"
              type="success"
            >
              {{ scope }}
            </n-tag>
            <n-tag v-if="policy.scopes.length > 3" size="small" type="info">
              +{{ policy.scopes.length - 3 }}
            </n-tag>
          </div>
        </div>

        <template #action>
          <div class="policy-actions">
            <n-button
              size="small"
              :type="policy.status === 'ACTIVE' ? 'warning' : 'success'"
              @click="handleToggleStatus(policy)"
            >
              <template #icon>
                <n-icon>
                  <component :is="policy.status === 'ACTIVE' ? PauseIcon : PlayIcon" />
                </n-icon>
              </template>
              {{ policy.status === 'ACTIVE' ? '停用' : '启用' }}
            </n-button>
            
            <n-button size="small" @click="handleEditPolicy(policy)">
              <template #icon>
                <n-icon><EditIcon /></n-icon>
              </template>
              编辑
            </n-button>
            
            <n-button size="small" type="info" @click="handleViewDetails(policy)">
              <template #icon>
                <n-icon><EyeIcon /></n-icon>
              </template>
              详情
            </n-button>
            
            <n-button size="small" type="error" @click="handleDeletePolicy(policy)">
              <template #icon>
                <n-icon><DeleteIcon /></n-icon>
              </template>
              删除
            </n-button>
          </div>
        </template>
      </n-card>
    </div>

    <!-- 空状态 -->
    <n-empty
      v-if="filteredPolicies.length === 0"
      class="empty-state"
      description="暂无认证策略"
    >
      <template #extra>
        <n-button size="large" type="primary" @click="handleCreatePolicy">
          创建第一个策略
        </n-button>
      </template>
    </n-empty>

    <!-- 策略详情对话框 -->
    <n-modal
      v-model:show="showDetailModal"
      preset="dialog"
      title="策略详情"
      positive-text="关闭"
      @positive-click="showDetailModal = false"
      style="width: 600px"
    >
      <div v-if="selectedPolicy" class="policy-detail">
        <n-descriptions label-placement="left" bordered column="1">
          <n-descriptions-item label="策略名称">
            {{ selectedPolicy.name }}
          </n-descriptions-item>
          <n-descriptions-item label="协议类型">
            <n-tag :type="getProtocolTagType(selectedPolicy.protocol)">
              {{ getProtocolText(selectedPolicy.protocol) }}
            </n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="状态">
            <n-tag :type="getStatusType(selectedPolicy.status)">
              {{ getStatusText(selectedPolicy.status) }}
            </n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="优先级">
            {{ selectedPolicy.priority }}
          </n-descriptions-item>
          <n-descriptions-item label="作用域">
            <n-space>
              <n-tag
                v-for="scope in selectedPolicy.scopes"
                :key="scope"
                size="small"
                type="success"
              >
                {{ scope }}
              </n-tag>
            </n-space>
          </n-descriptions-item>
          <n-descriptions-item label="描述">
            {{ selectedPolicy.description || '暂无描述' }}
          </n-descriptions-item>
          <n-descriptions-item label="创建时间">
            {{ formatTime(selectedPolicy.createdAt) }}
          </n-descriptions-item>
          <n-descriptions-item label="创建者">
            {{ selectedPolicy.createdBy }}
          </n-descriptions-item>
        </n-descriptions>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useMessage, useDialog } from 'naive-ui'
import {
  ShieldIcon,
  PlusIcon,
  UploadIcon,
  EditIcon,
  DeleteIcon,
  EyeIcon,
  PlayIcon,
  PauseIcon,
  ClockIcon,
  UserIcon
} from '@vicons/fa'

// 消息和对话框
const message = useMessage()
const dialog = useDialog()

// 响应式数据
const activeProtocol = ref('all')
const showDetailModal = ref(false)
const selectedPolicy = ref<any>(null)

// 模拟策略数据
const policies = ref([
  {
    id: 'policy-1',
    name: 'OAuth2 客户端认证',
    protocol: 'oauth2',
    status: 'ACTIVE',
    priority: 1,
    description: '用于第三方应用接入的OAuth2认证策略',
    scopes: ['read:user', 'write:user', 'read:profile'],
    createdAt: new Date('2024-01-15').toISOString(),
    createdBy: 'admin'
  },
  {
    id: 'policy-2',
    name: 'OIDC 单点登录',
    protocol: 'oidc',
    status: 'ACTIVE',
    priority: 2,
    description: '支持OpenID Connect协议的单点登录策略',
    scopes: ['openid', 'profile', 'email'],
    createdAt: new Date('2024-01-20').toISOString(),
    createdBy: 'admin'
  },
  {
    id: 'policy-3',
    name: 'SAML 企业集成',
    protocol: 'saml',
    status: 'INACTIVE',
    priority: 3,
    description: '用于企业SAML身份提供商集成',
    scopes: ['saml:authn'],
    createdAt: new Date('2024-02-01').toISOString(),
    createdBy: 'system'
  },
  {
    id: 'policy-4',
    name: 'LDAP 目录服务',
    protocol: 'ldap',
    status: 'ACTIVE',
    priority: 4,
    description: '集成企业LDAP目录服务的认证策略',
    scopes: ['ldap:bind'],
    createdAt: new Date('2024-02-10').toISOString(),
    createdBy: 'admin'
  },
  {
    id: 'policy-5',
    name: '本地密码认证',
    protocol: 'local',
    status: 'ACTIVE',
    priority: 5,
    description: '系统本地用户的密码认证策略',
    scopes: ['local:auth'],
    createdAt: new Date('2024-01-01').toISOString(),
    createdBy: 'system'
  }
])

// 计算属性
const filteredPolicies = computed(() => {
  if (activeProtocol.value === 'all') {
    return policies.value
  }
  return policies.value.filter(policy => policy.protocol === activeProtocol.value)
})

// 方法
const getProtocolIcon = (protocol: string) => {
  const iconMap: Record<string, any> = {
    oauth2: ShieldIcon,
    oidc: ShieldIcon,
    saml: ShieldIcon,
    ldap: ShieldIcon,
    local: ShieldIcon
  }
  return iconMap[protocol] || ShieldIcon
}

const getProtocolColor = (protocol: string) => {
  const colorMap: Record<string, string> = {
    oauth2: '#52c41a',
    oidc: '#1890ff',
    saml: '#722ed1',
    ldap: '#faad14',
    local: '#f5222d'
  }
  return colorMap[protocol] || '#8c8c8c'
}

const getProtocolTagType = (protocol: string) => {
  const typeMap: Record<string, any> = {
    oauth2: 'success',
    oidc: 'info',
    saml: 'warning',
    ldap: 'error',
    local: 'default'
  }
  return typeMap[protocol] || 'default'
}

const getProtocolText = (protocol: string) => {
  const textMap: Record<string, string> = {
    oauth2: 'OAuth2',
    oidc: 'OIDC',
    saml: 'SAML',
    ldap: 'LDAP',
    local: '本地认证'
  }
  return textMap[protocol] || protocol
}

const getStatusType = (status: string) => {
  const typeMap: Record<string, any> = {
    ACTIVE: 'success',
    INACTIVE: 'error',
    DRAFT: 'warning',
    ARCHIVED: 'default'
  }
  return typeMap[status] || 'default'
}

const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    ACTIVE: '启用',
    INACTIVE: '停用',
    DRAFT: '草稿',
    ARCHIVED: '归档'
  }
  return textMap[status] || status
}

const formatTime = (time: string) => {
  return new Date(time).toLocaleString('zh-CN')
}

const handleCreatePolicy = () => {
  message.info('创建策略功能开发中...')
}

const handleImport = () => {
  message.info('导入配置功能开发中...')
}

const handleToggleStatus = (policy: any) => {
  const newStatus = policy.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const action = policy.status === 'ACTIVE' ? '停用' : '启用'
  
  dialog.warning({
    title: '确认操作',
    content: `确定要${action}策略 "${policy.name}" 吗？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: () => {
      policy.status = newStatus
      message.success(`${action}策略成功`)
    }
  })
}

const handleEditPolicy = (policy: any) => {
  message.info(`编辑策略: ${policy.name}`)
}

const handleViewDetails = (policy: any) => {
  selectedPolicy.value = policy
  showDetailModal.value = true
}

const handleDeletePolicy = (policy: any) => {
  dialog.error({
    title: '确认删除',
    content: `确定要删除策略 "${policy.name}" 吗？此操作不可恢复。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: () => {
      policies.value = policies.value.filter(p => p.id !== policy.id)
      message.success('删除策略成功')
    }
  })
}

// 生命周期
onMounted(() => {
  // 可以在这里加载真实数据
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables';
@import '@/styles/mixins';

.policy-list-container {
  padding: $spacing-lg;
  height: 100%;
  overflow-y: auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: $spacing-xl;
  
  .header-left {
    .page-title {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: $text-color;
      
      .title-icon {
        color: $primary-color;
      }
    }
    
    .page-desc {
      margin: $spacing-xs 0 0 0;
      color: $text-color-secondary;
      font-size: $font-size-sm;
    }
  }
}

.protocol-filter-card {
  margin-bottom: $spacing-lg;
  
  .protocol-tabs {
    .n-radio-group {
      width: 100%;
    }
  }
}

.policy-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: $spacing-lg;
  
  .policy-card {
    transition: all 0.3s ease;
    
    &.policy-active {
      @include hologram-border($primary-color);
      
      &:hover {
        transform: translateY(-4px);
        box-shadow: $shadow-lg;
      }
    }
    
    .policy-header {
      display: flex;
      align-items: center;
      gap: $spacing-md;
      
      .policy-icon {
        flex-shrink: 0;
      }
      
      .policy-info {
        flex: 1;
        
        .policy-name {
          margin: 0 0 $spacing-xs 0;
          font-size: 16px;
          font-weight: 600;
          color: $text-color;
        }
      }
      
      .policy-priority {
        flex-shrink: 0;
      }
    }
    
    .policy-content {
      .policy-desc {
        margin-bottom: $spacing-md;
        color: $text-color-secondary;
        line-height: 1.5;
        @include text-ellipsis(3);
      }
      
      .policy-meta {
        display: flex;
        flex-direction: column;
        gap: $spacing-xs;
        margin-bottom: $spacing-md;
        
        .meta-item {
          display: flex;
          align-items: center;
          gap: $spacing-xs;
          color: $text-color-secondary;
          font-size: $font-size-sm;
        }
      }
      
      .policy-scopes {
        display: flex;
        flex-wrap: wrap;
        gap: $spacing-xs;
      }
    }
    
    .policy-actions {
      display: flex;
      gap: $spacing-xs;
      
      .n-button {
        flex: 1;
      }
    }
  }
}

.empty-state {
  margin: $spacing-xxl 0;
  text-align: center;
}

.policy-detail {
  .n-descriptions {
    margin-top: $spacing-md;
  }
}

// 响应式设计
@include respond-to('md') {
  .policy-grid {
    grid-template-columns: 1fr;
  }
}

@include respond-to('sm') {
  .policy-list-container {
    padding: $spacing-md;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-md;
  }
  
  .protocol-tabs .n-radio-group {
    .n-space {
      flex-wrap: wrap;
    }
  }
  
  .policy-actions {
    flex-direction: column;
  }
}
</style>