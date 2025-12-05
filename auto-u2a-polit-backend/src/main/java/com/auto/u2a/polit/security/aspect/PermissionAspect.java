package com.auto.u2a.polit.security.aspect;

import com.auto.u2a.polit.security.AuthContext;
import com.auto.u2a.polit.security.annotation.RequiresPermission;
import com.auto.u2a.polit.security.annotation.RequiresRole;
import com.auto.u2a.polit.service.PermissionService;
import com.auto.u2a.polit.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限切面
 * 实现注解驱动的权限控制
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {
    private static final Logger log = LoggerFactory.getLogger(PermissionAspect.class);
    
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final AuthContext authContext;
    
    /**
     * 权限注解切面
     */
    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        Long userId = authContext.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("未授权访问");
        }
        
        String permissionCode = requiresPermission.value();
        boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
        
        if (!hasPermission) {
            log.warn("用户 {} 无权限: {}", userId, permissionCode);
            throw new RuntimeException("权限不足: " + requiresPermission.name());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * 角色注解切面
     */
    @Around("@annotation(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        Long userId = authContext.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("未授权访问");
        }
        
        String roleCode = requiresRole.value();
        boolean hasRole = roleService.hasRole(userId, roleCode);
        
        if (!hasRole) {
            log.warn("用户 {} 无角色: {}", userId, roleCode);
            throw new RuntimeException("角色不足: " + requiresRole.name());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * 类级别权限检查
     */
    @Around("@within(requiresPermission)")
    public Object checkClassPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
        return checkPermission(joinPoint, requiresPermission);
    }
    
    /**
     * 类级别角色检查
     */
    @Around("@within(requiresRole)")
    public Object checkClassRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        return checkRole(joinPoint, requiresRole);
    }
}