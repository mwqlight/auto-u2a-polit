# 部署文档

## 📋 文档说明

本文档详细描述了 Auto U2A Polit 系统的部署流程，包括开发环境、测试环境和生产环境的部署配置。

## 🛠️ 环境要求

### 基础环境
| 组件 | 版本要求 | 说明 |
|------|----------|------|
| Java | 17+ | 后端运行环境 |
| Node.js | 16+ | 前端运行环境 |
| MySQL | 8.0+ | 数据库 |
| Redis | 6.0+ | 缓存服务 |
| Maven | 3.6+ | 后端构建工具 |
| npm | 8.0+ | 前端包管理 |

### 硬件要求
| 环境 | CPU | 内存 | 磁盘 | 网络 |
|------|-----|------|------|------|
| 开发环境 | 2核 | 4GB | 20GB | 10Mbps |
| 测试环境 | 4核 | 8GB | 50GB | 50Mbps |
| 生产环境 | 8核 | 16GB | 100GB | 100Mbps |

## 🚀 快速部署

### 1. 环境准备

#### 安装 Java 17
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# CentOS/RHEL
sudo yum install java-17-openjdk-devel

# macOS
brew install openjdk@17
```

#### 安装 Node.js 16+
```bash
# 使用 nvm 安装
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
nvm install 16
nvm use 16
```

#### 安装 MySQL 8.0
```bash
# Ubuntu/Debian
sudo apt install mysql-server

# CentOS/RHEL
sudo yum install mysql-server

# 启动服务
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### 安装 Redis 6.0+
```bash
# Ubuntu/Debian
sudo apt install redis-server

# CentOS/RHEL
sudo yum install redis

# 启动服务
sudo systemctl start redis
sudo systemctl enable redis
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE auto_u2a_polit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'auto_u2a_polit'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON auto_u2a_polit.* TO 'auto_u2a_polit'@'%';
FLUSH PRIVILEGES;
```

#### 数据库初始化
```bash
# 导入初始化脚本
mysql -u root -p auto_u2a_polit < docs/sql/init.sql
```

### 3. 后端部署

#### 配置环境变量
创建 `auto-u2a-polit-backend/.env` 文件：
```properties
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=auto_u2a_polit
DB_USERNAME=auto_u2a_polit
DB_PASSWORD=your_password

# Redis配置
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT配置
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=86400

# 服务器配置
SERVER_PORT=8080
SERVER_CONTEXT_PATH=/api
```

#### 构建和运行
```bash
cd auto-u2a-polit-backend

# 清理并构建
mvn clean package -DskipTests

# 运行应用
java -jar target/auto-u2a-polit-backend-1.0.0.jar

# 或者使用 Maven 直接运行
mvn spring-boot:run
```

### 4. 前端部署

#### 配置环境变量
创建 `auto-u2a-polit-frontend/.env` 文件：
```properties
# API 配置
VITE_API_BASE_URL=http://localhost:8080/api
VITE_API_TIMEOUT=30000

# 应用配置
VITE_APP_TITLE=Auto U2A Polit
VITE_APP_VERSION=1.0.0

# 开发服务器配置
VITE_DEV_SERVER_HOST=localhost
VITE_DEV_SERVER_PORT=3000
```

#### 安装依赖和构建
```bash
cd auto-u2a-polit-frontend

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境构建
npm run build

# 预览生产构建
npm run preview
```

## 📦 Docker 部署

### 1. 编写 Dockerfile

#### 后端 Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建好的 jar 文件
COPY target/auto-u2a-polit-backend-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 前端 Dockerfile
```dockerfile
FROM node:16-alpine as builder

# 设置工作目录
WORKDIR /app

# 复制 package.json
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制源代码
COPY . .

# 构建应用
RUN npm run build

# 生产阶段
FROM nginx:alpine

# 复制构建好的文件到 nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 nginx 配置
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
```

### 2. 编写 docker-compose.yml
```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: auto-u2a-polit-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: auto_u2a_polit
      MYSQL_USER: auto_u2a_polit
      MYSQL_PASSWORD: user_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./docs/sql/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - auto-u2a-polit-network

  redis:
    image: redis:6.2-alpine
    container_name: auto-u2a-polit-redis
    ports:
      - "6379:6379"
    networks:
      - auto-u2a-polit-network

  backend:
    build:
      context: ./auto-u2a-polit-backend
      dockerfile: Dockerfile
    container_name: auto-u2a-polit-backend
    environment:
      DB_HOST: mysql
      DB_PORT: 3306
      DB_NAME: auto_u2a_polit
      DB_USERNAME: auto_u2a_polit
      DB_PASSWORD: user_password
      REDIS_HOST: redis
      REDIS_PORT: 6379
      JWT_SECRET: your_jwt_secret_key_here
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    networks:
      - auto-u2a-polit-network

  frontend:
    build:
      context: ./auto-u2a-polit-frontend
      dockerfile: Dockerfile
    container_name: auto-u2a-polit-frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
    networks:
      - auto-u2a-polit-network

volumes:
  mysql_data:

networks:
  auto-u2a-polit-network:
    driver: bridge
```

### 3. 部署命令
```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## ☁️ Kubernetes 部署

### 1. 创建命名空间
```yaml
# k8s/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: auto-u2a-polit
```

### 2. 创建 ConfigMap
```yaml
# k8s/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: auto-u2a-polit-config
  namespace: auto-u2a-polit
