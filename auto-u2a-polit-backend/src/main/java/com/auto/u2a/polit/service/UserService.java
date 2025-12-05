package com.auto.u2a.polit.service;

import com.auto.u2a.polit.dto.request.UserCreateRequest;
import com.auto.u2a.polit.dto.request.UserUpdateRequest;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户服务接口
 */
public interface UserService extends UserDetailsService {
    
    /**
     * 创建用户
     */
    UserResponse createUser(UserCreateRequest request);
    
    /**
     * 更新用户
     */
    UserResponse updateUser(UUID id, UserUpdateRequest request);
    
    /**
     * 根据ID获取用户
     */
    User getUserById(UUID id);
    
    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);
    
    /**
     * 根据用户名和租户ID获取用户
     */
    User getUserByUsernameAndTenantId(String username, String tenantId);
    
    /**
     * 分页查询用户
     */
    Page<UserResponse> getUsers(Pageable pageable, String keyword);
    
    /**
     * 删除用户
     */
    void deleteUser(UUID id);
    
    /**
     * 启用用户
     */
    void enableUser(UUID id);
    
    /**
     * 禁用用户
     */
    void disableUser(UUID id);
    
    /**
     * 重置密码
     */
    void resetPassword(UUID id);
    
    /**
     * 记录登录失败
     */
    void recordLoginFailure(String username);
    
    /**
     * 记录登录成功
     */
    void recordLoginSuccess(String username);
}