# API 接口文档

## 📋 文档说明

本文档详细描述了 Auto U2A Polit 系统的 RESTful API 接口规范、请求/响应格式、错误码定义等内容。

## 🔑 认证与授权

### 认证方式
系统采用 JWT (JSON Web Token) 进行身份认证。

### 请求头
```http
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

## 📊 统一响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": 1650000000000
}
```

### 错误响应
```json
{
  "code": 401,
  "message": "未授权访问",
  "data": null,
  "timestamp": 1650000000000
}
```

## 🔢 错误码定义

| 错误码 | 说明 | 描述 |
|--------|------|------|
| 200 | 成功 | 操作成功 |
| 400 | 请求错误 | 参数验证失败 |
| 401 | 未授权 | 需要登录认证 |
| 403 | 禁止访问 | 权限不足 |
| 404 | 资源不存在 | 请求的资源不存在 |
| 500 | 服务器错误 | 系统内部错误 |

| 业务错误码 | 说明 | 描述 |
|------------|------|------|
| 10001 | 用户不存在 | 指定的用户不存在 |
| 10002 | 密码错误 | 密码验证失败 |
| 10003 | 用户已存在 | 用户名已被注册 |
| 20001 | 角色不存在 | 指定的角色不存在 |
| 20002 | 权限不存在 | 指定的权限不存在 |
| 30001 | 参数验证失败 | 请求参数不符合要求 |

## 👤 用户管理接口

### 用户登录
```http
POST /api/v1/auth/login
```

**请求参数：**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**响应数据：**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "roles": ["ROLE_ADMIN"]
    }
  },
  "timestamp": 1650000000000
}
```

### 获取当前用户信息
```http
GET /api/v1/auth/me
```

**响应数据：**
```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"],
    "permissions": ["user:read", "user:write"]
  },
  "timestamp": 1650000000000
}
```

### 用户列表查询
```http
GET /api/v1/users?page=1&size=10&username=admin
```

**查询参数：**
- `page`: 页码（默认1）
- `size`: 每页大小（默认10）
- `username`: 用户名模糊查询
- `email`: 邮箱模糊查询
- `status`: 用户状态（0-禁用，1-启用）

**响应数据：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "items": [
      {
        "id": 1,
        "username": "admin",
        "email": "admin@example.com",
        "status": 1,
        "createTime": "2023-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  },
  "timestamp": 1650000000000
}
```

## 👥 角色管理接口

### 角色列表查询
```http
GET /api/v1/roles?page=1&size=10&name=管理员
```

**查询参数：**
- `page`: 页码（默认1）
- `size`: 每页大小（默认10）
- `name`: 角色名称模糊查询
- `code`: 角色编码精确查询
- `status`: 角色状态（0-禁用，1-启用）

**响应数据：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "items": [
      {
        "id": 1,
        "name": "系统管理员",
        "code": "ROLE_ADMIN",
        "description": "系统最高权限角色",
        "status": 1,
        "isSystem": true,
        "createTime": "2023-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  },
  "timestamp": 1650000000000
}
```

### 创建角色
```http
POST /api/v1/roles
```

**请求参数：**
```json
{
  "name": "普通用户",
  "code": "ROLE_USER",
  "description": "普通用户角色",
  "parentId": null,
  "sort": 1,
  "status": 1
}
```

### 更新角色
```http
PUT /api/v1/roles/{id}
```

**路径参数：**
- `id`: 角色ID

**请求参数：**
```json
{
  "name": "普通用户",
  "code": "ROLE_USER",
  "description": "普通用户角色（更新）",
  "parentId": null,
  "sort": 1,
  "status": 1
}
```

### 删除角色
```http
DELETE /api/v1/roles/{id}
```

**路径参数：**
- `id`: 角色ID

## 🔐 权限管理接口

### 权限树查询
```http
GET /api/v1/permissions/tree
```

**响应数据：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": 1,
      "name": "系统管理",
      "code": "system:manage",
      "type": "MENU",
      "path": "/system",
      "icon": "system",
      "sort": 1,
      "children": [
        {
          "id": 2,
          "name": "用户管理",
          "code": "user:manage",
          "type": "MENU",
          "path": "/system/user",
          "icon": "user",
          "sort": 1,
          "children": [
            {
              "id": 3,
              "name": "用户查询",
              "code": "user:read",
              "type": "BUTTON",
              "sort": 1
            }
          ]
        }
      ]
    }
  ],
  "timestamp": 1650000000000
}
```

### 创建权限
```http
POST /api/v1/permissions
```

**请求参数：**
```json
{
  "name": "用户管理",
  "code": "user:manage",
  "type": "MENU",
  "parentId": 1,
  "path": "/system/user",
  "icon": "user",
  "sort": 1,
  "status": 1
}
```

### 为角色分配权限
```http
POST /api/v1/roles/{roleId}/permissions
```

**路径参数：**
- `roleId`: 角色ID

**请求参数：**
```json
{
  "permissionIds": [1, 2, 3, 4]
}
```

## 📊 系统监控接口

### 获取系统状态
```http
GET /api/v1/monitor/system
```

**响应数据：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "cpuUsage": 15.5,
    "memoryUsage": 65.2,
    "diskUsage": 45.8,
    "uptime": "15天2小时30分钟",
    "activeUsers": 25,
    "totalRequests": 12500
  },
  "timestamp": 1650000000000
}
```

### 操作日志查询
```http
GET /api/v1/logs/operation?page=1&size=10&username=admin
```

**查询参数：**
- `page`: 页码（默认1）
- `size`: 每页大小（默认10）
- `username`: 操作用户名
- `operation`: 操作类型
- `startTime`: 开始时间
- `endTime`: 结束时间

**响应数据：**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "items": [
      {
        "id": 1,
        "username": "admin",
        "operation": "用户登录",
        "method": "POST",
        "params": "{username: admin}",
        "ip": "192.168.1.100",
        "time": 500,
        "createTime": "2023-01-01 10:00:00"
      }
    ],
    "total": 1,
    "page": 1,
    "size": 10
  },
  "timestamp": 1650000000000
}
```

## 🔄 分页参数说明

所有列表查询接口都支持分页参数：

| 参数名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| page | Integer | 1 | 页码，从1开始 |
| size | Integer | 10 | 每页记录数 |
| sort | String | null | 排序字段，格式：field,asc/desc |

## 🔍 搜索参数说明

支持多种搜索方式：

### 精确匹配
```
GET /api/v1/users?id=1
```

### 模糊匹配
```
GET /api/v1/users?username=admin
```

### 范围查询
```
GET /api/v1/users?startTime=2023-01-01&endTime=2023-12-31
```

### 多条件查询
```
GET /api/v1/users?username=admin&status=1&page=1&size=10
```

## 📝 注意事项

1. **认证要求**：除登录接口外，所有接口都需要携带有效的 JWT Token
2. **权限控制**：部分接口需要特定的权限才能访问
3. **参数验证**：所有请求参数都会进行验证，不符合要求的参数会返回400错误
4. **数据安全**：敏感数据（如密码）在传输过程中会进行加密处理
5. **性能优化**：大数据量查询建议使用分页，避免一次性返回过多数据

## 🔗 相关文档

- [数据库设计文档](./database-design.md)
- [部署文档](./deployment.md)
- [开发规范文档](./development-guide.md)

---

*最后更新：2024年1月*