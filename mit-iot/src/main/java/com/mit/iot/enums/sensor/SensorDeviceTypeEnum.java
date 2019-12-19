package com.mit.iot.enums.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 无线传感器设备类型枚举值
 */
@Getter
@AllArgsConstructor
public enum SensorDeviceTypeEnum {

    TYPE01(1, "01", "无线远程压力采集终端"),
    TYPE02(2, "02", "无线远程液位采集终端"),
    TYPE03(3, "03", "无线远程温度采集终端"),
    TYPE04(4, "04", "无线智能消防栓检测终端"),
    TYPE05(5, "05", "无线远程温湿度采集终端"),
    TYPE06(6, "06", "无线智能多通道采集终端"),
    TYPE07(7, "07", "无线智能环境监测终端"),
    TYPE08(8, "08", "无线智能控制阀"),
    TYPE09(9, "09", "无线数字量监测终端");

    private int type;
    private String hexString;
    private String desc;
}
