package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.dto.request.UserCreateRequest;
import com.auto.u2a.polit.dto.request.UserUpdateRequest;
import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.service.UserService;
import java.util.UUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasPermission('user:create')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse user = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("用户创建成功", user));
    }
    
    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('user:update')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id, 
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", user));
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('user:read')")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("获取用户成功", UserResponse.fromEntity(user)));
    }
    
    /**
     * 分页查询用户列表
     */
    @GetMapping
    @PreAuthorize("hasPermission('user:read')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<UserResponse> users = userService.getUsers(pageable, keyword);
        return ResponseEntity.ok(ApiResponse.success("获取用户列表成功", users));
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('user:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户删除成功"));
    }
    
    /**
     * 启用用户
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasPermission('user:update')")
    public ResponseEntity<ApiResponse<Void>> enableUser(@PathVariable UUID id) {
        userService.enableUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户启用成功"));
    }
    
    /**
     * 禁用用户
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasPermission('user:update')")
    public ResponseEntity<ApiResponse<Void>> disableUser(@PathVariable UUID id) {
        userService.disableUser(id);
        return ResponseEntity.ok(ApiResponse.success("用户禁用成功"));
    }
    
    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasPermission('user:update')")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@PathVariable UUID id) {
        userService.resetPassword(id);
        return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
    }
}