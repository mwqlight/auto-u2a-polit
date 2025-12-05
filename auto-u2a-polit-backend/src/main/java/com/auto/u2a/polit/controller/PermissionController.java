package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.entity.Permission;
import com.auto.u2a.polit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/v1/permissions")
@Slf4j
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionService permissionService;
    
    /**
     * 创建权限
     */
    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission created = permissionService.createPermission(permission);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        permission.setId(id);
        Permission updated = permissionService.updatePermission(permission);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据ID获取权限
     */
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }
    
    /**
     * 根据编码获取权限
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Permission> getPermissionByCode(@PathVariable String code) {
        Permission permission = permissionService.getPermissionByCode(code);
        return ResponseEntity.ok(permission);
    }
    
    /**
     * 获取所有权限
     */
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * 根据类型获取权限
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Permission>> getPermissionsByType(@PathVariable String type) {
        List<Permission> permissions = permissionService.getPermissionsByType(type);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * 根据父权限ID获取子权限
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Permission>> getPermissionsByParentId(@PathVariable Long parentId) {
        List<Permission> permissions = permissionService.getPermissionsByParentId(parentId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * 根据用户ID获取权限
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Permission>> getPermissionsByUserId(@PathVariable Long userId) {
        List<Permission> permissions = permissionService.getPermissionsByUserId(userId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * 根据角色ID获取权限
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<Permission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }
    
    /**
     * 构建权限树
     */
    @GetMapping("/tree")
    public ResponseEntity<List<Permission>> buildPermissionTree() {
        List<Permission> permissionTree = permissionService.buildPermissionTree();
        return ResponseEntity.ok(permissionTree);
    }
    
    /**
     * 检查用户是否有权限
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasPermission(
            @RequestParam Long userId, 
            @RequestParam String permissionCode) {
        boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
        return ResponseEntity.ok(hasPermission);
    }
    
    /**
     * 检查用户是否有接口权限
     */
    @GetMapping("/check-api")
    public ResponseEntity<Boolean> hasApiPermission(
            @RequestParam Long userId, 
            @RequestParam String path, 
            @RequestParam String method) {
        boolean hasPermission = permissionService.hasApiPermission(userId, path, method);
        return ResponseEntity.ok(hasPermission);
    }
}