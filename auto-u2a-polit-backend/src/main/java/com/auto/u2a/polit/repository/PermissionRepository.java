package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问层
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
    /** 根据权限编码查找 */
    Optional<Permission> findByCode(String code);
    
    /** 根据权限类型查找 */
    List<Permission> findByType(String type);
    
    /** 根据父权限ID查找子权限 */
    List<Permission> findByParentId(Long parentId);
    
    /** 根据状态查找权限 */
    List<Permission> findByStatus(Integer status);
    
    /** 根据角色ID查找权限 */
    @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId AND p.status = 1")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);
    
    /** 根据用户ID查找权限 */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN p.roles r " +
           "JOIN UserRole ur ON ur.roleId = r.id " +
           "WHERE ur.userId = :userId AND p.status = 1 AND r.status = 1 AND ur.status = 1")
    List<Permission> findByUserId(@Param("userId") Long userId);
    
    /** 根据路径和方法查找权限 */
    Optional<Permission> findByPathAndMethod(String path, String method);
}