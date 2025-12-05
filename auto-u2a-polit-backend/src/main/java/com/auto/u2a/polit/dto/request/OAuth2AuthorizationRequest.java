package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

/**
 * OAuth2授权请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "OAuth2授权请求")
public class OAuth2AuthorizationRequest {
    
    @NotBlank(message = "客户端ID不能为空")
    @Schema(description = "客户端ID", example = "client123", required = true)
    private String clientId;
    
    @NotBlank(message = "响应类型不能为空")
    @Schema(description = "响应类型", example = "code", required = true)
    private String responseType;
    
    @Schema(description = "重定向URI", example = "https://example.com/callback")
    private String redirectUri;
    
    @Schema(description = "授权范围", example = "[\"openid\", \"profile\", \"email\"]")
    private Set<String> scope;
    
    @Schema(description = "状态参数", example = "random_state_string")
    private String state;
    
    @Schema(description = "Nonce参数", example = "random_nonce_string")
    private String nonce;
    
    @Schema(description = "代码挑战")
    private String codeChallenge;
    
    @Schema(description = "代码挑战方法", example = "S256")
    private String codeChallengeMethod;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
    
    @Schema(description = "显示参数", example = "page")
    private String display;
    
    @Schema(description = "提示参数", example = "login")
    private String prompt;
    
    @Schema(description = "最大认证年龄")
    private Integer maxAge;
    
    @Schema(description = "UI区域设置")
    private String uiLocales;
    
    // 手动添加getter方法
    public String getClientId() {
        return clientId;
    }
    
    public String getResponseType() {
        return responseType;
    }
    
    public String getRedirectUri() {
        return redirectUri;
    }
    
    public Set<String> getScope() {
        return scope;
    }
    
    public String getState() {
        return state;
    }
    
    public String getNonce() {
        return nonce;
    }
    
    public String getCodeChallenge() {
        return codeChallenge;
    }
    
    public String getCodeChallengeMethod() {
        return codeChallengeMethod;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public String getPrompt() {
        return prompt;
    }
    
    public Integer getMaxAge() {
        return maxAge;
    }
    
    public String getUiLocales() {
        return uiLocales;
    }
}