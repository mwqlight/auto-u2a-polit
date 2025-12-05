package com.auto.u2a.polit.service;

import com.auto.u2a.polit.dto.request.*;
import com.auto.u2a.polit.dto.response.OAuth2TokenResponse;
import com.auto.u2a.polit.entity.OAuth2Client;

import java.util.Map;

/**
 * 全协议认证中心服务接口
 * 支持OAuth2.0、OpenID Connect、SAML 2.0等认证协议
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
public interface AuthenticationService {
    
    // ==================== OAuth2.0 认证协议 ====================
    
    /**
     * OAuth2.0 授权码授权流程 - 生成授权码
     */
    String generateAuthorizationCode(OAuth2AuthorizationRequest request);
    
    /**
     * OAuth2.0 授权码授权流程 - 交换访问令牌
     */
    OAuth2TokenResponse exchangeAuthorizationCode(OAuth2TokenRequest request);
    
    /**
     * OAuth2.0 客户端凭据授权流程
     */
    OAuth2TokenResponse clientCredentialsGrant(OAuth2TokenRequest request);
    
    /**
     * OAuth2.0 密码授权流程
     */
    OAuth2TokenResponse passwordGrant(OAuth2TokenRequest request);
    
    /**
     * OAuth2.0 刷新令牌流程
     */
    OAuth2TokenResponse refreshToken(OAuth2TokenRequest request);
    
    /**
     * OAuth2.0 隐式授权流程
     */
    OAuth2TokenResponse implicitGrant(OAuth2AuthorizationRequest request);
    
    /**
     * 验证访问令牌
     */
    Map<String, Object> validateAccessToken(String accessToken);
    
    /**
     * 撤销访问令牌
     */
    void revokeAccessToken(String accessToken);
    
    /**
     * 获取令牌信息
     */
    Map<String, Object> getTokenInfo(String accessToken);
    
    // ==================== OpenID Connect 协议 ====================
    
    /**
     * OpenID Connect 用户信息端点
     */
    Map<String, Object> getUserInfo(String accessToken);
    
    /**
     * OpenID Connect 发现端点
     */
    Map<String, Object> getOpenIDConfiguration(String issuer);
    
    /**
     * OpenID Connect JWKS端点
     */
    Map<String, Object> getJWKS(String issuer);
    
    // ==================== SAML 2.0 协议 ====================
    
    /**
     * SAML 2.0 生成认证请求
     */
    String generateSAMLRequest(SAMLRequest request);
    
    /**
     * SAML 2.0 处理认证响应
     */
    Map<String, Object> processSAMLResponse(SAMLResponse response);
    
    /**
     * SAML 2.0 单点登出请求
     */
    String generateSAMLSignOutRequest(SAMLSignOutRequest request);
    
    /**
     * SAML 2.0 处理单点登出响应
     */
    void processSAMLSignOutResponse(SAMLSignOutResponse response);
    
    // ==================== 客户端管理 ====================
    
    /**
     * 创建OAuth2客户端
     */
    OAuth2Client createOAuth2Client(OAuth2ClientCreateRequest request);
    
    /**
     * 更新OAuth2客户端
     */
    OAuth2Client updateOAuth2Client(String clientId, OAuth2ClientUpdateRequest request);
    
    /**
     * 获取OAuth2客户端详情
     */
    OAuth2Client getOAuth2Client(String clientId);
    
    /**
     * 删除OAuth2客户端
     */
    void deleteOAuth2Client(String clientId);
    
    /**
     * 重置客户端密钥
     */
    OAuth2Client resetClientSecret(String clientId);
    
    // ==================== 会话管理 ====================
    
    /**
     * 获取用户的所有活跃会话
     */
    Map<String, Object> getUserSessions(String userId);
    
    /**
     * 强制登出用户会话
     */
    void forceLogoutUser(String userId, String sessionId);
    
    /**
     * 强制登出客户端所有会话
     */
    void forceLogoutClient(String clientId);
    
    // ==================== 安全审计 ====================
    
    /**
     * 记录认证事件
     */
    void logAuthenticationEvent(AuthenticationEvent event);
    
    /**
     * 获取认证审计日志
     */
    Map<String, Object> getAuthenticationLogs(String userId, String clientId, String period);
}