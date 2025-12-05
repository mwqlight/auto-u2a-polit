package com.auto.u2a.polit.security;

import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.LoginResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.enums.AuthProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证上下文
 * 管理多种认证策略，实现全协议认证中心
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthContext {
    private static final Logger log = LoggerFactory.getLogger(AuthContext.class);
    
    private final Map<AuthProtocol, AuthStrategy> strategies = new HashMap<>();
    
    /**
     * 注册认证策略
     */
    public void registerStrategy(AuthProtocol protocol, AuthStrategy strategy) {
        strategies.put(protocol, strategy);
        log.info("注册认证策略: {} - {}", protocol.getCode(), protocol.getDescription());
    }
    
    /**
     * 获取认证策略
     */
    public AuthStrategy getStrategy(AuthProtocol protocol) {
        AuthStrategy strategy = strategies.get(protocol);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的认证协议: " + protocol.getCode());
        }
        return strategy;
    }
    
    /**
     * 执行认证
     */
    public Authentication authenticate(AuthProtocol protocol, Object request) {
        AuthStrategy strategy = getStrategy(protocol);
        
        // 验证请求
        if (!strategy.validateRequest(request)) {
            throw new IllegalArgumentException("认证请求格式错误");
        }
        
        // 执行认证
        return strategy.authenticate(request);
    }
    
    /**
     * 获取认证用户
     */
    public User getAuthenticatedUser(AuthProtocol protocol, Authentication authentication) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.getAuthenticatedUser(authentication);
    }
    
    /**
     * 构建登录响应
     */
    public LoginResponse buildLoginResponse(AuthProtocol protocol, Authentication authentication, String token) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.buildLoginResponse(authentication, token);
    }
    
    /**
     * 处理认证失败
     */
    public ApiResponse<?> handleAuthenticationFailure(AuthProtocol protocol, Exception e) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.handleAuthenticationFailure(e);
    }
    
    /**
     * 检查协议是否支持
     */
    public boolean supportsProtocol(AuthProtocol protocol) {
        return strategies.containsKey(protocol);
    }
    
    /**
     * 获取所有支持的协议
     */
    public AuthProtocol[] getSupportedProtocols() {
        return strategies.keySet().toArray(new AuthProtocol[0]);
    }
    
    /**
     * 检查是否需要验证码
     */
    public boolean requiresCaptcha(AuthProtocol protocol) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.requiresCaptcha();
    }
    
    /**
     * 检查是否需要多因素认证
     */
    public boolean requiresMFA(AuthProtocol protocol) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.requiresMFA();
    }
    
    /**
     * 检查是否支持刷新令牌
     */
    public boolean supportsRefresh(AuthProtocol protocol) {
        AuthStrategy strategy = getStrategy(protocol);
        return strategy.supportsRefresh();
    }
    
    /**
     * 获取当前用户ID
     */
    public Long getCurrentUserId() {
        // TODO: 实现获取当前用户ID的逻辑
        // 暂时返回null，需要根据实际情况实现
        return null;
    }
}