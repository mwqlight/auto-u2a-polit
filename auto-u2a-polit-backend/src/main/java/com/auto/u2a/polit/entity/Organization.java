package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "organization")
@EntityListeners(AuditingEntityListener.class)
public class Organization {
    /**
     * 组织ID
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    /**
     * 组织名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 组织编码
     */
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;

    /**
     * 组织类型
     */
    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrganizationType type;

    /**
     * 父组织ID
     */
    @Column(name = "parent_id", length = 36)
    private String parentId;

    /**
     * 描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 状态
     */
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    /**
     * 排序
     */
    @Column(name = "sort", nullable = false)
    private Integer sort;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 子组织
     */
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sort asc")
    private List<Organization> children;

    /**
     * 组织类型枚举
     */
    public enum OrganizationType {
        COMPANY, DEPARTMENT, TEAM, GROUP
    }

    /**
     * 组织状态枚举
     */
    public enum OrganizationStatus {
        ACTIVE, INACTIVE
    }
}
