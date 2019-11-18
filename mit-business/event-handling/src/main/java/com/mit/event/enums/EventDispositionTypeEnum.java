package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件处置类型
 */
@Getter
@AllArgsConstructor
public enum EventDispositionTypeEnum {

    Process("流程", "PROCESS"),
    Notice("提醒", "NOTICE");

    private String name;
    private String code;
}
