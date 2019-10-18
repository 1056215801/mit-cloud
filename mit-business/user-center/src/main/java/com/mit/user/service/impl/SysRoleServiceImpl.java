package com.mit.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.model.SysRole;
import com.mit.user.mapper.SysRoleMapper;
import com.mit.user.mapper.SysRoleMenuMapper;
import com.mit.user.model.SysRoleMenu;
import com.mit.user.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色service
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 通过用户ID，查询角色信息
     * @param userId
     * @return
     */
    @Override
    public List listRolesByUserId(Long userId) {
        return baseMapper.listRolesByUserId(userId);
    }

    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = "menu_details", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleById(Long id) {
        sysRoleMenuMapper.delete(Wrappers
                .<SysRoleMenu>update().lambda()
                .eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }
}
