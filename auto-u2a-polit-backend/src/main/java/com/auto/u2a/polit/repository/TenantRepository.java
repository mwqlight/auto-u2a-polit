package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 租户数据访问接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, String>, JpaSpecificationExecutor<Tenant> {
    
    /**
     * 根据租户代码查询租户
     * 
     * @param code 租户代码
     * @return 租户信息
     */
    Optional<Tenant> findByCode(String code);
    
    /**
     * 检查租户代码是否存在
     * 
     * @param code 租户代码
     * @return 是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据状态查询租户列表
     * 
     * @param status 租户状态
     * @return 租户列表
     */
    java.util.List<Tenant> findByStatus(Tenant.TenantStatus status);
}