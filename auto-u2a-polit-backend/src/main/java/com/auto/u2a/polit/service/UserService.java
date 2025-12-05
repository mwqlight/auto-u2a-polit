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
    UserResponse updateUser(Long id, UserUpdateRequest request);
    
    /**
     * 根据ID获取用户
     */
    UserResponse getUserById(Long id);
    
    /**
     * 根据用户名获取用户
     */
    User getUserByUsername(String username);
    
    /**
     * 分页查询用户
     */
    Page<UserResponse> getUsers(Pageable pageable, String keyword);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);
    
    /**
     * 启用用户
     */
    void enableUser(Long id);
    
    /**
     * 禁用用户
     */
    void disableUser(Long id);
    
    /**
     * 重置密码
     */
    void resetPassword(Long id);
    
    /**
     * 记录登录失败
     */
    void recordLoginFailure(String username);
    
    /**
     * 记录登录成功
     */
    void recordLoginSuccess(String username);
}