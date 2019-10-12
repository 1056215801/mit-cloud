package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.model.SysRole;
import com.mit.common.web.PageResult;
import com.mit.common.web.Result;

import java.util.Map;

/**
* @author zlt
 */
public interface ISysRoleService extends IService<SysRole> {
	void saveRole(SysRole sysRole);

	void deleteRole(Long id);

	/**
	 * 角色列表
	 * @param params
	 * @return
	 */
	PageResult<SysRole> findRoles(Map<String, Object> params);

	/**
	 * 新增或更新角色
	 * @param sysRole
	 * @return Result
	 */
	Result saveOrUpdateRole(SysRole sysRole);
}
