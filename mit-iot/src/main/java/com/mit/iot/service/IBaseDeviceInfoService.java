package com.mit.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.iot.dto.BaseDeviceInfoDTO;
import com.mit.iot.dto.DeviceInfoQueryDTO;
import com.mit.iot.model.BaseDeviceInfo;

import java.util.List;

public interface IBaseDeviceInfoService extends IService<BaseDeviceInfo> {

    IPage<BaseDeviceInfo> listByCondition(DeviceInfoQueryDTO deviceInfoQueryDTO);

    boolean removeCascadeByIds(List<Long> idList);

    BaseDeviceInfo getByCommunityCodeAndDeviceName(String communityCode, String deviceName);

    BaseDeviceInfo getByDeviceNo(String deviceNo);

    BaseDeviceInfo getByDeviceSerialNo(String deviceSerialNo);

    int metricsDeviceNum(List<String> communityCodeList, List<String> deviceTypeList, List<Integer> status);

    void checkBaseInfo(BaseDeviceInfoDTO baseDeviceInfoDTO) throws Exception;
}
