package com.mit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.common.model.SysMenu;
import com.mit.user.model.SysRoleMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色菜单
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
}
