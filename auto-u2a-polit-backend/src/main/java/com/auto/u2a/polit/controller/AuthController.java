package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.dto.request.LoginRequest;
import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.LoginResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.enums.AuthProtocol;
import com.auto.u2a.polit.security.AuthContext;
import com.auto.u2a.polit.service.UserService;
import com.auto.u2a.polit.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 全协议认证控制器
 * 支持多种认证协议的统一认证入口
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthContext authContext;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 统一登录接口
     * 支持多种认证协议
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody Map<String, Object> request) {
        try {
            // 获取认证协议
            String protocolCode = (String) request.get("protocol");
            if (protocolCode == null) {
                // 默认使用用户名密码认证
                protocolCode = AuthProtocol.PASSWORD.getCode();
            }
            
            AuthProtocol protocol = AuthProtocol.fromCode(protocolCode);
            
            // 检查协议是否支持
            if (!authContext.supportsProtocol(protocol)) {
                return ApiResponse.error(400, "不支持的认证协议: " + protocolCode);
            }
            
            // 执行认证
            Authentication authentication = authContext.authenticate(protocol, request);
            
            // 设置认证上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(authentication.getName());
            
            // 构建响应
            LoginResponse response = authContext.buildLoginResponse(protocol, authentication, token);
            
            log.info("用户登录成功: 协议={}, 用户={}", protocolCode, authentication.getName());
            
            return ApiResponse.success("登录成功", response);
            
        } catch (Exception e) {
            // 处理认证失败
            String protocolCode = (String) request.get("protocol");
            if (protocolCode == null) {
                protocolCode = AuthProtocol.PASSWORD.getCode();
            }
            
            AuthProtocol protocol = AuthProtocol.fromCode(protocolCode);
            ApiResponse<?> errorResponse = authContext.handleAuthenticationFailure(protocol, e);
            
            log.warn("用户登录失败: 协议={}, 错误={}", protocolCode, e.getMessage());
            
            return ApiResponse.error(errorResponse.getCode(), errorResponse.getMessage());
        }
    }
    
    /**
     * 传统用户名密码登录（兼容旧版本）
     */
    @PostMapping("/login/password")
    public ApiResponse<LoginResponse> loginPassword(@Valid @RequestBody LoginRequest request) {
        try {
            // 使用用户名密码认证策略
            Authentication authentication = authContext.authenticate(AuthProtocol.PASSWORD, request);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(authentication.getName());
            
            // 构建响应
            LoginResponse response = authContext.buildLoginResponse(AuthProtocol.PASSWORD, authentication, token);
            
            log.info("用户名密码登录成功: {}", request.getUsername());
            
            return ApiResponse.success("登录成功", response);
            
        } catch (Exception e) {
            // 记录登录失败
            userService.recordLoginFailure(request.getUsername());
            
            ApiResponse<?> errorResponse = authContext.handleAuthenticationFailure(AuthProtocol.PASSWORD, e);
            
            log.warn("用户名密码登录失败: {} - {}", request.getUsername(), e.getMessage());
            
            return ApiResponse.error(errorResponse.getCode(), errorResponse.getMessage());
        }
    }
    
    /**
     * OAuth2登录
     */
    @PostMapping("/login/oauth2")
    public ApiResponse<LoginResponse> loginOAuth2(@RequestBody Map<String, String> request) {
        try {
            // 使用OAuth2认证策略
            Authentication authentication = authContext.authenticate(AuthProtocol.OAUTH2, request);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(authentication.getName());
            
            // 构建响应
            LoginResponse response = authContext.buildLoginResponse(AuthProtocol.OAUTH2, authentication, token);
            
            log.info("OAuth2登录成功: {}", authentication.getName());
            
            return ApiResponse.success("登录成功", response);
            
        } catch (Exception e) {
            ApiResponse<?> errorResponse = authContext.handleAuthenticationFailure(AuthProtocol.OAUTH2, e);
            
            log.warn("OAuth2登录失败: {}", e.getMessage());
            
            return ApiResponse.error(errorResponse.getCode(), errorResponse.getMessage());
        }
    }
    
    /**
     * 短信验证码登录
     */
    @PostMapping("/login/sms")
    public ApiResponse<LoginResponse> loginSMS(@RequestBody Map<String, String> request) {
        try {
            // 使用短信验证码认证策略
            Authentication authentication = authContext.authenticate(AuthProtocol.SMS, request);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(authentication.getName());
            
            // 构建响应
            LoginResponse response = authContext.buildLoginResponse(AuthProtocol.SMS, authentication, token);
            
            log.info("短信验证码登录成功: {}", authentication.getName());
            
            return ApiResponse.success("登录成功", response);
            
        } catch (Exception e) {
            ApiResponse<?> errorResponse = authContext.handleAuthenticationFailure(AuthProtocol.SMS, e);
            
            log.warn("短信验证码登录失败: {}", e.getMessage());
            
            return ApiResponse.error(errorResponse.getCode(), errorResponse.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("用户登出: {}", username);
            SecurityContextHolder.clearContext();
        }
        return ApiResponse.success("登出成功", null);
    }

    @GetMapping("/me")
    public ApiResponse<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            return ApiResponse.success(user);
        }
        return ApiResponse.error(401, "未认证");
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                
                // 生成新的令牌
                String newToken = jwtUtil.generateToken(username);
                
                LoginResponse response = new LoginResponse(newToken, "Bearer", jwtUtil.getExpirationDateFromToken(newToken).getTime(), username);
                
                return ApiResponse.success("令牌刷新成功", response);
            }
        }
        
        return ApiResponse.<LoginResponse>error(401, "令牌无效");
    }
    
    /**
     * 获取支持的认证协议列表
     */
    @GetMapping("/protocols")
    public ApiResponse<Map<String, Object>> getSupportedProtocols() {
        AuthProtocol[] protocols = authContext.getSupportedProtocols();
        
        Map<String, Object> result = Map.of(
            "protocols", protocols,
            "count", protocols.length,
            "defaultProtocol", AuthProtocol.PASSWORD.getCode()
        );
        
        return ApiResponse.success(result);
    }
    
    /**
     * 检查认证协议是否需要验证码
     */
    @GetMapping("/protocols/{protocol}/captcha")
    public ApiResponse<Boolean> requiresCaptcha(@PathVariable String protocol) {
        try {
            AuthProtocol authProtocol = AuthProtocol.fromCode(protocol);
            boolean requires = authContext.requiresCaptcha(authProtocol);
            return ApiResponse.success(requires);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "不支持的认证协议: " + protocol);
        }
    }
    
    /**
     * 检查认证协议是否需要多因素认证
     */
    @GetMapping("/protocols/{protocol}/mfa")
    public ApiResponse<Boolean> requiresMFA(@PathVariable String protocol) {
        try {
            AuthProtocol authProtocol = AuthProtocol.fromCode(protocol);
            boolean requires = authContext.requiresMFA(authProtocol);
            return ApiResponse.success(requires);
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(400, "不支持的认证协议: " + protocol);
        }
    }
}