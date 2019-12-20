package com.mit.iot.enums.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 数据类型枚举值
 */
@Getter
@AllArgsConstructor
public enum DataTypeEnum {

    TYPE01(1, "01", "上行，发送传感器采样数据"),
    TYPE02(2, "02", "下行，获取设备配置信息"),
    TYPE03(3, "03", "上行，发送设备配置信息"),
    TYPE04(4, "04", "下行，参数配置下发"),
    TYPE05(5, "05", "下行，请求设备软件升级"),
    TYPE06(6, "06", "下行，执行结果"),
    TYPE07(7, "07", "下行，下发天气信息"),
    TYPE08(8, "08", "上行，获取天气信息");

    private int type;
    private String hexString;
    private String desc;
}
