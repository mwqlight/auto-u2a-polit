package com.auto.u2a.polit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * OAuth2令牌响应DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "OAuth2令牌响应")
public class OAuth2TokenResponse {
    
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;
    
    @Schema(description = "过期时间（秒）", example = "3600")
    private Integer expiresIn;
    
    @Schema(description = "刷新令牌", example = "refresh_token_value")
    private String refreshToken;
    
    @Schema(description = "刷新令牌过期时间（秒）", example = "86400")
    private Integer refreshExpiresIn;
    
    @Schema(description = "授权范围", example = "openid profile email")
    private String scope;
    
    @Schema(description = "ID令牌（OpenID Connect）", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String idToken;
    
    @Schema(description = "会话状态")
    private String sessionState;
    
    @Schema(description = "错误代码")
    private String error;
    
    @Schema(description = "错误描述")
    private String errorDescription;
    
    @Schema(description = "错误URI")
    private String errorUri;
}