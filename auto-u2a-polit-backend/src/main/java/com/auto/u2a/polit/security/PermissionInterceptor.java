package com.auto.u2a.polit.security;

import com.auto.u2a.polit.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 * 拦截请求并检查用户是否有访问权限
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    
    private final PermissionService permissionService;
    private final SecurityContext securityContext;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取当前用户ID
        Long userId = securityContext.getCurrentUserId();
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        // 获取请求路径和方法
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // 检查权限
        boolean hasPermission = permissionService.hasApiPermission(userId, path, method);
        if (!hasPermission) {
            log.warn("用户 {} 无权限访问 {} {}", userId, method, path);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        
        return true;
    }
}