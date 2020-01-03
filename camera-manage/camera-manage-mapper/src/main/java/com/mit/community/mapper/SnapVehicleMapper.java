package com.mit.community.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.community.entity.hik.SnapFaceDataHik;
import com.mit.community.entity.hik.SnapVehicle;
import com.mit.community.entity.hik.Vo.SnapVehicleVo;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qsj
 * @since 2019-12-20
 */
public interface SnapVehicleMapper extends BaseMapper<SnapVehicle> {

    Page<SnapVehicleVo> selectPageList(Page<SnapVehicle> page, @Param("ew")QueryWrapper<SnapVehicle> wrapper);
}
