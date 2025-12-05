package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.OAuth2AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * OAuth2访问令牌数据访问接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Repository
public interface OAuth2AccessTokenRepository extends JpaRepository<OAuth2AccessToken, UUID> {
    
    /**
     * 根据令牌值查询访问令牌
     */
    Optional<OAuth2AccessToken> findByTokenValue(String tokenValue);
    
    /**
     * 根据刷新令牌值查询访问令牌
     */
    Optional<OAuth2AccessToken> findByRefreshTokenValue(String refreshTokenValue);
    
    /**
     * 根据客户端ID和用户ID查询有效的访问令牌
     */
    Optional<OAuth2AccessToken> findByClientIdAndUserIdAndExpiresAtAfter(
            String clientId, UUID userId, LocalDateTime now);
    
    /**
     * 根据用户ID查询所有访问令牌
     */
    List<OAuth2AccessToken> findByUserId(UUID userId);
    
    /**
     * 根据客户端ID查询所有访问令牌
     */
    List<OAuth2AccessToken> findByClientId(String clientId);
    
    /**
     * 根据租户ID查询所有访问令牌
     */
    List<OAuth2AccessToken> findByTenantId(String tenantId);
    
    /**
     * 删除过期的访问令牌
     */
    @Modifying
    @Query("DELETE FROM OAuth2AccessToken at WHERE at.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    /**
     * 根据客户端ID删除访问令牌
     */
    @Modifying
    @Query("DELETE FROM OAuth2AccessToken at WHERE at.clientId = :clientId")
    void deleteByClientId(@Param("clientId") String clientId);
    
    /**
     * 根据用户ID删除访问令牌
     */
    @Modifying
    @Query("DELETE FROM OAuth2AccessToken at WHERE at.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
    
    /**
     * 统计用户的有效令牌数量
     */
    @Query("SELECT COUNT(at) FROM OAuth2AccessToken at WHERE at.userId = :userId AND at.expiresAt > :now")
    long countActiveTokensByUserId(@Param("userId") UUID userId, @Param("now") LocalDateTime now);
}