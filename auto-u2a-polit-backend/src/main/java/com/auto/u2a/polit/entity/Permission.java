package com.auto.u2a.polit.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限实体类
 * 支持细粒度的权限控制
 */
@Entity
@Table(name = "sys_permission")
@Data
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 权限编码，唯一标识 */
    @Column(unique = true, nullable = false)
    private String code;
    
    /** 权限名称 */
    @Column(nullable = false)
    private String name;
    
    /** 权限描述 */
    private String description;
    
    /** 权限类型：MENU-菜单权限，BUTTON-按钮权限，API-接口权限，DATA-数据权限 */
    @Column(nullable = false)
    private String type;
    
    /** 权限路径/URL */
    private String path;
    
    /** 权限方法：GET, POST, PUT, DELETE等 */
    private String method;
    
    /** 父权限ID */
    private Long parentId;
    
    /** 排序号 */
    private Integer sortOrder = 0;
    
    /** 权限状态：0-禁用，1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 创建时间 */
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    /** 更新时间 */
    @Column(nullable = false)
    private LocalDateTime updateTime;
    
    /** 创建人 */
    private String createBy;
    
    /** 更新人 */
    private String updateBy;
    
    /** 子权限列表 */
    @Transient
    private List<Permission> children;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}