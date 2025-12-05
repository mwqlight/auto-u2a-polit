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
 * 组织架构实体 - 支持无限层级组织树
 * 
 * @author Auto U2A Polit Team
 */
@Entity
@Table(name = "organization_units", indexes = {
    @Index(name = "idx_org_tenant", columnList = "tenant_id"),
    @Index(name = "idx_org_parent", columnList = "parent_id"),
    @Index(name = "idx_org_path", columnList = "hierarchy_path"),
    @Index(name = "idx_org_code", columnList = "tenant_id, code")
})
@Data
@EqualsAndHashCode(callSuper = false)
public class OrganizationUnit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /** 所属租户 */
    @Column(name = "tenant_id", nullable = false, length = 36)
    private String tenantId;
    
    /** 父级组织ID */
    @Column(name = "parent_id", length = 36)
    private String parentId;
    
    /** 组织代码，租户内唯一 */
    @Column(name = "code", nullable = false, length = 50)
    private String code;
    
    /** 组织名称 */
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /** 组织类型：DEPARTMENT-部门，TEAM-团队，COMPANY-公司，GROUP-群组 */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private OrganizationType type = OrganizationType.DEPARTMENT;
    
    /** 层级路径（用于快速查询） */
    @Column(name = "hierarchy_path", nullable = false, length = 1000)
    private String hierarchyPath;
    
    /** 层级深度 */
    @Column(name = "depth", nullable = false)
    private Integer depth = 0;
    
    /** 排序序号 */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    
    /** 组织描述 */
    @Column(name = "description", length = 500)
    private String description;
    
    /** 组织属性（JSON格式） */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private String attributes;
    
    /** 组织状态：ACTIVE-激活，INACTIVE-未激活 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrganizationStatus status = OrganizationStatus.ACTIVE;
    
    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /** 负责人ID */
    @Column(name = "manager_id", length = 36)
    private String managerId;
    
    /** 元数据信息 */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;
    
    public enum OrganizationType {
        DEPARTMENT, TEAM, COMPANY, GROUP, DIVISION, UNIT
    }
    
    public enum OrganizationStatus {
        ACTIVE, INACTIVE, ARCHIVED
    }
}