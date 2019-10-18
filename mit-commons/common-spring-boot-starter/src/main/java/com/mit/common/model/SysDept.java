package com.mit.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 组织机构
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDept extends SuperEntity {
    private static final long serialVersionUID = -5886012896705137070L;
    /**
     * 组织机构名称
     */
    @NotBlank(message = "组织机构名称不能为空")
    private String name;
    /**
     * 上级id
     */
    @NotNull(message = "上级组织机构ID不能为空")
    private Long parentId;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /**
     * 描述
     */
    private String remark;

}
