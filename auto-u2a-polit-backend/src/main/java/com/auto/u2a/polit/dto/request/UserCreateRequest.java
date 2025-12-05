package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 用户创建请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "用户创建请求")
public class UserCreateRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和连字符")
    @Schema(description = "用户名", example = "zhangsan", required = true)
    private String username;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "zhangsan@example.com", required = true)
    private String email;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Schema(description = "密码", example = "password123")
    private String password;
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Schema(description = "真实姓名", example = "张三", required = true)
    private String realName;
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Schema(description = "用户类型", example = "INTERNAL")
    private String userType;
    
    @Schema(description = "部门ID")
    private String departmentId;
    
    @Schema(description = "职位")
    private String position;
    
    @Schema(description = "用户显示名")
    private String displayName;
    
    @Schema(description = "用户头像URL")
    private String avatarUrl;
    
    @Schema(description = "用户扩展信息")
    private Map<String, Object> profile;
    
    public Map<String, Object> getProfile() {
        return profile;
    }
    
    public void setProfile(Map<String, Object> profile) {
        this.profile = profile;
    }
    
    @Schema(description = "元数据")
    private Map<String, Object> metadata;
    
    @NotBlank(message = "租户ID不能为空")
    @Schema(description = "租户ID", example = "tenant001", required = true)
    private String tenantId;
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}