package com.auto.u2a.polit.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户角色关联实体类
 * 支持用户多角色分配
 */
@Entity
@Table(name = "sys_user_role")
@Data
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
}