package com.mit.iot.enums.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 数据计量单位
 *
 */
@Getter
@AllArgsConstructor
public enum MeasurementUnitEnum {

    PRESSURE_MPA(1, "MPa", "压力 MPa"),
    PRESSURE_BAR(2, "Bar", "压力 Bar"),
    PRESSURE_KPA(3, "KPa", "压力 KPa"),
    LEVEL(4, "M", "液位 M"),
    TEMPERATURE(5, "℃", "温度 ℃"),
    FLOW(6, "m³/h", "流量 m³/h"),
    ANGLE(7, "°", "角度 °");

    private int key;
    private String value;
    private String desc;
}
