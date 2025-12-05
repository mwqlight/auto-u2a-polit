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
    @Column(name = "config", columnDefinition = "json")
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
    
    /** 租户类型 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private TenantType type = TenantType.DEFAULT;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;
    
    // 手动添加getType()方法
    public TenantType getType() {
        return type;
    }
    
    // 手动添加setType()方法
    public void setType(TenantType type) {
        this.type = type;
    }
    
    public enum TenantStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
    
    public enum TenantType {
        ENTERPRISE, PERSONAL, DEFAULT
    }
    
    // 手动添加所有必要的getter和setter方法
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getConfig() {
        return config;
    }
    
    public void setConfig(String config) {
        this.config = config;
    }
    
    public TenantStatus getStatus() {
        return status;
    }
    
    public void setStatus(TenantStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}