package com.auto.u2a.polit.service.impl;

import com.auto.u2a.polit.entity.Permission;
import com.auto.u2a.polit.repository.PermissionRepository;
import com.auto.u2a.polit.repository.UserRoleRepository;
import com.auto.u2a.polit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    
    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        // 检查权限编码是否已存在
        if (permissionRepository.findByCode(permission.getCode()).isPresent()) {
            throw new RuntimeException("权限编码已存在: " + permission.getCode());
        }
        
        return permissionRepository.save(permission);
    }
    
    @Override
    @Transactional
    public Permission updatePermission(Permission permission) {
        Permission existing = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new RuntimeException("权限不存在: " + permission.getId()));
        
        // 检查权限编码是否重复（排除自身）
        if (!existing.getCode().equals(permission.getCode())) {
            permissionRepository.findByCode(permission.getCode())
                    .ifPresent(p -> {
                        throw new RuntimeException("权限编码已存在: " + permission.getCode());
                    });
        }
        
        existing.setName(permission.getName());
        existing.setDescription(permission.getDescription());
        existing.setType(permission.getType());
        existing.setPath(permission.getPath());
        existing.setMethod(permission.getMethod());
        existing.setParentId(permission.getParentId());
        existing.setSortOrder(permission.getSortOrder());
        existing.setStatus(permission.getStatus());
        
        return permissionRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + id));
        
        // 检查是否有子权限
        List<Permission> children = permissionRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子权限，无法删除");
        }
        
        permissionRepository.delete(permission);
    }
    
    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + id));
    }
    
    @Override
    public Permission getPermissionByCode(String code) {
        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("权限不存在: " + code));
    }
    
    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
    
    @Override
    public List<Permission> getPermissionsByType(String type) {
        return permissionRepository.findByType(type);
    }
    
    @Override
    public List<Permission> getPermissionsByParentId(Long parentId) {
        return permissionRepository.findByParentId(parentId);
    }
    
    @Override
    public List<Permission> getPermissionsByUserId(Long userId) {
        return permissionRepository.findByUserId(userId);
    }
    
    @Override
    public List<Permission> getPermissionsByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }
    
    @Override
    public List<Permission> buildPermissionTree() {
        List<Permission> allPermissions = permissionRepository.findByStatus(1);
        return buildTree(allPermissions, 0L);
    }
    
    private List<Permission> buildTree(List<Permission> permissions, Long parentId) {
        return permissions.stream()
                .filter(p -> Objects.equals(p.getParentId(), parentId))
                .peek(p -> p.setChildren(buildTree(permissions, p.getId())))
                .sorted(Comparator.comparing(Permission::getSortOrder))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        List<Permission> userPermissions = getPermissionsByUserId(userId);
        return userPermissions.stream()
                .anyMatch(p -> p.getCode().equals(permissionCode) && p.getStatus() == 1);
    }
    
    @Override
    public boolean hasApiPermission(Long userId, String path, String method) {
        List<Permission> userPermissions = getPermissionsByUserId(userId);
        return userPermissions.stream()
                .anyMatch(p -> {
                    boolean pathMatch = path.equals(p.getPath()) || 
                            (p.getPath() != null && path.matches(p.getPath().replace("*", ".*")));
                    boolean methodMatch = method.equalsIgnoreCase(p.getMethod()) || 
                            p.getMethod() == null || p.getMethod().equals("*");
                    return pathMatch && methodMatch && p.getStatus() == 1;
                });
    }
}