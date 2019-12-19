package com.mit.iot.dto.sensor;

import cn.hutool.core.util.EnumUtil;
import com.mit.iot.enums.sensor.SensorDeviceTypeEnum;
import com.mit.iot.protocol.sensor.Type01Struct;
import com.mit.iot.util.ByteUtils;
import lombok.Data;

/**
 * @Description 传感器采样基本数据
 */
@Data
public class SensorSamplingBaseData {

    public static final String YEAR_PRE = "20";

    public SensorSamplingBaseData(Type01Struct protocol) {
        this.deviceType = protocol.getDeviceType();
        this.deviceId = ByteUtils.byte2String(protocol.getDeviceId());
        this.IMEI = this.deviceId.substring(0, this.deviceId.length() - 1);
        StringBuilder sb = new StringBuilder();
        sb.append(YEAR_PRE).append(ByteUtils.bcd2String(protocol.getYear()))
                .append(ByteUtils.bcd2String(protocol.getMonth()))
                .append(ByteUtils.bcd2String(protocol.getDay()))
                .append(ByteUtils.bcd2String(protocol.getHour()))
                .append(ByteUtils.bcd2String(protocol.getMinute()))
                .append(ByteUtils.bcd2String(protocol.getSecond()));
        this.sendTime = sb.toString();
        this.batteryLevel = protocol.getBatteryLevel();
        this.signalStrength = protocol.getSignalStrength();
    }

    /**
     * 设备类型，枚举值见 DeviceTypeEnum
     */
    private int deviceType;
    /**
     * 设备标识
     */
    private String deviceId;
    /**
     * IMEI
     */
    private String IMEI;
    /**
     * 发送时间
     */
    private String sendTime;
    /**
     * 电池电量，0-100
     */
    private int batteryLevel;
    /**
     * 信号强度
     */
    private int signalStrength;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------解析后传感器采样数据-------").append(System.lineSeparator());
        sb.append("|设备类型---").append(deviceType).append("---")
                .append(EnumUtil.likeValueOf(SensorDeviceTypeEnum.class, deviceType).getDesc()).append(System.lineSeparator());
        sb.append("|设备标识---").append(deviceId).append(System.lineSeparator());
        sb.append("|IMEI------").append(IMEI).append(System.lineSeparator());
        sb.append("|发送时间---").append(sendTime).append(System.lineSeparator());
        sb.append("|电池电量---").append(batteryLevel).append(System.lineSeparator());
        sb.append("|信号强度---").append(signalStrength).append(System.lineSeparator());
        return sb.toString();
    }

}
