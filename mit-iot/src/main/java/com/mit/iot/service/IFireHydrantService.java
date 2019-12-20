package com.mit.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.iot.dto.BaseDeviceInfoDTO;
import com.mit.iot.dto.sensor.SensorSamplingDevice1To4Data;
import com.mit.iot.model.FireHydrant;

public interface IFireHydrantService extends IService<FireHydrant> {

    /**
     * 处理上报的数据
     * @param data 上报数据
     */
    void processUpDataStream(SensorSamplingDevice1To4Data data);

    /**
     * 根据设备基本ID获取设备信息
     * @param baseDeviceInfoId 设备基本信息ID
     * @return 唯一设备信息记录
     */
    FireHydrant getByBaseInfoId(Long baseDeviceInfoId);

    /**
     * 新增消防栓设备
     * @param baseDeviceInfoDTO 设备信息
     * @return 新增成功返回true，失败返回false
     */
    boolean saveWithBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO);

    /**
     * 修改消防栓设备
     * @param baseDeviceInfoDTO 设备信息
     * @return 新增成功返回true，失败返回false
     */
    boolean updateWithBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO);

    /**
     * 根据基本信息ID删除消防栓设备记录
     * @param baseInfoId 基本信息ID
     * @return 删除成功返回true.失败返回false
     */
    boolean deleteByBaseInfoId(Long baseInfoId) throws Exception;
}
