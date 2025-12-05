package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问层
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    /** 根据用户ID查找 */
    List<UserRole> findByUserId(Long userId);
    
    /** 根据角色ID查找 */
    List<UserRole> findByRoleId(Long roleId);
    
    /** 根据用户ID和角色ID查找 */
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    /** 根据用户ID和状态查找 */
    List<UserRole> findByUserIdAndStatus(Long userId, Integer status);
    
    /** 删除用户的所有角色关联 */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    /** 批量删除用户角色关联 */
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.userId = :userId AND ur.roleId IN :roleIds")
    void deleteByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}