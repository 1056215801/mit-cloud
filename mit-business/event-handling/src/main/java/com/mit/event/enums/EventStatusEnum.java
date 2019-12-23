package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件状态定义
 */
@Getter
@AllArgsConstructor
public enum  EventStatusEnum {

    NotAccepted("未受理", 1),
    Disposal("处置中", 2),
    Completed("已完成", 3),
    TimeOut("超时", 4),
    Reminders("催单", 5);

    private String name;
    private int value;
}
