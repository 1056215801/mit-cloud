package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件来源
 */
@Getter
@AllArgsConstructor
public enum EventSourceEnum {

    ActiveDiscovery("主动发现", "ACTIVE_DISCOVERY"),
    MassReport("群众上报", "MASS_REPORT"),
    DeviceAwareness("设备感知", "DEVICE_AWARENESS"),
    SupervisoryCoOrganizer("督办协办", "SUPERVISORY_CO_ORGANIZER");

    private String name;
    private String code;
}
