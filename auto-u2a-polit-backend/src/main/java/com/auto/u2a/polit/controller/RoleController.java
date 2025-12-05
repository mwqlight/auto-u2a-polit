package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.entity.Role;
import com.auto.u2a.polit.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/v1/roles")
@Slf4j
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;
    
    /**
     * 创建角色
     */
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role created = roleService.createRole(role);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        Role updated = roleService.updateRole(role);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
    
    /**
     * 根据编码获取角色
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Role> getRoleByCode(@PathVariable String code) {
        Role role = roleService.getRoleByCode(code);
        return ResponseEntity.ok(role);
    }
    
    /**
     * 获取所有角色
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    /**
     * 根据类型获取角色
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Role>> getRolesByType(@PathVariable String type) {
        List<Role> roles = roleService.getRolesByType(type);
        return ResponseEntity.ok(roles);
    }
    
    /**
     * 根据用户ID获取角色
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Role>> getRolesByUserId(@PathVariable Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        return ResponseEntity.ok(roles);
    }
    
    /**
     * 构建角色树
     */
    @GetMapping("/tree")
    public ResponseEntity<List<Role>> buildRoleTree() {
        List<Role> roleTree = roleService.buildRoleTree();
        return ResponseEntity.ok(roleTree);
    }
    
    /**
     * 为用户分配角色
     */
    @PostMapping("/user/{userId}/assign")
    public ResponseEntity<Void> assignRolesToUser(
            @PathVariable Long userId, 
            @RequestBody List<Long> roleIds) {
        roleService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 移除用户的角色
     */
    @PostMapping("/user/{userId}/remove")
    public ResponseEntity<Void> removeRolesFromUser(
            @PathVariable Long userId, 
            @RequestBody List<Long> roleIds) {
        roleService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 检查用户是否有角色
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasRole(
            @RequestParam Long userId, 
            @RequestParam String roleCode) {
        boolean hasRole = roleService.hasRole(userId, roleCode);
        return ResponseEntity.ok(hasRole);
    }
    
    /**
     * 为角色分配权限
     */
    @PostMapping("/{roleId}/permissions/assign")
    public ResponseEntity<Void> assignPermissionsToRole(
            @PathVariable Long roleId, 
            @RequestBody List<Long> permissionIds) {
        roleService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 移除角色的权限
     */
    @PostMapping("/{roleId}/permissions/remove")
    public ResponseEntity<Void> removePermissionsFromRole(
            @PathVariable Long roleId, 
            @RequestBody List<Long> permissionIds) {
        roleService.removePermissionsFromRole(roleId, permissionIds);
        return ResponseEntity.ok().build();
    }
}