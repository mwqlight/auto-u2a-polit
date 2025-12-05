package com.auto.u2a.polit.service;

import com.auto.u2a.polit.dto.request.OrganizationCreateRequest;
import com.auto.u2a.polit.dto.request.OrganizationUpdateRequest;
import com.auto.u2a.polit.dto.response.OrganizationResponse;
import com.auto.u2a.polit.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * 组织架构服务接口
 */
public interface OrganizationService {
    /**
     * 获取组织架构树
     * @param tenantId 租户ID
     * @return 组织架构树
     */
    List<OrganizationResponse> getOrganizationTree(String tenantId);

    /**
     * 获取组织列表
     * @param tenantId 租户ID
     * @param pageable 分页参数
     * @return 组织列表
     */
    Page<OrganizationResponse> getOrganizationList(String tenantId, Pageable pageable);

    /**
     * 获取组织详情
     * @param tenantId 租户ID
     * @param id 组织ID
     * @return 组织详情
     */
    OrganizationResponse getOrganizationById(String tenantId, UUID id);

    /**
     * 创建组织
     * @param tenantId 租户ID
     * @param request 组织创建请求
     * @return 创建的组织
     */
    OrganizationResponse createOrganization(String tenantId, OrganizationCreateRequest request);

    /**
     * 更新组织
     * @param tenantId 租户ID
     * @param id 组织ID
     * @param request 组织更新请求
     * @return 更新后的组织
     */
    OrganizationResponse updateOrganization(String tenantId, UUID id, OrganizationUpdateRequest request);

    /**
     * 删除组织
     * @param tenantId 租户ID
     * @param id 组织ID
     */
    void deleteOrganization(String tenantId, UUID id);

    /**
     * 移动组织
     * @param tenantId 租户ID
     * @param id 组织ID
     * @param parentId 新的父组织ID
     * @return 移动后的组织
     */
    OrganizationResponse moveOrganization(String tenantId, UUID id, UUID parentId);

    /**
     * 获取组织下的用户
     * @param tenantId 租户ID
     * @param id 组织ID
     * @param pageable 分页参数
     * @return 用户列表
     */
    Page<?> getOrganizationUsers(String tenantId, UUID id, Pageable pageable);

    /**
     * 为组织添加用户
     * @param tenantId 租户ID
     * @param id 组织ID
     * @param userIds 用户ID列表
     */
    void addUsersToOrganization(String tenantId, UUID id, List<UUID> userIds);

    /**
     * 从组织移除用户
     * @param tenantId 租户ID
     * @param id 组织ID
     * @param userId 用户ID
     */
    void removeUserFromOrganization(String tenantId, UUID id, UUID userId);

    /**
     * 检查组织编码是否存在
     * @param tenantId 租户ID
     * @param code 组织编码
     * @return 是否存在
     */
    boolean existsByCode(String tenantId, String code);

    /**
     * 检查组织编码是否存在（排除指定ID）
     * @param tenantId 租户ID
     * @param code 组织编码
     * @param id 组织ID
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String tenantId, String code, UUID id);
}
