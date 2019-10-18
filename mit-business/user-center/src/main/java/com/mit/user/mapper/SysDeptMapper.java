package com.mit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.common.model.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 关联dept——relation
     * @return 数据列表
     */
    List<SysDept> listDepts();
}
