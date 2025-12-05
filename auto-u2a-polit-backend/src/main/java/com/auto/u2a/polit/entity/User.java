package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 所属租户 */
    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;
    
    /** 用户名，租户内唯一 */
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    
    /** 邮箱地址，租户内唯一 */
    @Column(name = "email", length = 100)
    private String email;
    
    /** 手机号码，租户内唯一 */
    @Column(name = "phone", length = 20)
    private String phone;
    
    /** 密码哈希 */
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    
    /** 用户显示名称 */
    @Column(name = "display_name", length = 100)
    private String displayName;
    
    /** 用户头像URL */
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;
    
    /** 用户档案信息（JSON Schema支持） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "profile", columnDefinition = "jsonb")
    private String profile;
    
    /** 用户状态：ACTIVE-激活，INACTIVE-未激活，LOCKED-锁定，SUSPENDED-暂停 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;
    
    /** 用户类型：INTERNAL-内部用户，EXTERNAL-外部用户，SYSTEM-系统用户 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private UserType type = UserType.INTERNAL;
    

    
    /** 最后登录时间 */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /** 最后登录IP */
    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;
    
    /** 登录失败次数 */
    @Column(name = "failed_login_count", nullable = false)
    private Integer failedLoginCount = 0;
    
    /** 账户锁定时间 */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
    
    /** 密码最后修改时间 */
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    public enum UserStatus {
        ACTIVE, INACTIVE, LOCKED, SUSPENDED
    }
    
    public enum UserType {
        INTERNAL, EXTERNAL, SYSTEM
    }
}