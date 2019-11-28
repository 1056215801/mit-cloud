package com.mit.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.model.SuperEntity;
import com.mit.common.model.SysMenu;
import com.mit.common.model.SysRole;
import com.mit.common.model.SysUser;
import com.mit.user.dto.UserDTO;
import com.mit.user.mapper.SysUserMapper;
import com.mit.user.model.SysUserRole;
import com.mit.user.service.ISysDeptService;
import com.mit.user.service.ISysMenuService;
import com.mit.user.service.ISysRoleService;
import com.mit.user.service.ISysUserRoleService;
import com.mit.user.service.ISysUserService;
import com.mit.user.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Override
    public LoginAppUser getLoginAppUser(String username) {
        SysUser condition = new SysUser();
        condition.setUsername(username);
        SysUser sysUser = baseMapper.selectOne(new QueryWrapper<>(condition));
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser getLoginAppUser(SysUser sysUser) {
        if (sysUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(sysUser, loginAppUser);

            List<SysRole> sysRoles = sysRoleService.listRolesByUserId(sysUser.getId());
            // 设置角色
            loginAppUser.setSysRoles(new HashSet<>(sysRoles));

            // 设置权限列表
            Set<String> permissions = new HashSet<>();
            if (!CollectionUtils.isEmpty(sysRoles)) {
                Set<Long> roleIds = sysRoles.parallelStream().map(SuperEntity::getId).collect(Collectors.toSet());
                roleIds.forEach(roleId -> {
                    List<String> permissionList = sysMenuService.getMenuByRoleId(roleId)
                            .stream()
                            .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermissionCode()))
                            .map(SysMenu::getPermissionCode)
                            .collect(Collectors.toList());
                    permissions.addAll(permissionList);
                });
                loginAppUser.setPermissions(permissions);
            }
            return loginAppUser;
        }
        return null;
    }

    @Override
    public UserVO getUserVoById(Long id) {
        SysUser sysUser = baseMapper.selectById(id);
        if (null == sysUser) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(sysUser, userVO);
        // 设置角色
        List<SysRole> sysRoles = sysRoleService.listRolesByUserId(sysUser.getId());
        userVO.setRoleList(sysRoles);
        if (null != sysUser.getDeptId()) {
            userVO.setDeptName(sysDeptService.getById(sysUser.getDeptId()).getName());
        }
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.insert(sysUser);
        if (CollectionUtils.isEmpty(userDto.getRole())) {
            return true;
        }
        List<SysUserRole> userRoleList = userDto.getRole()
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserById(UserDTO userDto) {
        SysUser sysUser = this.getById(userDto.getId());
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.updateById(sysUser);
        if (CollectionUtils.isEmpty(userDto.getRole())) {
            return true;
        }
        List<SysUserRole> userRoleList = userDto.getRole()
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.updateBatchById(userRoleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserByUsername(UserDTO userDto) {
        SysUser sysUser = this.getUserByUsername(userDto.getUsername());
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.updateById(sysUser);
        if (CollectionUtils.isEmpty(userDto.getRole())) {
            return true;
        }
        List<SysUserRole> userRoleList = userDto.getRole()
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.updateBatchById(userRoleList);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 分页查询用户信息（含有角色信息）
     * @param page    分页对象
     * @param sysUser 参数列表
     * @return
     */
    @Override
    public IPage<List<UserVO>> getUserWithRolePage(Page page, SysUser sysUser) {
        //TODO
        //List userList = baseMapper.selectPage(page, new QueryWrapper<>(sysUser)).getRecords();
        //return baseMapper.getUserVosPage(page, userDTO);
        return null;
    }

    @Transactional
    @Override
    public Boolean updatePassword(Long id, String oldPassword, String newPassword) {
        /*SysUser sysUser = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(oldPassword)) {
            if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
                return Result.failed("旧密码错误");
            }
        }
        if (StrUtil.isBlank(newPassword)) {
            newPassword = CommonConstant.DEF_USER_PASSWORD;
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
        return Result.succeed("修改成功");*/
        return true;
    }

    /*@Override
    public PageResult<SysUser> findUsers(Map<String, Object> params) {
        Page<SysUser> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<SysUser> list = baseMapper.findList(page, params);
        long total = page.getTotal();
        if (total > 0) {
            List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

            List<SysRole> sysRoles = sysUserRoleService.findRolesByUserIds(userIds);
            list.forEach(u -> u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getId()))
                    .collect(Collectors.toList())));
        }
        return PageResult.<SysUser>builder().data(list).code(0).count(total).build();
    }*/


    @Override
    public Boolean updateEnabled(boolean isEnabled) {
        return true;
        /*Long id = MapUtils.getLong(params, "id");
        Integer status = MapUtils.getInteger(params, "status");

        SysUser appUser = baseMapper.selectById(id);
        if (appUser == null) {
            return Result.failed("用户不存在");
        }
        appUser.setStatus(status);
        appUser.setUpdateTime(new Date());

        int i = baseMapper.updateById(appUser);
        log.info("修改用户：{}", appUser);

        return i > 0 ? Result.succeed(appUser, "更新成功") : Result.failed("更新失败");*/
    }

}