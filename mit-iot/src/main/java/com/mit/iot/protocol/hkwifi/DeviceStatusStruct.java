package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 设备状态信息，80个字节，消息类型为9
 */
@Data
@AllArgsConstructor
public class DeviceStatusStruct {
    /**
     * 结构长度
     */
    private int length;
    /**
     * 平台index code，64个字节
     */
    private short[] indexCode;
    /**
     * 设备状态，0 - 异常，1 - 正常
     */
    private short status;
    /**
     * 终端MAC地址，6个字节长度
     */
    private short[] sourceMacAddr;
    /**
     * 设备类型，0 - WiFi，1 - RFID
     */
    private short type;
    /**
     * 保留字段，2个字节
     */
    private short[] res;
    /**
     * 获取时间，时间戳
     */
    private long acquisitionTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------设备状态信息-------").append(System.lineSeparator());
        sb.append("|结构长度----").append(ByteUtils.byte2HexString(ByteUtils.unsignedShort2Bytes(length))).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(indexCode))).append(System.lineSeparator());
        sb.append("|设备状态----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(status))).append(System.lineSeparator());
        sb.append("|MAC地址-----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(sourceMacAddr))).append(System.lineSeparator());
        sb.append("|设备类型----").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(type))).append(System.lineSeparator());
        sb.append("|res--------").append(ByteUtils.byte2HexString(ByteUtils.unsignedByte2Bytes(res))).append(System.lineSeparator());
        sb.append("|获取时间----").append(ByteUtils.byte2HexString(ByteUtils.unsignedInt2Bytes(acquisitionTime))).append(System.lineSeparator());
        return sb.toString();
    }
}
