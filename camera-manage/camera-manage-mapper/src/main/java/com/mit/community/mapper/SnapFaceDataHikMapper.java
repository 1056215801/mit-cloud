package com.mit.community.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mit.community.entity.CameraInfo;
import com.mit.community.entity.hik.SnapFaceDataHik;
import com.mit.community.entity.hik.Vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface SnapFaceDataHikMapper extends BaseMapper<SnapFaceDataHik> {

    List<PerceptionVo> getSnapData(@Param("communityCodes") List<String> communityCodes);

    List<RealTimeVo> getRealTime(@Param("communityCodes") List<String> communityCodes);

    List<NumberType> getNumberType(@Param("communityCodes") List<String> communityCodes);

    List<NumberType> getWeekNumber(@Param("communityCodes") List<String> communityCodes);

    List<CameraInfo> getCameraInfo(@Param("serialNumber") String serialNumber);

    OverviewVo getInformationOverview(@Param("communityCodes") List<String> communityCodes);

    SnapImageVo getImageInfo(@Param("id")Integer id);

    Page<SnapImageVo> getSnapImageList(Page<SnapFaceDataHik> page, @Param("ew")QueryWrapper<SnapFaceDataHik> wrapper);
}
