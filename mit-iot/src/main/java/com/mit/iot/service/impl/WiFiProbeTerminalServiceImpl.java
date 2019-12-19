package com.mit.iot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.mapper.WiFiProbeTerminalMapper;
import com.mit.iot.model.WiFiProbeAp;
import com.mit.iot.model.WiFiProbeTerminal;
import com.mit.iot.service.IWiFiProbeTerminalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description WiFi探针终端信息service类
 */
@Service
public class WiFiProbeTerminalServiceImpl extends ServiceImpl<WiFiProbeTerminalMapper, WiFiProbeTerminal>
        implements IWiFiProbeTerminalService {

    @Override
    public WiFiProbeTerminal getByTerminalMacAndIndexCode(String terminalMac, String indexCode) {
        return baseMapper.selectByTerminalMacAndIndexCode(terminalMac, indexCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processUpStream(List<WiFiProbeTerminal> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<WiFiProbeTerminal> saveList = new ArrayList<>();
        List<WiFiProbeTerminal> updateList = new ArrayList<>();
        list.forEach(wiFiProbeTerminal -> {
            WiFiProbeTerminal dbWiFiProbeTerminal = baseMapper.selectByTerminalMacAndIndexCode(
                    wiFiProbeTerminal.getTerminalMac(), wiFiProbeTerminal.getIndexCode());
            if (dbWiFiProbeTerminal == null) {
                saveList.add(wiFiProbeTerminal);
            } else {
                BeanUtil.copyProperties(wiFiProbeTerminal, dbWiFiProbeTerminal);
                updateList.add(dbWiFiProbeTerminal);
            }
        });
        this.saveBatch(saveList);
        this.updateBatchById(updateList);
    }

    @Override
    public IPage<WiFiProbeTerminal> listByCondition(WiFiQueryDTO wiFiQueryDTO) {
        IPage<WiFiProbeTerminal> page = new Page<>(wiFiQueryDTO.getPageNum(), wiFiQueryDTO.getPageSize());
        QueryWrapper<WiFiProbeTerminal> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(wiFiQueryDTO.getIndexCode())) {
            wrapper.eq("index_code", wiFiQueryDTO.getIndexCode());
        }
        if (StringUtils.isNotBlank(wiFiQueryDTO.getMac())) {
            wrapper.eq("terminal_mac", wiFiQueryDTO.getMac());
        }
        if (null != wiFiQueryDTO.getFirstAcquisitionTimeStart()) {
            wrapper.ge("first_acquisition_time", wiFiQueryDTO.getFirstAcquisitionTimeStart());
        }
        if (null != wiFiQueryDTO.getFirstAcquisitionTimeEnd()) {
            wrapper.le("first_acquisition_time", wiFiQueryDTO.getFirstAcquisitionTimeEnd());
        }
        if (null != wiFiQueryDTO.getLastAcquisitionTimeStart()) {
            wrapper.ge("last_acquisition_time", wiFiQueryDTO.getLastAcquisitionTimeStart());
        }
        if (null != wiFiQueryDTO.getLastAcquisitionTimeEnd()) {
            wrapper.le("last_acquisition_time", wiFiQueryDTO.getLastAcquisitionTimeEnd());
        }
        wrapper.orderByDesc("create_time");
        return this.page(page, wrapper);
    }
}
