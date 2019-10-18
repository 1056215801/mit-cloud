package com.mit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.common.model.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    List<SysMenu> listMenusByRoleId(Long roleId);

    /**
     * 通过角色ID查询权限
     *
     * @param roleIds Ids
     * @return
     */
    List<String> listPermissionsByRoleIds(Set<Long> roleIds);
}
