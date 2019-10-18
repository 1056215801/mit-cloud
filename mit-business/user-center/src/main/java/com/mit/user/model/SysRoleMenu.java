package com.mit.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色菜单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> {
	/**
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 菜单id
	 */
	private Long menuId;

}
