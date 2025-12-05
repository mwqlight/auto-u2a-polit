package com.auto.u2a.polit.security;

import com.auto.u2a.polit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 * 拦截请求并检查用户是否有访问权限
 */
@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(PermissionInterceptor.class);
    
    private final PermissionService permissionService;
    private final AuthContext authContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前用户ID
        Long userId = authContext.getCurrentUserId();
        if (userId == null) {
            response.setStatus(401);
            return false;
        }
        
        // 获取请求路径和方法
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // 检查权限
        boolean hasPermission = permissionService.hasApiPermission(userId, path, method);
        if (!hasPermission) {
            log.warn("用户 {} 无权限访问 {} {}", userId, method, path);
            response.setStatus(403);
            return false;
        }
        
        return true;
    }
}