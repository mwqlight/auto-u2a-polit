package com.auto.u2a.polit.dto.response;

/**
 * 登录响应DTO
 */
public class LoginResponse {
    
    private String token;
    
    private String tokenType = "Bearer";
    
    private Long expiresIn;
    
    private String username;
    
    private String realName;
    
    private String email;
    
    // 带参数的构造函数
    public LoginResponse(String token, String tokenType, Long expiresIn, String username) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.username = username;
    }
    
    // 无参数的构造函数
    public LoginResponse() {
    }
    
    // getter and setter methods
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    private String phone;
    
    private String protocol;
    
    private String displayName;
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}