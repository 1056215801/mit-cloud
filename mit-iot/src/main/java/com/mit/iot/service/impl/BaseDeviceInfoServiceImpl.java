package com.mit.iot.service.impl;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.iot.dto.BaseDeviceInfoDTO;
import com.mit.iot.dto.DeviceInfoQueryDTO;
import com.mit.iot.enums.DeviceTypeEnum;
import com.mit.iot.mapper.BaseDeviceInfoMapper;
import com.mit.iot.model.BaseDeviceInfo;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IFireHydrantService;
import com.mit.iot.service.IWiFiProbeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 设备基本信息service类
 */
@Service
public class BaseDeviceInfoServiceImpl extends ServiceImpl<BaseDeviceInfoMapper, BaseDeviceInfo>
        implements IBaseDeviceInfoService {

    @Autowired
    private IWiFiProbeService wiFiProbeService;

    @Autowired
    private IFireHydrantService fireHydrantService;

    @Override
    public IPage<BaseDeviceInfo> listByCondition(DeviceInfoQueryDTO deviceInfoQueryDTO) {
        IPage<BaseDeviceInfo> page = new Page<>(deviceInfoQueryDTO.getPageNum(), deviceInfoQueryDTO.getPageSize());
        QueryWrapper<BaseDeviceInfo> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(deviceInfoQueryDTO.getCommunityCodeList())) {
            wrapper.in("community_code", deviceInfoQueryDTO.getCommunityCodeList());
        }
        if (StringUtils.isNotBlank(deviceInfoQueryDTO.getZoneId())) {
            wrapper.eq("zone_id", deviceInfoQueryDTO.getZoneId());
        }
        if (StringUtils.isNotBlank(deviceInfoQueryDTO.getDeviceName())) {
            wrapper.like("device_name", deviceInfoQueryDTO.getDeviceName());
        }
        if (StringUtils.isNotBlank(deviceInfoQueryDTO.getDeviceType())) {
            wrapper.eq("device_type", deviceInfoQueryDTO.getDeviceType());
        }
        if (StringUtils.isNotBlank(deviceInfoQueryDTO.getDeviceNo())) {
            wrapper.like("device_no", deviceInfoQueryDTO.getDeviceNo());
        }
        if (StringUtils.isNotBlank(deviceInfoQueryDTO.getDeviceSerialNo())) {
            wrapper.like("device_serial_no", deviceInfoQueryDTO.getDeviceSerialNo());
        }
        if (null != deviceInfoQueryDTO.getStatus()) {
            wrapper.eq("status", deviceInfoQueryDTO.getStatus());
        }
        wrapper.orderByDesc("create_time");
        return this.page(page, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCascadeByIds(List<Long> idList) {
        idList.forEach(id -> {
            BaseDeviceInfo baseDeviceInfo = baseMapper.selectById(id);
            DeviceTypeEnum deviceTypeEnum = EnumUtil.likeValueOf(DeviceTypeEnum.class, baseDeviceInfo.getDeviceType());
            try {
                switch (deviceTypeEnum) {
                    case WIFI:
                        wiFiProbeService.deleteByBaseInfoId(baseDeviceInfo.getId());
                        break;
                    case FIRE_HYDRANT:
                        fireHydrantService.deleteByBaseInfoId(baseDeviceInfo.getId());
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        });
        return this.removeByIds(idList);
    }

    @Override
    public BaseDeviceInfo getByCommunityCodeAndDeviceName(String communityCode, String deviceName) {
        if (StringUtils.isEmpty(communityCode) || StringUtils.isEmpty(deviceName)) {
            return null;
        }
        QueryWrapper<BaseDeviceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("community_code", communityCode);
        wrapper.eq("device_name", deviceName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public BaseDeviceInfo getByDeviceNo(String deviceNo) {
        if (StringUtils.isEmpty(deviceNo)) {
            return null;
        }
        QueryWrapper<BaseDeviceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("device_no", deviceNo);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public BaseDeviceInfo getByDeviceSerialNo(String deviceSerialNo) {
        if (StringUtils.isEmpty(deviceSerialNo)) {
            return null;
        }
        QueryWrapper<BaseDeviceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("device_serial_no", deviceSerialNo);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public int metricsDeviceNum(List<String> communityCodeList, List<String> deviceTypeList, List<Integer> status) {
        QueryWrapper<BaseDeviceInfo> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(communityCodeList)) {
            wrapper.in("community_code", communityCodeList);
        }
        if (CollectionUtils.isNotEmpty(deviceTypeList)) {
            wrapper.in("device_type", deviceTypeList.stream().map(String::toUpperCase).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(status)) {
            wrapper.in("status", status);
        }
        return this.count(wrapper);
    }

    @Override
    public void checkBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO) throws Exception {
        baseDeviceInfoDTO.setDeviceType(baseDeviceInfoDTO.getDeviceType().toUpperCase());
        String deviceType = baseDeviceInfoDTO.getDeviceType();
        if (null == EnumUtil.likeValueOf(DeviceTypeEnum.class, deviceType)) {
            throw new Exception("设备类型标识不匹配，请检查拼写错误");
        }
        if (null != this.getByCommunityCodeAndDeviceName(baseDeviceInfoDTO.getCommunityCode(),
                baseDeviceInfoDTO.getDeviceName())) {
            throw new Exception("设备名称重复");
        }
        if (StringUtils.isNotBlank(baseDeviceInfoDTO.getDeviceNo()) &&
                null != this.getByDeviceNo(baseDeviceInfoDTO.getDeviceNo())) {
            throw new Exception("设备编号重复");
        }
        if (StringUtils.isNotBlank(baseDeviceInfoDTO.getDeviceSerialNo()) &&
                null != this.getByDeviceSerialNo(baseDeviceInfoDTO.getDeviceSerialNo())) {
            throw new Exception("设备序列号重复");
        }
    }
}
