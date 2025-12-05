# Auto U2A Polit - 智能权限管理系统

> 基于SpringBoot + Vue3的前后端分离权限管理系统，融合5A6S开发规范

## 🚀 项目简介

Auto U2A Polit 是一个现代化的权限管理系统，采用前后端分离架构，提供完整的权限管理解决方案。系统支持RBAC权限模型、多协议认证、细粒度权限控制等核心功能。

### 核心特性

- **🔐 全协议认证中心**：支持OAuth2、JWT、LDAP等多种认证协议
- **⚡ 灵活授权框架**：基于RBAC的细粒度权限控制
- **🛡️ 安全可靠**：遵循Spring Security最佳实践
- **📱 现代化界面**：基于Vue3 + TypeScript的高科技UI
- **📊 实时监控**：完整的操作日志和权限审计

## 🏗️ 技术架构

### 后端技术栈
- **框架**：Spring Boot 3.0+
- **安全**：Spring Security + JWT
- **数据库**：MySQL + MyBatis Plus
- **缓存**：Redis
- **文档**：SpringDoc OpenAPI 3.0
- **构建工具**：Maven

### 前端技术栈
- **框架**：Vue 3 + TypeScript
- **路由**：Vue Router 4
- **状态管理**：Pinia
- **UI组件**：Element Plus
- **构建工具**：Vite
- **包管理**：npm

## 📁 项目结构

```
auto-u2a-polit/
├── auto-u2a-polit-backend/     # 后端SpringBoot项目
│   ├── src/main/java/com/auto/u2a/polit/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器层
│   │   ├── dto/              # 数据传输对象
│   │   ├── entity/           # 实体类
│   │   ├── repository/       # 数据访问层
│   │   ├── service/          # 业务逻辑层
│   │   ├── security/         # 安全配置
│   │   └── Application.java  # 启动类
├── auto-u2a-polit-frontend/   # 前端Vue3项目
│   ├── src/
│   │   ├── api/              # API接口
│   │   ├── components/       # 公共组件
│   │   ├── composables/      # 组合式API
│   │   ├── router/           # 路由配置
│   │   ├── stores/           # 状态管理
│   │   ├── types/            # 类型定义
│   │   ├── utils/            # 工具函数
│   │   └── views/            # 页面视图
└── README.md                 # 项目说明
```

## 🚦 快速开始

### 环境要求

- **Java**: 17+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Redis**: 6.0+

### 后端启动

1. 配置数据库和Redis连接信息
2. 启动后端服务：
```bash
cd auto-u2a-polit-backend
mvn clean install
mvn spring-boot:run
```

### 前端启动

1. 安装依赖：
```bash
cd auto-u2a-polit-frontend
npm install
```

2. 启动开发服务器：
```bash
npm run dev
```

3. 访问地址：http://localhost:3000

## 📖 功能模块

### 🔐 认证中心
- 用户登录/注册
- 多协议认证支持
- 会话管理
- 安全退出

### 🛡️ 权限管理
- 用户管理
- 角色管理
- 权限管理
- 权限分配
- 权限验证

### 📊 系统监控
- 操作日志
- 权限审计
- 系统状态
- 性能监控

## 🔧 API文档

后端API文档通过Swagger UI提供，启动后端服务后访问：
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI文档: http://localhost:8080/v3/api-docs

## 🧪 测试

### 后端测试
```bash
cd auto-u2a-polit-backend
mvn test
```

### 前端测试
```bash
cd auto-u2a-polit-frontend
npm run test
```

## 📦 部署

### Docker部署

```bash
# 构建镜像
docker build -t auto-u2a-polit-backend:latest ./auto-u2a-polit-backend
docker build -t auto-u2a-polit-frontend:latest ./auto-u2a-polit-frontend

# 运行容器
docker-compose up -d
```

### 生产环境配置

参考 [部署文档](./docs/deployment.md) 获取详细的生产环境部署说明。

## 📈 开发规范

本项目严格遵循 **5A6S开发规范**：

### 5A原则
- **A1. Architecture**：清晰架构设计
- **A2. API**：统一接口规范
- **A3. Automation**：自动化工程
- **A4. Assurance**：质量保障
- **A5. Agility**：敏捷协作

### 6S标准
- **S1. Structure**：目录结构标准
- **S2. Standards**：编码标准
- **S3. Security**：安全标准
- **S4. Stability**：稳定性标准
- **S5. Scalability**：可扩展标准
- **S6. Sustainability**：可持续维护

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支：`git checkout -b feature/AmazingFeature`
3. 提交更改：`git commit -m 'Add some AmazingFeature'`
4. 推送分支：`git push origin feature/AmazingFeature`
5. 提交Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 📞 联系方式

- 项目主页：https://github.com/auto-u2a-polit
- 问题反馈：https://github.com/auto-u2a-polit/issues
- 邮箱：dev@auto-u2a-polit.com

## 🙏 致谢

感谢以下开源项目的支持：
- Spring Boot
- Vue.js
- Element Plus
- MyBatis Plus
- Redis

---

⭐ 如果这个项目对你有帮助，请给个Star支持一下！