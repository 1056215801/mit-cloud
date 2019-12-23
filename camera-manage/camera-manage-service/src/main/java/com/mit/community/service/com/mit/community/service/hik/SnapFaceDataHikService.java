package com.mit.community.service.com.mit.community.service.hik;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.community.entity.CameraInfo;
import com.mit.community.entity.hik.SnapFaceDataHik;
import com.mit.community.entity.hik.Vo.*;
import com.mit.community.mapper.SnapFaceDataHikMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class SnapFaceDataHikService extends ServiceImpl<SnapFaceDataHikMapper,SnapFaceDataHik> {
    @Autowired
    private SnapFaceDataHikMapper snapFaceDataHikMapper;


    public List<PerceptionVo> getSnapData(List<String> communityCode) {
        System.out.println("");
        List<PerceptionVo> list = snapFaceDataHikMapper.getSnapData(communityCode);
        return list;
    }


    public List<RealTimeVo> getRealTime(List<String> communityCodes) {
        List<RealTimeVo> list = snapFaceDataHikMapper.getRealTime(communityCodes);
        return list;
    }

    public List<NumberType> getNumberType(List<String> communityCodes) {

        return snapFaceDataHikMapper.getNumberType(communityCodes);
    }

    public List<NumberType> getWeekNumber(List<String> communityCodes) {
        return snapFaceDataHikMapper.getWeekNumber(communityCodes);
    }

    public List<CameraInfo> getCameraInfo(String serialNumber) {
        return snapFaceDataHikMapper.getCameraInfo(serialNumber);
    }

    public OverviewVo getInformationOverview(List<String> communityCodes) {
        return snapFaceDataHikMapper.getInformationOverview(communityCodes);
    }

    public SnapImageVo getImageInfo(Integer id) {
        return snapFaceDataHikMapper.getImageInfo(id);
    }


    public IPage<SnapImageVo> getSnapImageList(String startTime, String endTime, String communityCodes, String snapshotSite, Integer pageNum, Integer pageSize, String jacketColor, String pantsColor, Integer jacket, Integer pants, Integer bag, Integer things, Integer hat, Integer mask, Integer hairstyle, Integer sex, Integer glass) {
        String[] split = communityCodes.split(",");
        List<String> communityCodeList = Arrays.asList(split);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            Date end = null;
            if (StringUtils.isNotEmpty(startTime)) {
                start = sdf.parse(startTime);
            }
            if (StringUtils.isNotEmpty(endTime)) {
                end = sdf.parse(endTime);
            }
            Page<SnapFaceDataHik> page = new Page<>(pageNum, pageSize);
            QueryWrapper<SnapFaceDataHik> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(snapshotSite)) {
                wrapper.like("snapshot_site", snapshotSite);
            }
            if (communityCodeList.size()>0) {
                wrapper.in("snap.communityCode", communityCodeList);
            }
            if (start != null) {
                wrapper.ge("shoot_time", start);
            }
            if (end != null) {
                wrapper.le("shoot_time", end);
            }
            if (StringUtils.isNotEmpty(jacketColor)) {
                wrapper.eq("jacketColor",jacketColor);
            }
            if (StringUtils.isNotEmpty(pantsColor)) {
                wrapper.eq("jacketColor",pantsColor);
            }
            if (jacket!=null) {
                wrapper.eq("jacket",jacket);
            }
            if (pants!=null) {
                wrapper.eq("pants",pants);
            }
            if (bag!=null) {
                wrapper.eq("bag",bag);
            }
            if (things!=null) {
                wrapper.eq("things",things);
            }
            if (hat!=null) {
                wrapper.eq("hat",hat);
            }
            if (mask!=null) {
                wrapper.eq("mask",mask);
            }
            if (hairstyle!=null) {
                wrapper.eq("hairstyle",hairstyle);
            }
            if (sex!=null) {
                wrapper.eq("sex",sex);
            }
            if (glass!=null) {
                wrapper.eq("glass",glass);
            }
            wrapper.orderByDesc("shoot_time");

            return baseMapper.getSnapImageList(page,wrapper);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
