package com.auto.u2a.polit.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 * 支持角色继承和权限继承
 */
@Entity
@Table(name = "sys_role")
@Data
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 角色编码，唯一标识 */
    @Column(unique = true, nullable = false)
    private String code;
    
    /** 角色名称 */
    @Column(nullable = false)
    private String name;
    
    /** 角色描述 */
    private String description;
    
    /** 角色类型：SYSTEM-系统角色，CUSTOM-自定义角色 */
    @Column(nullable = false)
    private String type = "CUSTOM";
    
    /** 父角色ID，支持角色继承 */
    private Long parentId;
    
    /** 数据权限范围：ALL-全部，DEPT-本部门，SELF-仅自己，CUSTOM-自定义 */
    @Column(nullable = false)
    private String dataScope = "SELF";
    
    /** 自定义数据权限部门ID列表 */
    @Column(length = 2000)
    private String deptIds;
    
    /** 角色状态：0-禁用，1-启用 */
    @Column(nullable = false)
    private Integer status = 1;
    
    /** 排序号 */
    private Integer sortOrder = 0;
    
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
    
    /** 角色权限关联 */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sys_role_permission",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;
    
    /** 子角色列表 */
    @Transient
    private List<Role> children;
    
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