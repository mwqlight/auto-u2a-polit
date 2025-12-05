package com.auto.u2a.polit.dto.response;

import com.auto.u2a.polit.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应DTO
 */
@Data
public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String realName;
    private User.UserStatus status;
    private String tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static UserResponse fromEntity(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRealName(user.getRealName());
        response.setStatus(user.getStatus());
        response.setTenantId(user.getTenantId());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}