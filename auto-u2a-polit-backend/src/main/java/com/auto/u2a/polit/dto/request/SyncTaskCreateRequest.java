package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 数据同步任务创建请求DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */

@Schema(description = "数据同步任务创建请求")
public class SyncTaskCreateRequest {
    
    @NotBlank(message = "源租户ID不能为空")
    @Schema(description = "源租户ID", example = "tenant001", required = true)
    private String sourceTenantId;
    
    @NotBlank(message = "目标租户ID不能为空")
    @Schema(description = "目标租户ID", example = "tenant002", required = true)
    private String targetTenantId;
    
    @NotBlank(message = "同步类型不能为空")
    @Schema(description = "同步类型", example = "USER_SYNC", required = true)
    private String syncType;
    
    @Schema(description = "同步配置")
    private Map<String, Object> config;
    
    // getter and setter methods
    public String getSourceTenantId() {
        return sourceTenantId;
    }
    
    public void setSourceTenantId(String sourceTenantId) {
        this.sourceTenantId = sourceTenantId;
    }
    
    public String getTargetTenantId() {
        return targetTenantId;
    }
    
    public void setTargetTenantId(String targetTenantId) {
        this.targetTenantId = targetTenantId;
    }
    
    public String getSyncType() {
        return syncType;
    }
    
    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
    
    public Map<String, Object> getConfig() {
        return config;
    }
    
    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    @Schema(description = "是否增量同步", example = "true")
    private boolean incremental = true;
    
    @Schema(description = "是否覆盖目标数据", example = "false")
    private boolean overwriteTarget = false;
    
    @Schema(description = "同步任务名称")
    private String taskName;
    
    @Schema(description = "同步任务描述")
    private String description;
    
    @Schema(description = "计划执行时间")
    private String scheduledTime;
    
    @Schema(description = "重试次数", example = "3")
    private Integer retryCount = 3;
}