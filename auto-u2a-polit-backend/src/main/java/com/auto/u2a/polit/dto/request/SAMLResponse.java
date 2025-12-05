package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SAML认证响应DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "SAML认证响应")
public class SAMLResponse {
    
    @NotBlank(message = "SAML响应不能为空")
    @Schema(description = "SAML响应Base64编码", example = "PHNhbWxwOlJlc3BvbnNlIHhtbG5zOnNhbWxwPVwidXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sXCI+PC9zYW1scDpSZXNwb25zZT4=", required = true)
    private String response;
    
    @Schema(description = "RelayState参数")
    private String relayState;
    
    @Schema(description = "签名算法", example = "rsa-sha256")
    private String signatureAlgorithm;
    
    @Schema(description = "签名值")
    private String signature;
    
    @Schema(description = "证书")
    private String certificate;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
    
    // 手动添加getter方法
    public String getResponse() {
        return response;
    }
}