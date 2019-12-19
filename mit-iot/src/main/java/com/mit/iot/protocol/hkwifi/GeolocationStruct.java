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
    private short length;
    /**
     * 平台index code，64个字节
     */
    private byte[] indexCode;
    /**
     * 经度，10个字节
     */
    private byte[] longitude;
    /**
     * 纬度，10个字节
     */
    private byte[] latitude;
    /**
     * siteCode，14个字节
     */
    private byte[] siteCode;
    /**
     * 获取时间，时间戳
     */
    private int acquisitionTime;
    /**
     * 保留字节，152个字节
     */
    private byte[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        /*sb.append("-------地理位置信息-------").append(System.lineSeparator());
        sb.append("|结构长度----").append(ByteUtils.byte2HexString(ByteUtils.short2HexBytes(length))).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(indexCode)).append(System.lineSeparator());
        sb.append("|经度-------").append(ByteUtils.byte2HexString(longitude)).append(System.lineSeparator());
        sb.append("|纬度-------").append(ByteUtils.byte2HexString(latitude)).append(System.lineSeparator());
        sb.append("|site code--").append(ByteUtils.byte2HexString(siteCode)).append(System.lineSeparator());
        sb.append("|获取时间----").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(acquisitionTime))).append(System.lineSeparator());
        sb.append("|res--------").append(ByteUtils.byte2HexString(res)).append(System.lineSeparator());*/
        return sb.toString();
    }
}
