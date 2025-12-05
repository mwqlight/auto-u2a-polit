package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 用户数据访问层
 * 支持多租户架构的用户管理
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR " +
           "u.username LIKE %:keyword% OR " +
           "u.displayName LIKE %:keyword% OR " +
           "u.email LIKE %:keyword%)")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();
    
    // ==================== 多租户相关查询 ====================
    
    /**
     * 根据租户ID和用户ID查询用户
     */
    Optional<User> findByIdAndTenantId(UUID id, String tenantId);
    
    /**
     * 根据租户ID和用户名查询用户
     */
    Optional<User> findByTenantIdAndUsername(String tenantId, String username);
    
    /**
     * 根据租户ID和邮箱查询用户
     */
    Optional<User> findByTenantIdAndEmail(String tenantId, String email);
    
    /**
     * 根据租户ID和手机号查询用户
     */
    Optional<User> findByTenantIdAndPhone(String tenantId, String phone);
    
    /**
     * 检查租户内用户名是否存在
     */
    boolean existsByTenantIdAndUsername(String tenantId, String username);
    
    /**
     * 检查租户内邮箱是否存在
     */
    boolean existsByTenantIdAndEmail(String tenantId, String email);
    
    /**
     * 检查租户内手机号是否存在
     */
    boolean existsByTenantIdAndPhone(String tenantId, String phone);
    
    /**
     * 根据租户ID分页查询用户
     */
    Page<User> findByTenantId(String tenantId, Pageable pageable);
    
    /**
     * 根据租户ID和状态查询用户
     */
    Page<User> findByTenantIdAndStatus(String tenantId, User.UserStatus status, Pageable pageable);
    
    /**
     * 删除租户内指定用户名的用户
     */
    void deleteByTenantIdAndUsername(String tenantId, String username);
    
    /**
     * 统计租户内的用户数量
     */
    long countByTenantId(String tenantId);
    
    /**
     * 统计租户内指定状态的用户数量
     */
    long countByTenantIdAndStatus(String tenantId, User.UserStatus status);
}