package com.mit.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.model.WiFiProbeAp;

import java.util.List;

public interface IWiFiProbeApService extends IService<WiFiProbeAp> {

    /**
     * 处理设备上报信息
     * @param list 处理后的上报信息
     */
    void processUpStream(List<WiFiProbeAp> list);

    IPage<WiFiProbeAp> listByCondition(WiFiQueryDTO wiFiQueryDTO);
}
