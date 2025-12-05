package com.auto.u2a.polit.security;

import com.auto.u2a.polit.dto.request.LoginRequest;
import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.LoginResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.enums.AuthProtocol;
import com.auto.u2a.polit.service.UserService;
import com.auto.u2a.polit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 用户名密码认证策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordAuthStrategy implements AuthStrategy {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public AuthProtocol getSupportedProtocol() {
        return AuthProtocol.PASSWORD;
    }

    @Override
    public boolean validateRequest(Object request) {
        return request instanceof LoginRequest;
    }

    @Override
    public Authentication authenticate(Object request) {
        LoginRequest loginRequest = (LoginRequest) request;
        
        try {
            // 使用Spring Security的认证管理器进行认证，传递用户名和租户ID作为认证主体
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername() + ":" + loginRequest.getTenantId(), 
                    loginRequest.getPassword()
                )
            );
            
            // 记录登录成功
            userService.recordLoginSuccess(loginRequest.getUsername());
            
            log.info("用户名密码认证成功: {} (租户: {})", loginRequest.getUsername(), loginRequest.getTenantId());
            
            return authentication;
            
        } catch (Exception e) {
            // 记录登录失败
            userService.recordLoginFailure(loginRequest.getUsername());
            
            log.warn("用户名密码认证失败: {} (租户: {}) - {}", loginRequest.getUsername(), loginRequest.getTenantId(), e.getMessage());
            throw e;
        }
    }

    @Override
    public User getAuthenticatedUser(Authentication authentication) {
        String principal = authentication.getName();
        // 从认证主体中分离用户名和租户ID
        String[] parts = principal.split(":");
        String username = parts[0];
        String tenantId = parts[1];
        return userService.getUserByUsernameAndTenantId(username, tenantId);
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
        
        return response;
    }

    @Override
    public ApiResponse<?> handleAuthenticationFailure(Exception e) {
        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            return ApiResponse.error(401, "用户名或密码错误");
        } else if (e instanceof IllegalArgumentException) {
            return ApiResponse.error(400, "请求参数错误");
        } else {
            log.error("用户名密码认证异常: {}", e.getMessage(), e);
            return ApiResponse.error(500, "系统内部错误");
        }
    }

    @Override
    public boolean requiresCaptcha() {
        // 用户名密码认证需要验证码保护
        return true;
    }

    @Override
    public boolean requiresMFA() {
        // 根据用户配置决定是否需要多因素认证
        return false; // 可在用户服务中动态判断
    }
}