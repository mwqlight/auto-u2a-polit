package com.auto.u2a.polit.service.impl;

import com.auto.u2a.polit.entity.Permission;
import com.auto.u2a.polit.entity.Role;
import com.auto.u2a.polit.entity.UserRole;
import com.auto.u2a.polit.repository.PermissionRepository;
import com.auto.u2a.polit.repository.RoleRepository;
import com.auto.u2a.polit.repository.UserRoleRepository;
import com.auto.u2a.polit.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    
    @Override
    @Transactional
    public Role createRole(Role role) {
        // 检查角色编码是否已存在
        if (roleRepository.findByCode(role.getCode()).isPresent()) {
            throw new RuntimeException("角色编码已存在: " + role.getCode());
        }
        
        return roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public Role updateRole(Role role) {
        Role existing = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RuntimeException("角色不存在: " + role.getId()));
        
        // 检查角色编码是否重复（排除自身）
        if (!existing.getCode().equals(role.getCode())) {
            roleRepository.findByCode(role.getCode())
                    .ifPresent(r -> {
                        throw new RuntimeException("角色编码已存在: " + role.getCode());
                    });
        }
        
        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        existing.setType(role.getType());
        existing.setParentId(role.getParentId());
        existing.setDataScope(role.getDataScope());
        existing.setDeptIds(role.getDeptIds());
        existing.setStatus(role.getStatus());
        existing.setSortOrder(role.getSortOrder());
        
        return roleRepository.save(existing);
    }
    
    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + id));
        
        // 检查是否有子角色
        List<Role> children = roleRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子角色，无法删除");
        }
        
        // 检查是否有用户关联
        List<UserRole> userRoles = userRoleRepository.findByRoleId(id);
        if (!userRoles.isEmpty()) {
            throw new RuntimeException("存在用户关联，无法删除");
        }
        
        roleRepository.delete(role);
    }
    
    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + id));
    }
    
    @Override
    public Role getRoleByCode(String code) {
        return roleRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + code));
    }
    
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    @Override
    public List<Role> getRolesByType(String type) {
        return roleRepository.findByType(type);
    }
    
    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return roleRepository.findByUserId(userId);
    }
    
    @Override
    public List<Role> buildRoleTree() {
        List<Role> allRoles = roleRepository.findByStatus(1);
        return buildTree(allRoles, 0L);
    }
    
    private List<Role> buildTree(List<Role> roles, Long parentId) {
        return roles.stream()
                .filter(r -> Objects.equals(r.getParentId(), parentId))
                .peek(r -> r.setChildren(buildTree(roles, r.getId())))
                .sorted(Comparator.comparing(Role::getSortOrder))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // 删除用户现有的角色关联
        userRoleRepository.deleteByUserId(userId);
        
        // 添加新的角色关联
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
            
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setStatus(1);
            userRole.setCreateBy("system");
            
            userRoleRepository.save(userRole);
        }
    }
    
    @Override
    @Transactional
    public void removeRolesFromUser(Long userId, List<Long> roleIds) {
        userRoleRepository.deleteByUserIdAndRoleIds(userId, roleIds);
    }
    
    @Override
    public boolean hasRole(Long userId, String roleCode) {
        List<Role> userRoles = getRolesByUserId(userId);
        return userRoles.stream()
                .anyMatch(r -> r.getCode().equals(roleCode) && r.getStatus() == 1);
    }
    
    @Override
    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
        
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            throw new RuntimeException("部分权限不存在");
        }
        
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在: " + roleId));
        
        List<Permission> currentPermissions = role.getPermissions();
        if (currentPermissions != null) {
            List<Permission> updatedPermissions = currentPermissions.stream()
                    .filter(p -> !permissionIds.contains(p.getId()))
                    .collect(Collectors.toList());
            role.setPermissions(updatedPermissions);
            roleRepository.save(role);
        }
    }
}