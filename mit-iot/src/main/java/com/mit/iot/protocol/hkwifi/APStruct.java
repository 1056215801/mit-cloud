package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 热点信息数据结构，152个字节，消息类型为3
 */
@Data
@AllArgsConstructor
public class APStruct {
    /**
     * 结构长度
     */
    private int length;
    /**
     * 热点MAC地址，6个字节长度
     */
    private short[]  sourceMacAddr;
    /**
     * 本次上传的 Mac地址第一次被采集到的时间，时间戳
     */
    private long firstAcquisitionTime;
    /**
     * 本次上传的 Mac地址最后一次被采集到的时间，时间戳
     */
    private long lastAcquisitionTime;
    /**
     * 扫描次数，本次上传该Mac被采集到的次数
     */
    private int scanTime;
    /**
     * 信号强度，0-100
     */
    private short wifiFieldIntensity;
    /**
     * 加密方式
     */
    private short wifiSpotEncryptType;
    /**
     * 热点频道
     */
    private long channel;
    /**
     * 热点名称，32个字节，utf-8
     */
    private short[] ssid;
    /**
     * 平台index code，64个字节
     */
    private short[] indexCode;
    /**
     * 保留字节，32个字节
     */
    private short[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------AP热点信息-------").append(System.lineSeparator());
        sb.append("|结构长度-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(length))).append(System.lineSeparator());
        sb.append("|MAC地址-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(sourceMacAddr))).append(System.lineSeparator());
        sb.append("|第一次被采集时间----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(firstAcquisitionTime))).append(System.lineSeparator());
        sb.append("|最后一次被采集时间--").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(lastAcquisitionTime))).append(System.lineSeparator());
        sb.append("|扫描次数-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(scanTime))).append(System.lineSeparator());
        sb.append("|信号强度-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(wifiFieldIntensity))).append(System.lineSeparator());
        sb.append("|加密方式-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(wifiSpotEncryptType))).append(System.lineSeparator());
        sb.append("|热点频道-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(channel))).append(System.lineSeparator());
        sb.append("|热点名称------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(ssid))).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(indexCode))).append(System.lineSeparator());
        sb.append("|res----------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res))).append(System.lineSeparator());
        return sb.toString();
    }
}
