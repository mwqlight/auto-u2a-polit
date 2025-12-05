package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * OAuth2令牌请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "OAuth2令牌请求")
public class OAuth2TokenRequest {
    
    @NotBlank(message = "授权类型不能为空")
    @Schema(description = "授权类型", example = "authorization_code", required = true)
    private String grantType;
    
    @Schema(description = "授权码", example = "authorization_code_value")
    private String code;
    
    @Schema(description = "重定向URI", example = "https://example.com/callback")
    private String redirectUri;
    
    @Schema(description = "客户端ID", example = "client123")
    private String clientId;
    
    @Schema(description = "客户端密钥", example = "client_secret")
    private String clientSecret;
    
    @Schema(description = "用户名", example = "user@example.com")
    private String username;
    
    @Schema(description = "密码", example = "password")
    private String password;
    
    @Schema(description = "刷新令牌", example = "refresh_token_value")
    private String refreshToken;
    
    @Schema(description = "代码验证器")
    private String codeVerifier;
    
    @Schema(description = "授权范围", example = "openid profile email")
    private String scope;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
}