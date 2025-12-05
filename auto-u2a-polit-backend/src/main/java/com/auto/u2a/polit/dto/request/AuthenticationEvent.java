package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 认证事件DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "认证事件")
public class AuthenticationEvent {
    
    @NotBlank(message = "事件类型不能为空")
    @Schema(description = "事件类型", example = "LOGIN_SUCCESS", required = true)
    private String eventType;
    
    @Schema(description = "用户ID", example = "user_123456")
    private String userId;
    
    @Schema(description = "客户端ID", example = "client_123456")
    private String clientId;
    
    @Schema(description = "IP地址", example = "192.168.1.1")
    private String ipAddress;
    
    @Schema(description = "用户代理", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
    private String userAgent;
    
    @Schema(description = "认证协议", example = "OAUTH2")
    private String protocol;
    
    @Schema(description = "授权类型", example = "authorization_code")
    private String grantType;
    
    @Schema(description = "授权范围", example = "openid profile email")
    private String scope;
    
    @Schema(description = "错误代码", example = "invalid_grant")
    private String errorCode;
    
    @Schema(description = "错误描述")
    private String errorDescription;
    
    @Schema(description = "会话ID", example = "session_123456")
    private String sessionId;
    
    @Schema(description = "令牌ID", example = "token_123456")
    private String tokenId;
    
    @Schema(description = "事件时间戳", example = "2024-01-01T12:00:00Z")
    private String timestamp;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
    
    @Schema(description = "事件元数据")
    private String metadata;
}