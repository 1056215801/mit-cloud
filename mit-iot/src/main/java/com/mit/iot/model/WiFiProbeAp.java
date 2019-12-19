package com.mit.iot.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mit.common.model.SuperEntity;
import com.mit.iot.enums.hkwifi.WIFISpotEncryptType;
import com.mit.iot.protocol.hkwifi.APStruct;
import com.mit.iot.util.ByteUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description wifi探针 AP 热点信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("wifi_probe_ap")
public class WiFiProbeAp extends SuperEntity {
    /**
     * AP热点 mac 地址
     */
    private String apMac;
    /**
     * 第一次被采集到的时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date firstAcquisitionTime;
    /**
     * 最后一次被采集到的时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date lastAcquisitionTime;
    /**
     * 被扫描次数
     */
    private int scanTime;
    /**
     * 信号强度，0-100
     */
    private int wifiFieldIntensity;
    /**
     * 加密方式
     */
    private String wifiSpotEncryptType;
    /**
     * 热点频道
     */
    private int channel;
    /**
     * 热点名称
     */
    private String ssid;
    /**
     * 平台indexCode
     */
    private String indexCode;

    public WiFiProbeAp(APStruct apStruct) {
        this.apMac = ByteUtils.byte2String(apStruct.getSourceMacAddr());
        this.firstAcquisitionTime = new Date(apStruct.getFirstAcquisitionTime() * 1000);
        this.lastAcquisitionTime = new Date(apStruct.getLastAcquisitionTime() * 1000);
        this.scanTime = apStruct.getScanTime();
        this.wifiFieldIntensity = apStruct.getWifiFieldIntensity();
        this.wifiSpotEncryptType = EnumUtil.likeValueOf(WIFISpotEncryptType.class, apStruct.getWifiSpotEncryptType()).getValue();
        this.channel = apStruct.getChannel();
        this.ssid = ByteUtils.byte2String(apStruct.getSsid(), "UTF-8");
        this.indexCode = ByteUtils.byte2String(apStruct.getIndexCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------解析后的AP热点信息-------").append(System.lineSeparator());
        sb.append("|MAC地址-----").append(apMac).append(System.lineSeparator());
        sb.append("|第一次被采集时间----").append(firstAcquisitionTime).append(System.lineSeparator());
        sb.append("|最后一次被采集时间--").append(lastAcquisitionTime).append(System.lineSeparator());
        sb.append("|扫描次数-----").append(scanTime).append(System.lineSeparator());
        sb.append("|信号强度-----").append(wifiFieldIntensity).append(System.lineSeparator());
        sb.append("|加密方式-----").append(wifiSpotEncryptType).append(System.lineSeparator());
        sb.append("|热点频道-----").append(channel).append(System.lineSeparator());
        sb.append("|热点名称------").append(ssid).append(System.lineSeparator());
        sb.append("|平台index code---").append(indexCode).append(System.lineSeparator());
        return sb.toString();
    }
}
