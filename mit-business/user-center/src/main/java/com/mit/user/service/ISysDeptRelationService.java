package com.mit.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.model.SysDept;
import com.mit.common.model.SysDeptRelation;

/**
 * 部门关系服务接口
 */
public interface ISysDeptRelationService extends IService<SysDeptRelation> {

	/**
	 * 新建部门关系
	 *
	 * @param sysDept 部门
	 */
	void saveDeptRelation(SysDept sysDept);

	/**
	 * 通过ID删除部门关系
	 *
	 * @param id
	 */
	void removeDeptRelationById(Long id);

	/**
	 * 更新部门关系
	 *
	 * @param relation
	 */
	void updateDeptRelation(SysDeptRelation relation);
}
