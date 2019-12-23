package com.mit.iot.protocol.sensor;

import com.mit.iot.util.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Description 数据类型为 0x01 的数据体格式，传感器采样数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type01Struct extends BaseStruct {

    public Type01Struct(byte type, short length, byte[] body, short crc) {
        super(type, length, body, crc);
        decodeBody(body, length);
    }

    /**
     * 设备类型
     */
    protected byte deviceType;
    /**
     * 设备标识，16位，IMEI取前15位字符型
     */
    protected byte[] deviceId;
    /**
     * 年，BCD码
     */
    protected byte year;
    /**
     * 月，BCD码
     */
    protected byte month;
    /**
     * 日，BCD码
     */
    protected byte day;
    /**
     * 时，BCD码
     */
    protected byte hour;
    /**
     * 分，BCD码
     */
    protected byte minute;
    /**
     * 秒，BCD码
     */
    protected byte second;
    /**
     * 电池电量，百分比数值，0-100
     */
    protected byte batteryLevel;
    /**
     * 信号强度
     */
    protected byte signalStrength;
    /**
     * sample data
     */
    protected byte[] sampleData;

    /**
     * 解析数据体
     * @param body 数据体
     * @param length 数据体长度
     */
    private void decodeBody(byte[] body, short length) {
        ByteBuf byteBuf = Unpooled.buffer(length);
        byteBuf.writeBytes(body);
        // 设备类型
        this.deviceType = byteBuf.readByte();
        byte[] deviceId = new byte[16];
        byteBuf.readBytes(deviceId);
        this.deviceId = deviceId;
        this.year = byteBuf.readByte();
        this.month = byteBuf.readByte();
        this.day = byteBuf.readByte();
        this.hour = byteBuf.readByte();
        this.minute = byteBuf.readByte();
        this.second = byteBuf.readByte();
        this.batteryLevel = byteBuf.readByte();
        this.signalStrength = byteBuf.readByte();
        byte[] sampleData = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(sampleData);
        this.sampleData = sampleData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("|设备类型---").append(ByteUtils.byte2HexString(deviceType)).append(System.lineSeparator());
        sb.append("|设备标识---").append(ByteUtils.byte2HexString(deviceId)).append(System.lineSeparator());
        sb.append("|年月日时分秒-").append(ByteUtils.byte2HexString(year)).append(" ")
                .append(ByteUtils.byte2HexString(month)).append(" ")
                .append(ByteUtils.byte2HexString(day)).append(" ")
                .append(ByteUtils.byte2HexString(hour)).append(" ")
                .append(ByteUtils.byte2HexString(minute)).append(" ")
                .append(ByteUtils.byte2HexString(second)).append(" ")
                .append(System.lineSeparator());
        sb.append("|电池电量---").append(ByteUtils.byte2HexString(batteryLevel)).append(System.lineSeparator());
        sb.append("|信号强度---").append(ByteUtils.byte2HexString(signalStrength)).append(System.lineSeparator());
        sb.append("|SampleData-").append(ByteUtils.byte2HexString(sampleData)).append(System.lineSeparator());
        return sb.toString();
    }
}
