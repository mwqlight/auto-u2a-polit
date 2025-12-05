package com.auto.u2a.polit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户更新请求DTO
 */
@Data
public class UserUpdateRequest {
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;
}