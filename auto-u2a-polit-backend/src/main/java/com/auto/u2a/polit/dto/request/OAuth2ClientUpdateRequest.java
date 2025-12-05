package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * OAuth2客户端更新请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "OAuth2客户端更新请求")
public class OAuth2ClientUpdateRequest {
    
    @Size(max = 100, message = "客户端名称长度不能超过100个字符")
    @Schema(description = "客户端名称", example = "更新后的应用名称")
    private String clientName;
    
    @Size(max = 500, message = "客户端描述长度不能超过500个字符")
    @Schema(description = "客户端描述", example = "更新后的应用描述")
    private String description;
    
    @Schema(description = "重定向URI列表", example = "[\"https://example.com/callback\"]")
    private Set<String> redirectUris;
    
    @Schema(description = "授权类型列表", example = "[\"authorization_code\", \"client_credentials\"]")
    private Set<String> grantTypes;
    
    @Schema(description = "授权范围列表", example = "[\"openid\", \"profile\", \"email\"]")
    private Set<String> scopes;
    
    @Schema(description = "客户端状态", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"})
    private String status;
    
    @Schema(description = "访问令牌有效期（秒）", example = "3600")
    private Integer accessTokenValidity;
    
    @Schema(description = "刷新令牌有效期（秒）", example = "86400")
    private Integer refreshTokenValidity;
    
    @Schema(description = "是否自动批准", example = "false")
    private Boolean autoApprove;
    
    @Schema(description = "联系人姓名")
    private String contactName;
    
    @Schema(description = "联系人邮箱")
    private String contactEmail;
    
    @Schema(description = "联系人电话")
    private String contactPhone;
    
    @Schema(description = "客户端元数据")
    private String clientMetadata;
}