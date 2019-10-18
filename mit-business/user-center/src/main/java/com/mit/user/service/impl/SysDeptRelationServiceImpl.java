package com.mit.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.model.SysDept;
import com.mit.common.model.SysDeptRelation;
import com.mit.user.mapper.SysDeptRelationMapper;
import com.mit.user.service.ISysDeptRelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门关系服务实现类
 */
@Service
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation> implements ISysDeptRelationService {

	@Resource
	private SysDeptRelationMapper sysDeptRelationMapper;

	/**
	 * 维护部门关系
	 * @param sysDept 部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveDeptRelation(SysDept sysDept) {
		//增加部门关系表
		SysDeptRelation condition = new SysDeptRelation();
		condition.setDescendant(sysDept.getParentId());
		List<SysDeptRelation> relationList = sysDeptRelationMapper
			.selectList(Wrappers.<SysDeptRelation>query().lambda()
				.eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
			.stream().map(relation -> {
				relation.setDescendant(sysDept.getId());
				return relation;
			}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(relationList)) {
			this.saveBatch(relationList);
		}

		//自己也要维护到关系表中
		SysDeptRelation own = new SysDeptRelation();
		own.setDescendant(sysDept.getId());
		own.setAncestor(sysDept.getId());
		sysDeptRelationMapper.insert(own);
	}

	/**
	 * 通过ID删除部门关系
	 *
	 * @param id
	 */
	@Override
	public void removeDeptRelationById(Long id) {
		baseMapper.deleteDeptRelationsById(id);
	}

	/**
	 * 更新部门关系
	 *
	 * @param relation
	 */
	@Override
	public void updateDeptRelation(SysDeptRelation relation) {
		baseMapper.updateDeptRelations(relation);
	}

}
