package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 地理位置信息，256个字节，消息类型为5
 */
@Data
@AllArgsConstructor
public class GeolocationStruct {
    /**
     * 结构长度
     */
    private int length;
    /**
     * 平台index code，64个字节
     */
    private short[] indexCode;
    /**
     * 经度，10个字节
     */
    private short[] longitude;
    /**
     * 纬度，10个字节
     */
    private short[] latitude;
    /**
     * siteCode，14个字节
     */
    private short[] siteCode;
    /**
     * 获取时间，时间戳
     */
    private long acquisitionTime;
    /**
     * 保留字节，152个字节
     */
    private short[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------地理位置信息-------").append(System.lineSeparator());
        sb.append("|结构长度----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(length))).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(indexCode))).append(System.lineSeparator());
        sb.append("|经度-------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(longitude))).append(System.lineSeparator());
        sb.append("|纬度-------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(latitude))).append(System.lineSeparator());
        sb.append("|site code--").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(siteCode))).append(System.lineSeparator());
        sb.append("|获取时间----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(acquisitionTime))).append(System.lineSeparator());
        sb.append("|res--------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res))).append(System.lineSeparator());
        return sb.toString();
    }
}
