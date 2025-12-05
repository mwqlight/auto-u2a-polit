package com.auto.u2a.polit.service.impl;

import com.auto.u2a.polit.dto.request.OrganizationCreateRequest;
import com.auto.u2a.polit.dto.request.OrganizationUpdateRequest;
import com.auto.u2a.polit.dto.response.OrganizationResponse;
import com.auto.u2a.polit.entity.Organization;
import com.auto.u2a.polit.exception.BusinessException;
import com.auto.u2a.polit.exception.ErrorCode;
import com.auto.u2a.polit.repository.OrganizationRepository;
import com.auto.u2a.polit.service.OrganizationService;
import com.auto.u2a.polit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 组织架构服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public List<OrganizationResponse> getOrganizationTree(String tenantId) {
        List<Organization> organizations = organizationRepository.findByTenantIdAndParentIdIsNull(tenantId);
        return buildOrganizationTree(organizations, tenantId);
    }

    private List<OrganizationResponse> buildOrganizationTree(List<Organization> organizations, String tenantId) {
        List<OrganizationResponse> responseList = new ArrayList<>();
        for (Organization organization : organizations) {
            OrganizationResponse response = modelMapper.map(organization, OrganizationResponse.class);
            List<Organization> children = organizationRepository.findByTenantIdAndParentId(tenantId, organization.getId());
            response.setChildren(buildOrganizationTree(children, tenantId));
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    public Page<OrganizationResponse> getOrganizationList(String tenantId, Pageable pageable) {
        Page<Organization> organizations = organizationRepository.findByTenantId(tenantId, pageable);
        return organizations.map(organization -> modelMapper.map(organization, OrganizationResponse.class));
    }

    @Override
    public OrganizationResponse getOrganizationById(String tenantId, UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
        if (!organization.getTenantId().equals(tenantId)) {
            throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }
        return modelMapper.map(organization, OrganizationResponse.class);
    }

    @Override
    @Transactional
    public OrganizationResponse createOrganization(String tenantId, OrganizationCreateRequest request) {
        // 检查组织编码是否存在
        if (organizationRepository.existsByTenantIdAndCode(tenantId, request.getCode())) {
            throw new BusinessException(ErrorCode.ORGANIZATION_CODE_EXISTS);
        }

        // 检查组织名称是否存在
        if (organizationRepository.existsByTenantIdAndName(tenantId, request.getName())) {
            throw new BusinessException(ErrorCode.ORGANIZATION_NAME_EXISTS);
        }

        // 创建组织
        Organization organization = modelMapper.map(request, Organization.class);
        organization.setTenantId(tenantId);
        organization.setStatus(Organization.OrgStatus.ACTIVE);
        organization.setSort(request.getSort() != null ? request.getSort() : 0);

        // 生成路径
        if (request.getParentId() != null) {
            Organization parent = organizationRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
            if (!parent.getTenantId().equals(tenantId)) {
                throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
            }
            organization.setPath(parent.getPath() + parent.getId() + "/");
        } else {
            organization.setPath("/");
        }

        organization = organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationResponse.class);
    }

    @Override
    @Transactional
    public OrganizationResponse updateOrganization(String tenantId, UUID id, OrganizationUpdateRequest request) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
        if (!organization.getTenantId().equals(tenantId)) {
            throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        // 检查组织编码是否存在（排除当前组织）
        if (request.getCode() != null && !request.getCode().equals(organization.getCode())) {
            if (organizationRepository.existsByTenantIdAndCode(tenantId, request.getCode())) {
                throw new BusinessException(ErrorCode.ORGANIZATION_CODE_EXISTS);
            }
        }

        // 检查组织名称是否存在（排除当前组织）
        if (request.getName() != null && !request.getName().equals(organization.getName())) {
            if (organizationRepository.existsByTenantIdAndName(tenantId, request.getName())) {
                throw new BusinessException(ErrorCode.ORGANIZATION_NAME_EXISTS);
            }
        }

        // 更新组织信息
        modelMapper.map(request, organization);

        // 如果父组织ID改变，更新路径
        if (request.getParentId() != null && !request.getParentId().equals(organization.getParentId())) {
            Organization parent = organizationRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
            if (!parent.getTenantId().equals(tenantId)) {
                throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
            }

            String oldPath = organization.getPath();
            String newPath = parent.getPath() + parent.getId() + "/";
            organization.setPath(newPath);

            // 更新子组织的路径
            organizationRepository.updatePathByTenantIdAndPathStartingWith(tenantId, oldPath, newPath);
        }

        organization = organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationResponse.class);
    }

    @Override
    @Transactional
    public void deleteOrganization(String tenantId, UUID id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
        if (!organization.getTenantId().equals(tenantId)) {
            throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        // 检查是否有子组织
        List<Organization> children = organizationRepository.findByTenantIdAndParentId(tenantId, id);
        if (!children.isEmpty()) {
            throw new BusinessException(ErrorCode.ORGANIZATION_HAS_CHILDREN);
        }

        // 检查是否有用户
        // TODO: 实现检查组织是否有用户的逻辑

        organizationRepository.delete(organization);
    }

    @Override
    @Transactional
    public OrganizationResponse moveOrganization(String tenantId, UUID id, UUID parentId) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
        if (!organization.getTenantId().equals(tenantId)) {
            throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
        }

        // 不能移动到自己或子组织下
        if (id.equals(parentId)) {
            throw new BusinessException(ErrorCode.ORGANIZATION_CANNOT_MOVE_TO_SELF);
        }

        // 检查父组织是否存在
        Organization parent = null;
        if (parentId != null) {
            parent = organizationRepository.findById(parentId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND));
            if (!parent.getTenantId().equals(tenantId)) {
                throw new BusinessException(ErrorCode.ORGANIZATION_NOT_FOUND);
            }

            // 检查是否移动到子组织下
            List<Organization> children = organizationRepository.findByTenantIdAndPathStartingWith(tenantId, organization.getPath());
            if (children.stream().anyMatch(child -> child.getId().equals(parentId))) {
                throw new BusinessException(ErrorCode.ORGANIZATION_CANNOT_MOVE_TO_CHILD);
            }
        }

        // 更新父组织ID和路径
        String oldPath = organization.getPath();
        String newPath = parent != null ? parent.getPath() + parent.getId() + "/" : "/";
        organization.setParentId(parentId);
        organization.setPath(newPath);

        // 更新子组织的路径
        organizationRepository.updatePathByTenantIdAndPathStartingWith(tenantId, oldPath, newPath);

        organization = organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationResponse.class);
    }

    @Override
    public Page<?> getOrganizationUsers(String tenantId, UUID id, Pageable pageable) {
        // TODO: 实现获取组织下用户的逻辑
        return null;
    }

    @Override
    @Transactional
    public void addUsersToOrganization(String tenantId, UUID id, List<UUID> userIds) {
        // TODO: 实现为组织添加用户的逻辑
    }

    @Override
    @Transactional
    public void removeUserFromOrganization(String tenantId, UUID id, UUID userId) {
        // TODO: 实现从组织移除用户的逻辑
    }

    @Override
    public boolean existsByCode(String tenantId, String code) {
        return organizationRepository.existsByTenantIdAndCode(tenantId, code);
    }

    @Override
    public boolean existsByCodeAndIdNot(String tenantId, String code, UUID id) {
        return organizationRepository.existsByTenantIdAndCode(tenantId, code) && !organizationRepository.findByTenantIdAndCode(tenantId, code).get().getId().equals(id);
    }
}
