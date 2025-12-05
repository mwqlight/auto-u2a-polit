package com.auto.u2a.polit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

/**
 * 组织更新请求DTO
 */
@Data
@Schema(description = "组织更新请求")
public class OrganizationUpdateRequest {
    @Size(min = 2, max = 20, message = "组织代码长度必须在2-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "组织代码只能包含字母、数字、下划线和连字符")
    @Schema(description = "组织代码", example = "dept001")
    private String code;

    @Size(max = 100, message = "组织名称长度不能超过100个字符")
    @Schema(description = "组织名称", example = "技术研发部")
    private String name;

    @Size(max = 500, message = "组织描述长度不能超过500个字符")
    @Schema(description = "组织描述", example = "负责公司技术研发工作")
    private String description;

    @Schema(description = "父组织ID", example = "null")
    private String parentId;

    @Schema(description = "组织类型", example = "DEPARTMENT")
    private String type;

    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Schema(description = "负责人ID")
    private String managerId;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    private String contactEmail;

    @Schema(description = "组织地址")
    private String address;

    @Schema(description = "扩展信息")
    private Map<String, Object> metadata;

    @Schema(description = "组织状态", example = "ACTIVE")
    private String status;
}
