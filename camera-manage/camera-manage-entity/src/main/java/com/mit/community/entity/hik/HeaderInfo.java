package com.mit.community.entity.hik;

import lombok.Data;

/**
 * @Author qishengjun
 * @Date Created in 14:09 2019/12/6
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class HeaderInfo {

    //报文总长度
    private int dwlength;
    //加密秘钥的校验和
    private int checkSum;

    //<将要接收的单个消息的长度
    private int wInfolength;

    //<将要接收的消息个数
    private int wInfoNum;

    /*<消息类型，0-无，1.WIFI_GPS （1.0 的协议，包含终端和热
    点，新的双 ap 设备已经不支持）2.终端 3.ap 4.终端虚拟身份 5.地理位置信息 9.设备状态
    信息 10.RFID 信息*/
    private int byInfoType;

    private int byRes1;

    private int wVersion;//版本号（目前版本是 1）


}
