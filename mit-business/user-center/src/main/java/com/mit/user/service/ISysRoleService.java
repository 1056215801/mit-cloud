package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.model.SysRole;

import java.util.List;

/**
*
 */
public interface ISysRoleService extends IService<SysRole> {
	/**
	 * 通过用户ID，查询角色信息
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Long userId);

	/**
	 * 通过角色ID，删除角色
	 * @param id
	 * @return
	 */
	Boolean removeRoleById(Long id);
}
