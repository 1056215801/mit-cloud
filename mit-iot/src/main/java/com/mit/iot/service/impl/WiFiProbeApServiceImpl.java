package com.mit.iot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.utils.BeanUtils;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.mapper.WiFiProbeApMapper;
import com.mit.iot.model.WiFiProbeAp;
import com.mit.iot.service.IWiFiProbeApService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description wifi探针 AP热点信息service处理类
 */
@Service
public class WiFiProbeApServiceImpl extends ServiceImpl<WiFiProbeApMapper, WiFiProbeAp> implements IWiFiProbeApService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processUpStream(List<WiFiProbeAp> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<WiFiProbeAp> saveList = new ArrayList<>();
        List<WiFiProbeAp> updateList = new ArrayList<>();
        list.forEach(wiFiProbeAp -> {
            WiFiProbeAp dbWiFiProbeAp = baseMapper.selectByApMacAndIndexCode(wiFiProbeAp.getApMac(),
                    wiFiProbeAp.getIndexCode());
            if (dbWiFiProbeAp == null) {
                saveList.add(wiFiProbeAp);
            } else {
                BeanUtil.copyProperties(wiFiProbeAp, dbWiFiProbeAp, BeanUtils.getNullPropertyNames(wiFiProbeAp));
                dbWiFiProbeAp.setUpdateTime(new Date());
                updateList.add(dbWiFiProbeAp);
            }
        });

        if (CollectionUtils.isNotEmpty(saveList)) {
            this.saveBatch(saveList);
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
    }

    @Override
    public IPage<WiFiProbeAp> listByCondition(WiFiQueryDTO wiFiQueryDTO) {
        IPage<WiFiProbeAp> page = new Page<>(wiFiQueryDTO.getPageNum(), wiFiQueryDTO.getPageSize());
        QueryWrapper<WiFiProbeAp> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(wiFiQueryDTO.getIndexCode())) {
            wrapper.eq("index_code", wiFiQueryDTO.getIndexCode());
        }
        if (StringUtils.isNotBlank(wiFiQueryDTO.getMac())) {
            wrapper.eq("ap_mac", wiFiQueryDTO.getMac());
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
