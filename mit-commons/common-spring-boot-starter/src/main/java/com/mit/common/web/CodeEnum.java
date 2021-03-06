package com.mit.common.web;

/**
 * @Description 返回码
 */
public enum CodeEnum {
    SUCCESS(1),
    ERROR(0);

    private Integer code;

    CodeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
