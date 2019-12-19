package com.mit.iot.enums.hkwifi;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description wifi密码加密方式
 */
@Getter
@AllArgsConstructor
public enum WIFISpotEncryptType {
    NONE(0, "", "不加密"),
    WPA_WPA2(1, "WPA/WPA2", "WPA/WPA2"),
    WPA_PSK_WPA2_PSK(2, "WPA-PSK/WPA2-PSK", "WPA-PSK/WPA2-PSK"),
    WEP(3, "WEP", "WEP");

    private int type;
    private String value;
    private String desc;
}
