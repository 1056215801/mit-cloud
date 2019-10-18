package com.mit.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRole extends SuperEntity {
    private static final long serialVersionUID = 4497149010220586111L;
    /**
     * 角色标识
     */
    @NotBlank(message = "角色标识不能为空")
    private String code;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /**
     * 描述
     */
    private String remark;
}
