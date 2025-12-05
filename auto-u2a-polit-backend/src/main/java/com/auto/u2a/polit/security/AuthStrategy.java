package com.auto.u2a.polit.security;

import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.LoginResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.enums.AuthProtocol;
import org.springframework.security.core.Authentication;

/**
 * 认证策略接口
 * 定义统一的认证方法，支持多种认证协议
 */
public interface AuthStrategy {
    
    /**
     * 获取支持的认证协议
     */
    AuthProtocol getSupportedProtocol();
    
    /**
     * 验证认证请求
     */
    boolean validateRequest(Object request);
    
    /**
     * 执行认证
     */
    Authentication authenticate(Object request);
    
    /**
     * 获取认证后的用户信息
     */
    User getAuthenticatedUser(Authentication authentication);
    
    /**
     * 构建登录响应
     */
    LoginResponse buildLoginResponse(Authentication authentication, String token);
    
    /**
     * 处理认证失败
     */
    ApiResponse<?> handleAuthenticationFailure(Exception e);
    
    /**
     * 是否需要验证码
     */
    default boolean requiresCaptcha() {
        return false;
    }
    
    /**
     * 是否需要多因素认证
     */
    default boolean requiresMFA() {
        return false;
    }
    
    /**
     * 是否支持刷新令牌
     */
    default boolean supportsRefresh() {
        return true;
    }
}