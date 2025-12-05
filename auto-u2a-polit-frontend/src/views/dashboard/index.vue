<template>
  <div class="dashboard-container">
    <!-- 页面头部 -->
    <div class="dashboard-header">
      <div class="header-left">
        <h1 class="page-title">
          <n-icon size="28" class="title-icon">
            <DashboardIcon />
          </n-icon>
          系统驾驶舱
        </h1>
        <p class="page-desc">实时监控系统运行状态和关键指标</p>
      </div>
      <div class="header-right">
        <n-space>
          <n-button type="primary" @click="handleRefresh">
            <template #icon>
              <n-icon><RefreshIcon /></n-icon>
            </template>
            刷新数据
          </n-button>
          <n-button @click="handleExport">
            <template #icon>
              <n-icon><DownloadIcon /></n-icon>
            </template>
            导出报告
          </n-button>
        </n-space>
      </div>
    </div>

    <!-- 关键指标卡片 -->
    <div class="metrics-grid">
      <n-card class="metric-card" hoverable>
        <div class="metric-content">
          <div class="metric-icon user-metric">
            <n-icon size="32">
              <UserGroupIcon />
            </n-icon>
          </div>
          <div class="metric-info">
            <div class="metric-value">{{ metrics.totalUsers }}</div>
            <div class="metric-label">总用户数</div>
            <div class="metric-trend" :class="metrics.userTrend >= 0 ? 'positive' : 'negative'">
              <n-icon :size="16">
                <TrendingUpIcon v-if="metrics.userTrend >= 0" />
                <TrendingDownIcon v-else />
              </n-icon>
              {{ Math.abs(metrics.userTrend) }}%
            </div>
          </div>
        </div>
      </n-card>

      <n-card class="metric-card" hoverable>
        <div class="metric-content">
          <div class="metric-icon auth-metric">
            <n-icon size="32">
              <ShieldCheckIcon />
            </n-icon>
          </div>
          <div class="metric-info">
            <div class="metric-value">{{ metrics.activeSessions }}</div>
            <div class="metric-label">活跃会话</div>
            <div class="metric-trend" :class="metrics.sessionTrend >= 0 ? 'positive' : 'negative'">
              <n-icon :size="16">
                <TrendingUpIcon v-if="metrics.sessionTrend >= 0" />
                <TrendingDownIcon v-else />
              </n-icon>
              {{ Math.abs(metrics.sessionTrend) }}%
            </div>
          </div>
        </div>
      </n-card>

      <n-card class="metric-card" hoverable>
        <div class="metric-content">
          <div class="metric-icon org-metric">
            <n-icon size="32">
              <BuildingIcon />
            </n-icon>
          </div>
          <div class="metric-info">
            <div class="metric-value">{{ metrics.totalOrganizations }}</div>
            <div class="metric-label">组织架构</div>
            <div class="metric-trend" :class="metrics.orgTrend >= 0 ? 'positive' : 'negative'">
              <n-icon :size="16">
                <TrendingUpIcon v-if="metrics.orgTrend >= 0" />
                <TrendingDownIcon v-else />
              </n-icon>
              {{ Math.abs(metrics.orgTrend) }}%
            </div>
          </div>
        </div>
      </n-card>

      <n-card class="metric-card" hoverable>
        <div class="metric-content">
          <div class="metric-icon policy-metric">
            <n-icon size="32">
              <PolicyIcon />
            </n-icon>
          </div>
          <div class="metric-info">
            <div class="metric-value">{{ metrics.activePolicies }}</div>
            <div class="metric-label">认证策略</div>
            <div class="metric-trend" :class="metrics.policyTrend >= 0 ? 'positive' : 'negative'">
              <n-icon :size="16">
                <TrendingUpIcon v-if="metrics.policyTrend >= 0" />
                <TrendingDownIcon v-else />
              </n-icon>
              {{ Math.abs(metrics.policyTrend) }}%
            </div>
          </div>
        </div>
      </n-card>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <n-grid cols="1 s:1 m:2 l:2" responsive="screen" :x-gap="16" :y-gap="16">
        <n-gi>
          <n-card title="用户活跃趋势" class="chart-card">
            <div ref="userChartRef" class="chart-container"></div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="认证请求分布" class="chart-card">
            <div ref="authChartRef" class="chart-container"></div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="系统性能指标" class="chart-card">
            <div ref="performanceChartRef" class="chart-container"></div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="安全事件统计" class="chart-card">
            <div ref="securityChartRef" class="chart-container"></div>
          </n-card>
        </n-gi>
      </n-grid>
    </div>

    <!-- 实时活动 -->
    <div class="activity-section">
      <n-card title="实时活动监控" class="activity-card">
        <n-list>
          <n-list-item v-for="activity in recentActivities" :key="activity.id">
            <template #prefix>
              <n-avatar :size="40" :src="activity.avatar" round>
                <n-icon v-if="!activity.avatar">
                  <UserIcon />
                </n-icon>
              </n-avatar>
            </template>
            <n-thing :title="activity.title" :description="activity.description">
              <template #avatar>
                <n-tag :type="getActivityType(activity.type)" size="small">
                  {{ activity.type }}
                </n-tag>
              </template>
              <template #action>
                <n-time :time="activity.timestamp" type="relative" />
              </template>
            </n-thing>
          </n-list-item>
        </n-list>
      </n-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useMessage } from 'naive-ui'
