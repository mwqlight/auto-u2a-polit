-- Auto U2A Polit 数据库初始化脚本
-- 版本: 1.0.0
-- 创建时间: 2024年1月

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `auto_u2a_polit` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `auto_u2a_polit`;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色ID',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `is_system` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否系统内置：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '权限名称',
  `code` varchar(100) NOT NULL COMMENT '权限编码',
  `type` varchar(20) NOT NULL COMMENT '权限类型：MENU-菜单，BUTTON-按钮，API-接口',
  `path` varchar(200) DEFAULT NULL COMMENT '路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限ID',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ----------------------------
-- Table structure for user_permissions
-- ----------------------------
DROP TABLE IF EXISTS `user_permissions`;
CREATE TABLE `user_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_permission` (`user_id`,`permission_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户权限关联表';

-- ----------------------------
-- Table structure for operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `operation_logs`;
CREATE TABLE `operation_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) NOT NULL COMMENT '操作用户名',
  `operation` varchar(200) NOT NULL COMMENT '操作描述',
  `method` varchar(10) NOT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) NOT NULL COMMENT 'IP地址',
  `time` int(11) NOT NULL DEFAULT '0' COMMENT '执行时间（毫秒）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_username` (`username`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_operation` (`operation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ----------------------------
-- Foreign Key structure
-- ----------------------------
ALTER TABLE `user_roles` 
ADD CONSTRAINT `fk_user_roles_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_user_roles_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE;

ALTER TABLE `role_permissions` 
ADD CONSTRAINT `fk_role_permissions_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_role_permissions_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE;

ALTER TABLE `user_permissions` 
ADD CONSTRAINT `fk_user_permissions_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
ADD CONSTRAINT `fk_user_permissions_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE;

ALTER TABLE `operation_logs` 
ADD CONSTRAINT `fk_operation_logs_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;

ALTER TABLE `roles` 
ADD CONSTRAINT `fk_roles_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `roles` (`id`) ON DELETE SET NULL;

ALTER TABLE `permissions` 
ADD CONSTRAINT `fk_permissions_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `permissions` (`id`) ON DELETE SET NULL;

-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 插入系统内置角色
INSERT INTO `roles` (`id`, `name`, `code`, `description`, `parent_id`, `sort`, `status`, `is_system`, `create_time`) VALUES
(1, '系统管理员', 'ROLE_ADMIN', '系统最高权限角色，拥有所有权限', NULL, 1, 1, 1, NOW()),
(2, '普通用户', 'ROLE_USER', '普通用户角色，拥有基本权限', NULL, 2, 1, 1, NOW()),
(3, '访客', 'ROLE_GUEST', '访客角色，拥有只读权限', NULL, 3, 1, 1, NOW());

-- 插入系统权限数据
INSERT INTO `permissions` (`id`, `name`, `code`, `type`, `path`, `icon`, `component`, `parent_id`, `sort`, `status`, `create_time`) VALUES
-- 系统管理菜单
(1, '系统管理', 'system:manage', 'MENU', '/system', 'system', 'Layout', NULL, 1, 1, NOW()),
(2, '用户管理', 'user:manage', 'MENU', '/system/user', 'user', 'system/user/index', 1, 1, 1, NOW()),
(3, '角色管理', 'role:manage', 'MENU', '/system/role', 'role', 'system/role/index', 1, 2, 1, NOW()),
(4, '权限管理', 'permission:manage', 'MENU', '/system/permission', 'permission', 'system/permission/index', 1, 3, 1, NOW()),

-- 用户管理权限
(5, '用户查询', 'user:read', 'BUTTON', NULL, NULL, NULL, 2, 1, 1, NOW()),
(6, '用户新增', 'user:create', 'BUTTON', NULL, NULL, NULL, 2, 2, 1, NOW()),
(7, '用户编辑', 'user:update', 'BUTTON', NULL, NULL, NULL, 2, 3, 1, NOW()),
(8, '用户删除', 'user:delete', 'BUTTON', NULL, NULL, NULL, 2, 4, 1, NOW()),

-- 角色管理权限
(9, '角色查询', 'role:read', 'BUTTON', NULL, NULL, NULL, 3, 1, 1, NOW()),
(10, '角色新增', 'role:create', 'BUTTON', NULL, NULL, NULL, 3, 2, 1, NOW()),
(11, '角色编辑', 'role:update', 'BUTTON', NULL, NULL, NULL, 3, 3, 1, NOW()),
(12, '角色删除', 'role:delete', 'BUTTON', NULL, NULL, NULL, 3, 4, 1, NOW()),

-- 权限管理权限
(13, '权限查询', 'permission:read', 'BUTTON', NULL, NULL, NULL, 4, 1, 1, NOW()),
(14, '权限新增', 'permission:create', 'BUTTON', NULL, NULL, NULL, 4, 2, 1, NOW()),
(15, '权限编辑', 'permission:update', 'BUTTON', NULL, NULL, NULL, 4, 3, 1, NOW()),
(16, '权限删除', 'permission:delete', 'BUTTON', NULL, NULL, NULL, 4, 4, 1, NOW()),

-- 认证中心权限
(17, '认证中心', 'auth:manage', 'MENU', '/auth', 'auth', 'Layout', NULL, 2, 1, NOW()),
(18, '登录管理', 'auth:login', 'MENU', '/auth/login', 'login', 'auth/login/index', 17, 1, 1, NOW()),
(19, '注册管理', 'auth:register', 'MENU', '/auth/register', 'register', 'auth/register/index', 17, 2, 1, NOW()),

-- 授权中心权限
(20, '授权中心', 'authorization:manage', 'MENU', '/authorization', 'authorization', 'Layout', NULL, 3, 1, NOW()),
(21, '角色管理', 'authorization:role', 'MENU', '/authorization/role', 'role', 'authorization/role/index', 20, 1, 1, NOW()),
(22, '权限管理', 'authorization:permission', 'MENU', '/authorization/permission', 'permission', 'authorization/permission/index', 20, 2, 1, NOW()),
(23, '授权策略', 'authorization:policy', 'MENU', '/authorization/policy', 'policy', 'authorization/policy/index', 20, 3, 1, NOW()),

-- 权限管理模块权限
(24, '权限管理模块', 'permission:module', 'MENU', '/permission', 'permission', 'Layout', NULL, 4, 1, NOW()),
(25, '权限列表', 'permission:list', 'MENU', '/permission/permissions', 'list', 'permission/PermissionList', 24, 1, 1, NOW()),
(26, '角色列表', 'permission:role', 'MENU', '/permission/roles', 'role', 'permission/RoleList', 24, 2, 1, NOW());

-- 插入默认管理员用户（密码：admin123，使用BCrypt加密）
INSERT INTO `users` (`id`, `username`, `password`, `email`, `phone`, `status`, `create_time`) VALUES
(1, 'admin', '$2a$10$r3d6Y5t7u8i9o0p1a2s3d4f5g6h7j8k9l0m1n2o3p4q5r6s7t8u9v0w1x2y3z', 'admin@example.com', '13800138000', 1, NOW());

-- 分配管理员角色
INSERT INTO `user_roles` (`user_id`, `role_id`, `create_time`) VALUES (1, 1, NOW());

-- 为管理员角色分配所有权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`, `create_time`)
SELECT 1, id, NOW() FROM `permissions`;

