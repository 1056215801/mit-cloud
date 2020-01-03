package com.mit.community.service.com.mit.community.service.hik;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.hik.SnapVehicle;
import com.mit.community.entity.hik.Vo.SnapVehicleVo;
import com.mit.community.mapper.SnapVehicleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qsj
 * @since 2019-12-20
 */
@Service
public class SnapVehicleService extends ServiceImpl<SnapVehicleMapper, SnapVehicle>{

    public Page<SnapVehicleVo> getSnapVehicleList(String communityCodes, String startTime, String endTime, String snapshotSite, String plateNo, String vehicleColor, String plateType, String plateColor, Integer pageSize, Integer pageNum) {
        IPage<SnapVehicle> snapVehicleIPage=null;
        try {
            String[] split = communityCodes.split(",");
            SimpleDateFormat format=new SimpleDateFormat("yy-mm-dd hh:mm:ss");
            List<String> list = Arrays.asList(split);
            Page<SnapVehicle> page=new Page<>(pageSize,pageNum);
            QueryWrapper<SnapVehicle> wrapper=new QueryWrapper<>();
            if (StringUtils.isNotEmpty(startTime)) {
                Date start = format.parse(startTime);
                wrapper.ge("shoot_time",start);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                Date end = format.parse(endTime);
                wrapper.le("shoot_time",end);
            }
            if (StringUtils.isNotEmpty(plateNo)) {
                wrapper.like("plateNo",plateNo);
            }
            if (StringUtils.isNotEmpty(vehicleColor)) {
                wrapper.eq("vehicleColor",vehicleColor);
            }
            if (StringUtils.isNotEmpty(plateType)) {
                wrapper.eq("plateType",plateType);
            }
            if (StringUtils.isNotEmpty(plateColor)) {
                wrapper.eq("plateColor",plateColor);
            }
            if (StringUtils.isNotEmpty(snapshotSite)) {
                wrapper.like("snapshotSite",snapshotSite);
            }
            wrapper.in("community_code",list);
            wrapper.orderByDesc("shoot_time");
            return baseMapper.selectPageList(page, wrapper);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
