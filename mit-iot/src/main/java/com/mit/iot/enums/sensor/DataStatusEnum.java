package com.mit.iot.enums.sensor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 数据状态枚举值
 * 数据类型为 0x01，设备类型为 0x01、0x02、0x03、0x04的 Sample_data中定义
 */
@Getter
@AllArgsConstructor
public enum DataStatusEnum {

    NORMAL(0, "数据正常"),
    LOWER_THRESHOLD_ALARM(1, "数据阈值下限告警"),
    UPPER_THRESHOLD_ALARM(2, "数据阈值上限告警"),
    BREAKDOWN(3, "设备故障"),
    DYNAMIC_THRESHOLD_ALARM(4, "数据动态变化阈值告警"),
    COLLISION_ALARM(5, "碰撞告警"),
    TILT_ALARM(6, "倾斜告警"),
    WATER_FLOW_ALARM(7, "水流告警"),
    WATER_INGRESS_ALARM(8, "进水告警"),
    LOW_BATTERY_ALARM(9, "低电量告警");

    private int value;
    private String desc;
}
