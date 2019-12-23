package com.mit.community.module.device.controller;

import com.mit.common.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @Author qishengjun
 * @Date Created in 9:59 2019/12/6
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "wifi探针")
@RequestMapping(value = "/wifi")
public class WifiController {

    @PostMapping("/getWifiProbe")
    @ApiOperation("获取WIFI探针信息")
    public void getWifiProbe() throws IOException {
        //输出打印
        // 初始化服务端socket并且绑定9999端口
        //
        ServerSocket serverSocket = new ServerSocket(8307);
        int length = 24;
        int count = 0;
        int[] arrs = null;
        //等待客户端的连接
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress());
            //获取输入流,并且指定统一的编码格式
            //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            String hex = binaryToHexString(buffer);
            System.out.println(hex);
            System.out.println(count);
            if (count == 0) {
                arrs = readHead(hex);
            } else {
                count--;
                int type = arrs[4];
                System.out.println(type);
                switch (type) {
                    case 9:
                        readDeviceStatus(hex);
                        break;
                    default:
                        break;
                }
            }
            count = arrs[3];
            if (count == 0) {
                length = 24;
            } else {
                length = arrs[2];
            }
            /*int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                System.out.println(byteArrayToInt(buffer));
            }*/
            /*byte[] data = new byte[1024];
            int len = inputStream.read(data);
            System.out.println(new String(data,0,len));*/

            /*DataInputStream dis = new DataInputStream(socket.getInputStream());
            String msg = dis.readUTF();
            System.out.println(msg);
            dis.close();
            inputStream.close();*/
            inputStream.close();
            socket.close();
        }
    }
    @PostMapping("/getWifiProbeInfo")
    public void getWifiProbeInfo() throws IOException{
        ServerSocket serverSocket = new ServerSocket(8307);
        int length=24;
        while (true){
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress());
            //获取输入流,并且指定统一的编码格式
            //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
        }
    }
    public int[] readHead(String hex) {
        int[] a = {4, 4, 2, 2, 1, 1, 2, 8};
        String str = hex.replaceAll(" ", "");
        int[] arrs = new int[8];
        int cur = 0;
        for(int i=0; i<a.length; i++) {
            arrs[i] = Integer.parseUnsignedInt(str.substring(cur, cur + a[i]*2), 16);
            cur += a[i]*2;
        }
        Arrays.asList(arrs).forEach(arr -> {
            System.out.print(arr + " ");
        });
        return arrs;
    }
    public void readDeviceStatus(String hex) {
        int[] a = {2, 64, 1, 6, 1, 2, 4};
        String str = hex.replaceAll(" ", "");
//        long[] arrs = new long[7];
        int[] arr=new int[7];
        int cur = 0;
        for(int i=0; i<a.length; i++) {
//            arrs[i] = Long.parseUnsignedLong(str.substring(cur, cur + a[i]*2), 16);
            arr[i] =hexStringToString(str.substring(cur, cur + a[i]*2));
            cur += a[i]*2;
        }
//        Arrays.asList(arrs).forEach(arr -> {
//            System.out.print(arr.toString() + " ");
//        });
    }
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        //读取一行数据
        //通过while循环不断读取信息，
//        while ((str = bufferedReader.readLine()) != null) {
//            System.out.println(str);
//        }
        //return Result.succeed("");
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    public static String binaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }

        return result;
    }

    public static String convertHexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for (int i = 0; i < hex.length() - 1; i += 2) {

            String s = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(s, 16);
            sb.append((char) decimal);
            sb2.append(decimal);
        }
        return sb.toString();
    }
    public static int hexStringToString(String s) {
        int anInt=0;
        if (s == null || s.equals("")) {
            return 0;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            anInt = bytesToInt(baKeyword);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return anInt;
    }
    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(255, bs.length - i - 1);
        }
        return a;
    }
}