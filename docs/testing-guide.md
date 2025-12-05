# 测试指南

## 📋 文档说明

本文档详细描述了 Auto U2A Polit 系统的测试策略、测试方法、测试用例编写规范和自动化测试实施指南，确保系统质量和稳定性。

## 🎯 测试策略

### 测试金字塔模型

```
        /
       / E2E测试 (10%)       - 用户场景验证
      /________________
     /                /\
    / 集成测试 (20%)   /  - 模块间集成验证
   /________________/__
  /                    /\
 /   单元测试 (70%)     /  - 单个组件验证
/____________________/____
```

### 测试覆盖率目标

| 测试类型 | 覆盖率目标 | 关键指标 |
|---------|-----------|----------|
| 单元测试 | ≥70% | 核心业务逻辑100%覆盖 |
| 集成测试 | ≥40% | 关键接口100%覆盖 |
| E2E测试 | 核心路径 | 主要用户流程100%覆盖 |

## 🔧 测试工具链

### 后端测试工具

| 工具 | 用途 | 版本 |
|------|------|------|
| JUnit 5 | 单元测试框架 | 5.8.x |
| Mockito | Mock框架 | 4.x |
| Testcontainers | 集成测试容器 | 1.17.x |
| Spring Boot Test | Spring测试支持 | 3.0.x |
| Jacoco | 代码覆盖率 | 0.8.x |

### 前端测试工具

| 工具 | 用途 | 版本 |
|------|------|------|
| Vitest | 单元测试框架 | 0.25.x |
| Vue Test Utils | Vue组件测试 | 2.x |
| Cypress | E2E测试 | 10.x |
| Testing Library | 组件测试工具 | 6.x |

## 📝 测试用例编写规范

### 后端测试用例规范

#### 单元测试示例
```java
package com.auto.u2a.polit.service;

import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.repository.UserRepository;
import com.auto.u2a.polit.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户服务测试")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setEmail("test@example.com");
        testUser.setStatus(true);
    }
    
    @Test
    @DisplayName("根据用户名查询用户 - 用户存在")
    void findByUsername_WhenUserExists_ShouldReturnUser() {
        // 给定
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));
        
        // 当
        Optional<User> result = userService.findByUsername("testuser");
        
        // 那么
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    @DisplayName("根据用户名查询用户 - 用户不存在")
    void findByUsername_WhenUserNotExists_ShouldReturnEmpty() {
        // 给定
        when(userRepository.findByUsername("nonexistent"))
            .thenReturn(Optional.empty());
        
        // 当
        Optional<User> result = userService.findByUsername("nonexistent");
        
        // 那么
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
    
    @Test
    @DisplayName("创建用户 - 用户名已存在")
    void createUser_WhenUsernameExists_ShouldThrowException() {
        // 给定
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");
        
        when(userRepository.existsByUsername("testuser"))
            .thenReturn(true);
        
        // 当 & 那么
        assertThrows(BusinessException.class, () -> {
            userService.createUser(request);
        });
        
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("创建用户 - 成功创建")
    void createUser_WhenValidRequest_ShouldCreateUser() {
        // 给定
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setEmail("new@example.com");
        
        when(userRepository.existsByUsername("newuser"))
            .thenReturn(false);
        when(passwordEncoder.encode("password123"))
            .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
            .thenReturn(testUser);
        
        // 当
        Long userId = userService.createUser(request);
        
        // 那么
        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
```

#### 集成测试示例
```java
package com.auto.u2a.polit.integration;

import com.auto.u2a.polit.entity.User;
import com.auto.u2a.polit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户仓库集成测试
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    void saveUser_ShouldPersistUser() {
        // 给定
        User user = new User();
        user.setUsername("integrationuser");
        user.setPassword("password");
        user.setEmail("integration@example.com");
        user.setStatus(true);
        
        // 当
        User savedUser = userRepository.save(user);
        
        // 那么
        assertNotNull(savedUser.getId());
        assertEquals("integrationuser", savedUser.getUsername());
        
        // 验证数据库查询
        Optional<User> foundUser = userRepository.findByUsername("integrationuser");
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }
}
```

### 前端测试用例规范

