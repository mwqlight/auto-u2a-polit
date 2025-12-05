package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * OAuth2授权码实体类
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "oauth2_authorization_codes")
public class OAuth2AuthorizationCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "code", nullable = false, unique = true, length = 200)
    private String code;
    
    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;
    
    @Column(name = "redirect_uri", length = 500)
    private String redirectUri;
    
    @Column(name = "scopes", length = 1000)
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> scopes;
    
    @Column(name = "state", length = 200)
    private String state;
    
    @Column(name = "nonce", length = 200)
    private String nonce;
    
    @Column(name = "code_challenge", length = 200)
    private String codeChallenge;
    
    @Column(name = "code_challenge_method", length = 20)
    private String codeChallengeMethod;
    
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(name = "used")
    private Boolean used = false;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "metadata")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;
    
    /**
     * 检查授权码是否过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    // 手动添加setter方法
    public void setCode(String code) {
        this.code = code;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public void setScope(String scope) {
        // 这里需要将字符串转换为Set<String>类型
        this.scopes = Set.of(scope.split(" "));
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public void setUsed(boolean used) {
        this.used = used;
    }
    
    // 手动添加getter方法
    public boolean isUsed() {
        return used;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public String getScope() {
        return scopes != null ? String.join(" ", scopes) : "";
    }
    
    public Set<String> getScopes() {
        return scopes;
    }
    
    /**
     * 检查授权码是否有效（未使用且未过期）
     */
    public boolean isValid() {
        return !used && !isExpired();
    }
}