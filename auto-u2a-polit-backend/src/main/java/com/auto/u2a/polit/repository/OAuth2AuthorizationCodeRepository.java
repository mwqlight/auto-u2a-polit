package com.auto.u2a.polit.repository;

import com.auto.u2a.polit.entity.OAuth2AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * OAuth2授权码数据访问接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Repository
public interface OAuth2AuthorizationCodeRepository extends JpaRepository<OAuth2AuthorizationCode, UUID> {
    
    /**
     * 根据授权码查询授权码信息
     */
    Optional<OAuth2AuthorizationCode> findByCode(String code);
    
    /**
     * 根据客户端ID和用户ID查询有效的授权码
     */
    Optional<OAuth2AuthorizationCode> findByClientIdAndUserIdAndUsedFalseAndExpiresAtAfter(
            String clientId, UUID userId, LocalDateTime now);
    
    /**
     * 标记授权码为已使用
     */
    @Modifying
    @Query("UPDATE OAuth2AuthorizationCode ac SET ac.used = true, ac.usedAt = :usedAt WHERE ac.code = :code")
    void markAsUsed(@Param("code") String code, @Param("usedAt") LocalDateTime usedAt);
    
    /**
     * 删除过期的授权码
     */
    @Modifying
    @Query("DELETE FROM OAuth2AuthorizationCode ac WHERE ac.expiresAt < :now")
    void deleteExpiredCodes(@Param("now") LocalDateTime now);
    
    /**
     * 根据客户端ID删除授权码
     */
    @Modifying
    @Query("DELETE FROM OAuth2AuthorizationCode ac WHERE ac.clientId = :clientId")
    void deleteByClientId(@Param("clientId") String clientId);
    
    /**
     * 根据用户ID删除授权码
     */
    @Modifying
    @Query("DELETE FROM OAuth2AuthorizationCode ac WHERE ac.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}