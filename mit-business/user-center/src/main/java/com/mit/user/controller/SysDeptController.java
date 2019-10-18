package com.mit.user.controller;

import com.mit.common.dto.DeptTree;
import com.mit.common.model.SysDept;
import com.mit.common.web.Result;
import com.mit.log.annotation.LogAnnotation;
import com.mit.user.constant.UpmsPermissionCode;
import com.mit.user.service.ISysDeptService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @Description 部门管理
 */
@Slf4j
@RestController
@RequestMapping("/dept")
@Api(tags = "部门管理")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 通过ID查询
     * @param id ID
     * @return SysDept
     */
    @GetMapping("/{id}")
    public Result<SysDept> getById(@PathVariable Long id) {
        return Result.succeed(sysDeptService.getById(id));
    }

    /**
     * 返回树形菜单集合
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public Result<List<DeptTree>> listDeptTrees() {
        return Result.succeed(sysDeptService.listDeptTrees());
    }

    /**
     * 返回当前用户树形菜单集合
     * @return 树形菜单
     */
    @GetMapping(value = "/user-tree")
    public Result<List<DeptTree>> listCurrentUserDeptTrees() {
        return Result.succeed(sysDeptService.listCurrentUserDeptTrees());
    }

    /**
     * 添加
     * @param sysDept 实体
     * @return success/false
     */
    @PostMapping
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_DEPT_ADD + "')")
    public Result save(@Valid @RequestBody SysDept sysDept) {
        return Result.succeed(sysDeptService.saveDept(sysDept));
    }

    /**
     * 删除
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_DEPT_DEL + "')")
    public Result removeById(@PathVariable Long id) {
        return Result.succeed(sysDeptService.removeDeptById(id));
    }

    /**
     * 编辑
     * @param sysDept 实体
     * @return success/false
     */
    @PutMapping
    @PreAuthorize("hasAuthority('" + UpmsPermissionCode.SYS_DEPT_EDIT + "')")
    public Result update(@Valid @RequestBody SysDept sysDept) {
        if (null == sysDept.getId()) {
            return Result.failed("部门ID不能为空");
        }
        return Result.succeed(sysDeptService.updateDept(sysDept));
    }

}
