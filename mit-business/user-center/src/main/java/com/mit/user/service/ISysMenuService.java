package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.model.SysMenu;
import com.mit.common.web.Result;

import java.util.List;

/**
 * 菜单权限
 */
public interface ISysMenuService extends IService<SysMenu> {
	/**
	 * 通过角色编号查询URL 权限
	 * @param roleId 角色ID
	 * @return 菜单列表
	 */
	List<SysMenu> getMenuByRoleId(Long roleId);

	/**
	 * 级联删除菜单
	 * @param id 菜单ID
	 * @return 成功、失败
	 */
	Result removeMenuById(Long id);

	/**
	 * 更新菜单信息
	 * @param sysMenu 菜单信息
	 * @return 成功、失败
	 */
	Boolean updateMenuById(SysMenu sysMenu);
}
