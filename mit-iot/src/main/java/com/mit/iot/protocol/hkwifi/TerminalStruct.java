package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 终端信息数据结构
 * 单个信息长度为 276 个字节，消息类型为2
 */
@Data
@AllArgsConstructor
public class TerminalStruct {
    /**
     * 结构长度
     */
    private int length;
    /**
     * 终端MAC地址，6个字节长度
     */
    private short[] sourceMacAddr;
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
     * 终端历史SSID个数，最多5个
     */
    private short historySSIDNum;
    /**
     * byte[5][32]，每个SSID长度最长32个字节，utf-8编码，可能有中文
     */
    private short[][] historySSID;
    /**
     * 平台index code，64个字节
     */
    private short[] indexCode;
    /**
     * 正在连接的热点mac地址，6个字节
     */
    private short[] connectedMacAddr;
    /**
     * 手机品牌，16个字节
     */
    private short[] phoneBrand;
    /**
     * 保留字节，10个字节
     */
    private short[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------终端信息-------").append(System.lineSeparator());
        sb.append("|结构长度-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(length))).append(System.lineSeparator());
        sb.append("|MAC地址-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(sourceMacAddr))).append(System.lineSeparator());
        sb.append("|第一次被采集时间----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(firstAcquisitionTime))).append(System.lineSeparator());
        sb.append("|最后一次被采集时间--").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(lastAcquisitionTime))).append(System.lineSeparator());
        sb.append("|扫描次数-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(scanTime))).append(System.lineSeparator());
        sb.append("|信号强度-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(wifiFieldIntensity))).append(System.lineSeparator());
        sb.append("|SSID个数-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSIDNum))).append(System.lineSeparator());
        sb.append("|SSID[1]------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSID[0]))).append(System.lineSeparator());
        sb.append("|SSID[2]------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSID[1]))).append(System.lineSeparator());
        sb.append("|SSID[3]------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSID[2]))).append(System.lineSeparator());
        sb.append("|SSID[4]------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSID[3]))).append(System.lineSeparator());
        sb.append("|SSID[5]------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(historySSID[4]))).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(indexCode))).append(System.lineSeparator());
        sb.append("|连接的热点Mac地址-").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(connectedMacAddr))).append(System.lineSeparator());
        sb.append("|手机品牌---------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(phoneBrand))).append(System.lineSeparator());
        sb.append("|res----------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res))).append(System.lineSeparator());
        return sb.toString();
    }
}
