package com.mit.iot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 设备类型
 */
@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {

    WIFI("WIFI", "wifi探针"),
    MANHOLE_COVER("MANHOLE_COVER", "智能井盖传感器"),
    FIRE_HYDRANT("FIRE_HYDRANT", "消防栓传感器"),
    CIRCUIT_MONITOR("CIRCUIT_MONITOR", "电路监测传感器");

    private String value;
    private String desc;
}
