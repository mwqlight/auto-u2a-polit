package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.OAuth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * OAuth2客户端数据访问接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Repository
public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, UUID>, JpaSpecificationExecutor<OAuth2Client> {
    
    /**
     * 根据客户端ID查询客户端
     */
    Optional<OAuth2Client> findByClientId(String clientId);
    
    /**
     * 根据租户ID和客户端ID查询客户端
     */
    Optional<OAuth2Client> findByTenantIdAndClientId(String tenantId, String clientId);
    
    /**
     * 检查客户端ID是否存在
     */
    boolean existsByClientId(String clientId);
    
    /**
     * 检查租户内客户端ID是否存在
     */
    boolean existsByTenantIdAndClientId(String tenantId, String clientId);
    
    /**
     * 根据租户ID和状态查询客户端列表
     */
    java.util.List<OAuth2Client> findByTenantIdAndClientStatus(String tenantId, OAuth2Client.ClientStatus status);
    
    /**
     * 统计租户内的客户端数量
     */
    long countByTenantId(String tenantId);
    
    /**
     * 统计租户内指定状态的客户端数量
     */
    long countByTenantIdAndClientStatus(String tenantId, OAuth2Client.ClientStatus status);
    
    /**
     * 根据重定向URI查询客户端
     */
    @Query("SELECT c FROM OAuth2Client c WHERE :redirectUri MEMBER OF c.redirectUris")
    java.util.List<OAuth2Client> findByRedirectUri(@Param("redirectUri") String redirectUri);
}