#### 组件单元测试示例
```typescript
// tests/unit/UserTable.spec.ts
import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import UserTable from '@/components/business/UserTable.vue'
import { ElMessage } from 'element-plus'

// Mock API
vi.mock('@/api/user', () => ({
  getUserList: vi.fn()
}))

// Mock Element Plus 组件
vi.mock('element-plus', () => ({
  ElMessage: {
    error: vi.fn()
  }
}))

describe('UserTable', () => {
  let wrapper: any
  
  const mockUserData = [
    {
      id: 1,
      username: 'testuser',
      email: 'test@example.com',
      status: true
    }
  ]
  
  beforeEach(() => {
    vi.clearAllMocks()
  })
  
  it('应该正确渲染用户表格', async () => {
    // 准备
    const mockGetUserList = vi.fn().mockResolvedValue({
      data: {
        items: mockUserData,
        total: 1
      }
    })
    
    vi.mocked(userApi.getUserList).mockImplementation(mockGetUserList)
    
    // 执行
    wrapper = mount(UserTable)
    
    // 等待异步操作完成
    await wrapper.vm.$nextTick()
    
    // 断言
    expect(wrapper.find('.user-table').exists()).toBe(true)
    expect(wrapper.find('el-table').exists()).toBe(true)
    expect(mockGetUserList).toHaveBeenCalledTimes(1)
  })
  
  it('搜索功能应该正常工作', async () => {
    // 准备
    const mockGetUserList = vi.fn().mockResolvedValue({
      data: {
        items: mockUserData,
        total: 1
      }
    })
    
    vi.mocked(userApi.getUserList).mockImplementation(mockGetUserList)
    
    wrapper = mount(UserTable)
    await wrapper.vm.$nextTick()
    
    // 执行搜索
    const searchInput = wrapper.find('el-input')
    await searchInput.setValue('test')
    
    const searchButton = wrapper.find('el-button')
    await searchButton.trigger('click')
    
    // 断言
    expect(mockGetUserList).toHaveBeenCalledWith({
      username: 'test',
      page: 1,
      size: 10
    })
  })
  
  it('API调用失败时应该显示错误消息', async () => {
    // 准备
    const mockGetUserList = vi.fn().mockRejectedValue(new Error('API Error'))
    vi.mocked(userApi.getUserList).mockImplementation(mockGetUserList)
    
    // 执行
    wrapper = mount(UserTable)
    await wrapper.vm.$nextTick()
    
    // 断言
    expect(ElMessage.error).toHaveBeenCalledWith('获取用户列表失败')
  })
})
```

#### E2E测试示例
```typescript
// tests/e2e/user-management.cy.ts
describe('用户管理', () => {
  beforeEach(() => {
    // 登录
    cy.visit('/login')
    cy.get('[data-testid=username]').type('admin')
    cy.get('[data-testid=password]').type('admin123')
    cy.get('[data-testid=login-btn]').click()
    
    // 导航到用户管理页面
    cy.get('[data-testid=system-menu]').click()
    cy.get('[data-testid=user-management]').click()
  })
  
  it('应该能够查看用户列表', () => {
    // 断言表格存在
    cy.get('[data-testid=user-table]').should('exist')
    
    // 断言表格有数据
    cy.get('[data-testid=user-table]').find('tr').should('have.length.at.least', 2)
  })
  
  it('应该能够搜索用户', () => {
    // 输入搜索条件
    cy.get('[data-testid=search-input]').type('testuser')
    cy.get('[data-testid=search-btn]').click()
    
    // 断言搜索结果
    cy.get('[data-testid=user-table]').should('contain', 'testuser')
  })
  
  it('应该能够创建新用户', () => {
    // 点击创建按钮
    cy.get('[data-testid=create-user-btn]').click()
    
    // 填写表单
    cy.get('[data-testid=username-input]').type('newuser')
    cy.get('[data-testid=email-input]').type('newuser@example.com')
    cy.get('[data-testid=password-input]').type('password123')
    
    // 提交表单
    cy.get('[data-testid=submit-btn]').click()
    
    // 断言创建成功
    cy.contains('用户创建成功').should('exist')
    cy.get('[data-testid=user-table]').should('contain', 'newuser')
  })
  
  it('应该能够编辑用户', () => {
    // 点击编辑按钮
    cy.get('[data-testid=edit-btn]').first().click()
    
    // 修改邮箱
    cy.get('[data-testid=email-input]').clear().type('updated@example.com')
    
    // 提交修改
    cy.get('[data-testid=submit-btn]').click()
    
    // 断言修改成功
    cy.contains('用户信息更新成功').should('exist')
  })
})
```

## 🚀 测试执行

### 本地测试执行

#### 后端测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceImplTest

# 运行集成测试
mvn verify

# 生成覆盖率报告
mvn jacoco:report
```

#### 前端测试
```bash
# 运行单元测试
npm run test:unit

# 运行E2E测试
npm run test:e2e

# 运行所有测试
npm run test

