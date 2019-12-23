package com.mit.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 事件分类
 */
@Getter
@AllArgsConstructor
public enum EventClassificationEnum {

    PublicManagement("公共管理", "PUBLIC_MANAGEMENT"),
    PublicSecurity("公共安全", "PUBLIC_SECURITY"),
    PublicService("公共服务", "PUBLIC_SERVICE");

    private String name;
    private String code;

}
