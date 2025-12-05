package com.auto.u2a.polit.security;

import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.LoginResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.enums.AuthProtocol;
import com.auto.u2a.polit.service.UserService;
import com.auto.u2a.polit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * 短信验证码认证策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SMSAuthStrategy implements AuthStrategy {
    
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public AuthProtocol getSupportedProtocol() {
        return AuthProtocol.SMS;
    }

    @Override
    public boolean validateRequest(Object request) {
        if (!(request instanceof Map)) {
            return false;
        }
        
        Map<?, ?> smsRequest = (Map<?, ?>) request;
        return smsRequest.containsKey("phone") && smsRequest.containsKey("code");
    }

    @Override
    public Authentication authenticate(Object request) {
        Map<String, String> smsRequest = (Map<String, String>) request;
        String phone = smsRequest.get("phone");
        String code = smsRequest.get("code");
        
        try {
            // 验证短信验证码
            if (!validateSMSCode(phone, code)) {
                throw new IllegalArgumentException("验证码错误或已过期");
            }
            
            // 查找用户
            User user = findUserByPhone(phone);
            if (user == null) {
                // 如果用户不存在，自动创建用户
                user = createUserFromPhone(phone);
            }
            
            // 创建认证对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            
            // 记录登录成功
            userService.recordLoginSuccess(user.getUsername());
            
            // 清除已使用的验证码
            clearSMSCode(phone);
            
            log.info("短信验证码认证成功: {}", phone);
            
            return authentication;
            
        } catch (Exception e) {
            log.warn("短信验证码认证失败: {} - {}", phone, e.getMessage());
            throw new RuntimeException("短信验证码认证失败", e);
        }
    }

    @Override
    public User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    @Override
    public LoginResponse buildLoginResponse(Authentication authentication, String token) {
        User user = getAuthenticatedUser(authentication);
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtUtil.getExpirationDateFromToken(token).getTime());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setDisplayName(user.getDisplayName());
        response.setProtocol(getSupportedProtocol().getCode());
        
        return response;
    }

    @Override
    public ApiResponse<?> handleAuthenticationFailure(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return ApiResponse.error(400, e.getMessage());
        } else {
            log.error("短信验证码认证异常: {}", e.getMessage(), e);
            return ApiResponse.error(401, "短信验证码认证失败");
        }
    }

    @Override
    public boolean requiresCaptcha() {
        // 短信验证码认证不需要额外的验证码
        return false;
    }

    @Override
    public boolean requiresMFA() {
        // 短信验证码本身就是一种多因素认证
        return false;
    }

    /**
     * 验证短信验证码
     */
    private boolean validateSMSCode(String phone, String code) {
        // 这里实现短信验证码验证逻辑
        // 实际项目中需要集成短信服务商
        
        log.debug("验证短信验证码: phone={}, code={}", phone, code);
        
        // 示例实现 - 实际项目中需要验证Redis中的验证码
        return true; // 模拟验证通过
    }
    
    /**
     * 根据手机号查找用户
     */
    private User findUserByPhone(String phone) {
        // 这里实现根据手机号查找用户的逻辑
        
        log.debug("根据手机号查找用户: {}", phone);
        
        // 示例实现 - 实际项目中需要调用用户服务
        return null; // 模拟用户不存在
    }
    
    /**
     * 根据手机号创建用户
     */
    private User createUserFromPhone(String phone) {
        // 这里实现自动创建用户的逻辑
        
        log.debug("根据手机号创建用户: {}", phone);
        
        // 示例实现
        User user = new User();
        user.setUsername("sms_" + phone);
        user.setPhone(phone);
        user.setDisplayName("手机用户 " + phone);
        user.setTenantId("default");
        
        return user;
    }
    
    /**
     * 清除已使用的验证码
     */
    private void clearSMSCode(String phone) {
        // 这里实现清除验证码的逻辑
        
        log.debug("清除短信验证码: {}", phone);
    }
}