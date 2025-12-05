package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * OAuth2客户端实体类
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "oauth2_clients")
public class OAuth2Client extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "client_id", nullable = false, unique = true, length = 100)
    private String clientId;
    
    @Column(name = "client_secret", length = 200)
    private String clientSecret;
    
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;
    
    @Column(name = "client_uri", length = 500)
    private String clientUri;
    
    @Column(name = "logo_uri", length = 500)
    private String logoUri;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;
    
    @Column(name = "client_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    
    @Column(name = "client_status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ClientStatus clientStatus;
    
    @Column(name = "redirect_uris", length = 2000)
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> redirectUris;
    
    @Column(name = "grant_types", length = 500)
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> grantTypes;
    
    @Column(name = "scopes", length = 1000)
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<String> scopes;
    
    @Column(name = "token_endpoint_auth_method", length = 50)
    private String tokenEndpointAuthMethod;
    
    @Column(name = "token_expires_in")
    private Integer tokenExpiresIn;
    
    @Column(name = "refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;
    
    @Column(name = "require_consent")
    private Boolean requireConsent = false;
    
    @Column(name = "require_proof_key")
    private Boolean requireProofKey = false;
    
    @Column(name = "jwk_set_uri", length = 500)
    private String jwkSetUri;
    
    @Column(name = "jwk_set")
    @JdbcTypeCode(SqlTypes.JSON)
    private String jwkSet;
    
    @Column(name = "metadata")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;
    
    /**
     * 客户端类型枚举
     */
    public enum ClientType {
        CONFIDENTIAL,   // 机密客户端
        PUBLIC,         // 公开客户端
        HYBRID          // 混合客户端
    }
    
    /**
     * 客户端状态枚举
     */
    public enum ClientStatus {
        ACTIVE,         // 活跃
        INACTIVE,       // 非活跃
        SUSPENDED,      // 暂停
        DELETED         // 已删除
    }
}