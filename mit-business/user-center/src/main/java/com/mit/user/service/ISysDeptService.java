package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.dto.DeptTree;
import com.mit.common.model.SysDept;

import java.util.List;

/**
 * @Description 部门管理服务接口
 */
public interface ISysDeptService extends IService<SysDept> {
    /**
     * 查询部门树菜单
     * @return 树
     */
    List<DeptTree> listDeptTrees();

    /**
     * 查询用户部门树
     * @return 树
     */
    List<DeptTree> listCurrentUserDeptTrees();

    /**
     * 添加信息部门
     * @param sysDept 部门信息
     * @return true/false
     */
    Boolean saveDept(SysDept sysDept);

    /**
     * 删除部门
     * @param id 部门 ID
     * @return 成功、失败
     */
    Boolean removeDeptById(Long id);

    /**
     * 更新部门
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    Boolean updateDept(SysDept sysDept);

}
