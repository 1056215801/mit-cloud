package com.mit.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.model.SysRole;
import com.mit.common.model.SysUser;
import com.mit.common.web.PageResult;
import com.mit.common.web.Result;
import com.mit.user.dto.UserDTO;
import com.mit.user.model.SysUserExcel;
import com.mit.user.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* 用户service接口
 */
public interface ISysUserService extends IService<SysUser> {
	/**
	 * 根据用户名获取登录对象
	 * @param	username	用户名
	 * @return	LoginAppUser
	 */
	LoginAppUser getLoginAppUser(String username);

	/**
	 * 通过SysUser 转换为 LoginAppUser，把roles和permissions也查询出来
	 * @param	sysUser	用户
	 * @return	LoginAppUser
	 */
	LoginAppUser getLoginAppUser(SysUser sysUser);

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息UserVO，包含角色
	 */
	UserVO getUserVoById(Long id);

	/**
	 * 保存用户信息
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	Boolean saveUser(UserDTO userDto);

	/**
	 * 修改用户信息
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	Boolean updateUserById(UserDTO userDto);

	/**
	 * 修改用户信息
	 * @param userDto DTO 对象
	 * @return success/fail
	 */
	Boolean updateUserByUsername(UserDTO userDto);

	SysUser getUserByUsername(String username);

	/**
	 * 分页查询用户信息（含有角色信息）
	 * @param page    分页对象
	 * @param sysUser 参数列表
	 * @return
	 */
	IPage<List<UserVO>> getUserWithRolePage(Page page, SysUser sysUser);

	/**
	 * 更新密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	Boolean updatePassword(Long id, String oldPassword, String newPassword);

	/**
	 * 状态变更
	 * @param isEnabled
	 * @return
	 */
	Boolean updateEnabled(boolean isEnabled);


}
