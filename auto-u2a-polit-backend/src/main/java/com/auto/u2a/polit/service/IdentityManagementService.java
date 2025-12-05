package com.auto.u2a.polit.service;

import com.auto.u2a.polit.dto.request.UserCreateRequest;
import com.auto.u2a.polit.dto.request.UserUpdateRequest;
import com.auto.u2a.polit.dto.request.OrganizationCreateRequest;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.dto.response.OrganizationResponse;
import java.util.UUID;
import com.auto.u2a.polit.entity.OrganizationUnit;
import com.auto.u2a.polit.entity.Tenant;
import com.auto.u2a.polit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 统一身份管理引擎服务接口
 * 提供多模型用户架构、组织架构管理、数据同步等核心功能
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
public interface IdentityManagementService {
    
    // ==================== 用户管理 ====================
    
    /**
     * 创建用户（支持动态字段配置）
     * 
     * @param request 用户创建请求
     * @param tenantId 租户ID
     * @return 用户响应
     */
    UserResponse createUser(UserCreateRequest request, String tenantId);
    
    /**
     * 更新用户信息（支持部分更新）
     * 
     * @param userId 用户ID
     * @param request 用户更新请求
     * @param tenantId 租户ID
     * @return 用户响应
     */
    UserResponse updateUser(String userId, UserUpdateRequest request, String tenantId);
    
    /**
     * 根据ID查询用户
     * 
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户响应
     */
    UserResponse getUserById(String userId, String tenantId);
    
    /**
     * 分页查询用户列表
     * 
     * @param tenantId 租户ID
     * @param pageable 分页参数
     * @param filters 过滤条件
     * @return 用户分页列表
     */
    Page<UserResponse> getUsers(String tenantId, Pageable pageable, Map<String, Object> filters);
    
    /**
     * 批量导入用户
     * 
     * @param users 用户列表
     * @param tenantId 租户ID
     * @param conflictStrategy 冲突解决策略
     * @return 导入结果
     */
    Map<String, Object> batchImportUsers(List<User> users, String tenantId, String conflictStrategy);
    
    /**
     * 导出用户数据
     * 
     * @param tenantId 租户ID
     * @param filters 过滤条件
     * @param format 导出格式（CSV/JSON/XML）
     * @return 导出数据
     */
    String exportUsers(String tenantId, Map<String, Object> filters, String format);
    
    // ==================== 组织架构管理 ====================
    
    /**
     * 创建组织
     * 
     * @param request 组织创建请求
     * @return 创建的组织
     */
    OrganizationResponse createOrganization(OrganizationCreateRequest request);
    
    /**
     * 更新组织
     * 
     * @param orgId 组织ID
     * @param request 组织更新请求
     * @return 更新后的组织
     */
    OrganizationResponse updateOrganization(UUID orgId, OrganizationCreateRequest request);
    
    /**
     * 删除组织
     * 
     * @param orgId 组织ID
     * @param tenantId 租户ID
     */
    void deleteOrganization(UUID orgId, String tenantId);
    
    /**
     * 获取组织详情
     * 
     * @param orgId 组织ID
     * @param tenantId 租户ID
     * @return 组织响应
     */
    OrganizationResponse getOrganization(UUID orgId, String tenantId);
    
    /**
     * 移动组织
     * 
     * @param orgId 组织ID
     * @param tenantId 租户ID
     * @param newParentId 新的父组织ID
     * @return 组织响应
     */
    OrganizationResponse moveOrganization(UUID orgId, String tenantId, String newParentId);
    
    /**
     * 创建组织单元
     * 
     * @param orgUnit 组织单元
     * @param tenantId 租户ID
     * @return 创建的组织单元
     */
    OrganizationUnit createOrganizationUnit(OrganizationUnit orgUnit, String tenantId);
    
    /**
     * 更新组织单元
     * 
     * @param orgUnitId 组织单元ID
     * @param orgUnit 组织单元数据
     * @param tenantId 租户ID
     * @return 更新的组织单元
     */
    OrganizationUnit updateOrganizationUnit(String orgUnitId, OrganizationUnit orgUnit, String tenantId);
    
    /**
     * 获取组织架构树
     * 
     * @param tenantId 租户ID
     * @param rootId 根节点ID（可选）
     * @return 组织架构树
     */
    List<OrganizationResponse> getOrganizationTree(String tenantId, String rootId);
    
    /**
     * 移动组织单元
     * 
     * @param orgUnitId 组织单元ID
     * @param newParentId 新的父节点ID
     * @param tenantId 租户ID
     */
    void moveOrganizationUnit(String orgUnitId, String newParentId, String tenantId);
    
    /**
     * 获取组织单元下的用户
     * 
     * @param orgUnitId 组织单元ID
     * @param tenantId 租户ID
     * @param includeChildren 是否包含子组织单元
     * @return 用户列表
     */
    List<User> getUsersByOrganizationUnit(String orgUnitId, String tenantId, boolean includeChildren);
    
    // ==================== 数据同步 ====================
    
    /**
     * 创建数据同步任务
     * 
     * @param sourceType 数据源类型
     * @param targetType 目标类型
     * @param config 同步配置
     * @param tenantId 租户ID
     * @return 同步任务ID
     */
    String createSyncTask(String sourceType, String targetType, Map<String, Object> config, String tenantId);
    
    /**
     * 执行数据同步
     * 
     * @param taskId 同步任务ID
     * @param tenantId 租户ID
     * @return 同步结果
     */
    Map<String, Object> executeSync(String taskId, String tenantId);
    
    /**
     * 获取同步任务状态
     * 
     * @param taskId 同步任务ID
     * @param tenantId 租户ID
     * @return 任务状态
     */
    Map<String, Object> getSyncTaskStatus(String taskId, String tenantId);
    
    /**
     * 配置数据源连接器
     * 
     * @param connectorType 连接器类型
     * @param config 连接配置
     * @param tenantId 租户ID
     * @return 连接器ID
     */
    String configureDataSourceConnector(String connectorType, Map<String, Object> config, String tenantId);
    
    // ==================== 租户管理 ====================
    
    /**
     * 创建租户
     * 
     * @param tenant 租户信息
     * @return 创建的租户
     */
    Tenant createTenant(Tenant tenant);
    
    /**
     * 更新租户信息
     * 
     * @param tenantId 租户ID
     * @param tenant 租户数据
     * @return 更新的租户
     */
    Tenant updateTenant(String tenantId, Tenant tenant);
    
    /**
     * 获取租户信息
     * 
     * @param tenantId 租户ID
     * @return 租户信息
     */
    Tenant getTenant(String tenantId);
    
    /**
     * 获取所有租户
     * 
     * @return 租户列表
     */
    List<Tenant> getAllTenants();
    
    /**
     * 初始化租户数据
     * 
     * @param tenantId 租户ID
     * @param template 初始化模板
     */
    void initializeTenant(String tenantId, String template);
}