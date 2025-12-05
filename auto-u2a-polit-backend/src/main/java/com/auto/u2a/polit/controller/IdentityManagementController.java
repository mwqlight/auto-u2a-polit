package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.dto.request.*;
import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.OrganizationResponse;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.service.IdentityManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 统一身份管理引擎控制器
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/identity")
@RequiredArgsConstructor
@Tag(name = "统一身份管理引擎", description = "提供多租户用户管理、组织架构管理、数据同步等功能")
public class IdentityManagementController {
    
    private final IdentityManagementService identityManagementService;
    
    // ==================== 用户管理模块 ====================
    
    @PostMapping("/users")
    @Operation(summary = "创建用户", description = "在指定租户下创建新用户")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserCreateRequest request) {
        log.info("创建用户请求: {}", request.getUsername());
        UserResponse user = identityManagementService.createUser(request);
        return ResponseEntity.ok(ApiResponse.success("用户创建成功", user));
    }
    
    @PutMapping("/users/{userId}")
    @Operation(summary = "更新用户", description = "更新指定用户的信息")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest request) {
        log.info("更新用户请求: {}", userId);
        UserResponse user = identityManagementService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success("用户更新成功", user));
    }
    
    @GetMapping("/users/{userId}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable UUID userId,
            @RequestParam String tenantId) {
        log.info("获取用户详情: {}, 租户: {}", userId, tenantId);
        UserResponse user = identityManagementService.getUser(userId, tenantId);
        return ResponseEntity.ok(ApiResponse.success("获取用户详情成功", user));
    }
    
    @GetMapping("/users")
    @Operation(summary = "分页查询用户", description = "根据条件分页查询用户列表")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam String tenantId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        log.info("分页查询用户: 租户={}, 关键词={}, 状态={}", tenantId, keyword, status);
        Page<UserResponse> users = identityManagementService.getUsers(tenantId, keyword, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("查询用户列表成功", users));
    }
    
    @PostMapping("/users/batch-import")
    @Operation(summary = "批量导入用户", description = "批量导入用户数据")
    public ResponseEntity<ApiResponse<String>> batchImportUsers(
            @Valid @RequestBody BatchUserImportRequest request) {
        log.info("批量导入用户: 租户={}, 数量={}", request.getTenantId(), request.getUsers().size());
        String batchId = identityManagementService.batchImportUsers(request);
        return ResponseEntity.ok(ApiResponse.success("用户批量导入任务已提交", batchId));
    }
    
    @GetMapping("/users/export")
    @Operation(summary = "导出用户数据", description = "导出指定条件的用户数据")
    public ResponseEntity<ApiResponse<String>> exportUsers(
            @RequestParam String tenantId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "csv") String format) {
        
        log.info("导出用户数据: 租户={}, 格式={}", tenantId, format);
        String exportId = identityManagementService.exportUsers(tenantId, keyword, status, format);
        return ResponseEntity.ok(ApiResponse.success("用户导出任务已提交", exportId));
    }
    
    // ==================== 组织架构管理模块 ====================
    
    @PostMapping("/organizations")
    @Operation(summary = "创建组织", description = "在指定租户下创建新组织")
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody OrganizationCreateRequest request) {
        log.info("创建组织请求: {}", request.getName());
        OrganizationResponse organization = identityManagementService.createOrganization(request);
        return ResponseEntity.ok(ApiResponse.success("组织创建成功", organization));
    }
    
    @PutMapping("/organizations/{orgId}")
    @Operation(summary = "更新组织", description = "更新指定组织的信息")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable UUID orgId,
            @Valid @RequestBody OrganizationCreateRequest request) {
        log.info("更新组织请求: {}", orgId);
        OrganizationResponse organization = identityManagementService.updateOrganization(orgId, request);
        return ResponseEntity.ok(ApiResponse.success("组织更新成功", organization));
    }
    
    @GetMapping("/organizations/{orgId}")
    @Operation(summary = "获取组织详情", description = "根据组织ID获取组织详细信息")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganization(
            @PathVariable UUID orgId,
            @RequestParam String tenantId) {
        log.info("获取组织详情: {}, 租户: {}", orgId, tenantId);
        OrganizationResponse organization = identityManagementService.getOrganization(orgId, tenantId);
        return ResponseEntity.ok(ApiResponse.success("获取组织详情成功", organization));
    }
    
    @GetMapping("/organizations")
    @Operation(summary = "获取组织树", description = "获取指定租户的组织架构树")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getOrganizationTree(
            @RequestParam String tenantId,
            @RequestParam(required = false) String parentId) {
        
        log.info("获取组织树: 租户={}, 父组织={}", tenantId, parentId);
        List<OrganizationResponse> organizationTree = identityManagementService.getOrganizationTree(tenantId, parentId);
        return ResponseEntity.ok(ApiResponse.success("获取组织树成功", organizationTree));
    }
    
    @PutMapping("/organizations/{orgId}/move")
    @Operation(summary = "移动组织", description = "将组织移动到新的父组织下")
    public ResponseEntity<ApiResponse<OrganizationResponse>> moveOrganization(
            @PathVariable UUID orgId,
            @RequestParam String tenantId,
            @RequestParam(required = false) String newParentId) {
        
        log.info("移动组织: {}, 租户={}, 新父组织={}", orgId, tenantId, newParentId);
        OrganizationResponse organization = identityManagementService.moveOrganization(orgId, tenantId, newParentId);
        return ResponseEntity.ok(ApiResponse.success("组织移动成功", organization));
    }
    
    // ==================== 数据同步模块 ====================
    
    @PostMapping("/sync/tasks")
    @Operation(summary = "创建数据同步任务", description = "创建用户数据同步任务")
    public ResponseEntity<ApiResponse<String>> createSyncTask(
            @Valid @RequestBody SyncTaskCreateRequest request) {
        log.info("创建数据同步任务: 源租户={}, 目标租户={}", request.getSourceTenantId(), request.getTargetTenantId());
        String taskId = identityManagementService.createSyncTask(request);
        return ResponseEntity.ok(ApiResponse.success("数据同步任务创建成功", taskId));
    }
    
    @PostMapping("/sync/tasks/{taskId}/execute")
    @Operation(summary = "执行数据同步任务", description = "执行指定的数据同步任务")
    public ResponseEntity<ApiResponse<String>> executeSyncTask(
            @PathVariable String taskId) {
        log.info("执行数据同步任务: {}", taskId);
        String executionId = identityManagementService.executeSyncTask(taskId);
        return ResponseEntity.ok(ApiResponse.success("数据同步任务执行成功", executionId));
    }
    
    @GetMapping("/sync/tasks/{taskId}/status")
    @Operation(summary = "获取同步任务状态", description = "获取数据同步任务的执行状态")
    public ResponseEntity<ApiResponse<Object>> getSyncTaskStatus(
            @PathVariable String taskId) {
        log.info("获取同步任务状态: {}", taskId);
        Object status = identityManagementService.getSyncTaskStatus(taskId);
        return ResponseEntity.ok(ApiResponse.success("获取同步任务状态成功", status));
    }
    
    // ==================== 租户管理模块 ====================
    
    @PostMapping("/tenants")
    @Operation(summary = "创建租户", description = "创建新的租户")
    public ResponseEntity<ApiResponse<String>> createTenant(
            @Valid @RequestBody TenantCreateRequest request) {
        log.info("创建租户请求: {}", request.getCode());
        String tenantId = identityManagementService.createTenant(request);
        return ResponseEntity.ok(ApiResponse.success("租户创建成功", tenantId));
    }
    
    @PostMapping("/tenants/{tenantId}/initialize")
    @Operation(summary = "初始化租户", description = "初始化租户的基础数据")
    public ResponseEntity<ApiResponse<String>> initializeTenant(
            @PathVariable String tenantId) {
        log.info("初始化租户: {}", tenantId);
        String result = identityManagementService.initializeTenant(tenantId);
        return ResponseEntity.ok(ApiResponse.success("租户初始化成功", result));
    }
}