-- 为普通用户角色分配基本权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`, `create_time`) VALUES
(2, 17, NOW()),
(2, 18, NOW()),
(2, 19, NOW());

-- 为访客角色分配只读权限
INSERT INTO `role_permissions` (`role_id`, `permission_id`, `create_time`) VALUES
(3, 17, NOW()),
(3, 18, NOW());

-- 插入示例操作日志
INSERT INTO `operation_logs` (`user_id`, `username`, `operation`, `method`, `params`, `ip`, `time`, `create_time`) VALUES
(1, 'admin', '用户登录', 'POST', '{"username":"admin"}', '127.0.0.1', 150, NOW()),
(1, 'admin', '查看用户列表', 'GET', '{"page":1,"size":10}', '127.0.0.1', 80, NOW()),
(1, 'admin', '创建新角色', 'POST', '{"name":"测试角色","code":"TEST_ROLE"}', '127.0.0.1', 200, NOW());

-- 重置自增ID
ALTER TABLE `users` AUTO_INCREMENT = 1000;
ALTER TABLE `roles` AUTO_INCREMENT = 1000;
ALTER TABLE `permissions` AUTO_INCREMENT = 1000;
ALTER TABLE `user_roles` AUTO_INCREMENT = 1000;
ALTER TABLE `role_permissions` AUTO_INCREMENT = 1000;
ALTER TABLE `user_permissions` AUTO_INCREMENT = 1000;
ALTER TABLE `operation_logs` AUTO_INCREMENT = 1000;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 数据库初始化完成
-- ----------------------------

SELECT '数据库初始化完成！' AS '状态';
SELECT 
    (SELECT COUNT(*) FROM users) AS '用户数量',
    (SELECT COUNT(*) FROM roles) AS '角色数量',
    (SELECT COUNT(*) FROM permissions) AS '权限数量',
    (SELECT COUNT(*) FROM user_roles) AS '用户角色关联数量',
    (SELECT COUNT(*) FROM role_permissions) AS '角色权限关联数量',
    (SELECT COUNT(*) FROM operation_logs) AS '操作日志数量';

-- 显示管理员账户信息
SELECT 
    u.username AS '用户名',
    u.email AS '邮箱',
    u.status AS '状态',
    r.name AS '角色',
    COUNT(rp.permission_id) AS '权限数量'
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
JOIN role_permissions rp ON r.id = rp.role_id
WHERE u.username = 'admin'
GROUP BY u.id, r.id;