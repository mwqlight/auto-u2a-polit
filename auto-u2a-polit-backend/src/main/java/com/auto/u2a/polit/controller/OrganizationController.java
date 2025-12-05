package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.dto.request.OrganizationCreateRequest;
import com.auto.u2a.polit.dto.request.OrganizationUpdateRequest;
import com.auto.u2a.polit.dto.response.OrganizationResponse;
import com.auto.u2a.polit.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 组织架构控制器
 */
@RestController
@RequestMapping("/api/v1/organizations")
@Tag(name = "组织架构管理", description = "组织架构相关接口")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @Operation(summary = "获取组织架构树", description = "获取租户下的组织架构树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('organization:view')")
    public ResponseEntity<List<OrganizationResponse>> getOrganizationTree(
            @Parameter(description = "租户ID") @RequestParam String tenantId) {
        List<OrganizationResponse> tree = organizationService.getOrganizationTree(tenantId);
        return ResponseEntity.ok(tree);
    }

    @Operation(summary = "获取组织列表", description = "分页获取租户下的组织列表")
    @GetMapping
    @PreAuthorize("hasAuthority('organization:view')")
    public ResponseEntity<Page<OrganizationResponse>> getOrganizationList(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<OrganizationResponse> organizations = organizationService.getOrganizationList(tenantId, pageable);
        return ResponseEntity.ok(organizations);
    }

    @Operation(summary = "获取组织详情", description = "获取组织详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:view')")
    public ResponseEntity<OrganizationResponse> getOrganizationById(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id) {
        OrganizationResponse organization = organizationService.getOrganizationById(tenantId, id);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "创建组织", description = "创建新的组织")
    @PostMapping
    @PreAuthorize("hasAuthority('organization:create')")
    public ResponseEntity<OrganizationResponse> createOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @RequestBody OrganizationCreateRequest request) {
        OrganizationResponse organization = organizationService.createOrganization(tenantId, request);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "更新组织", description = "更新组织信息")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:update')")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id,
            @RequestBody OrganizationUpdateRequest request) {
        OrganizationResponse organization = organizationService.updateOrganization(tenantId, id, request);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "删除组织", description = "删除组织")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:delete')")
    public ResponseEntity<Void> deleteOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id) {
        organizationService.deleteOrganization(tenantId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "移动组织", description = "移动组织到指定父组织下")
    @PatchMapping("/{id}/move")
    @PreAuthorize("hasAuthority('organization:update')")
    public ResponseEntity<OrganizationResponse> moveOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id,
            @Parameter(description = "新的父组织ID") @RequestParam(required = false) UUID parentId) {
        OrganizationResponse organization = organizationService.moveOrganization(tenantId, id, parentId);
        return ResponseEntity.ok(organization);
    }

    @Operation(summary = "获取组织下的用户", description = "分页获取组织下的用户列表")
    @GetMapping("/{id}/users")
    @PreAuthorize("hasAuthority('organization:view')")
    public ResponseEntity<Page<?>> getOrganizationUsers(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<?> users = organizationService.getOrganizationUsers(tenantId, id, pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "为组织添加用户", description = "为组织添加多个用户")
    @PostMapping("/{id}/users")
    @PreAuthorize("hasAuthority('organization:update')")
    public ResponseEntity<Void> addUsersToOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id,
            @RequestBody List<UUID> userIds) {
        organizationService.addUsersToOrganization(tenantId, id, userIds);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "从组织移除用户", description = "从组织移除指定用户")
    @DeleteMapping("/{id}/users/{userId}")
    @PreAuthorize("hasAuthority('organization:update')")
    public ResponseEntity<Void> removeUserFromOrganization(
            @Parameter(description = "租户ID") @RequestParam String tenantId,
            @Parameter(description = "组织ID") @PathVariable UUID id,
            @Parameter(description = "用户ID") @PathVariable UUID userId) {
        organizationService.removeUserFromOrganization(tenantId, id, userId);
        return ResponseEntity.noContent().build();
    }
}
