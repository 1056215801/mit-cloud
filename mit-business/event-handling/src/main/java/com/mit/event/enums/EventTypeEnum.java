package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件类型
 */
@Getter
@AllArgsConstructor
public enum EventTypeEnum {

    FacilityManagement("设施管理", "FACILITY_MANAGEMENT"),
    PeaceManagement("平安治理", "PEACE_MANAGEMENT"),
    RoadTraffic("道路交通", "ROAD_TRAFFIC"),
    LivelihoodServices("民生服务", "LIVELIHOOD_SERVICES"),
    EmergencyEvent("突发事件", "EMERGENCY_EVENT"),
    Others("其它事件", "OTHERS");

    private String name;
    private String code;
}
