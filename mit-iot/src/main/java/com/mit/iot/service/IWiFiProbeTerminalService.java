package com.mit.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.model.WiFiProbeTerminal;

import java.util.List;

public interface IWiFiProbeTerminalService extends IService<WiFiProbeTerminal> {
    /**
     * 根据终端mac地址、indexCode获取终端信息
     * 两个条件确定唯一一条记录
     * @param terminalMac 终端mac
     * @param indexCode 海康平台的索引
     * @return 如果存在返回唯一的一条记录，不存在则返回空
     */
    WiFiProbeTerminal getByTerminalMacAndIndexCode(String terminalMac, String indexCode);

    /**
     * 处理设备上报信息
     * @param list 处理后的上报信息
     */
    void processUpStream(List<WiFiProbeTerminal> list);

    IPage<WiFiProbeTerminal> listByCondition(WiFiQueryDTO wiFiQueryDTO);
}
