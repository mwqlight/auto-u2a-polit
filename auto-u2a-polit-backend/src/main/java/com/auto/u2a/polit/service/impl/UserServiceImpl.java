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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRealName(request.getRealName());
        user.setTenantId(request.getTenantId());

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = getUserById(id);
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("邮箱已存在");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        
        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + id));
        return UserResponse.fromEntity(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
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
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void enableUser(Long id) {
        User user = getUserById(id);
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void disableUser(Long id) {
        User user = getUserById(id);
        user.setStatus(User.UserStatus.DISABLED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        User user = getUserById(id);
        // 默认重置密码为"123456"
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void recordLoginFailure(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            
            // 如果登录失败次数超过5次，锁定账户30分钟
            if (user.getLoginAttempts() >= 5) {
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
            user.setLoginAttempts(0);
            user.setLockedUntil(null);
            user.setLastLoginAt(LocalDateTime.now());
            
            if (user.getStatus() == User.UserStatus.LOCKED) {
                user.setStatus(User.UserStatus.ACTIVE);
            }
            
            userRepository.save(user);
        }
    }
}