package com.auto.u2a.polit.dto.response;

import com.auto.u2a.polit.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户响应DTO
 */
@Data
public class UserResponse {
    
    private UUID id;
    
    // 手动添加setId()方法
    public void setId(UUID id) {
        this.id = id;
    }
    
    private String username;
    
    // 手动添加setUsername()方法
    public void setUsername(String username) {
        this.username = username;
    }
    
    private String email;
    
    // 手动添加setEmail()方法
    public void setEmail(String email) {
        this.email = email;
    }
    
    private String phone;
    
    // 手动添加setPhone()方法
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    private String realName;
    
    // 手动添加setRealName()方法
    public void setRealName(String realName) {
        this.realName = realName;
    }
    private String status;
    
    // 手动添加setStatus()方法
    public void setStatus(String status) {
        this.status = status;
    }
    private String tenantId;
    
    // 手动添加setTenantId()方法
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    private LocalDateTime createdAt;
    
    // 手动添加setCreatedAt()方法
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    private LocalDateTime updatedAt;
    
    // 手动添加setUpdatedAt()方法
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    private String avatarUrl;
    
    // 手动添加getAvatarUrl()方法
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    // 手动添加setAvatarUrl()方法
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    private LocalDateTime lastLoginAt;
    
    // 手动添加getLastLoginAt()方法
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    // 手动添加setLastLoginAt()方法
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    private String displayName;
    
    // 手动添加getDisplayName()方法
    public String getDisplayName() {
        return displayName;
    }
    
    // 手动添加setDisplayName()方法
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    private String type;
    
    // 手动添加getType()方法
    public String getType() {
        return type;
    }
    
    // 手动添加setType()方法
    public void setType(String type) {
        this.type = type;
    }
    
    // 手动添加getId()方法
    public UUID getId() {
        return id;
    }
    
    // 手动添加getUsername()方法
    public String getUsername() {
        return username;
    }
    
    // 手动添加getEmail()方法
    public String getEmail() {
        return email;
    }
    
    // 手动添加getPhone()方法
    public String getPhone() {
        return phone;
    }
    
    // 手动添加getStatus()方法
    public String getStatus() {
        return status != null ? status.toString() : null;
    }
    
    // 手动添加getCreatedAt()方法
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    // 手动添加getUpdatedAt()方法
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRealName(user.getRealName());
        response.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        response.setTenantId(user.getTenantId());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setLastLoginAt(user.getLastLoginAt());
        response.setDisplayName(user.getDisplayName());
        response.setType(user.getType() != null ? user.getType().name() : null);
        return response;
    }
}