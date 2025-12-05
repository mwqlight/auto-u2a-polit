import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { setupRouterGuard } from './guard'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
    meta: {
      title: '注册',
      requiresAuth: false
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/dashboard/index.vue'),
    meta: {
      title: '驾驶舱',
      requiresAuth: true
    }
  },
  {
    path: '/identity',
    name: 'Identity',
    redirect: '/identity/users',
    component: () => import('@/views/layout/BasicLayout.vue'),
    meta: {
      title: '身份管理',
      requiresAuth: true,
      icon: 'User'
    },
    children: [
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/identity/UserList.vue'),
        meta: {
          title: '用户管理',
          requiresAuth: true
        }
      },
      {
        path: 'organizations',
        name: 'OrganizationManagement',
        component: () => import('@/views/identity/OrganizationTree.vue'),
        meta: {
          title: '组织架构',
          requiresAuth: true
        }
      },
      {
        path: 'tenants',
        name: 'TenantManagement',
        component: () => import('@/views/identity/TenantManagement.vue'),
        meta: {
          title: '租户管理',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/authentication',
    name: 'Authentication',
    redirect: '/authentication/policies',
    component: () => import('@/views/layout/BasicLayout.vue'),
    meta: {
      title: '认证中心',
      requiresAuth: true,
      icon: 'Lock'
    },
    children: [
      {
        path: 'policies',
        name: 'AuthPolicyManagement',
        component: () => import('@/views/authentication/AuthPolicyManagement.vue'),
        meta: {
          title: '认证策略',
          requiresAuth: true
        }
      },
      {
        path: 'clients',
        name: 'ClientManagement',
        component: () => import('@/views/authentication/ClientManagement.vue'),
        meta: {
          title: '客户端管理',
          requiresAuth: true
        }
      },
      {
        path: 'sessions',
        name: 'SessionManagement',
        component: () => import('@/views/authentication/SessionManagement.vue'),
        meta: {
          title: '会话管理',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/authorization',
    name: 'Authorization',
    redirect: '/authorization/roles',
    component: () => import('@/views/layout/BasicLayout.vue'),
    meta: {
      title: '授权中心',
      requiresAuth: true,
      icon: 'Shield'
    },
    children: [
      {
        path: 'roles',
        name: 'RoleManagement',
        component: () => import('@/views/authorization/RoleManagement.vue'),
        meta: {
          title: '角色管理',
          requiresAuth: true
        }
      },
      {
        path: 'permissions',
        name: 'PermissionManagement',
        component: () => import('@/views/authorization/PermissionManagement.vue'),
        meta: {
          title: '权限管理',
          requiresAuth: true
        }
      },
      {
        path: 'policies',
        name: 'AuthorizationPolicyManagement',
        component: () => import('@/views/authorization/AuthorizationPolicyManagement.vue'),
        meta: {
          title: '授权策略',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/permission',
    name: 'Permission',
    redirect: '/permission/permissions',
    component: () => import('@/views/layout/BasicLayout.vue'),
    meta: {
      title: '权限管理',
      requiresAuth: true,
      icon: 'Safety'
    },
    children: [
      {
        path: 'permissions',
        name: 'PermissionList',
        component: () => import('@/views/permission/PermissionList.vue'),
        meta: {
          title: '权限列表',
          requiresAuth: true
        }
      },
      {
        path: 'roles',
        name: 'RoleList',
        component: () => import('@/views/permission/RoleList.vue'),
        meta: {
          title: '角色列表',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/learning',
    name: 'Learning',
    redirect: '/learning/concurrent',
    component: () => import('@/views/learning/index.vue'),
    meta: {
      title: '学习中心',
      requiresAuth: true,
      icon: 'Book'
    },
    children: [
      {
        path: 'basic',
        name: 'BasicSyntax',
        component: () => import('@/views/learning/basic/index.vue'),
        meta: {
          title: '基础语法',
          requiresAuth: true
        }
      },
      {
        path: 'functions',
        name: 'Functions',
        component: () => import('@/views/learning/functions/index.vue'),
        meta: {
          title: '函数与方法',
          requiresAuth: true
        }
      },
      {
        path: 'data-structures',
        name: 'DataStructures',
        component: () => import('@/views/learning/data-structures/index.vue'),
        meta: {
          title: '数据结构',
          requiresAuth: true
        }
      },
      {
        path: 'concurrent',
        name: 'ConcurrentProgramming',
        component: () => import('@/views/learning/concurrent/index.vue'),
        meta: {
          title: '并发编程',
          requiresAuth: true
        }
      },
      {
        path: 'advanced',
        name: 'AdvancedFeatures',
        component: () => import('@/views/learning/advanced/index.vue'),
        meta: {
          title: '高级特性',
          requiresAuth: true
        }
      },
      {
        path: 'sandbox',
        name: 'CodeSandbox',
        component: () => import('@/views/learning/sandbox/index.vue'),
        meta: {
          title: '代码沙盒',
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: {
      title: '页面不存在',
      requiresAuth: false
    }
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 设置路由守卫
setupRouterGuard(router)

export default router