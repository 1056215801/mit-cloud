package com.mit.iot.protocol.sensor;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 消防水压协议
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseStruct {

    /**
     * 最短长度
     */
    public static int MIN_LEN = 10;
    /**
     * 帧头
     */
    public static String header_string = "tpsl";
    public static int header_int = 1953526636;

    /**
     * 数据类型
     */
    protected byte type;

    /**
     * 数据长度
     */
    protected short length;

    /**
     * 数据体
     */
    protected byte[] body;

    /**
     * 校验值
     */
    protected short crc;

    /**
     * 帧尾
     */
    public static String tail_string = "iot";
    public static byte[] tail = {0x69, 0x6f, 0x74};

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        /*sb.append("-------解析前数据包-------").append(System.lineSeparator());
        sb.append("|开始标志---").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(header_int))).append(System.lineSeparator());
        sb.append("|数据类型---").append(ByteUtils.byte2HexString(type)).append(System.lineSeparator());
        sb.append("|数据长度(10)").append(length).append(System.lineSeparator());
        sb.append("|数据体-----").append(ByteUtils.byte2HexString(body)).append(System.lineSeparator());
        sb.append("|校验值-----").append(ByteUtils.byte2HexString(ByteUtils.short2HexBytes(crc))).append(System.lineSeparator());
        sb.append("|结束标志---").append(ByteUtils.byte2HexString(tail)).append(System.lineSeparator());*/
        return sb.toString();
    }

}
