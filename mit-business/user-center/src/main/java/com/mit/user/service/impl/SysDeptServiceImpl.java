package com.mit.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.dto.DeptTree;
import com.mit.common.model.SysDept;
import com.mit.common.model.SysDeptRelation;
import com.mit.common.utils.SecurityUtils;
import com.mit.common.utils.TreeUtil;
import com.mit.user.mapper.SysDeptMapper;
import com.mit.user.service.ISysDeptRelationService;
import com.mit.user.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Description 部门管理service实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

    @Autowired
    private ISysDeptRelationService sysDeptRelationService;

    /**
     * 添加信息部门
     * @param sysDept 部门
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept sysDept) {
        this.save(sysDept);
        sysDeptRelationService.saveDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    /**
     * 删除部门
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Long id) {
        //级联删除部门
        List<Long> idList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda()
                        .eq(SysDeptRelation::getAncestor, id))
                .stream()
                .map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
        //删除部门级联关系
        sysDeptRelationService.removeDeptRelationById(id);
        return Boolean.TRUE;
    }

    /**
     * 更新部门
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDept(SysDept sysDept) {
        //更新部门状态
        this.updateById(sysDept);
        //更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getId());
        sysDeptRelationService.updateDeptRelation(relation);
        return Boolean.TRUE;
    }

    /**
     * 查询全部部门树
     * @return 树
     */
    @Override
    public List<DeptTree> listDeptTrees() {
        return getDeptTree(this.list(Wrappers.emptyWrapper()), -1);
    }

    /**
     * 查询用户部门树
     * @return List<DeptTree>
     */
    @Override
    public List<DeptTree> listCurrentUserDeptTrees() {
        Long deptId = Objects.requireNonNull(SecurityUtils.getUser()).getDeptId();
        if (deptId == null) {
            return null;
        }
        List<Long> descendantIdList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda()
                        .eq(SysDeptRelation::getAncestor, deptId))
                .stream().map(SysDeptRelation::getDescendant)
                .collect(Collectors.toList());

        List<SysDept> deptList = baseMapper.selectBatchIds(descendantIdList);
        Long parentId = baseMapper.selectById(deptId).getParentId();
        return getDeptTree(deptList, parentId);
    }

    /**
     * 构建部门树
     * @param depts 部门
     * @return
     */
    private List<DeptTree> getDeptTree(List<SysDept> depts, long root) {
        List<DeptTree> treeList = depts.stream()
                .filter(dept -> !dept.getId().equals(dept.getParentId()))
                .map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());
                    return node;
                }).collect(Collectors.toList());
        return TreeUtil.buildByLoop(treeList, root);
    }
}
