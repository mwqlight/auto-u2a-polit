package com.auto.u2a.polit.dto.response;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    
    private String token;
    
    private String tokenType = "Bearer";
    
    private Long expiresIn;
    
    private String username;
    
    private String realName;
    
    private String email;
}