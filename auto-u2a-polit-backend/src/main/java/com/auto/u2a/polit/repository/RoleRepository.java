package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问层
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /** 根据角色编码查找 */
    Optional<Role> findByCode(String code);
    
    /** 根据角色类型查找 */
    List<Role> findByType(String type);
    
    /** 根据父角色ID查找子角色 */
    List<Role> findByParentId(Long parentId);
    
    /** 根据状态查找角色 */
    List<Role> findByStatus(Integer status);
    
    /** 根据用户ID查找角色 */
    @Query("SELECT r FROM Role r " +
           "JOIN UserRole ur ON ur.roleId = r.id " +
           "WHERE ur.userId = :userId AND r.status = 1 AND ur.status = 1")
    List<Role> findByUserId(@Param("userId") Long userId);
    
    /** 查找系统内置角色 */
    List<Role> findByTypeAndStatus(String type, Integer status);
}