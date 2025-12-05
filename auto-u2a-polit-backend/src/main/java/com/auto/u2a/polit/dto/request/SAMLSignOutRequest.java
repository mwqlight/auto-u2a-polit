package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SAML单点登出请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "SAML单点登出请求")
public class SAMLSignOutRequest {
    
    @NotBlank(message = "会话索引不能为空")
    @Schema(description = "会话索引", example = "session_123456", required = true)
    private String sessionIndex;
    
    @NotBlank(message = "名称ID不能为空")
    @Schema(description = "名称ID", example = "user@example.com", required = true)
    private String nameId;
    
    @Schema(description = "名称ID格式", example = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress")
    private String nameIdFormat;
    
    @Schema(description = "颁发者标识", example = "https://idp.example.com")
    private String issuer;
    
    @Schema(description = "目标URL", example = "https://sp.example.com/saml/slo")
    private String destination;
    
    @Schema(description = "请求ID")
    private String requestId;
    
    @Schema(description = "颁发时间")
    private String issueInstant;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
}