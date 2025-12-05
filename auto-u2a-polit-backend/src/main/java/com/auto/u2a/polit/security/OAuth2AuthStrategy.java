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
 * OAuth2认证策略
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthStrategy implements AuthStrategy {
    
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    public AuthProtocol getSupportedProtocol() {
        return AuthProtocol.OAUTH2;
    }

    @Override
    public boolean validateRequest(Object request) {
        return request instanceof Map && ((Map<?, ?>) request).containsKey("code");
    }

    @Override
    public Authentication authenticate(Object request) {
        Map<String, String> oauthRequest = (Map<String, String>) request;
        String code = oauthRequest.get("code");
        String provider = oauthRequest.get("provider");
        String redirectUri = oauthRequest.get("redirect_uri");
        
        try {
            // 验证OAuth2授权码
            String accessToken = exchangeCodeForToken(code, provider, redirectUri);
            
            // 获取用户信息
            Map<String, Object> userInfo = getUserInfo(accessToken, provider);
            
            // 查找或创建用户
            User user = findOrCreateUser(userInfo, provider);
            
            // 创建认证对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            
            // 记录登录成功
            userService.recordLoginSuccess(user.getUsername());
            
            log.info("OAuth2认证成功: {} - {}", provider, user.getUsername());
            
            return authentication;
            
        } catch (Exception e) {
            log.warn("OAuth2认证失败: {} - {}", provider, e.getMessage());
            throw new RuntimeException("OAuth2认证失败", e);
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
            return ApiResponse.error(400, "OAuth2请求参数错误");
        } else {
            log.error("OAuth2认证异常: {}", e.getMessage(), e);
            return ApiResponse.error(401, "OAuth2认证失败");
        }
    }

    @Override
    public boolean requiresCaptcha() {
        // OAuth2认证不需要验证码
        return false;
    }

    @Override
    public boolean requiresMFA() {
        // OAuth2认证通常不需要多因素认证
        return false;
    }

    /**
     * 使用授权码交换访问令牌
     */
    private String exchangeCodeForToken(String code, String provider, String redirectUri) {
        // 这里实现具体的OAuth2令牌交换逻辑
        // 根据provider调用不同的OAuth2服务商
        
        // 示例实现 - 实际项目中需要集成具体的OAuth2服务
        log.debug("交换OAuth2令牌: provider={}, code={}", provider, code);
        
        // 模拟令牌交换
        return "mock_access_token_" + System.currentTimeMillis();
    }
    
    /**
     * 获取用户信息
     */
    private Map<String, Object> getUserInfo(String accessToken, String provider) {
        // 这里实现获取用户信息的逻辑
        // 根据provider调用不同的用户信息接口
        
        log.debug("获取OAuth2用户信息: provider={}", provider);
        
        // 模拟用户信息
        return Map.of(
            "sub", "oauth_user_" + System.currentTimeMillis(),
            "name", "OAuth2 User",
            "email", "oauth@example.com",
            "provider", provider
        );
    }
    
    /**
     * 查找或创建用户
     */
    private User findOrCreateUser(Map<String, Object> userInfo, String provider) {
        String externalId = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        
        // 根据外部ID查找用户
        // 如果不存在则创建新用户
        
        log.debug("查找或创建OAuth2用户: externalId={}, provider={}", externalId, provider);
        
        // 模拟用户查找/创建
        User user = new User();
        user.setUsername("oauth_" + externalId);
        user.setEmail(email);
        user.setDisplayName(name);
        user.setTenantId("default");
        
        return user;
    }
}