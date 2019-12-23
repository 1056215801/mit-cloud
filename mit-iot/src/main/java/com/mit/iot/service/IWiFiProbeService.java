package com.mit.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.iot.dto.hkwifi.WiFiDeviceStatusDTO;
import com.mit.iot.dto.hkwifi.WiFiGeolocationDTO;
import com.mit.iot.dto.hkwifi.WiFiProbeDTO;
import com.mit.iot.model.WiFiProbe;

import java.util.List;

public interface IWiFiProbeService extends IService<WiFiProbe> {

    /**
     * 根据设备基本ID获取设备信息
     * @param baseDeviceInfoId 设备基本信息ID
     * @return 唯一设备信息记录
     */
    WiFiProbe getByBaseInfoId(Long baseDeviceInfoId);

    /**
     * 根据平台索引获取WiFi探针设备信息
     * @param indexCode 平台索引
     * @return 唯一设备信息记录
     */
    WiFiProbe getByIndexCode(String indexCode);

    /**
     * 处理上报的设备状态信息
     * @param list 设备状态信息
     */
    void processUpDeviceStatusStream(List<WiFiDeviceStatusDTO> list);

    /**
     * 处理上报的设备地理位置信息
     * @param list 设备地理位置信息
     */
    void processUpGeolocationStream(List<WiFiGeolocationDTO> list);

    /**
     * 新增WiFi探针设备
     * @param wiFiProbeDTO 设备信息
     * @return 新增成功返回true，失败返回false
     */
    boolean saveWithBaseInfo(WiFiProbeDTO wiFiProbeDTO);

    /**
     * 修改WiFi探针设备
     * @param wiFiProbeDTO 设备信息
     * @return 新增成功返回true，失败返回false
     */
    boolean updateWithBaseInfo(WiFiProbeDTO wiFiProbeDTO) throws Exception;

    /**
     * 根据基本信息ID删除WiFi探针设备记录
     * @param baseInfoId 基本信息ID
     * @return 删除成功返回true.失败返回false
     */
    boolean deleteByBaseInfoId(Long baseInfoId) throws Exception;

}
