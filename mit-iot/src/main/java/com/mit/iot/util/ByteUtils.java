package com.mit.iot.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * @Description 字节转换工具
 */
public class ByteUtils {

    private static final char[] ascii = "0123456789ABCDEF".toCharArray();

    /**
     * short类型转换为字节数组
     * @param s 短整型值
     * @return 字节数组
     */
    public static byte[] short2Bytes(short s) {
        return ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).putShort(s).array();
    }

    /**
     * int类型转换为字节数组
     * @param n 整型值
     * @return 字节数组
     */
    public static byte[] int2Bytes(int n) {
        return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(n).array();
    }

    /**
     * long类型转换为字节数组
     * @param n 长整型
     * @return 字节数组
     */
    public static byte[] long2Bytes(long n) {
        return ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(n).array();
    }

    /**
     * 无符号byte类型，即short类型转字节数组
     * @param n short类型数值
     * @return 字节数组
     */
    public static byte unsignedByte2Bytes(short n) {
        return (byte) (n & 0xff);
    }

    /**
     * 无符号byte数组，即short数组转字节数组
     * @param n short数组
     * @return 字节数组
     */
    public static byte[] unsignedByte2Bytes(short[] n) {
        byte[] bytes = new byte[n.length];
        for (int i=0; i<n.length; i++) {
            bytes[i] = (byte) (n[i] & 0xff);
        }
        return bytes;
    }

    /**
     * 无符号short类型，即整型转字节数组
     * @param n 整型数值
     * @return 字节数组
     */
    public static byte[] unsignedShort2Bytes(int n) {
        byte[] bytes = new byte[2];
        for (int i = 0; i < 2; i++) {
            int i1 = (1 - i) << 3;
            bytes[i] = (byte) ((n >> i1) & 0xff);
        }
        return bytes;
    }

    /**
     * 无符号int类型，即长整型转字节数组
     * @param n 长整型数值
     * @return 字节数组
     */
    public static byte[] unsignedInt2Bytes(long n) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            int i1 = (3 - i) << 3;
            bytes[i] = (byte) ((n >> i1) & 0xff);
        }
        return bytes;
    }

    /**
     * 字节转换为十六进制字符串
     * @param b 一个字节
     * @return 转换的十六进制字符串
     */
    public static String byte2HexString(byte b) {
        return String.format("%02X", b);
    }

    /**
     * 字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 转换的十六进制字符串
     */
    public static String byte2HexString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (byte aByte : bytes) {
            str.append(String.format("%02X", aByte));
        }
        return str.toString();
    }

    /**
     * 字节数组转换为字符串
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String byte2String(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * 字节数组转换为字符串
     * @param bytes 字节数组
     * @param charsetName 字节编码格式
     * @return 字符串
     */
    public static String byte2String(byte[] bytes, String charsetName) {
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 十六进制字符串转int
     * @param hexString 16进制字符串
     * @return int
     */
    public static int hexString2Int(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    /**
     * 16进制字符串转string
     * @param hexString 16进制字符串
     * @return 字符串
     */
    public static String hexStringToString(String hexString) {
        StringBuilder sb = new StringBuilder();
        //StringBuilder sb2 = new StringBuilder();

        for (int i = 0; i < hexString.length() - 1; i += 2) {
            String s = hexString.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            sb.append((char) decimal);
            //sb2.append(decimal);
        }
        return sb.toString();
    }

    /**
     * BCD码单字节转换为十进制
     * @param bcd BCD码字节
     * @return 十进制数据
     */
    public static String bcd2String(byte bcd) {
        byte[] temp = new byte[2];
        temp[0] = (byte) ((bcd >> 4) & 0x0f);
        temp[1] = (byte) (bcd & 0x0f);
        StringBuilder res = new StringBuilder();
        for (byte b : temp) {
            res.append(ascii[b]);
        }
        return res.toString();
    }

    /**
     * BCD码字节数组转换为十进制
     * @param bcd BCD码字节数组
     * @return 十进制串
     */
    public static String bcd2String(byte[] bcd) {
        if (bcd == null || bcd.length == 0) {
            return null;
        }
        byte[] temp = new byte[2 * bcd.length];
        for (int i = 0; i < bcd.length; i++) {
            temp[i * 2] = (byte) ((bcd[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bcd[i] & 0x0f);
        }
        StringBuilder res = new StringBuilder();
        for (byte b : temp) {
            res.append(ascii[b]);
        }
        return res.toString();
    }

    /**
     * 获取字节高四位
     * @param b 字节
     * @return 十进制
     */
    public static int bcdByteHeight4(byte b) {
        return ((b & 0xf0) >> 4);
    }

    /**
     * 获取字节低四位
     * @param b 字节
     * @return 十进制
     */
    public static int bcdByteLow4(byte b) {
        return (b & 0x0f);
    }

}
