package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.model.SysMenu;
import com.mit.user.model.SysRoleMenu;

import java.util.List;
import java.util.Set;

/**
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {
	/**
	 * 更新角色菜单
	 * @param role
	 * @param roleId  角色
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	Boolean saveRoleMenus(String role, Long roleId, String menuIds);
}
