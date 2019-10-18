package com.mit.common.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 部门关系表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptRelation extends Model<SysDeptRelation> {
    /**
     * 祖先节点
     */
    private Long ancestor;
    /**
     * 后代节点
     */
    private Long descendant;
}
