package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 客户端实体 - OAuth2客户端注册
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "clients", indexes = {
    @Index(name = "idx_client_tenant", columnList = "tenant_id"),
    @Index(name = "idx_client_id", columnList = "client_id"),
    @Index(name = "idx_client_status", columnList = "status")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 所属租户 */
    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;
    
    /** 客户端ID */
    @Column(name = "client_id", unique = true, nullable = false, length = 100)
    private String clientId;
    
    /** 客户端密钥哈希 */
    @Column(name = "client_secret_hash", nullable = false, length = 100)
    private String clientSecretHash;
    
    /** 客户端名称 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /** 重定向URI列表（JSON数组格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "redirect_uris", columnDefinition = "jsonb")
    private String redirectUris;
    
    /** 客户端设置（JSON格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settings", columnDefinition = "jsonb")
    private String settings;
    
    /** 授权类型：AUTHORIZATION_CODE, CLIENT_CREDENTIALS, PASSWORD, REFRESH_TOKEN */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "authorization_grant_types", columnDefinition = "jsonb")
    private String authorizationGrantTypes;
    
    /** 作用域列表（JSON数组格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scopes", columnDefinition = "jsonb")
    private String scopes;
    
    /** 客户端状态：ACTIVE-激活，INACTIVE-未激活，REVOKED-吊销 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ClientStatus status = ClientStatus.ACTIVE;
    
    /** 客户端类型：PUBLIC-公开，CONFIDENTIAL-机密 */
    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    private ClientType clientType = ClientType.CONFIDENTIAL;
    
    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** 过期时间 */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    /** 最后使用时间 */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    public enum ClientStatus {
        ACTIVE, INACTIVE, REVOKED, EXPIRED
    }
    
    public enum ClientType {
        PUBLIC, CONFIDENTIAL
    }
}