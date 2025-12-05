package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Collections;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * OAuth2访问令牌实体类
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "oauth2_access_tokens")
public class OAuth2AccessToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "token_value", nullable = false, unique = true, length = 500)
    private String tokenValue;
    
    @Column(name = "token_type", nullable = false, length = 20)
    private String tokenType;
    
    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;
    
    @Column(name = "scopes", length = 1000)
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> scopes;
    
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "refresh_token_value", length = 500)
    private String refreshTokenValue;
    
    @Column(name = "refresh_token_issued_at")
    private LocalDateTime refreshTokenIssuedAt;
    
    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;
    
    @Column(name = "authorization_code", length = 200)
    private String authorizationCode;
    
    @Column(name = "oidc_id_token", length = 2000)
    private String oidcIdToken;
    
    @Column(name = "metadata")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;
    
    @Column(name = "revoked")
    private Boolean revoked = false;
    
    /**
     * 检查访问令牌是否过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * 检查刷新令牌是否过期
     */
    public boolean isRefreshTokenExpired() {
        return refreshTokenExpiresAt != null && LocalDateTime.now().isAfter(refreshTokenExpiresAt);
    }
    
    // 手动添加getter和setter方法
    public UUID getUserId() {
        return userId;
    }
    
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshTokenValue = refreshToken;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public String getUserIdAsString() {
        return userId != null ? userId.toString() : "";
    }
    
    public Set<String> getScopes() {
        return scopes;
    }
    
    public String getScope() {
        return scopes != null ? String.join(" ", scopes) : "";
    }
    
    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
    
    public boolean isRevoked() {
        return revoked;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public void setScope(String scope) {
        if (scope != null) {
            this.scopes = Set.of(scope.split(" "));
        } else {
            this.scopes = Collections.emptySet();
        }
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public void setRefreshExpiresAt(LocalDateTime refreshExpiresAt) {
        this.refreshTokenExpiresAt = refreshExpiresAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * 检查令牌是否有效（未过期）
     */
    public boolean isValid() {
        return !isExpired();
    }
}