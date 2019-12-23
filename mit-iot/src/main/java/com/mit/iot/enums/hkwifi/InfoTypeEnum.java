package com.mit.iot.enums.hkwifi;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description wifi探针消息类型
 */
@Getter
@AllArgsConstructor
public enum InfoTypeEnum {

    NONE_TYPE(0, "无"),
    WIFI_GPS(1, "WIFI GPS"), //1.0协议包含终端和热点，新的双ap设备已经不支持
    TERMINAL(2, "终端"),
    AP(3, "ap"),
    TERMINAL_VIRTUAL_IDENTITY(4, "终端虚拟身份"),
    GEOGRAPHICAL_INFORMATION(5, "地理位置信息"),
    DEVICE_STATUS(9, "设备状态信息"),
    RFID(10, "RFID信息");

    private int type;
    private String desc;
}
