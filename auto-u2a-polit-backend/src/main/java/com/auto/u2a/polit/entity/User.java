package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户实体 - 多模型用户架构的核心
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_tenant", columnList = "tenant_id"),
    @Index(name = "idx_user_username", columnList = "tenant_id, username"),
    @Index(name = "idx_user_email", columnList = "tenant_id, email"),
    @Index(name = "idx_user_phone", columnList = "tenant_id, phone")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {
    /** 创建时间 */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    // 手动添加getCreatedAt()方法
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    // 手动添加setCreatedAt()方法
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /** 更新时间 */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 手动添加getUpdatedAt()方法
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // 手动添加setUpdatedAt()方法
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    // 手动添加getId()方法
    public UUID getId() {
        return id;
    }
    
    // 手动添加setId()方法
    public void setId(UUID id) {
        this.id = id;
    }
    
    /** 所属租户 */
    @Column(name = "tenant_id", nullable = true, length = 64)
    private String tenantId;
    
    // 手动添加getTenantId()方法
    public String getTenantId() {
        return tenantId;
    }
    
    // 手动添加setTenantId()方法
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    /** 用户名 */
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    

    
    // 手动添加setUsername()方法
    public void setUsername(String username) {
        this.username = username;
    }
    
    /** 邮箱地址，租户内唯一 */
    @Column(name = "email", length = 100)
    private String email;
    
    // 手动添加getEmail()方法
    public String getEmail() {
        return email;
    }
    
    // 手动添加setEmail()方法
    public void setEmail(String email) {
        this.email = email;
    }
    
    /** 手机号码，租户内唯一 */
    @Column(name = "phone", length = 20)
    private String phone;
    
    // 手动添加getPhone()方法
    public String getPhone() {
        return phone;
    }
    
    // 手动添加setPhone()方法
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /** 密码哈希 */
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    
    // 手动添加getPassword()方法
    @Override
    public String getPassword() {
        return passwordHash;
    }
    
    // 手动添加setPassword()方法
    public void setPassword(String password) {
        this.passwordHash = password;
    }
    
    // 手动添加setPasswordHash()方法
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    // 手动添加getPasswordHash()方法
    public String getPasswordHash() {
        return passwordHash;
    }
    
    // 实现UserDetails接口的其他方法
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 目前返回空集合，后续可以根据实际情况添加权限
        return Collections.emptyList();
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
    
    /** 用户显示名称 */
    @Column(name = "display_name", length = 100)
    private String displayName;
    
    // 手动添加getDisplayName()方法
    public String getDisplayName() {
        return displayName;
    }
    
    // 手动添加setDisplayName()方法
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    /** 用户类型 */
    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private UserType type = UserType.INTERNAL;
    
    // 手动添加getType()方法
    public UserType getType() {
        return type;
    }
    
    // 手动添加setType()方法
    public void setType(UserType type) {
        this.type = type;
    }
    
    /** 真实姓名 */
    @Column(name = "real_name", length = 50)
    private String realName;
    
    // 手动添加getRealName()方法
    public String getRealName() {
        return realName;
    }
    
    // 手动添加setRealName()方法
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    /** 用户头像URL */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    
    // 手动添加getAvatarUrl()方法
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    // 手动添加setAvatarUrl()方法
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    /** 用户档案信息（JSON Schema支持） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "profile", columnDefinition = "json")
    private String profile;
    
    // 手动添加getProfile()方法
    public String getProfile() {
        return profile;
    }
    
    // 手动添加setProfile()方法
    public void setProfile(String profile) {
        this.profile = profile;
    }
    
    /** 用户状态：ACTIVE-激活，INACTIVE-未激活，LOCKED-锁定，SUSPENDED-暂停 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;
    
    // 手动添加getStatus()方法
    public UserStatus getStatus() {
        return status;
    }
    
    // 手动添加setStatus()方法
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    

    

    
    /** 最后登录时间 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    // 手动添加getLastLoginAt()方法
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    // 手动添加setLastLoginAt()方法
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    /** 最后登录IP */
    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;
    
    /** 登录失败次数 */
    @Column(name = "failed_login_count", nullable = false)
    private Integer failedLoginCount = 0;
    
    // 手动添加getFailedLoginCount()方法
    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }
    
    // 手动添加setFailedLoginCount()方法
    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }
    
    /** 账户锁定时间 */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
    
    // 手动添加getLockedUntil()方法
    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }
    
    // 手动添加setLockedUntil()方法
    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
    
    /** 密码最后修改时间 */
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    // 手动添加getPasswordChangedAt()方法
    public LocalDateTime getPasswordChangedAt() {
        return passwordChangedAt;
    }
    
    // 手动添加setPasswordChangedAt()方法
    public void setPasswordChangedAt(LocalDateTime passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;
    
    public enum UserStatus {
        ACTIVE, INACTIVE, LOCKED, SUSPENDED
    }
    
    public enum UserType {
        INTERNAL, EXTERNAL, SYSTEM
    }
}