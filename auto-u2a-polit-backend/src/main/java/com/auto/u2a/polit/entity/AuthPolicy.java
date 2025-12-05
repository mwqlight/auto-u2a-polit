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
 * 认证策略实体 - 支持多种认证协议和策略
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "auth_policies", indexes = {
    @Index(name = "idx_policy_tenant", columnList = "tenant_id"),
    @Index(name = "idx_policy_type", columnList = "type"),
    @Index(name = "idx_policy_priority", columnList = "priority")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class AuthPolicy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 所属租户 */
    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;
    
    /** 策略名称 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /** 策略类型：OAUTH2, OIDC, SAML, LDAP, WEBAUTHN, TOTP, CAS */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private PolicyType type;
    
    /** 策略优先级（数字越小优先级越高） */
    @Column(name = "priority", nullable = false)
    private Integer priority = 100;
    
    /** 策略规则（JSON格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "rules", columnDefinition = "json")
    private String rules;
    
    /** 策略作用域 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scopes", columnDefinition = "json")
    private String scopes;
    
    /** 策略状态：ACTIVE-激活，INACTIVE-未激活 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PolicyStatus status = PolicyStatus.ACTIVE;
    
    /** 策略描述 */
    @Column(name = "description", length = 500)
    private String description;
    
    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** 生效时间 */
    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;
    
    /** 失效时间 */
    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;
    
    /** 版本号 */
    @Column(name = "version", nullable = false)
    private Integer version = 1;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;
    
    public enum PolicyType {
        OAUTH2, OIDC, SAML, LDAP, WEBAUTHN, TOTP, CAS, PASSWORD, SOCIAL
    }
    
    public enum PolicyStatus {
        ACTIVE, INACTIVE, DRAFT, ARCHIVED
    }
}