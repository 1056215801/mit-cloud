package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件定义
 * 后续可做成可配置
 */
@Getter
@AllArgsConstructor
public enum EventEnum {

    ControlPersonnelAppear("布控人员出现", "CONTROL_PERSONNEL");


/*    DoorNotClosed("门未关", "01", EventClassificationEnum.PublicManagement.getValue(),
            EventTypeEnum.FacilityManagement.getValue(), EventSourceEnum.MassReport.getValue(),
            EventDispositionTypeEnum.Process.getValue(), false, ""),

    PeopleCardDifferent("人卡不一", "02", EventClassificationEnum.PublicManagement.getValue(),
            EventTypeEnum.Others.getValue(), EventSourceEnum.ActiveDiscovery.getValue(),
            null, false, ""),

    MultiDoorNotClosed("多次门未关", "03", EventClassificationEnum.PublicManagement.getValue(),
            EventTypeEnum.PeaceManagement.getValue(), EventSourceEnum.ActiveDiscovery.getValue(),
            EventDispositionTypeEnum.Process.getValue(), true, ""),

    FrequentPersonnelAccess("人员频繁出入", "04", EventClassificationEnum.PublicManagement.getValue(),
            EventTypeEnum.PeaceManagement.getValue(), EventSourceEnum.ActiveDiscovery.getValue(),
            EventDispositionTypeEnum.Process.getValue(), true, ""),

    StrangerWarning("陌生人预警", "05", EventClassificationEnum.PublicManagement.getValue(),
            EventTypeEnum.PeaceManagement.getValue(), EventSourceEnum.ActiveDiscovery.getValue(),
            EventDispositionTypeEnum.Process.getValue(), true, ""),*/


    private String name;
    private String code;
}