import * as echarts from 'echarts'
import {
  DashboardIcon,
  RefreshIcon,
  DownloadIcon,
  UserGroupIcon,
  ShieldCheckIcon,
  BuildingIcon,
  PolicyIcon,
  TrendingUpIcon,
  TrendingDownIcon,
  UserIcon
} from '@vicons/fa'

// 消息提示
const message = useMessage()

// 图表引用
const userChartRef = ref<HTMLElement>()
const authChartRef = ref<HTMLElement>()
const performanceChartRef = ref<HTMLElement>()
const securityChartRef = ref<HTMLElement>()

// 图表实例
let userChart: echarts.ECharts | null = null
let authChart: echarts.ECharts | null = null
let performanceChart: echarts.ECharts | null = null
let securityChart: echarts.ECharts | null = null

// 响应式数据
const metrics = reactive({
  totalUsers: 0,
  activeSessions: 0,
  totalOrganizations: 0,
  activePolicies: 0,
  userTrend: 0,
  sessionTrend: 0,
  orgTrend: 0,
  policyTrend: 0
})

const recentActivities = ref([
  {
    id: 1,
    title: '用户登录',
    description: 'admin 从 192.168.1.100 登录系统',
    type: 'login',
    timestamp: Date.now() - 30000,
    avatar: ''
  },
  {
    id: 2,
    title: '策略更新',
    description: 'OAuth2 认证策略配置已更新',
    type: 'policy',
    timestamp: Date.now() - 120000,
    avatar: ''
  },
  {
    id: 3,
    title: '用户创建',
    description: '新用户 testuser 已创建',
    type: 'user',
    timestamp: Date.now() - 300000,
    avatar: ''
  },
  {
    id: 4,
    title: '安全告警',
    description: '检测到异常登录尝试',
    type: 'security',
    timestamp: Date.now() - 600000,
    avatar: ''
  }
])

// 方法
const getActivityType = (type: string) => {
  const typeMap: Record<string, any> = {
    login: 'success',
    policy: 'info',
    user: 'warning',
    security: 'error'
  }
  return typeMap[type] || 'default'
}

const handleRefresh = async () => {
  message.info('正在刷新数据...')
  await loadDashboardData()
  message.success('数据刷新完成')
}

const handleExport = () => {
  message.info('导出功能开发中...')
}

const loadDashboardData = async () => {
  // 模拟数据加载
  metrics.totalUsers = Math.floor(Math.random() * 10000) + 5000
  metrics.activeSessions = Math.floor(Math.random() * 1000) + 200
  metrics.totalOrganizations = Math.floor(Math.random() * 100) + 50
  metrics.activePolicies = Math.floor(Math.random() * 20) + 10
  
  metrics.userTrend = Math.random() * 20 - 10
  metrics.sessionTrend = Math.random() * 15 - 5
  metrics.orgTrend = Math.random() * 10 - 5
  metrics.policyTrend = Math.random() * 5 - 2
}

