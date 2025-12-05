package com.auto.u2a.polit.service;

import com.auto.u2a.polit.entity.Permission;
import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 创建权限
     */
    Permission createPermission(Permission permission);
    
    /**
     * 更新权限
     */
    Permission updatePermission(Permission permission);
    
    /**
     * 删除权限
     */
    void deletePermission(Long id);
    
    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long id);
    
    /**
     * 根据编码获取权限
     */
    Permission getPermissionByCode(String code);
    
    /**
     * 获取所有权限
     */
    List<Permission> getAllPermissions();
    
    /**
     * 根据类型获取权限
     */
    List<Permission> getPermissionsByType(String type);
    
    /**
     * 根据父权限ID获取子权限
     */
    List<Permission> getPermissionsByParentId(Long parentId);
    
    /**
     * 根据用户ID获取权限
     */
    List<Permission> getPermissionsByUserId(Long userId);
    
    /**
     * 根据角色ID获取权限
     */
    List<Permission> getPermissionsByRoleId(Long roleId);
    
    /**
     * 构建权限树
     */
    List<Permission> buildPermissionTree();
    
    /**
     * 检查用户是否有权限
     */
    boolean hasPermission(Long userId, String permissionCode);
    
    /**
     * 检查用户是否有接口权限
     */
    boolean hasApiPermission(Long userId, String path, String method);
}