package com.mit.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mit.common.constant.CommonConstant;
import com.mit.common.dto.MenuTree;
import com.mit.common.model.SysMenu;
import com.mit.common.utils.SecurityUtils;
import com.mit.common.utils.TreeUtil;
import com.mit.common.web.Result;
import com.mit.user.constant.UpmsPermissionCode;
import com.mit.user.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单权限controller
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单权限管理")
public class SysMenuController {

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 返回树形菜单集合
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    @ApiOperation("获取全部树形菜单")
    public Result<List<MenuTree>> getTree() {
        return Result.succeed(TreeUtil.buildTree(sysMenuService.list(Wrappers.emptyWrapper()), -1));
    }

    /**
     * 返回当前用户的树形菜单集合
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "/user-tree")
    @ApiOperation("获取当前用户树形菜单")
    public Result<List<MenuTree>> getUserMenu() {
        // 获取符合条件的菜单
        Set<SysMenu> all = new HashSet<>();
        SecurityUtils.getRoles()
                .forEach(roleId -> all.addAll(sysMenuService.getMenuByRoleId(roleId)));
        List<MenuTree> menuTreeList = all.stream()
                .filter(menuVo -> CommonConstant.MENU.equals(menuVo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return Result.succeed(TreeUtil.buildByLoop(menuTreeList, (long) -1));
    }

    /**
     * 返回角色的菜单集合
     * @param roleId 角色ID
     * @return 角色菜单ID集合
     */
    @GetMapping("/{roleId}/menus")
    @ApiOperation("获取某角色菜单ID集合")
    public Result<List<Long>> getRoleTree(@PathVariable Long roleId) {
        return Result.succeed(
                sysMenuService.getMenuByRoleId(roleId).stream().map(SysMenu::getId)
                .collect(Collectors.toList()));
    }

    /**
     * 通过ID查询菜单的详细信息
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取菜单信息")
    public Result<SysMenu> getById(@PathVariable Long id) {
        return Result.succeed(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     * @param sysMenu 菜单信息
     * @return true/false
     */
    @PostMapping
    @ApiOperation("新增菜单")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_MENU_ADD + "')")
    public Result save(@Valid @RequestBody SysMenu sysMenu) {
        return Result.succeed(sysMenuService.save(sysMenu));
    }

    /**
     * 删除菜单
     * @param id 菜单ID
     * @return true/false
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除菜单")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_MENU_DEL + "')")
    public Result removeById(@PathVariable Long id) {
        return sysMenuService.removeMenuById(id);
    }

    /**
     * 更新菜单
     * @param sysMenu 菜单信息
     * @return true/false
     */
    @PutMapping
    @ApiOperation("修改菜单")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_MENU_EDIT + "')")
    public Result update(@Valid @RequestBody SysMenu sysMenu) {
        if (null == sysMenu.getId()) {
            return Result.failed("菜单ID不能为空");
        }
        return Result.succeed(sysMenuService.updateMenuById(sysMenu));
    }

}
