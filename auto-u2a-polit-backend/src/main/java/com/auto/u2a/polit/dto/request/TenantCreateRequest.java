package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 租户创建请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "租户创建请求")
public class TenantCreateRequest {
    
    @NotBlank(message = "租户代码不能为空")
    @Size(min = 2, max = 20, message = "租户代码长度必须在2-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "租户代码只能包含字母、数字、下划线和连字符")
    @Schema(description = "租户代码", example = "tenant001", required = true)
    private String code;
    
    @NotBlank(message = "租户名称不能为空")
    @Size(max = 100, message = "租户名称长度不能超过100个字符")
    @Schema(description = "租户名称", example = "示例租户", required = true)
    private String name;
    
    @Size(max = 500, message = "租户描述长度不能超过500个字符")
    @Schema(description = "租户描述", example = "这是一个示例租户")
    private String description;
    
    @Schema(description = "租户状态", example = "ACTIVE")
    private String status = "ACTIVE";
    
    @Schema(description = "租户配置")
    private Map<String, Object> config;
    
    @Schema(description = "联系人姓名")
    private String contactName;
    
    @Schema(description = "联系人邮箱")
    private String contactEmail;
    
    @Schema(description = "联系人电话")
    private String contactPhone;
    
    @Schema(description = "租户域名")
    private String domain;
    
    @Schema(description = "租户Logo URL")
    private String logoUrl;
    
    @Schema(description = "租户主题配置")
    private Map<String, Object> themeConfig;
}