package com.auto.u2a.polit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 基础实体类
 * 所有实体类的基类，包含公共字段
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@MappedSuperclass
@Data
public abstract class BaseEntity {
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 创建者ID
     */
    @Column(name = "created_by", length = 36)
    private String createdBy;
    
    /**
     * 更新者ID
     */
    @Column(name = "updated_by", length = 36)
    private String updatedBy;
    
    /**
     * 逻辑删除标记
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    @Column(name = "version", nullable = false)
    private Long version = 0L;
}