# 生成覆盖率报告
npm run test:coverage
```

### CI/CD 测试配置

#### GitHub Actions 配置示例
```yaml
name: Test and Build

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test-backend:
    runs-on: ubuntu-latest
    
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: auto_u2a_polit
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=3
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Run backend tests
      run: |
        mvn test
        mvn jacoco:report
        
    - name: Upload coverage reports
      uses: codecov/codecov-action@v3
      with:
        file: ./target/site/jacoco/jacoco.xml
        
  test-frontend:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
        cache: 'npm'
        cache-dependency-path: auto-u2a-polit-frontend/package-lock.json
        
    - name: Install dependencies
      run: |
        cd auto-u2a-polit-frontend
        npm ci
        
    - name: Run frontend tests
      run: |
        cd auto-u2a-polit-frontend
        npm run test:unit
        npm run test:e2e
        
    - name: Upload test results
      uses: actions/upload-artifact@v3
      with:
        name: frontend-test-results
        path: auto-u2a-polit-frontend/test-results/
```

## 📊 测试报告

### 覆盖率报告配置

#### 后端 Jacoco 配置
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <excludes>
            <exclude>**/entity/*</exclude>
            <exclude>**/dto/*</exclude>
            <exclude>**/config/*</exclude>
        </excludes>
    </configuration>
</plugin>
```

#### 前端 Vitest 配置
```javascript
// vitest.config.ts
export default defineConfig({
  test: {
    coverage: {
      reporter: ['text', 'json', 'html'],
      exclude: [
        'coverage/**',
        'dist/**',
        '**/[.]**',
        'packages/*/test?(s)/**',
        '**/*.d.ts',
        '**/virtual:*',
        '**/__x00__*',
        '**/\x00*',
        'cypress/**',
        'test?(s)/**',
        'test?(-*).?(c|m)[jt]s?(x)',
        '**/*{.,-}test.?(c|m)[jt]s?(x)',
        '**/*{.,-}spec.?(c|m)[jt]s?(x)',
        '**/__tests__/**',
        '**/{karma,rollup,webpack,vite,vitest,jest,ava,babel,nyc,cypress,tsup,build}.config.*',
        '**/vitest.config.*',
        '**/vite.config.*'
      ],
      thresholds: {
        lines: 70,
        functions: 70,
        branches: 70,
        statements: 70
      }
    }
  }
})
```

## 🔍 测试最佳实践

### 测试命名规范

#### 后端测试命名
- 类名：`被测试类名 + Test`
- 方法名：`被测试方法名_测试场景_预期结果`
- 使用 `@DisplayName` 提供可读的描述

#### 前端测试命名
- 文件命名：`组件名.spec.ts` 或 `功能名.cy.ts`
- 描述块：`describe('组件名/功能名', () => {})`
- 测试用例：`it('应该...', () => {})`

### 测试数据管理

#### 测试数据工厂
```java
public class UserTestFactory {
    
    public static User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setStatus(true);
        return user;
    }
    
    public static User createUserWithId(Long id) {
        User user = createUser();
        user.setId(id);
        return user;
    }
}
```

#### 测试数据清理
```java
@AfterEach
void tearDown() {
    // 清理测试数据
    userRepository.deleteAll();
}
```

### 性能测试

#### 后端性能测试
```java
@Test
@DisplayName("批量查询用户性能测试")
void batchQueryUser_PerformanceTest() {
    // 准备大量测试数据
    List<User> users = IntStream.range(0, 1000)
        .mapToObj(i -> UserTestFactory.createUserWithId((long) i))
        .collect(Collectors.toList());
    
    userRepository.saveAll(users);
    
    // 性能测试
    long startTime = System.currentTimeMillis();
    
    List<User> result = userRepository.findAll();
    
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;
    
    // 断言性能要求
    assertTrue(duration < 1000, "查询1000条数据应在1秒内完成");
    assertEquals(1000, result.size());
}
```

## 🚨 常见问题排查

### 测试失败排查步骤

1. **检查测试环境**
   - 数据库连接是否正常
   - 测试数据是否准备完整
   - 依赖服务是否启动

2. **分析错误信息**
   - 查看详细的错误堆栈
   - 检查测试数据是否正确
   - 验证Mock对象的行为

3. **调试测试用例**
   - 使用断点调试
   - 添加详细的日志输出
   - 检查测试执行顺序

### 测试优化建议

1. **减少测试执行时间**
   - 使用内存数据库进行测试
   - 并行执行测试用例
   - 优化测试数据准备

2. **提高测试稳定性**
   - 避免测试间的依赖
   - 使用独立的测试数据
   - 添加重试机制

3. **增强测试可维护性**
   - 使用Page Object模式
   - 提取公共测试方法
   - 保持测试代码简洁

---

*最后更新：2024年1月*

**注意**：测试是保证软件质量的关键环节，请确保每个功能都有相应的测试用例覆盖。