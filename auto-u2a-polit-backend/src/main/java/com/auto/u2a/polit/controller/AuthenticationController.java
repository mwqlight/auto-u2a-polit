package com.auto.u2a.polit.controller;

import com.auto.u2a.polit.dto.request.*;
import com.auto.u2a.polit.dto.response.ApiResponse;
import com.auto.u2a.polit.dto.response.OAuth2TokenResponse;
import com.auto.u2a.polit.entity.OAuth2Client;
import com.auto.u2a.polit.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 全协议认证中心控制器
 * 提供OAuth2.0、OpenID Connect、SAML 2.0等认证协议的REST接口
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证中心", description = "全协议认证中心管理接口")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    // ==================== OAuth2.0 认证端点 ====================
    
    @GetMapping("/oauth2/authorize")
    @Operation(summary = "OAuth2授权端点", description = "生成授权码或直接返回访问令牌")
    public ResponseEntity<ApiResponse<String>> authorize(
            @Parameter(description = "OAuth2授权请求参数") @Valid OAuth2AuthorizationRequest request) {
        
        log.info("OAuth2授权请求: clientId={}, responseType={}", request.getClientId(), request.getResponseType());
        
        try {
            if ("token".equals(request.getResponseType())) {
                // 隐式授权流程
                OAuth2TokenResponse tokenResponse = authenticationService.implicitGrant(request);
                return ResponseEntity.ok(ApiResponse.success("授权成功", tokenResponse.getAccessToken()));
            } else {
                // 授权码授权流程
                String authorizationCode = authenticationService.generateAuthorizationCode(request);
                return ResponseEntity.ok(ApiResponse.success("授权码生成成功", authorizationCode));
            }
        } catch (Exception e) {
            log.error("OAuth2授权失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/oauth2/token")
    @Operation(summary = "OAuth2令牌端点", description = "交换授权码或直接获取访问令牌")
    public ResponseEntity<ApiResponse<OAuth2TokenResponse>> token(
            @Parameter(description = "OAuth2令牌请求参数") @Valid @RequestBody OAuth2TokenRequest request) {
        
        log.info("OAuth2令牌请求: grantType={}, clientId={}", request.getGrantType(), request.getClientId());
        
        try {
            OAuth2TokenResponse tokenResponse;
            
            switch (request.getGrantType()) {
                case "authorization_code":
                    tokenResponse = authenticationService.exchangeAuthorizationCode(request);
                    break;
                case "client_credentials":
                    tokenResponse = authenticationService.clientCredentialsGrant(request);
                    break;
                case "password":
                    tokenResponse = authenticationService.passwordGrant(request);
                    break;
                case "refresh_token":
                    tokenResponse = authenticationService.refreshToken(request);
                    break;
                default:
                    return ResponseEntity.badRequest().body(ApiResponse.error("不支持的授权类型"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("令牌获取成功", tokenResponse));
        } catch (Exception e) {
            log.error("OAuth2令牌获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/oauth2/revoke")
    @Operation(summary = "撤销访问令牌", description = "使指定的访问令牌失效")
    public ResponseEntity<ApiResponse<Void>> revokeToken(
            @Parameter(description = "访问令牌") @RequestParam String token) {
        
        log.info("撤销访问令牌: token={}", token);
        
        try {
            authenticationService.revokeAccessToken(token);
            return ResponseEntity.ok(ApiResponse.success("令牌撤销成功"));
        } catch (Exception e) {
            log.error("令牌撤销失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/oauth2/token-info")
    @Operation(summary = "获取令牌信息", description = "验证并返回访问令牌的详细信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTokenInfo(
            @Parameter(description = "访问令牌") @RequestParam String token) {
        
        log.info("获取令牌信息: token={}", token);
        
        try {
            Map<String, Object> tokenInfo = authenticationService.getTokenInfo(token);
            return ResponseEntity.ok(ApiResponse.success("令牌信息获取成功", tokenInfo));
        } catch (Exception e) {
            log.error("令牌信息获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/oauth2/introspect")
    @Operation(summary = "令牌内省", description = "验证访问令牌的有效性")
    public ResponseEntity<ApiResponse<Map<String, Object>>> introspectToken(
            @Parameter(description = "访问令牌") @RequestParam String token) {
        
        log.info("令牌内省: token={}", token);
        
        try {
            Map<String, Object> introspectionResult = authenticationService.validateAccessToken(token);
            return ResponseEntity.ok(ApiResponse.success("令牌内省成功", introspectionResult));
        } catch (Exception e) {
            log.error("令牌内省失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== OpenID Connect 端点 ====================
    
    @GetMapping("/oauth2/userinfo")
    @Operation(summary = "用户信息端点", description = "获取当前认证用户的详细信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserInfo(
            @Parameter(description = "访问令牌") @RequestHeader("Authorization") String authorization) {
        
        log.info("获取用户信息");
        
        try {
            String accessToken = extractAccessToken(authorization);
            Map<String, Object> userInfo = authenticationService.getUserInfo(accessToken);
            return ResponseEntity.ok(ApiResponse.success("用户信息获取成功", userInfo));
        } catch (Exception e) {
            log.error("用户信息获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/.well-known/openid-configuration")
    @Operation(summary = "OpenID配置发现", description = "返回OpenID Connect配置信息")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOpenIDConfiguration(
            @Parameter(description = "颁发者标识") @RequestParam(required = false) String issuer) {
        
        log.info("获取OpenID配置: issuer={}", issuer);
        
        try {
            Map<String, Object> config = authenticationService.getOpenIDConfiguration(
                    issuer != null ? issuer : "https://example.com"
            );
            return ResponseEntity.ok(ApiResponse.success("OpenID配置获取成功", config));
        } catch (Exception e) {
            log.error("OpenID配置获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/oauth2/jwks")
    @Operation(summary = "JWKS端点", description = "返回JSON Web Key Set")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJWKS(
            @Parameter(description = "颁发者标识") @RequestParam(required = false) String issuer) {
        
        log.info("获取JWKS: issuer={}", issuer);
        
        try {
            Map<String, Object> jwks = authenticationService.getJWKS(
                    issuer != null ? issuer : "https://example.com"
            );
            return ResponseEntity.ok(ApiResponse.success("JWKS获取成功", jwks));
        } catch (Exception e) {
            log.error("JWKS获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== SAML 2.0 端点 ====================
    
    @GetMapping("/saml/auth")
    @Operation(summary = "SAML认证请求", description = "生成SAML认证请求")
    public ResponseEntity<ApiResponse<String>> generateSAMLRequest(
            @Parameter(description = "SAML请求参数") @Valid SAMLRequest request) {
        
        log.info("生成SAML认证请求: issuer={}", request.getIssuer());
        
        try {
            String samlRequest = authenticationService.generateSAMLRequest(request);
            return ResponseEntity.ok(ApiResponse.success("SAML认证请求生成成功", samlRequest));
        } catch (Exception e) {
            log.error("SAML认证请求生成失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/saml/acs")
    @Operation(summary = "SAML断言消费服务", description = "处理SAML认证响应")
    public ResponseEntity<ApiResponse<Map<String, Object>>> processSAMLResponse(
            @Parameter(description = "SAML响应参数") @Valid @RequestBody SAMLResponse response) {
        
        log.info("处理SAML认证响应");
        
        try {
            Map<String, Object> result = authenticationService.processSAMLResponse(response);
            return ResponseEntity.ok(ApiResponse.success("SAML认证响应处理成功", result));
        } catch (Exception e) {
            log.error("SAML认证响应处理失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/saml/slo")
    @Operation(summary = "SAML单点登出请求", description = "生成SAML单点登出请求")
    public ResponseEntity<ApiResponse<String>> generateSAMLSignOutRequest(
            @Parameter(description = "SAML登出请求参数") @Valid SAMLSignOutRequest request) {
        
        log.info("生成SAML单点登出请求: sessionIndex={}", request.getSessionIndex());
        
        try {
            String signOutRequest = authenticationService.generateSAMLSignOutRequest(request);
            return ResponseEntity.ok(ApiResponse.success("SAML单点登出请求生成成功", signOutRequest));
        } catch (Exception e) {
            log.error("SAML单点登出请求生成失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/saml/slo")
    @Operation(summary = "SAML单点登出响应", description = "处理SAML单点登出响应")
    public ResponseEntity<ApiResponse<Void>> processSAMLSignOutResponse(
            @Parameter(description = "SAML登出响应参数") @Valid @RequestBody SAMLSignOutResponse response) {
        
        log.info("处理SAML单点登出响应");
        
        try {
            authenticationService.processSAMLSignOutResponse(response);
            return ResponseEntity.ok(ApiResponse.success("SAML单点登出响应处理成功"));
        } catch (Exception e) {
            log.error("SAML单点登出响应处理失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== OAuth2客户端管理 ====================
    
    @PostMapping("/clients")
    @Operation(summary = "创建OAuth2客户端", description = "创建新的OAuth2客户端应用")
    public ResponseEntity<ApiResponse<OAuth2Client>> createClient(
            @Parameter(description = "客户端创建请求") @Valid @RequestBody OAuth2ClientCreateRequest request) {
        
        log.info("创建OAuth2客户端: name={}", request.getClientName());
        
        try {
            OAuth2Client client = authenticationService.createOAuth2Client(request);
            return ResponseEntity.ok(ApiResponse.success("客户端创建成功", client));
        } catch (Exception e) {
            log.error("客户端创建失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/clients/{clientId}")
    @Operation(summary = "更新OAuth2客户端", description = "更新指定的OAuth2客户端信息")
    public ResponseEntity<ApiResponse<OAuth2Client>> updateClient(
            @Parameter(description = "客户端ID") @PathVariable String clientId,
            @Parameter(description = "客户端更新请求") @Valid @RequestBody OAuth2ClientUpdateRequest request) {
        
        log.info("更新OAuth2客户端: clientId={}", clientId);
        
        try {
            OAuth2Client client = authenticationService.updateOAuth2Client(clientId, request);
            return ResponseEntity.ok(ApiResponse.success("客户端更新成功", client));
        } catch (Exception e) {
            log.error("客户端更新失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/clients/{clientId}")
    @Operation(summary = "获取OAuth2客户端详情", description = "获取指定OAuth2客户端的详细信息")
    public ResponseEntity<ApiResponse<OAuth2Client>> getClient(
            @Parameter(description = "客户端ID") @PathVariable String clientId) {
        
        log.info("获取OAuth2客户端详情: clientId={}", clientId);
        
        try {
            OAuth2Client client = authenticationService.getOAuth2Client(clientId);
            return ResponseEntity.ok(ApiResponse.success("客户端详情获取成功", client));
        } catch (Exception e) {
            log.error("客户端详情获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/clients/{clientId}")
    @Operation(summary = "删除OAuth2客户端", description = "删除指定的OAuth2客户端")
    public ResponseEntity<ApiResponse<Void>> deleteClient(
            @Parameter(description = "客户端ID") @PathVariable String clientId) {
        
        log.info("删除OAuth2客户端: clientId={}", clientId);
        
        try {
            authenticationService.deleteOAuth2Client(clientId);
            return ResponseEntity.ok(ApiResponse.success("客户端删除成功"));
        } catch (Exception e) {
            log.error("客户端删除失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/clients/{clientId}/reset-secret")
    @Operation(summary = "重置客户端密钥", description = "重置指定OAuth2客户端的密钥")
    public ResponseEntity<ApiResponse<OAuth2Client>> resetClientSecret(
            @Parameter(description = "客户端ID") @PathVariable String clientId) {
        
        log.info("重置客户端密钥: clientId={}", clientId);
        
        try {
            OAuth2Client client = authenticationService.resetClientSecret(clientId);
            return ResponseEntity.ok(ApiResponse.success("客户端密钥重置成功", client));
        } catch (Exception e) {
            log.error("客户端密钥重置失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== 会话管理 ====================
    
    @GetMapping("/sessions/user/{userId}")
    @Operation(summary = "获取用户会话", description = "获取指定用户的所有活跃会话")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserSessions(
            @Parameter(description = "用户ID") @PathVariable String userId) {
        
        log.info("获取用户会话: userId={}", userId);
        
        try {
            Map<String, Object> sessions = authenticationService.getUserSessions(userId);
            return ResponseEntity.ok(ApiResponse.success("用户会话获取成功", sessions));
        } catch (Exception e) {
            log.error("用户会话获取失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/sessions/user/{userId}/logout")
    @Operation(summary = "强制登出用户", description = "强制登出指定用户的会话")
    public ResponseEntity<ApiResponse<Void>> forceLogoutUser(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "会话ID") @RequestParam(required = false) String sessionId) {
        
        log.info("强制登出用户: userId={}, sessionId={}", userId, sessionId);
        
        try {
            authenticationService.forceLogoutUser(userId, sessionId);
            return ResponseEntity.ok(ApiResponse.success("用户登出成功"));
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/sessions/client/{clientId}/logout")
    @Operation(summary = "强制登出客户端", description = "强制登出指定客户端的所有会话")
    public ResponseEntity<ApiResponse<Void>> forceLogoutClient(
            @Parameter(description = "客户端ID") @PathVariable String clientId) {
        
        log.info("强制登出客户端: clientId={}", clientId);
        
        try {
            authenticationService.forceLogoutClient(clientId);
            return ResponseEntity.ok(ApiResponse.success("客户端登出成功"));
        } catch (Exception e) {
            log.error("客户端登出失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // ==================== 私有辅助方法 ====================
    
    private String extractAccessToken(String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        throw new RuntimeException("无效的Authorization头");
    }
}