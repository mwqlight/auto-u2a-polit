package com.auto.u2a.polit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OAuth2令牌响应DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
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
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Integer refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    // 其他getter和setter方法
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }
}