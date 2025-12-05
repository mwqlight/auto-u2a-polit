package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SAML认证请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "SAML认证请求")
public class SAMLRequest {
    
    @NotBlank(message = "颁发者不能为空")
    @Schema(description = "颁发者标识", example = "https://idp.example.com", required = true)
    private String issuer;
    
    @Schema(description = "目标URL", example = "https://sp.example.com/saml/acs")
    private String destination;
    
    @Schema(description = "断言消费者服务URL")
    private String assertionConsumerServiceUrl;
    
    @Schema(description = "单点登出服务URL")
    private String singleLogoutServiceUrl;
    
    @Schema(description = "名称ID格式", example = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress")
    private String nameIdFormat;
    
    @Schema(description = "是否强制认证", example = "false")
    private Boolean forceAuthn;
    
    @Schema(description = "是否被动认证", example = "false")
    private Boolean isPassive;
    
    @Schema(description = "请求ID")
    private String requestId;
    
    @Schema(description = "颁发时间")
    private String issueInstant;
    
    @Schema(description = "协议绑定", example = "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST")
    private String protocolBinding;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
    
    // 手动添加getter方法
    public String getIssuer() {
        return issuer;
    }
}