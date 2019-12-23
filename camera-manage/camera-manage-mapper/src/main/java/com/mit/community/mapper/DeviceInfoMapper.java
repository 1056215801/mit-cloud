package com.mit.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mit.community.entity.hik.DeviceInfo;
import com.mit.community.entity.hik.IntranetPenetration;
import com.mit.community.entity.hik.Vo.EquipmentStatisticsVo;
import com.mit.community.entity.hik.Vo.SnapImageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qsj
 * @since 2019-11-14
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    SnapImageVo getImageInfo(Integer id);

    IntranetPenetration getInfo(@Param("arr")String arr);

    List<EquipmentStatisticsVo> getDeviceTotal(@Param("communityCodeList") List<String> communityCodeList);
}
