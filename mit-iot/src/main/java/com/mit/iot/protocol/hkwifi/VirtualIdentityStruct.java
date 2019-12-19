package com.mit.iot.protocol.hkwifi;

import com.mit.iot.util.ByteUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description 终端虚拟身份信息，556个字节，消息类型为4
 */
@Data
@AllArgsConstructor
public class VirtualIdentityStruct {
    /**
     * 结构长度
     */
    private short length;
    /**
     * 终端MAC地址，6个字节长度
     */
    private byte[]  sourceMacAddr;
    /**
     * 获取时间，时间戳
     */
    private int acquisitionTime;
    /**
     * 终端信息，128个字节
     */
    private byte[] deviceContent;
    /**
     * qq号，32个字节
     */
    private byte[] qq;
    /**
     * 微信号，32个字节
     */
    private byte[] weiXin;
    /**
     * 淘宝号，32个字节
     */
    private byte[] taoBao;
    /**
     * 微博，32个字节
     */
    private byte[] sina;
    /**
     * IMEI，32个字节
     */
    private byte[] IMEI;
    /**
     * IMSI，32个字节
     */
    private byte[] IMSI;
    /**
     * 手机号码
     */
    private byte[] phoneNum;
    /**
     * 平台index code，64个字节
     */
    private byte[] indexCode;
    /**
     * 携程，32个字节
     */
    private byte[] ctrip;
    /**
     * 保留字节，96个字节
     */
    private byte[] res;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------终端虚拟身份信息-------").append(System.lineSeparator());
        /*sb.append("|结构长度-----").append(ByteUtils.byte2HexString(ByteUtils.short2HexBytes(length))).append(System.lineSeparator());
        sb.append("|MAC地址-----").append(ByteUtils.byte2HexString(sourceMacAddr)).append(System.lineSeparator());
        sb.append("|获取时间-----").append(ByteUtils.byte2HexString(ByteUtils.int2HexBytes(acquisitionTime))).append(System.lineSeparator());*/
        sb.append("|终端信息-----").append(ByteUtils.byte2HexString(deviceContent)).append(System.lineSeparator());
        sb.append("|qq号--------").append(ByteUtils.byte2HexString(qq)).append(System.lineSeparator());
        sb.append("|微信号------").append(ByteUtils.byte2HexString(weiXin)).append(System.lineSeparator());
        sb.append("|淘宝号------").append(ByteUtils.byte2HexString(taoBao)).append(System.lineSeparator());
        sb.append("|微博号------").append(ByteUtils.byte2HexString(sina)).append(System.lineSeparator());
        sb.append("|IMEI-------").append(ByteUtils.byte2HexString(IMEI)).append(System.lineSeparator());
        sb.append("|IMSI-------").append(ByteUtils.byte2HexString(IMSI)).append(System.lineSeparator());
        sb.append("|手机号码----").append(ByteUtils.byte2HexString(phoneNum)).append(System.lineSeparator());
        sb.append("|平台index code---").append(ByteUtils.byte2HexString(indexCode)).append(System.lineSeparator());
        sb.append("|携程--------").append(ByteUtils.byte2HexString(ctrip)).append(System.lineSeparator());
        sb.append("|保留字节----").append(ByteUtils.byte2HexString(res)).append(System.lineSeparator());
        return sb.toString();
    }
}
