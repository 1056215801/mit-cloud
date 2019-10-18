package com.mit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.common.model.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
	/**
	 * 通过用户ID，查询角色信息
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Long userId);
}
