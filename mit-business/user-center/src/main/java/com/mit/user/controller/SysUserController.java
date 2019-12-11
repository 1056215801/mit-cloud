package com.mit.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.common.constant.CommonConstant;
import com.mit.common.dto.ClusterCommunity;
import com.mit.common.dto.LoginAppUser;
import com.mit.common.model.SysRole;
import com.mit.common.model.SysUser;
import com.mit.common.utils.ExcelUtil;
import com.mit.common.utils.SecurityUtils;
import com.mit.common.web.PageResult;
import com.mit.common.web.Result;
import com.mit.log.annotation.LogAnnotation;
import com.mit.user.constant.UpmsPermissionCode;
import com.mit.user.dto.UserDTO;
import com.mit.user.feign.CommunityFeign;
import com.mit.user.model.SysUserExcel;
import com.mit.user.service.ISysUserService;
import com.mit.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class SysUserController {
    private static final String ADMIN_CHANGE_MSG = "超级管理员不给予修改";

    @Autowired
    private ISysUserService sysUserService;

    @Resource
    private CommunityFeign communityFeign;

    /**
     * 查询用户实体对象SysUser
     */
    @GetMapping(value = "/name/{username}")
    @ApiOperation(value = "根据用户名查询用户实体")
    //@Cacheable(value = "user", key = "#username")
    public Result<SysUser> selectByUsername(@PathVariable String username) {
        SysUser condition = new SysUser();
        condition.setUsername(username);
        return Result.succeed(sysUserService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 查询用户登录对象LoginAppUser，权限中心调用登录
     */
    @GetMapping(value = "/users-anon/login")
    @ApiOperation(value = "根据用户名查询用户")
    @LogAnnotation(module = "user-center")
    public Result<LoginAppUser> login(@RequestParam String username) {
        return Result.succeed(sysUserService.getLoginAppUser(username));
    }

    /**
     * 获取当前登录用户信息
     * @return  Result<LoginAppUser>
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    public Result<LoginAppUser> info() {
        String username = "";
        try {
            username = Objects.requireNonNull(SecurityUtils.getUser()).getUsername();
        } catch (Exception e) {
            return Result.failed("未登录");
        }
        LoginAppUser user = sysUserService.getLoginAppUser(username);
        List<ClusterCommunity> communityList = new ArrayList<>();
        switch (user.getLevel()) {
            case 1:
                //总账号admin
                try {
                    List list = communityFeign.getCommunityList().getDatas();

                    list.stream().forEach(object -> {
                        ClusterCommunity community = JSON.parseObject(JSON.toJSONString(object), new TypeReference<ClusterCommunity>(){});
                        communityList.add(community);
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return Result.failed("调用小区服务查询小区列表异常");
                }
                break;
            case 8:
                //小区账号
                ClusterCommunity community = new ClusterCommunity(user);
                communityList.add(community);
                break;
            default:
                break;
        }
        user.setCommunityList(communityList);
        return Result.succeed(user);
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return Result<SysUser>
     */
    @GetMapping("/{id}")
    public Result<UserVO> findUserById(@PathVariable Long id) {
        return Result.succeed(sysUserService.getUserVoById(id));
    }

    /**
     * 通过用户名模糊查询
     * @param username 用户名
     * @return
     */
    @GetMapping("/filter")
    public Result<List<SysUser>> filterByUserName(@RequestParam String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        return Result.succeed(sysUserService.list(queryWrapper));
    }

    /**
     * 通过小区code查询用户
     * @param communityCode 小区code
     * @return
     */
    @GetMapping("/query/byCommunityCode")
    public Result<SysUser> findByCommunityCode(@RequestParam String communityCode) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_code", communityCode);
        return Result.succeed(sysUserService.getOne(queryWrapper));
    }

    /**
     * 新增用户
     * @param userDTO 用户对象
     */
    @PostMapping(value = "/save")
    //@PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_USER_ADD + "')")
    //@CachePut(value = "user", key = "#sysUser.username")
    public Result saveSysUser(@RequestBody UserDTO userDTO) {
        if (sysUserService.getUserByUsername(userDTO.getUsername()) != null) {
            return Result.failed("用户名重复");
        }
        return Result.succeed(sysUserService.saveUser(userDTO));
    }

    /**
     * 修改用户
     * @param userDTO 用户对象
     */
    @PutMapping
    //@PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_USER_EDIT + "')")
    //@CachePut(value = "user", key = "#sysUser.username")
    public Result updateSysUser(@RequestBody UserDTO userDTO) {
        if (null == userDTO.getId()) {
            return Result.failed("用户ID不能为空");
        }
        return Result.succeed(sysUserService.updateUserById(userDTO));
    }

    /**
     * 根据用户名修改用户，小区服务调用
     * @param userDTO 用户对象
     */
    @PutMapping(value = "/updateByUsername")
    //@PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_USER_EDIT + "')")
    //@CachePut(value = "user", key = "#sysUser.username")
    public Result updateSysUserByUsername(@RequestBody UserDTO userDTO) {
        if (null == userDTO.getUsername()) {
            return Result.failed("用户名不能为空");
        }
        return Result.succeed(sysUserService.updateUserByUsername(userDTO));
    }


    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param sysUser 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    public Result getUserPage(Page page, SysUser sysUser) {
        return Result.succeed(sysUserService.getUserWithRolePage(page, sysUser));
    }


    /**
     * 修改用户状态
     *
     * @param enabled
     * @return
     */
    @ApiOperation(value = "修改用户状态")
    @GetMapping("/updateEnabled")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean")
    })
    public Result updateEnabled(@RequestParam Long id, Boolean enabled) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        return Result.succeed(sysUserService.updateEnabled(enabled));
    }

    /**
     * 管理后台，给用户重置密码
     * @param id
     */
    @PutMapping(value = "/users/{id}/password")
    public Result resetPassword(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        sysUserService.updatePassword(id, null, null);
        return Result.succeed("重置成功");
    }

    /**
     * 用户自己修改密码
     */
    @PutMapping(value = "/users/password")
    public Result resetPassword(@RequestBody SysUser sysUser, @RequestParam String oldPassword) {
        if (checkAdmin(sysUser.getId())) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        // TODO
        sysUserService.updatePassword(sysUser.getId(), oldPassword, sysUser.getPassword());
        return Result.succeed("重置成功");
    }

    /**
     * 删除用户
     * @param id
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_USER_DEL + "')")
    public Result delete(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        sysUserService.removeById(id);
        return Result.succeed("删除成功");
    }

    /**
     * 导出excel
     *
     * @return
     */
    @PostMapping("/users/export")
    public void exportUser(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        //List<SysUserExcel> result = sysUserService.findAllUsers(params);
        //导出操作
        //ExcelUtil.exportExcel(result, null, "用户", SysUserExcel.class, "user", response);
    }

    @PostMapping(value = "/users/import")
    public Result importExcl(@RequestParam("file") MultipartFile excl) throws Exception {
        int rowNum = 0;
        if(!excl.isEmpty()) {
            List<SysUserExcel> list = ExcelUtil.importExcel(excl, 0, 1, SysUserExcel.class);
            rowNum = list.size();
            if (rowNum > 0) {
                List<SysUser> users = new ArrayList<>(rowNum);
                list.forEach(u -> {
                    SysUser user = new SysUser();
                    BeanUtil.copyProperties(u, user);
                    user.setPassword(CommonConstant.DEF_USER_PASSWORD);
                    //user.setType(UserType.BACKEND.name());
                    users.add(user);
                });
                sysUserService.saveBatch(users);
            }
        }
        return Result.succeed("导入数据成功，一共【"+rowNum+"】行");
    }

    /**
     * 是否超级管理员
     */
    private boolean checkAdmin(long id) {
        return id == 1L;
    }
}
