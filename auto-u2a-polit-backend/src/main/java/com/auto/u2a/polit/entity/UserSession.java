package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户会话实体 - 会话管理
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_session_user", columnList = "user_id"),
    @Index(name = "idx_session_expires", columnList = "expires_at"),
    @Index(name = "idx_session_device", columnList = "device_fingerprint"),
    @Index(name = "idx_session_token", columnList = "session_token")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 关联用户ID */
    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;
    
    /** 会话令牌 */
    @Column(name = "session_token", unique = true, nullable = false, length = 500)
    private String sessionToken;
    
    /** 设备指纹 */
    @Column(name = "device_fingerprint", length = 200)
    private String deviceFingerprint;
    
    /** 过期时间 */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    
    /** 会话状态：ACTIVE-活跃，EXPIRED-已过期，REVOKED-已吊销 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status = SessionStatus.ACTIVE;
    
    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 最后活动时间 */
    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;
    
    /** 登录IP地址 */
    @Column(name = "login_ip", length = 45)
    private String loginIp;
    
    /** 用户代理信息 */
    @Column(name = "user_agent", length = 500)
    private String userAgent;
    
    /** 会话上下文信息（JSON格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context", columnDefinition = "json")
    private String context;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;
    
    public enum SessionStatus {
        ACTIVE, EXPIRED, REVOKED, LOGOUT
    }
}