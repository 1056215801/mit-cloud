package com.mit.iot.model;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mit.common.model.SuperEntity;
import com.mit.iot.protocol.hkwifi.TerminalStruct;
import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Description wifi探针终端信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wifi_probe_terminal")
public class WiFiProbeTerminal extends SuperEntity {
    /**
     * 终端 mac 地址
     */
    private String terminalMac;
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
     * 平台indexCode
     */
    private String indexCode;
    /**
     * 连接的热点mac地址
     */
    private String connectedApMac;
    /**
     * 手机品牌
     */
    private String phoneBrand;

    public WiFiProbeTerminal(TerminalStruct terminalStruct) {
        this.terminalMac = ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(terminalStruct.getSourceMacAddr()));
        this.firstAcquisitionTime = new Date(terminalStruct.getFirstAcquisitionTime() * 1000);
        this.lastAcquisitionTime = new Date(terminalStruct.getLastAcquisitionTime() * 1000);
        this.scanTime = terminalStruct.getScanTime();
        this.wifiFieldIntensity = terminalStruct.getWifiFieldIntensity();
        this.indexCode = ByteUtils.byte2String(ByteUtils.unsignedByte2Bytes(terminalStruct.getIndexCode())).trim();
        byte[] connected = ByteUtils.unsignedByte2Bytes(terminalStruct.getConnectedMacAddr());
        this.connectedApMac = StringUtils.isBlank(new String(connected).trim()) ? "" : ByteUtils.byte2HexString(connected);
        this.phoneBrand = ByteUtils.byte2String(ByteUtils.unsignedByte2Bytes(terminalStruct.getPhoneBrand()), "UTF-8").trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------解析后的终端信息-------").append(System.lineSeparator());
        sb.append("|MAC地址-----").append(terminalMac).append(System.lineSeparator());
        sb.append("|第一次被采集时间----").append(firstAcquisitionTime).append(System.lineSeparator());
        sb.append("|最后一次被采集时间--").append(lastAcquisitionTime).append(System.lineSeparator());
        sb.append("|扫描次数-----").append(scanTime).append(System.lineSeparator());
        sb.append("|信号强度-----").append(wifiFieldIntensity).append(System.lineSeparator());
        sb.append("|平台index code---").append(indexCode).append(System.lineSeparator());
        sb.append("|连接的热点Mac地址-").append(connectedApMac).append(System.lineSeparator());
        sb.append("|手机品牌---------").append(phoneBrand).append(System.lineSeparator());
        return sb.toString();
    }
}
