package com.auto.u2a.polit.service.impl;

import com.auto.u2a.polit.dto.request.UserCreateRequest;
import com.auto.u2a.polit.dto.request.UserUpdateRequest;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.entity.OrganizationUnit;
import com.auto.u2a.polit.entity.Tenant;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.repository.TenantRepository;
import com.auto.u2a.polit.repository.UserRepository;
import com.auto.u2a.polit.repository.OrganizationUnitRepository;
import com.auto.u2a.polit.service.IdentityManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统一身份管理引擎服务实现类
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class IdentityManagementServiceImpl implements IdentityManagementService {
    
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final OrganizationUnitRepository organizationUnitRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserResponse createUser(UserCreateRequest request, String tenantId) {
        log.info("创建用户，租户ID: {}, 用户名: {}", tenantId, request.getUsername());
        
        // 验证租户是否存在
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("租户不存在: " + tenantId));
        
        // 检查用户名是否唯一
        if (userRepository.existsByTenantIdAndUsername(tenantId, request.getUsername())) {
            throw new RuntimeException("用户名已存在: " + request.getUsername());
        }
        
        // 检查邮箱是否唯一
        if (request.getEmail() != null && 
            userRepository.existsByTenantIdAndEmail(tenantId, request.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + request.getEmail());
        }
        
        // 检查手机号是否唯一
        if (request.getPhone() != null && 
            userRepository.existsByTenantIdAndPhone(tenantId, request.getPhone())) {
            throw new RuntimeException("手机号已存在: " + request.getPhone());
        }
        
        // 创建用户实体
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setTenantId(tenantId);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDisplayName(request.getDisplayName());
        user.setAvatarUrl(request.getAvatarUrl());
        
        // 密码加密（强度≥12）
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPasswordChangedAt(LocalDateTime.now());
        
        // 设置用户档案信息
        if (request.getProfile() != null) {
            user.setProfile(request.getProfile());
        }
        
        // 保存用户
        User savedUser = userRepository.save(user);
        log.info("用户创建成功，用户ID: {}", savedUser.getId());
        
        return convertToResponse(savedUser);
    }
    
    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request, String tenantId) {
        log.info("更新用户信息，用户ID: {}, 租户ID: {}", userId, tenantId);
        
        // 查询用户
        User user = userRepository.findByIdAndTenantId(UUID.fromString(userId), tenantId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));
        
        // 更新基本信息
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // 检查邮箱是否唯一
            if (userRepository.existsByTenantIdAndEmail(tenantId, request.getEmail())) {
                throw new RuntimeException("邮箱已存在: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            // 检查手机号是否唯一
            if (userRepository.existsByTenantIdAndPhone(tenantId, request.getPhone())) {
                throw new RuntimeException("手机号已存在: " + request.getPhone());
            }
            user.setPhone(request.getPhone());
        }
        
        // 更新用户档案
        if (request.getProfile() != null) {
            user.setProfile(request.getProfile());
        }
        
        // 更新状态
        if (request.getStatus() != null) {
            user.setStatus(User.UserStatus.valueOf(request.getStatus()));
        }
        
        // 保存更新
        User updatedUser = userRepository.save(user);
        log.info("用户信息更新成功，用户ID: {}", updatedUser.getId());
        
        return convertToResponse(updatedUser);
    }
    
    @Override
    public UserResponse getUserById(String userId, String tenantId) {
        log.debug("查询用户信息，用户ID: {}, 租户ID: {}", userId, tenantId);
        
        User user = userRepository.findByIdAndTenantId(UUID.fromString(userId), tenantId)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + userId));
        
        return convertToResponse(user);
    }
    
    @Override
    public Page<UserResponse> getUsers(String tenantId, Pageable pageable, Map<String, Object> filters) {
        log.debug("分页查询用户列表，租户ID: {}, 过滤条件: {}", tenantId, filters);
        
        // 构建查询条件
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 租户过滤
            predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            
            // 动态过滤条件
            if (filters != null) {
                if (filters.containsKey("username") && filters.get("username") != null) {
                    predicates.add(criteriaBuilder.like(root.get("username"), 
                            "%" + filters.get("username") + "%"));
                }
                if (filters.containsKey("email") && filters.get("email") != null) {
                    predicates.add(criteriaBuilder.like(root.get("email"), 
                            "%" + filters.get("email") + "%"));
                }
                if (filters.containsKey("phone") && filters.get("phone") != null) {
                    predicates.add(criteriaBuilder.like(root.get("phone"), 
                            "%" + filters.get("phone") + "%"));
                }
                if (filters.containsKey("status") && filters.get("status") != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), 
                            User.UserStatus.valueOf(filters.get("status").toString())));
                }
                if (filters.containsKey("type") && filters.get("type") != null) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), 
                            User.UserType.valueOf(filters.get("type").toString())));
                }
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<User> userPage = userRepository.findAll(spec, pageable);
        return userPage.map(this::convertToResponse);
    }
    
    @Override
    public Map<String, Object> batchImportUsers(List<User> users, String tenantId, String conflictStrategy) {
        log.info("批量导入用户，租户ID: {}, 用户数量: {}, 冲突策略: {}", 
                tenantId, users.size(), conflictStrategy);
        
        Map<String, Object> result = new HashMap<>();
        List<User> successUsers = new ArrayList<>();
        List<Map<String, Object>> failedUsers = new ArrayList<>();
        
        for (User user : users) {
            try {
                // 设置租户ID
                user.setTenantId(tenantId);
                
                // 检查冲突
                boolean usernameExists = userRepository.existsByTenantIdAndUsername(tenantId, user.getUsername());
                
                if (usernameExists) {
                    if ("skip".equals(conflictStrategy)) {
                        Map<String, Object> failedInfo = new HashMap<>();
                        failedInfo.put("username", user.getUsername());
                        failedInfo.put("reason", "用户名已存在");
                        failedUsers.add(failedInfo);
                        continue;
                    } else if ("overwrite".equals(conflictStrategy)) {
                        // 删除已存在的用户
                        userRepository.deleteByTenantIdAndUsername(tenantId, user.getUsername());
                    }
                }
                
                // 密码加密
                if (user.getPasswordHash() != null) {
                    user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
                }
                
                // 保存用户
                User savedUser = userRepository.save(user);
                successUsers.add(savedUser);
                
            } catch (Exception e) {
                log.error("导入用户失败: {}", user.getUsername(), e);
                Map<String, Object> failedInfo = new HashMap<>();
                failedInfo.put("username", user.getUsername());
                failedInfo.put("reason", e.getMessage());
                failedUsers.add(failedInfo);
            }
        }
        
        result.put("total", users.size());
        result.put("success", successUsers.size());
        result.put("failed", failedUsers.size());
        result.put("successUsers", successUsers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
        result.put("failedUsers", failedUsers);
        
        log.info("批量导入完成，成功: {}, 失败: {}", successUsers.size(), failedUsers.size());
        return result;
    }
    
    @Override
    public String exportUsers(String tenantId, Map<String, Object> filters, String format) {
        log.info("导出用户数据，租户ID: {}, 格式: {}", tenantId, format);
        
        // 查询用户数据
        Pageable pageable = Pageable.unpaged();
        Page<UserResponse> userPage = getUsers(tenantId, pageable, filters);
        List<UserResponse> users = userPage.getContent();
        
        // 根据格式生成导出数据
        switch (format.toLowerCase()) {
            case "csv":
                return generateCsv(users);
            case "json":
                return generateJson(users);
            case "xml":
                return generateXml(users);
            default:
                throw new RuntimeException("不支持的导出格式: " + format);
        }
    }
    
    // ==================== 组织架构管理 ====================
    
    @Override
    public OrganizationUnit createOrganizationUnit(OrganizationUnit orgUnit, String tenantId) {
        log.info("创建组织单元，租户ID: {}, 组织代码: {}", tenantId, orgUnit.getCode());
        
        // 检查组织代码是否唯一
        if (organizationUnitRepository.existsByTenantIdAndCode(tenantId, orgUnit.getCode())) {
            throw new RuntimeException("组织代码已存在: " + orgUnit.getCode());
        }
        
        // 设置租户ID
        orgUnit.setTenantId(tenantId);
        
        // 处理父组织
        if (orgUnit.getParentId() != null) {
            // 检查父组织是否存在
            OrganizationUnit parentOrg = organizationUnitRepository.findById(UUID.fromString(orgUnit.getParentId()))
                    .orElseThrow(() -> new RuntimeException("父组织不存在: " + orgUnit.getParentId()));
            
            // 设置层级路径和深度
            orgUnit.setHierarchyPath(parentOrg.getHierarchyPath() + "/" + orgUnit.getCode());
            orgUnit.setDepth(parentOrg.getDepth() + 1);
        } else {
            // 根组织
            orgUnit.setHierarchyPath(orgUnit.getCode());
            orgUnit.setDepth(0);
        }
        
        OrganizationUnit savedOrgUnit = organizationUnitRepository.save(orgUnit);
        log.info("组织单元创建成功，组织ID: {}", savedOrgUnit.getId());
        
        return savedOrgUnit;
    }
    
    @Override
    public OrganizationUnit updateOrganizationUnit(String orgUnitId, OrganizationUnit orgUnit, String tenantId) {
        log.info("更新组织单元信息，组织ID: {}, 租户ID: {}", orgUnitId, tenantId);
        
        OrganizationUnit existingOrgUnit = organizationUnitRepository.findById(UUID.fromString(orgUnitId))
                .orElseThrow(() -> new RuntimeException("组织单元不存在: " + orgUnitId));
        
        // 检查租户一致性
        if (!existingOrgUnit.getTenantId().equals(tenantId)) {
            throw new RuntimeException("组织单元不属于该租户");
        }
        
        // 更新基本信息
        if (orgUnit.getName() != null) {
            existingOrgUnit.setName(orgUnit.getName());
        }
        if (orgUnit.getDescription() != null) {
            existingOrgUnit.setDescription(orgUnit.getDescription());
        }
        if (orgUnit.getType() != null) {
            existingOrgUnit.setType(orgUnit.getType());
        }
        if (orgUnit.getStatus() != null) {
            existingOrgUnit.setStatus(orgUnit.getStatus());
        }
        if (orgUnit.getAttributes() != null) {
            existingOrgUnit.setAttributes(orgUnit.getAttributes());
        }
        if (orgUnit.getManagerId() != null) {
            existingOrgUnit.setManagerId(orgUnit.getManagerId());
        }
        if (orgUnit.getMetadata() != null) {
            existingOrgUnit.setMetadata(orgUnit.getMetadata());
        }
        
        OrganizationUnit updatedOrgUnit = organizationUnitRepository.save(existingOrgUnit);
        log.info("组织单元信息更新成功，组织ID: {}", updatedOrgUnit.getId());
        
        return updatedOrgUnit;
    }
    
    @Override
    public List<OrganizationUnit> getOrganizationTree(String tenantId, String rootId) {
        log.debug("查询组织架构树，租户ID: {}, 根组织ID: {}", tenantId, rootId);
        
        List<OrganizationUnit> orgUnits;
        if (rootId == null) {
            // 查询所有根组织
            orgUnits = organizationUnitRepository.findByTenantIdAndParentIdIsNull(tenantId);
        } else {
            // 查询指定根组织及其子组织
            OrganizationUnit rootOrg = organizationUnitRepository.findById(UUID.fromString(rootId))
                    .orElseThrow(() -> new RuntimeException("根组织不存在: " + rootId));
            orgUnits = organizationUnitRepository.findByTenantIdAndHierarchyPathStartingWith(tenantId, rootOrg.getHierarchyPath());
        }
        
        // 构建树形结构
        return buildOrganizationTree(orgUnits);
    }
    
    @Override
    public void moveOrganizationUnit(String orgUnitId, String newParentId, String tenantId) {
        log.info("移动组织单元，组织ID: {}, 新父组织ID: {}, 租户ID: {}", orgUnitId, newParentId, tenantId);
        
        OrganizationUnit orgUnit = organizationUnitRepository.findById(UUID.fromString(orgUnitId))
                .orElseThrow(() -> new RuntimeException("组织单元不存在: " + orgUnitId));
        
        // 检查租户一致性
        if (!orgUnit.getTenantId().equals(tenantId)) {
            throw new RuntimeException("组织单元不属于该租户");
        }
        
        // 处理新父组织
        OrganizationUnit newParentOrg = null;
        String newPath = orgUnit.getCode();
        int newDepth = 0;
        
        if (newParentId != null) {
            newParentOrg = organizationUnitRepository.findById(UUID.fromString(newParentId))
                    .orElseThrow(() -> new RuntimeException("新父组织不存在: " + newParentId));
            
            // 检查租户一致性
            if (!newParentOrg.getTenantId().equals(tenantId)) {
                throw new RuntimeException("新父组织不属于该租户");
            }
            
            // 检查是否移动到自己的子组织下
            if (orgUnit.getHierarchyPath().startsWith(newParentOrg.getHierarchyPath() + "/")) {
                throw new RuntimeException("不能将组织单元移动到自己的子组织下");
            }
            
            newPath = newParentOrg.getHierarchyPath() + "/" + orgUnit.getCode();
            newDepth = newParentOrg.getDepth() + 1;
        }
        
        // 更新组织单元的父ID、路径和深度
        String oldPath = orgUnit.getHierarchyPath();
        orgUnit.setParentId(newParentId);
        orgUnit.setHierarchyPath(newPath);
        orgUnit.setDepth(newDepth);
        organizationUnitRepository.save(orgUnit);
        
        // 更新所有子组织的路径
        organizationUnitRepository.updateHierarchyPathByTenantIdAndPathStartingWith(tenantId, oldPath + "/", newPath + "/");
        
        log.info("组织单元移动成功，组织ID: {}", orgUnitId);
    }
    
    @Override
    public List<User> getUsersByOrganizationUnit(String orgUnitId, String tenantId, boolean includeChildren) {
        log.debug("查询组织单元用户，组织ID: {}, 租户ID: {}, 包含子组织: {}", orgUnitId, tenantId, includeChildren);
        
        OrganizationUnit orgUnit = organizationUnitRepository.findById(UUID.fromString(orgUnitId))
                .orElseThrow(() -> new RuntimeException("组织单元不存在: " + orgUnitId));
        
        // 检查租户一致性
        if (!orgUnit.getTenantId().equals(tenantId)) {
            throw new RuntimeException("组织单元不属于该租户");
        }
        
        // TODO: 实现组织单元用户查询逻辑
        // 需要在User实体中添加组织单元关联，或者创建中间表
        throw new UnsupportedOperationException("组织单元用户查询功能待实现");
    }
    
    /**
     * 构建组织架构树
     */
    private List<OrganizationUnit> buildOrganizationTree(List<OrganizationUnit> orgUnits) {
        // TODO: 实现组织架构树构建逻辑
        // 需要根据parentId和hierarchyPath构建树形结构
        return orgUnits;
    }
    
    // ==================== 数据同步 ====================
    
    @Override
    public String createSyncTask(String sourceType, String targetType, Map<String, Object> config, String tenantId) {
        // TODO: 实现数据同步任务创建逻辑
        throw new UnsupportedOperationException("数据同步功能待实现");
    }
    
    @Override
    public Map<String, Object> executeSync(String taskId, String tenantId) {
        // TODO: 实现数据同步执行逻辑
        throw new UnsupportedOperationException("数据同步功能待实现");
    }
    
    @Override
    public Map<String, Object> getSyncTaskStatus(String taskId, String tenantId) {
        // TODO: 实现同步任务状态查询逻辑
        throw new UnsupportedOperationException("数据同步功能待实现");
    }
    
    @Override
    public String configureDataSourceConnector(String connectorType, Map<String, Object> config, String tenantId) {
        // TODO: 实现数据源连接器配置逻辑
        throw new UnsupportedOperationException("数据同步功能待实现");
    }
    
    // ==================== 租户管理 ====================
    
    @Override
    public Tenant createTenant(Tenant tenant) {
        log.info("创建租户，租户代码: {}", tenant.getCode());
        
        // 检查租户代码是否唯一
        if (tenantRepository.existsByCode(tenant.getCode())) {
            throw new RuntimeException("租户代码已存在: " + tenant.getCode());
        }
        
        // 设置默认值
        if (tenant.getStatus() == null) {
            tenant.setStatus(Tenant.TenantStatus.ACTIVE);
        }
        if (tenant.getType() == null) {
            tenant.setType(Tenant.TenantType.ENTERPRISE);
        }
        
        Tenant savedTenant = tenantRepository.save(tenant);
        log.info("租户创建成功，租户ID: {}", savedTenant.getId());
        
        return savedTenant;
    }
    
    @Override
    public Tenant updateTenant(String tenantId, Tenant tenant) {
        log.info("更新租户信息，租户ID: {}", tenantId);
        
        Tenant existingTenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("租户不存在: " + tenantId));
        
        // 更新基本信息
        if (tenant.getName() != null) {
            existingTenant.setName(tenant.getName());
        }
        if (tenant.getDescription() != null) {
            existingTenant.setDescription(tenant.getDescription());
        }
        if (tenant.getStatus() != null) {
            existingTenant.setStatus(tenant.getStatus());
        }
        if (tenant.getConfig() != null) {
            existingTenant.setConfig(tenant.getConfig());
        }
        if (tenant.getMetadata() != null) {
            existingTenant.setMetadata(tenant.getMetadata());
        }
        if (tenant.getExpiresAt() != null) {
            existingTenant.setExpiresAt(tenant.getExpiresAt());
        }
        
        Tenant updatedTenant = tenantRepository.save(existingTenant);
        log.info("租户信息更新成功，租户ID: {}", updatedTenant.getId());
        
        return updatedTenant;
    }
    
    @Override
    public Tenant getTenant(String tenantId) {
        log.debug("查询租户信息，租户ID: {}", tenantId);
        
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("租户不存在: " + tenantId));
    }
    
    @Override
    public List<Tenant> getAllTenants() {
        log.debug("查询所有租户");
        
        return tenantRepository.findAll();
    }
    
    @Override
    public void initializeTenant(String tenantId, String template) {
        log.info("初始化租户数据，租户ID: {}, 模板: {}", tenantId, template);
        
        // TODO: 实现租户数据初始化逻辑
        // 根据模板创建默认的组织架构、角色、权限等
        throw new UnsupportedOperationException("租户初始化功能待实现");
    }
    
    // ==================== 私有方法 ====================
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setTenantId(user.getTenantId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setDisplayName(user.getDisplayName());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setStatus(user.getStatus().name());
        response.setType(user.getType().name());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setLastLoginAt(user.getLastLoginAt());
        return response;
    }
    
    private String generateCsv(List<UserResponse> users) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,用户名,邮箱,手机号,显示名称,状态,类型,创建时间\n");
        
        for (UserResponse user : users) {
            csv.append(user.getId()).append(",")
               .append(user.getUsername()).append(",")
               .append(user.getEmail() != null ? user.getEmail() : "").append(",")
               .append(user.getPhone() != null ? user.getPhone() : "").append(",")
               .append(user.getDisplayName() != null ? user.getDisplayName() : "").append(",")
               .append(user.getStatus()).append(",")
               .append(user.getType()).append(",")
               .append(user.getCreatedAt()).append("\n");
        }
        
        return csv.toString();
    }
    
    private String generateJson(List<UserResponse> users) {
        // 简化实现，实际应该使用Jackson等库
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < users.size(); i++) {
            UserResponse user = users.get(i);
            json.append("  {\n")
               .append("    \"id\": \"").append(user.getId()).append("\",\n")
               .append("    \"username\": \"").append(user.getUsername()).append("\",\n")
               .append("    \"email\": \"").append(user.getEmail() != null ? user.getEmail() : "").append("\",\n")
               .append("    \"phone\": \"").append(user.getPhone() != null ? user.getPhone() : "").append("\",\n")
               .append("    \"displayName\": \"").append(user.getDisplayName() != null ? user.getDisplayName() : "").append("\",\n")
               .append("    \"status\": \"").append(user.getStatus()).append("\",\n")
               .append("    \"type\": \"").append(user.getType()).append("\",\n")
               .append("    \"createdAt\": \"").append(user.getCreatedAt()).append("\"\n")
               .append("  }").append(i < users.size() - 1 ? "," : "").append("\n");
        }
        
        json.append("]");
        return json.toString();
    }
    
    private String generateXml(List<UserResponse> users) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<users>\n");
        
        for (UserResponse user : users) {
            xml.append("  <user>\n")
               .append("    <id>").append(user.getId()).append("</id>\n")
               .append("    <username>").append(user.getUsername()).append("</username>\n")
               .append("    <email>").append(user.getEmail() != null ? user.getEmail() : "").append("</email>\n")
               .append("    <phone>").append(user.getPhone() != null ? user.getPhone() : "").append("</phone>\n")
               .append("    <displayName>").append(user.getDisplayName() != null ? user.getDisplayName() : "").append("</displayName>\n")
               .append("    <status>").append(user.getStatus()).append("</status>\n")
               .append("    <type>").append(user.getType()).append("</type>\n")
               .append("    <createdAt>").append(user.getCreatedAt()).append("</createdAt>\n")
               .append("  </user>\n");
        }
        
        xml.append("</users>");
        return xml.toString();
    }
}