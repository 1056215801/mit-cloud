package com.mit.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.user.mapper.SysRoleMenuMapper;
import com.mit.user.model.SysRoleMenu;
import com.mit.user.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

	@Autowired(required = false)
	private CacheManager cacheManager;

	/**
	 * @param role
	 * @param roleId  角色
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	//@CacheEvict(value = "menu_details", key = "#roleId + '_menu'")
	public Boolean saveRoleMenus(String role, Long roleId, String menuIds) {
		this.remove(Wrappers.<SysRoleMenu>query().lambda()
				.eq(SysRoleMenu::getRoleId, roleId));

		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		List<SysRoleMenu> roleMenuList = Arrays
				.stream(menuIds.split(","))
				.map(menuId -> {
					SysRoleMenu roleMenu = new SysRoleMenu();
					roleMenu.setRoleId(roleId);
					roleMenu.setMenuId(Long.valueOf(menuId));
					return roleMenu;
				}).collect(Collectors.toList());

		//清空userinfo
		//cacheManager.getCache("user_details").clear();
		return this.saveBatch(roleMenuList);
	}
}
