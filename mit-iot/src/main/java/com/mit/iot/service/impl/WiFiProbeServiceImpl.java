package com.mit.iot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.utils.BeanUtils;
import com.mit.iot.dto.hkwifi.WiFiDeviceStatusDTO;
import com.mit.iot.dto.hkwifi.WiFiGeolocationDTO;
import com.mit.iot.dto.hkwifi.WiFiProbeDTO;
import com.mit.iot.enums.DeviceTypeEnum;
import com.mit.iot.mapper.WiFiProbeMapper;
import com.mit.iot.model.BaseDeviceInfo;
import com.mit.iot.model.WiFiProbe;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IWiFiProbeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description WiFi探针设备service类
 */
@Service
public class WiFiProbeServiceImpl extends ServiceImpl<WiFiProbeMapper, WiFiProbe> implements IWiFiProbeService {

    @Value("${netty.server.wifi.autoSave:true}")
    private boolean isAutoSave;

    @Autowired
    private IBaseDeviceInfoService baseDeviceInfoService;

    @Override
    public WiFiProbe getByBaseInfoId(Long baseDeviceInfoId) {
        QueryWrapper<WiFiProbe> wrapper = new QueryWrapper<>();
        wrapper.eq("base_device_info_id", baseDeviceInfoId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public WiFiProbe getByIndexCode(String indexCode) {
        QueryWrapper<WiFiProbe> wrapper = new QueryWrapper<>();
        wrapper.eq("index_code", indexCode);
        return baseMapper.selectOne(wrapper);
    }

    /**
     * 处理上报的状态信息
     * @param list 设备状态信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processUpDeviceStatusStream(List<WiFiDeviceStatusDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(wiFiDeviceStatusDTO -> {
            WiFiProbe wiFiProbe = this.getByIndexCode(wiFiDeviceStatusDTO.getIndexCode());
            if (wiFiProbe == null) {
                // 是否自动保存
                if (isAutoSave) {
                    autoSaveWithUpDeviceStatus(wiFiDeviceStatusDTO);
                }
                return;
            }
            // 存在则更新信息
            BaseDeviceInfo dbBaseDeviceInfo = baseDeviceInfoService.getById(wiFiProbe.getBaseDeviceInfoId());
            dbBaseDeviceInfo.setStatus(wiFiDeviceStatusDTO.getStatus());
            dbBaseDeviceInfo.setAcquisitionTime(wiFiDeviceStatusDTO.getAcquisitionTime());
            dbBaseDeviceInfo.setUpdateTime(new Date());
            baseDeviceInfoService.updateById(dbBaseDeviceInfo);
        });
    }

    /**
     * 根据上报的设备状态信息自动保存
     * @param wiFiDeviceStatusDTO 设备状态信息
     */
    private void autoSaveWithUpDeviceStatus(WiFiDeviceStatusDTO wiFiDeviceStatusDTO) {
        BaseDeviceInfo baseDeviceInfo = new BaseDeviceInfo();
        baseDeviceInfo.setDeviceName(UUID.randomUUID().toString());
        baseDeviceInfo.setDeviceType(DeviceTypeEnum.WIFI.getValue());
        baseDeviceInfo.setStatus(wiFiDeviceStatusDTO.getStatus());
        baseDeviceInfo.setAcquisitionTime(wiFiDeviceStatusDTO.getAcquisitionTime());
        baseDeviceInfoService.save(baseDeviceInfo);

        WiFiProbe wiFiProbe = new WiFiProbe();
        wiFiProbe.setBaseDeviceInfoId(baseDeviceInfo.getId());
        wiFiProbe.setIndexCode(wiFiDeviceStatusDTO.getIndexCode());
        wiFiProbe.setMac(wiFiDeviceStatusDTO.getMac());
        baseMapper.insert(wiFiProbe);
    }

    /**
     * 处理上报的地理位置信息
     * @param list 设备地理位置信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processUpGeolocationStream(List<WiFiGeolocationDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(wiFiGeolocationDTO -> {
            WiFiProbe wiFiProbe = this.getByIndexCode(wiFiGeolocationDTO.getIndexCode());
            if (wiFiProbe == null) {
                if (isAutoSave) {
                    autoSaveWithUpGeolocation(wiFiGeolocationDTO);
                }
                return;
            }
            // 存在则更新信息
            BaseDeviceInfo dbBaseDeviceInfo = baseDeviceInfoService.getById(wiFiProbe.getBaseDeviceInfoId());
            dbBaseDeviceInfo.setLongitude(wiFiGeolocationDTO.getLongitude());
            dbBaseDeviceInfo.setLatitude(wiFiGeolocationDTO.getLatitude());
            dbBaseDeviceInfo.setAcquisitionTime(wiFiGeolocationDTO.getAcquisitionTime());
            dbBaseDeviceInfo.setUpdateTime(new Date());
            baseDeviceInfoService.updateById(dbBaseDeviceInfo);
        });
    }

    /**
     * 根据上报的设备地理位置信息自动保存
     * @param wiFiGeolocationDTO 设备地理位置信息
     */
    private void autoSaveWithUpGeolocation(WiFiGeolocationDTO wiFiGeolocationDTO) {
        BaseDeviceInfo baseDeviceInfo = new BaseDeviceInfo();
        baseDeviceInfo.setDeviceName(UUID.randomUUID().toString());
        baseDeviceInfo.setDeviceType(DeviceTypeEnum.WIFI.getValue());
        baseDeviceInfo.setLongitude(wiFiGeolocationDTO.getLongitude());
        baseDeviceInfo.setLatitude(wiFiGeolocationDTO.getLatitude());
        baseDeviceInfo.setAcquisitionTime(wiFiGeolocationDTO.getAcquisitionTime());
        baseDeviceInfoService.save(baseDeviceInfo);

        WiFiProbe wiFiProbe = new WiFiProbe();
        wiFiProbe.setBaseDeviceInfoId(baseDeviceInfo.getId());
        wiFiProbe.setIndexCode(wiFiGeolocationDTO.getIndexCode());
        baseMapper.insert(wiFiProbe);
    }

    /**
     * 新增设备
     * @param wiFiProbeDTO 设备信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithBaseInfo(WiFiProbeDTO wiFiProbeDTO) {
        BaseDeviceInfo baseDeviceInfo = new BaseDeviceInfo();
        BeanUtil.copyProperties(wiFiProbeDTO, baseDeviceInfo);
        baseDeviceInfoService.save(baseDeviceInfo);

        long baseInfoId = baseDeviceInfo.getId();
        WiFiProbe wiFiProbe = new WiFiProbe(wiFiProbeDTO);
        wiFiProbe.setBaseDeviceInfoId(baseInfoId);
        baseMapper.insert(wiFiProbe);
        return true;
    }

    /**
     * 修改设备信息
     * @param wiFiProbeDTO 设备信息
     * @throws Exception 要修改的信息不存在时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithBaseInfo(WiFiProbeDTO wiFiProbeDTO) throws Exception {
        BaseDeviceInfo dbBaseDeviceInfo = baseDeviceInfoService.getById(wiFiProbeDTO.getId());
        BeanUtil.copyProperties(wiFiProbeDTO, dbBaseDeviceInfo, BeanUtils.getNullPropertyNames(wiFiProbeDTO));
        dbBaseDeviceInfo.setUpdateTime(new Date());
        baseDeviceInfoService.updateById(dbBaseDeviceInfo);

        WiFiProbe dbWiFiProbe = this.getByBaseInfoId(dbBaseDeviceInfo.getId());
        if (dbWiFiProbe == null) {
            throw new Exception("WiFi探针数据不存在");
        }
        BeanUtil.copyProperties(wiFiProbeDTO, dbWiFiProbe, BeanUtils.getNullPropertyNames(wiFiProbeDTO));
        baseMapper.updateById(dbWiFiProbe);
        return true;
    }

    /**
     * 删除设备信息
     * @param baseInfoId 基本信息ID
     * @throws Exception 记录不存在则抛出异常
     */
    @Override
    public boolean deleteByBaseInfoId(Long baseInfoId) throws Exception{
        WiFiProbe wiFiProbe = this.getByBaseInfoId(baseInfoId);
        if (wiFiProbe == null) {
            throw new Exception("记录不存在");
        }
        return this.removeById(wiFiProbe.getId());
    }
}
