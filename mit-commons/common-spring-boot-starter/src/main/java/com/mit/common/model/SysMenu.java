package com.mit.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 菜单权限实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class SysMenu extends SuperEntity {
	private static final long serialVersionUID = 749360940290141180L;
	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	private String name;
	/**
	 * 菜单类型，1菜单 2按钮
	 */
	@NotNull(message = "菜单类型不能为空")
	@Min(1)
	@Max(2)
	private Integer type;
	/**
	 * 前端路由URL
	 */
	private String path;
	/**
	 * 菜单权限标识
	 */
	private String permissionCode;
	/**
	 * 父菜单id
	 */
	@NotNull(message = "必须有父级菜单")
	private Long parentId;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 菜单排序值
	 */
	@NotNull(message = "排序值不能为空")
	private Integer sort;
	/**
	 * 是否隐藏，true隐藏
	 */
	private Boolean hidden;

}
