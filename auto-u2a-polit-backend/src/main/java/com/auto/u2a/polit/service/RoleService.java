package com.auto.u2a.polit.service;

import com.auto.u2a.polit.entity.Role;
import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    
    /**
     * 创建角色
     */
    Role createRole(Role role);
    
    /**
     * 更新角色
     */
    Role updateRole(Role role);
    
    /**
     * 删除角色
     */
    void deleteRole(Long id);
    
    /**
     * 根据ID获取角色
     */
    Role getRoleById(Long id);
    
    /**
     * 根据编码获取角色
     */
    Role getRoleByCode(String code);
    
    /**
     * 获取所有角色
     */
    List<Role> getAllRoles();
    
    /**
     * 根据类型获取角色
     */
    List<Role> getRolesByType(String type);
    
    /**
     * 根据用户ID获取角色
     */
    List<Role> getRolesByUserId(Long userId);
    
    /**
     * 构建角色树
     */
    List<Role> buildRoleTree();
    
    /**
     * 为用户分配角色
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);
    
    /**
     * 移除用户的角色
     */
    void removeRolesFromUser(Long userId, List<Long> roleIds);
    
    /**
     * 检查用户是否有角色
     */
    boolean hasRole(Long userId, String roleCode);
    
    /**
     * 为角色分配权限
     */
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);
    
    /**
     * 移除角色的权限
     */
    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);
}