package com.mit.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户角色表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_role")
public class SysUserRole extends Model<SysUserRole> {
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 角色id
	 */
	private Long roleId;
}
