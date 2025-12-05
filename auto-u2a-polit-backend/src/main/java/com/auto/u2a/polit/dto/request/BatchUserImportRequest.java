package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 批量用户导入请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "批量用户导入请求")
public class BatchUserImportRequest {
    
    @NotNull(message = "用户列表不能为空")
    @Schema(description = "用户列表", required = true)
    private List<UserCreateRequest> users;
    
    @NotBlank(message = "租户ID不能为空")
    @Schema(description = "租户ID", example = "tenant001", required = true)
    private String tenantId;
    
    @Schema(description = "是否覆盖已存在的用户", example = "false")
    private boolean overwriteExisting = false;
    
    @Schema(description = "是否发送欢迎邮件", example = "true")
    private boolean sendWelcomeEmail = true;
    
    @Schema(description = "导入批次号")
    private String batchNumber;
    
    @Schema(description = "导入备注")
    private String remark;
}