package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 * 支持用户多角色分配
 */
@Entity
@Table(name = "sys_user_role")
public class UserRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(nullable = false)
    private Long userId;
    
    /** 角色ID */
    @Column(nullable = false)
    private Long roleId;
    
    /** 关联状态：0-禁用，1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 创建时间 */
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    /** 创建人 */
    private String createBy;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
