package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.OrganizationUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 组织架构数据访问接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Repository
public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnit, UUID>, JpaSpecificationExecutor<OrganizationUnit> {
    
    /**
     * 根据租户ID和父组织ID查询子组织列表
     */
    List<OrganizationUnit> findByTenantIdAndParentId(String tenantId, String parentId);
    
    /**
     * 根据租户ID查询根组织列表（parentId为null）
     */
    List<OrganizationUnit> findByTenantIdAndParentIdIsNull(String tenantId);
    
    /**
     * 根据租户ID和代码查询组织
     */
    Optional<OrganizationUnit> findByTenantIdAndCode(String tenantId, String code);
    
    /**
     * 根据租户ID和名称查询组织
     */
    Optional<OrganizationUnit> findByTenantIdAndName(String tenantId, String name);
    
    /**
     * 检查租户内组织代码是否存在
     */
    boolean existsByTenantIdAndCode(String tenantId, String code);
    
    /**
     * 检查租户内组织名称是否存在
     */
    boolean existsByTenantIdAndName(String tenantId, String name);
    
    /**
     * 根据租户ID分页查询组织
     */
    Page<OrganizationUnit> findByTenantId(String tenantId, Pageable pageable);
    
    /**
     * 根据租户ID和状态查询组织
     */
    Page<OrganizationUnit> findByTenantIdAndStatus(String tenantId, OrganizationUnit.OrganizationStatus status, Pageable pageable);
    
    /**
     * 根据租户ID和类型查询组织
     */
    List<OrganizationUnit> findByTenantIdAndType(String tenantId, OrganizationUnit.OrganizationType type);
    
    /**
     * 统计租户内的组织数量
     */
    long countByTenantId(String tenantId);
    
    /**
     * 统计租户内指定状态的组织数量
     */
    long countByTenantIdAndStatus(String tenantId, OrganizationUnit.OrganizationStatus status);
    
    /**
     * 查询指定路径下的组织树
     */
    @Query("SELECT o FROM OrganizationUnit o WHERE o.tenantId = :tenantId AND o.hierarchyPath LIKE :path%")
    List<OrganizationUnit> findByTenantIdAndHierarchyPathStartingWith(@Param("tenantId") String tenantId, @Param("path") String path);
    
    /**
     * 查询组织及其所有子组织
     */
    @Query("SELECT o FROM OrganizationUnit o WHERE o.tenantId = :tenantId AND o.hierarchyPath LIKE CONCAT(:path, '%')")
    List<OrganizationUnit> findOrganizationTree(@Param("tenantId") String tenantId, @Param("path") String path);
    
    /**
     * 更新组织路径（用于移动组织）
     */
    @Query("UPDATE OrganizationUnit o SET o.hierarchyPath = REPLACE(o.hierarchyPath, :oldPath, :newPath) WHERE o.tenantId = :tenantId AND o.hierarchyPath LIKE CONCAT(:oldPath, '%')")
    void updateHierarchyPathByTenantIdAndPathStartingWith(@Param("tenantId") String tenantId, 
                                                         @Param("oldPath") String oldPath, 
                                                         @Param("newPath") String newPath);
}