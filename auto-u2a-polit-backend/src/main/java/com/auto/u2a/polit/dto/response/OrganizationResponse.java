package com.auto.u2a.polit.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 组织响应DTO
 * 
 * @author Auto U2A Polit Team
 * @version 1.0.0
 */
@Data
@Schema(description = "组织响应信息")
public class OrganizationResponse {
    
    @Schema(description = "组织ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;
    
    @Schema(description = "组织代码", example = "dept001")
    private String code;
    
    @Schema(description = "组织名称", example = "技术研发部")
    private String name;
    
    @Schema(description = "组织描述", example = "负责公司技术研发工作")
    private String description;
    
    @Schema(description = "父组织ID", example = "null")
    private String parentId;
    
    @Schema(description = "父组织名称")
    private String parentName;
    
    @Schema(description = "组织类型", example = "DEPARTMENT")
    private String type;
    
    @Schema(description = "组织状态", example = "ACTIVE")
    private String status;
    
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;
    
    @Schema(description = "负责人ID")
    private String managerId;
    
    @Schema(description = "负责人姓名")
    private String managerName;
    
    @Schema(description = "联系电话")
    private String contactPhone;
    
    @Schema(description = "联系邮箱")
    private String contactEmail;
    
    @Schema(description = "组织地址")
    private String address;
    
    @Schema(description = "组织路径", example = "/dept001")
    private String path;
    
    @Schema(description = "层级深度", example = "1")
    private Integer level;
    
    @Schema(description = "子组织数量", example = "5")
    private Integer childrenCount;
    
    @Schema(description = "用户数量", example = "50")
    private Integer userCount;
    
    @Schema(description = "扩展信息")
    private Map<String, Object> metadata;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "创建者ID")
    private String createdBy;
    
    @Schema(description = "更新者ID")
    private String updatedBy;
    
    @Schema(description = "租户ID", example = "tenant001")
    private String tenantId;
    
    @Schema(description = "子组织列表")
    private List<OrganizationResponse> children;
}