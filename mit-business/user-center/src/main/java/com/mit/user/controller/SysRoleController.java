package com.mit.user.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.model.SysRole;
import com.mit.common.web.Result;
import com.mit.user.constant.UpmsPermissionCode;
import com.mit.user.service.ISysRoleMenuService;
import com.mit.user.service.ISysRoleService;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 通过ID查询角色信息
     * @param id ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable Long id) {
        return Result.succeed(sysRoleService.getById(id));
    }

    /**
     * 获取角色列表
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<List<SysRole>> listRoles() {
        return Result.succeed(sysRoleService.list(Wrappers.emptyWrapper()));
    }

    /**
     * 分页查询角色信息
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Result getRolePage(Page page) {
        return Result.succeed(sysRoleService.page(page, Wrappers.emptyWrapper()));
    }

    /**
     * 添加角色
     * @param sysRole 角色信息
     * @return success、false
     */
    @PostMapping
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_ROLE_ADD + "')")
    public Result save(@Valid @RequestBody SysRole sysRole) {
        return Result.succeed(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     * @param sysRole 角色信息
     * @return success/false
     */
    @PutMapping
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_ROLE_EDIT + "')")
    public Result update(@Valid @RequestBody SysRole sysRole) {
        if (null == sysRole.getId()){
            return Result.failed("角色ID不能为空");
        }
        return Result.succeed(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_ROLE_DEL + "')")
    public Result removeById(@PathVariable Long id) {
        return Result.succeed(sysRoleService.removeRoleById(id));
    }

    /**
     * 更新角色菜单
     * @param roleId  角色ID
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return success、false
     */
    @PutMapping("/menu")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_ROLE_AUTH + "')")
    public Result saveRoleMenus(@RequestParam Long roleId, @RequestParam(value = "menuIds", required = false) String menuIds) {
        SysRole sysRole = sysRoleService.getById(roleId);
        return Result.succeed(sysRoleMenuService.saveRoleMenus(sysRole.getCode(), roleId, menuIds));
    }
}
