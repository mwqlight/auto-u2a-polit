package com.auto.u2a.polit.service.impl;

import com.auto.u2a.polit.dto.request.UserCreateRequest;
import com.auto.u2a.polit.dto.request.UserUpdateRequest;
import com.auto.u2a.polit.dto.response.UserResponse;
import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.repository.UserRepository;
import com.auto.u2a.polit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 处理多租户认证，用户名格式为"用户名:租户ID"
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("用户名格式不正确: " + username);
        }
        String actualUsername = parts[0];
        String tenantId = parts[1];
        
        return getUserByUsernameAndTenantId(actualUsername, tenantId);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 检查用户名是否已存在（多租户）
        if (userRepository.existsByTenantIdAndUsername(request.getTenantId(), request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在（多租户）
        if (userRepository.existsByTenantIdAndEmail(request.getTenantId(), request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 检查手机号是否已存在（多租户）
        if (request.getPhone() != null && userRepository.existsByTenantIdAndPhone(request.getTenantId(), request.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDisplayName(request.getDisplayName());
        user.setTenantId(request.getTenantId());
        user.setStatus(User.UserStatus.ACTIVE);
        user.setType(User.UserType.INTERNAL);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPasswordChangedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        // 获取当前租户ID（这里假设从上下文获取，实际实现需要根据项目的租户上下文机制调整）
        String currentTenantId = getCurrentTenantId();
        
        // 根据ID和租户ID查询用户，确保只能更新当前租户内的用户
        User user = userRepository.findByIdAndTenantId(id, currentTenantId)
                .orElseThrow(() -> new RuntimeException("用户不存在或不属于当前租户"));
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByTenantIdAndEmail(currentTenantId, request.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByTenantIdAndPhone(currentTenantId, request.getPhone())) {
                throw new RuntimeException("手机号已存在");
            }
            user.setPhone(request.getPhone());
        }
        
        if (request.getDisplayName() != null) {
            user.setDisplayName(request.getDisplayName());
        }
        
        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        // 获取当前租户ID（这里假设从上下文获取，实际实现需要根据项目的租户上下文机制调整）
        String currentTenantId = getCurrentTenantId();
        
        return userRepository.findByIdAndTenantId(id, currentTenantId)
                .orElseThrow(() -> new RuntimeException("用户不存在或不属于当前租户"));
    }
    
    /**
     * 获取当前租户ID
     * 这里需要根据项目的实际租户上下文机制实现
     * 例如：从ThreadLocal、JWT token、请求头或Spring Security上下文获取
     */
    private String getCurrentTenantId() {
        // 临时实现，实际项目中需要替换为正确的租户ID获取逻辑
        // 例如：return TenantContextHolder.getTenantId();
        return "default-tenant"; // 默认租户ID，实际项目中需要动态获取
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByUsernameAndTenantId(String username, String tenantId) {
        return userRepository.findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username + " (租户: " + tenantId + ")"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable, String keyword) {
        Page<User> userPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            userPage = userRepository.findByKeyword(keyword.trim(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }
        
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
        
        return new PageImpl<>(userResponses, pageable, userPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void enableUser(UUID id) {
        User user = getUserById(id);
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(UUID id) {
        User user = getUserById(id);
        user.setStatus(User.UserStatus.DISABLED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(UUID id) {
        User user = getUserById(id);
        // 默认重置密码为"123456"
        user.setPasswordHash(passwordEncoder.encode("123456"));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void recordLoginFailure(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFailedLoginCount(user.getFailedLoginCount() + 1);
            
            // 如果登录失败次数超过5次，锁定账户30分钟
            if (user.getFailedLoginCount() >= 5) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(30));
                user.setStatus(User.UserStatus.LOCKED);
            }
            
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void recordLoginSuccess(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFailedLoginCount(0);
            user.setLockedUntil(null);
            user.setStatus(User.UserStatus.ACTIVE);
            user.setLastLoginAt(LocalDateTime.now());
            
            if (user.getStatus() == User.UserStatus.LOCKED) {
                user.setStatus(User.UserStatus.ACTIVE);
            }
            
            userRepository.save(user);
        }
    }
}