data:
  application.properties: |
    # 数据库配置
    spring.datasource.url=jdbc:mysql://mysql:3306/auto_u2a_polit
    spring.datasource.username=auto_u2a_polit
    spring.datasource.password=user_password
    
    # Redis配置
    spring.redis.host=redis
    spring.redis.port=6379
    
    # JWT配置
    jwt.secret=your_jwt_secret_key_here
    jwt.expiration=86400
```

### 3. 创建 Deployment
```yaml
# k8s/backend-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auto-u2a-polit-backend
  namespace: auto-u2a-polit
spec:
  replicas: 2
  selector:
    matchLabels:
      app: auto-u2a-polit-backend
  template:
    metadata:
      labels:
        app: auto-u2a-polit-backend
    spec:
      containers:
      - name: backend
        image: auto-u2a-polit-backend:1.0.0
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: auto-u2a-polit-config
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

### 4. 创建 Service
```yaml
# k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: auto-u2a-polit-backend
  namespace: auto-u2a-polit
spec:
  selector:
    app: auto-u2a-polit-backend
  ports:
  - port: 8080
    targetPort: 8080
  type: ClusterIP
```

### 5. 创建 Ingress
```yaml
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: auto-u2a-polit-ingress
  namespace: auto-u2a-polit
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: auto-u2a-polit.example.com
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: auto-u2a-polit-backend
            port:
              number: 8080
      - path: /
        pathType: Prefix
        backend:
          service:
            name: auto-u2a-polit-frontend
            port:
              number: 80
```

### 6. 部署命令
```bash
# 应用所有配置
kubectl apply -f k8s/

# 查看部署状态
kubectl get all -n auto-u2a-polit

# 查看 Pod 日志
kubectl logs -f deployment/auto-u2a-polit-backend -n auto-u2a-polit
```

## 🔧 生产环境配置

### 1. 安全配置

#### SSL/TLS 配置
```nginx
# nginx SSL 配置
server {
    listen 443 ssl http2;
    server_name auto-u2a-polit.example.com;
    
    ssl_certificate /etc/ssl/certs/auto-u2a-polit.crt;
    ssl_certificate_key /etc/ssl/private/auto-u2a-polit.key;
    
    # 安全头
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
}
```

#### 防火墙配置
```bash
# 开放必要端口
sudo ufw allow 22    # SSH
sudo ufw allow 80    # HTTP
sudo ufw allow 443   # HTTPS
sudo ufw allow 3306  # MySQL
sudo ufw allow 6379  # Redis
sudo ufw enable
```

### 2. 监控配置

#### 应用监控
```yaml
# Prometheus 配置
scrape_configs:
  - job_name: 'auto-u2a-polit-backend'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
```

#### 日志收集
```yaml
# Filebeat 配置
filebeat.inputs:
- type: log
  paths:
    - /var/log/auto-u2a-polit/*.log
  fields:
    service: auto-u2a-polit
```

### 3. 备份策略

#### 数据库备份
```bash
#!/bin/bash
# 数据库备份脚本
BACKUP_DIR="/backup/mysql"
DATE=$(date +%Y%m%d_%H%M%S)

mysqldump -u root -p$DB_PASSWORD auto_u2a_polit > $BACKUP_DIR/auto_u2a_polit_$DATE.sql

# 保留最近7天的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
```

#### 应用备份
```bash
#!/bin/bash
# 应用备份脚本
BACKUP_DIR="/backup/app"
DATE=$(date +%Y%m%d_%H%M%S)

# 备份配置文件
tar -czf $BACKUP_DIR/config_$DATE.tar.gz /etc/auto-u2a-polit/

# 备份日志文件
tar -czf $BACKUP_DIR/logs_$DATE.tar.gz /var/log/auto-u2a-polit/
```

## 🚨 故障排除

### 常见问题

#### 1. 数据库连接失败
```bash
# 检查数据库服务状态
sudo systemctl status mysql

# 检查连接
mysql -u auto_u2a_polit -p -h localhost -P 3306
```

#### 2. Redis 连接失败
```bash
# 检查 Redis 服务状态
sudo systemctl status redis

# 测试连接
redis-cli ping
```

#### 3. 应用启动失败
```bash
# 检查端口占用
netstat -tulpn | grep 8080

# 查看应用日志
tail -f /var/log/auto-u2a-polit/application.log
```

#### 4. 前端资源加载失败
```bash
# 检查 Nginx 配置
nginx -t

# 重启 Nginx
sudo systemctl restart nginx
```

### 性能优化

#### 数据库优化
```sql
-- 创建索引
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_role_code ON roles(code);
CREATE INDEX idx_permission_parent_id ON permissions(parent_id);

-- 优化查询
EXPLAIN SELECT * FROM users WHERE username = 'admin';
```

#### JVM 优化
```bash
# JVM 参数优化
java -jar app.jar \
  -Xms512m -Xmx1024m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:ParallelGCThreads=4 \
  -XX:ConcGCThreads=2
```

## 📊 监控指标

### 关键指标
- 应用响应时间：P95 < 300ms
- 错误率：< 1%
- CPU 使用率：< 80%
- 内存使用率：< 85%
- 数据库连接数：< 最大连接数的80%

### 告警规则
```yaml
# Prometheus 告警规则
groups:
- name: auto-u2a-polit
  rules:
  - alert: HighErrorRate
    expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.05
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "高错误率检测"
      description: "5分钟内错误率超过5%"
```

---

*最后更新：2024年1月*

**注意**：生产环境部署前请务必进行安全审计和性能测试。