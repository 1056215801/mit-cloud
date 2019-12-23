package com.mit.iot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.utils.BeanUtils;
import com.mit.iot.dto.BaseDeviceInfoDTO;
import com.mit.iot.dto.sensor.SensorSamplingDevice1To4Data;
import com.mit.iot.enums.DeviceTypeEnum;
import com.mit.iot.mapper.FireHydrantMapper;
import com.mit.iot.model.BaseDeviceInfo;
import com.mit.iot.model.FireHydrant;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IFireHydrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @Description 消防栓设备service类
 */
@Service
public class FireHydrantServiceImpl extends ServiceImpl<FireHydrantMapper, FireHydrant> implements IFireHydrantService {

    @Value("${netty.server.sensor.autoSave:true}")
    private boolean isAutoSave;

    @Autowired
    private IBaseDeviceInfoService baseDeviceInfoService;

    /**
     * 处理上报数据
     * 存在设备则更新其信息，不存在根据配置是否自动保存
     * @param data 上报数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processUpDataStream(SensorSamplingDevice1To4Data data) {
        if (null == data) {
            return;
        }
        BaseDeviceInfo baseDeviceInfo = baseDeviceInfoService.getByDeviceNo(data.getDeviceId());
        if (baseDeviceInfo == null) {
            if (isAutoSave) {
                autoSaveUpDataStream(data);
            }
            return;
        }
        // 存在则更新信息
        baseDeviceInfo.setStatus((data.getDataStatus() == 0) ? 1 : 0);
        baseDeviceInfo.setAcquisitionTime(data.getSendTime());
        baseDeviceInfo.setUpdateTime(new Date());
        baseDeviceInfoService.updateById(baseDeviceInfo);

        FireHydrant fireHydrant = this.getByBaseInfoId(baseDeviceInfo.getId());
        fireHydrant.setBatteryLevel(data.getBatteryLevel());
        fireHydrant.setSignalStrength(data.getSignalStrength());
        fireHydrant.setDataStatus(data.getDataStatus());
        fireHydrant.setDataValue(data.getValue());
        fireHydrant.setDataMeasurementUnit(data.getMeasurementUnitString());
        baseMapper.updateById(fireHydrant);
    }

    private void autoSaveUpDataStream(SensorSamplingDevice1To4Data data) {
        BaseDeviceInfo baseDeviceInfo = new BaseDeviceInfo();
        baseDeviceInfo.setDeviceName(UUID.randomUUID().toString());
        baseDeviceInfo.setDeviceType(DeviceTypeEnum.FIRE_HYDRANT.getValue());
        baseDeviceInfo.setDeviceNo(data.getDeviceId());
        baseDeviceInfo.setStatus((data.getDataStatus() == 0) ? 1 : 0);
        baseDeviceInfo.setAcquisitionTime(data.getSendTime());
        baseDeviceInfoService.save(baseDeviceInfo);

        FireHydrant fireHydrant = new FireHydrant(data);
        fireHydrant.setBaseDeviceInfoId(baseDeviceInfo.getId());
        baseMapper.insert(fireHydrant);
    }

    @Override
    public FireHydrant getByBaseInfoId(Long baseDeviceInfoId) {
        QueryWrapper<FireHydrant> wrapper = new QueryWrapper<>();
        wrapper.eq("base_device_info_id", baseDeviceInfoId);
        return baseMapper.selectOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO) {
        BaseDeviceInfo baseDeviceInfo = new BaseDeviceInfo();
        BeanUtil.copyProperties(baseDeviceInfoDTO, baseDeviceInfo);
        baseDeviceInfoService.save(baseDeviceInfo);

        FireHydrant fireHydrant = new FireHydrant();
        fireHydrant.setBaseDeviceInfoId(baseDeviceInfo.getId());
        baseMapper.insert(fireHydrant);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateWithBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO) {
        BaseDeviceInfo dbBaseDeviceInfo = baseDeviceInfoService.getById(baseDeviceInfoDTO.getId());
        BeanUtil.copyProperties(baseDeviceInfoDTO, dbBaseDeviceInfo, BeanUtils.getNullPropertyNames(baseDeviceInfoDTO));
        dbBaseDeviceInfo.setUpdateTime(new Date());
        baseDeviceInfoService.updateById(dbBaseDeviceInfo);
        return true;
    }

    @Override
    public boolean deleteByBaseInfoId(Long baseInfoId) throws Exception {
        FireHydrant fireHydrant = this.getByBaseInfoId(baseInfoId);
        if (fireHydrant == null) {
            throw new Exception("记录不存在");
        }
        return this.removeById(fireHydrant.getId());
    }
}
