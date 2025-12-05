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
 * 租户实体 - 多租户架构的核心
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "tenants", indexes = {
    @Index(name = "idx_tenant_code", columnList = "code"),
    @Index(name = "idx_tenant_domain", columnList = "domain")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class Tenant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 租户代码，唯一标识 */
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;
    
    /** 租户名称 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /** 租户域名 */
    @Column(name = "domain", unique = true, length = 100)
    private String domain;
    
    /** 租户描述 */
    @Column(name = "description", length = 500)
    private String description;
    
    /** 租户配置信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "config", columnDefinition = "jsonb")
    private String config;
    
    /** 租户状态：ACTIVE-激活，INACTIVE-未激活，SUSPENDED-暂停 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TenantStatus status = TenantStatus.ACTIVE;
    
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
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    public enum TenantStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}