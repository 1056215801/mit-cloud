package com.mit.iot.protocol.sensor;

import com.mit.iot.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 数据类型为 0x01，设备类型为 0x01、0x02、0x03、0x04的 Sample_data数据格式
 * 4个字节，BCD码
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Type01Device1To4Struct extends Type01Struct {

    public Type01Device1To4Struct(byte type, short length, byte[] body, short crc) {
        super(type, length, body, crc);
        decodeSampleData(this.sampleData);
    }

    /**
     * 数据状态，第1个字节的高四位，定义见 DataStatusEnum
     */
    private int dataStatus;
    /**
     * 数据计量单位，第1个字节的低四位，定义见 MeasurementUnitEnum
     */
    private int measurementUnit;
    /**
     * 小数点位置，第2个字节的高四位
     * 0 - 无小数
     * 1 - 小数点后有 1 位有效数字
     * 2 - 小数点后有 2 位有效数字
     * 3 - 小数点后有 3 位有效数字
     * 4 - 小数点后有 4 位有效数字
     */
    private int decimalPointPosition;
    /**
     * 数据的5位有效数字中的第 1 位，第2个字节的低四位
     * 当第1个字节的低四位即 measurementUnit 为 5时（范围为-50.0-150.0℃），该字段表示温度的正负
     * 0 - 正    1 - 负，  后边第3个字节和第4个字节的有效数字表示温度的第1位至第4位的数字
     */
    private int position1;
    /**
     * 数据的5位有效数字中的第 2 位，第3个字节的高四位（温度数据第 1 位）
     */
    private int position2;
    /**
     * 数据的5位有效数字中的第 3 位，第3个字节的低四位（温度数据第 2 位）
     */
    private int position3;
    /**
     * 数据的5位有效数字中的第 4 位，第4个字节的高四位（温度数据第 3 位）
     */
    private int position4;
    /**
     * 数据的5位有效数字中的第 5 位，第4个字节的低四位（温度数据第 4 位）
     */
    private int position5;

    /**
     * 解析sample_data，4字节长度
     * @param sampleData sampleData
     */
    private void decodeSampleData(byte[] sampleData) {
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeBytes(sampleData);
        byte b1 = byteBuf.readByte();
        byte b2 = byteBuf.readByte();
        byte b3 = byteBuf.readByte();
        byte b4 = byteBuf.readByte();
        this.dataStatus = ByteUtils.bcdByteHeight4(b1);
        this.measurementUnit = ByteUtils.bcdByteLow4(b1);
        this.decimalPointPosition = ByteUtils.bcdByteHeight4(b2);
        this.position1 = ByteUtils.bcdByteLow4(b2);
        this.position2 = ByteUtils.bcdByteHeight4(b3);
        this.position3 = ByteUtils.bcdByteLow4(b3);
        this.position4 = ByteUtils.bcdByteHeight4(b4);
        this.position5 = ByteUtils.bcdByteLow4(b4);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        /*sb.append("|数据状态---").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(dataStatus))).append(System.lineSeparator());
        sb.append("|计量单位---").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(measurementUnit))).append(System.lineSeparator());
        sb.append("|小数点位置-").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(decimalPointPosition))).append(System.lineSeparator());
        sb.append("|第1个数字--").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(position1))).append(System.lineSeparator());
        sb.append("|第2个数字--").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(position2))).append(System.lineSeparator());
        sb.append("|第3个数字--").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(position3))).append(System.lineSeparator());
        sb.append("|第4个数字--").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(position4))).append(System.lineSeparator());
        sb.append("|第5个数字--").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(position5))).append(System.lineSeparator());*/
        return sb.toString();
    }
}
