package com.mit.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.model.SysMenu;
import com.mit.common.web.Result;
import com.mit.user.mapper.SysMenuMapper;
import com.mit.user.mapper.SysRoleMenuMapper;
import com.mit.user.model.SysRoleMenu;
import com.mit.user.service.ISysMenuService;
import com.mit.user.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 菜单权限实现类
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

	@Resource
	private SysRoleMenuMapper sysRoleMenuMapper;

	@Override
	@Cacheable(value = "menu_details", key = "#roleId  + '_menu'")
	public List<SysMenu> getMenuByRoleId(Long roleId) {
		return baseMapper.listMenusByRoleId(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = "menu_details", allEntries = true)
	public Result removeMenuById(Long id) {
		// 查询父节点为当前节点的节点
		List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query()
				.lambda().eq(SysMenu::getParentId, id));
		if (CollUtil.isNotEmpty(menuList)) {
			return Result.failed("菜单含有下级不能删除");
		}
		sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query()
				.lambda().eq(SysRoleMenu::getMenuId, id));

		//删除当前菜单及其子菜单
		return Result.succeed(this.removeById(id));
	}

	@Override
	@CacheEvict(value = "menu_details", allEntries = true)
	public Boolean updateMenuById(SysMenu sysMenu) {
		return this.updateById(sysMenu);
	}
}
