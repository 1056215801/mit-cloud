package com.mit.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.model.SysRole;
import com.mit.user.mapper.SysUserRoleMapper;
import com.mit.user.model.SysUserRole;
import com.mit.user.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
 	@Resource
	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	public int deleteUserRole(Long userId, Long roleId) {
		return sysUserRoleMapper.deleteUserRole(userId, roleId);
	}

	@Override
	public int saveUserRoles(Long userId, Long roleId) {
		return sysUserRoleMapper.saveUserRoles(userId, roleId);
	}

	@Override
	public List<SysRole> findRolesByUserId(Long userId) {
		return sysUserRoleMapper.findRolesByUserId(userId);
	}

	@Override
	public List<SysRole> findRolesByUserIds(List<Long> userIds) {
		return sysUserRoleMapper.findRolesByUserIds(userIds);
	}
}