const initCharts = () => {
  if (userChartRef.value) {
    userChart = echarts.init(userChartRef.value)
    userChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'] },
      yAxis: { type: 'value' },
      series: [{
        data: [120, 200, 150, 80, 70, 110, 130],
        type: 'line',
        smooth: true,
        lineStyle: { color: '#1890ff' },
        areaStyle: { color: 'rgba(24, 144, 255, 0.1)' }
      }]
    })
  }

  if (authChartRef.value) {
    authChart = echarts.init(authChartRef.value)
    authChart.setOption({
      tooltip: { trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: [
          { value: 335, name: 'OAuth2' },
          { value: 310, name: 'OIDC' },
          { value: 234, name: 'SAML' },
          { value: 135, name: 'LDAP' },
          { value: 1548, name: '本地认证' }
        ]
      }]
    })
  }

  if (performanceChartRef.value) {
    performanceChart = echarts.init(performanceChartRef.value)
    performanceChart.setOption({
      tooltip: { trigger: 'axis' },
      radar: {
        indicator: [
          { name: '响应时间', max: 100 },
          { name: '并发数', max: 1000 },
          { name: '成功率', max: 100 },
          { name: 'CPU使用率', max: 100 },
          { name: '内存使用率', max: 100 }
        ]
      },
      series: [{
        type: 'radar',
        data: [{ value: [85, 750, 99, 45, 60] }]
      }]
    })
  }

  if (securityChartRef.value) {
    securityChart = echarts.init(securityChartRef.value)
    securityChart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
      yAxis: { type: 'value' },
      series: [
        { name: '异常登录', type: 'bar', data: [5, 8, 3, 12, 7, 4] },
        { name: '密码尝试', type: 'bar', data: [2, 5, 1, 8, 3, 2] },
        { name: '权限变更', type: 'bar', data: [1, 3, 2, 5, 4, 3] }
      ]
    })
  }
}

const resizeCharts = () => {
  userChart?.resize()
  authChart?.resize()
  performanceChart?.resize()
  securityChart?.resize()
}

// 生命周期
onMounted(async () => {
  await loadDashboardData()
  initCharts()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  userChart?.dispose()
  authChart?.dispose()
  performanceChart?.dispose()
  securityChart?.dispose()
})
</script>

<style lang="scss" scoped>
@import '@/styles/variables';
@import '@/styles/mixins';

.dashboard-container {
  padding: $spacing-lg;
  height: 100%;
  overflow-y: auto;
}

.dashboard-header {
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

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: $spacing-lg;
  margin-bottom: $spacing-xl;
  
  .metric-card {
    @include hologram-border($primary-color);
    
    .metric-content {
      display: flex;
      align-items: center;
      gap: $spacing-md;
      
      .metric-icon {
        padding: $spacing-md;
        border-radius: $border-radius-lg;
        
        &.user-metric {
          background: linear-gradient(135deg, rgba($primary-color, 0.1), rgba($accent-color, 0.1));
          color: $primary-color;
        }
        
        &.auth-metric {
          background: linear-gradient(135deg, rgba($success-color, 0.1), rgba($info-color, 0.1));
          color: $success-color;
        }
        
        &.org-metric {
          background: linear-gradient(135deg, rgba($warning-color, 0.1), rgba($accent-color, 0.1));
          color: $warning-color;
        }
        
        &.policy-metric {
          background: linear-gradient(135deg, rgba($error-color, 0.1), rgba($accent-color, 0.1));
          color: $error-color;
        }
      }
      
      .metric-info {
        flex: 1;
        
        .metric-value {
          font-size: 32px;
          font-weight: 600;
          color: $text-color;
          line-height: 1;
        }
        
        .metric-label {
          margin: $spacing-xs 0;
          color: $text-color-secondary;
          font-size: $font-size-sm;
        }
        
        .metric-trend {
          display: flex;
          align-items: center;
          gap: $spacing-xs;
          font-size: $font-size-sm;
          
          &.positive {
            color: $success-color;
          }
          
          &.negative {
            color: $error-color;
          }
        }
      }
    }
  }
}

.charts-section {
  margin-bottom: $spacing-xl;
  
  .chart-card {
    height: 300px;
    
    .chart-container {
      width: 100%;
      height: 250px;
    }
  }
}

.activity-section {
  .activity-card {
    .n-list-item {
      padding: $spacing-md 0;
      
      &:not(:last-child) {
        border-bottom: 1px solid $border-color-light;
      }
    }
  }
}

// 响应式设计
@include respond-to('md') {
  .metrics-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@include respond-to('sm') {
  .dashboard-container {
    padding: $spacing-md;
  }
  
  .dashboard-header {
    flex-direction: column;
    align-items: flex-start;
    gap: $spacing-md;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>