package com.mit.community.hardware;


import com.mit.common.utils.SpringUtils;
import com.mit.common.web.Result;
import com.mit.community.feign.FileUploadFeign;
import com.mit.community.module.device.controller.AlarmDeviceController;
import com.mit.community.module.hik.face.controller.PlatformController;
import com.mit.community.util.ImgCompass;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
//import sun.misc.BASE64Encoder;
import util.Base64Util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static com.mit.community.util.ImgCompass.bytesToKB;

/**
 * @Author qishengjun
 * @Date Created in 17:16 2019/11/22
 * @Company: mitesofor </p>
 * @Description:~
 */
@Slf4j
public class FMSGCallBack_V31 implements HCNetSDK.FMSGCallBack_V31 {

    private static PlatformController platformController;
    private static AlarmDeviceController alarmDeviceController;
    static {
        platformController = SpringUtils.getBean(PlatformController.class);
        alarmDeviceController=SpringUtils.getBean(AlarmDeviceController.class);
    }

    /*@Autowired
    private PlatformController platformController;*/
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        return false;
    }

    public void AlarmDataHandle(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
//        DefaultTableModel alarmTableModel = ((DefaultTableModel) jTableAlarm.getModel());//获取表格模型
//        AlarmDeviceController alarmDeviceController = new AlarmDeviceController();
        //PlatformController platformController=new PlatformController();
        String sAlarmType = new String();
        String[] newRow = new String[3];
        //报警时间
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String[] sIP = new String[2];
        sAlarmType = new String("lCommand=") + lCommand;
        String serial = new String(pAlarmer.sSerialNumber, StandardCharsets.UTF_8).trim();
        String serialNumber = "";
        if (!StringUtils.isEmpty(serial)) {
            serialNumber = serial.substring(serial.length() - 9);
        }
        switch (lCommand) {
            case HCNetSDK.COMM_UPLOAD_FACESNAP_RESULT:
                //实时人脸抓拍上传
                HCNetSDK.NET_VCA_FACESNAP_RESULT strFaceSnapInfo = new HCNetSDK.NET_VCA_FACESNAP_RESULT();
                strFaceSnapInfo.write();
                Pointer pFaceSnapInfo = strFaceSnapInfo.getPointer();
                pFaceSnapInfo.write(0, pAlarmInfo.getByteArray(0, strFaceSnapInfo.size()), 0, strFaceSnapInfo.size());
                strFaceSnapInfo.read();
                System.out.println("人脸抓拍被触发");
                sAlarmType = sAlarmType + "：人脸抓拍上传，人脸评分：" + strFaceSnapInfo.dwFaceScore + "，年龄段：" + strFaceSnapInfo.struFeature.byAgeGroup + "，性别：" + strFaceSnapInfo.struFeature.bySex;
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType;
                //报警设备IP地址
                sIP = new String(strFaceSnapInfo.struDevInfo.struDevIP.sIpV4).split("\0", 2);
                newRow[2] = sIP[0];
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); //设置日期格式
                String time = df.format(new Date()); // new Date()为获取当前系统时间

                //人脸图片写文件
                try {
                   /* FileOutputStream small = new FileOutputStream(System.getProperty("user.dir") + "\\pic\\" + time + "small.jpg");
                    FileOutputStream big = new FileOutputStream(System.getProperty("user.dir") + "\\pic\\" + time + "big.jpg");*/
                    File file = new File(System.getProperty("user.dir") + "\\pic");
                    if (!file.exists() && !file.isDirectory()) {
                        file.mkdir();
                    }
                    File smallFile = new File(System.getProperty("user.dir") + "\\pic\\" + time + "small.jpg");
                    File bigsmallFile = new File(System.getProperty("user.dir") + "\\pic\\" + time + "big.jpg");
                    FileOutputStream small = new FileOutputStream(smallFile);
                    FileOutputStream big = new FileOutputStream(bigsmallFile);
                    if (strFaceSnapInfo.dwFacePicLen > 0) {
                        try {
                            small.write(strFaceSnapInfo.pBuffer1.getByteArray(0, strFaceSnapInfo.dwFacePicLen), 0, strFaceSnapInfo.dwFacePicLen);
                            FileInputStream inputFile = new FileInputStream(smallFile);
                            byte[] buffer = new byte[(int) smallFile.length()];
                            inputFile.read(buffer);
                            inputFile.close();
                            String base64Url =Base64Util.encode(buffer);
                            String toBase64Byte = ImgCompass.convertFlieImageToBase64Byte(smallFile, 200);
                            platformController.getImageUrl(base64Url, serialNumber,toBase64Byte);
                            small.close();
                        } catch (IOException ex) {
                        }

                    }
                    if (strFaceSnapInfo.dwFacePicLen > 0) {
                        try {
                            big.write(strFaceSnapInfo.pBuffer2.getByteArray(0, strFaceSnapInfo.dwBackgroundPicLen), 0, strFaceSnapInfo.dwBackgroundPicLen);
                            big.close();
                        } catch (IOException ex) {
                        }
                    }
                } catch (FileNotFoundException ex) {
                }
                break;
            case HCNetSDK.COMM_ISAPI_ALARM:
                HCNetSDK.NET_DVR_ALARM_ISAPI_INFO struEventISAPI = new HCNetSDK.NET_DVR_ALARM_ISAPI_INFO();
                struEventISAPI.write();
                Pointer pEventISAPI = struEventISAPI.getPointer();
                pEventISAPI.write(0, pAlarmInfo.getByteArray(0, struEventISAPI.size()), 0, struEventISAPI.size());
                struEventISAPI.read();
                System.out.println("报警被触发");
                sAlarmType = sAlarmType + "：ISAPI协议报警信息, 数据格式:" + struEventISAPI.byDataType +
                        ", 图片个数:" + struEventISAPI.byPicturesNumber;
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType;
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmss");
                String curTime = sf1.format(new Date());
                FileOutputStream foutdata;
                String jsonfilename = "";
                File path = null;
                try {
                    //获取跟目录
                    path = new File(ResourceUtils.getURL("classpath:").getPath());
                    if (!path.exists()) {
                        path = new File("");
                    }
                    System.out.println("path:" + path.getAbsolutePath());
                    jsonfilename =".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + curTime + "_ISAPI_Alarm_" + ".json";
                    File file=new File(jsonfilename);

                    if (!file.exists()) {
                        file.getParentFile().mkdir();
                        file.createNewFile();
                    }
                    foutdata = new FileOutputStream(file);
                    //将字节写入文件
                    ByteBuffer jsonbuffers = struEventISAPI.pAlarmData.getByteBuffer(0, struEventISAPI.dwAlarmDataLen);
                    byte[] jsonbytes = new byte[struEventISAPI.dwAlarmDataLen];
//                    String str=new String(jsonbytes,StandardCharsets.UTF_8);
                    jsonbuffers.rewind();
                    jsonbuffers.get(jsonbytes);
                    foutdata.write(jsonbytes);
                    foutdata.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String imageUrl = "";
                String base64Url="";
                String toBase64Byte="";
                String aByte ="";
                for (int i = 0; i < struEventISAPI.byPicturesNumber; i++) {
                    HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA struPicData = new HCNetSDK.NET_DVR_ALARM_ISAPI_PICDATA();
                    struPicData.write();
                    Pointer pPicData = struPicData.getPointer();
                    pPicData.write(0, struEventISAPI.pPicPackData.getByteArray(i * struPicData.size(), struPicData.size()), 0, struPicData.size());
                    struPicData.read();
                    FileOutputStream fout;
                    try {
                        String filename =".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + curTime +
                                "_ISAPIPic_" + i + "_" + new String(struPicData.szFilename).trim() + ".jpg";
                        File file = new File(filename);
                        if (!file.exists()) {
                            file.getParentFile().mkdir();
                           file.createNewFile();
                        }
                        fout = new FileOutputStream(file);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struPicData.pPicData.getByteBuffer(offset, struPicData.dwPicLen);
                        byte[] bytes = new byte[struPicData.dwPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        if (i == 0) {
                            FileInputStream inputFile = new FileInputStream(file);
                            byte[] buffer = new byte[(int) file.length()];
                            inputFile.read(buffer);
                            inputFile.close();
                            base64Url =Base64Util.encode(buffer);
                            aByte = ImgCompass.convertFlieImageToBase64(file, 200);
                            //计算base64图片的字节数(单位:字节)
                            Integer size = ImgCompass.imageSize(aByte);
                            System.out.println("字节 = "+size);
                            //把字节转换单位为kb或mb
                            float size2 = bytesToKB(size);
                            if (size2>200||size2<10){
                                aByte = ImgCompass.convertFlieImageToBase64Byte(file, 200);
                            }
                            Integer size3 = ImgCompass.imageSize(aByte);
                            System.out.println("字节 = "+size);
                            //把字节转换单位为kb或mb
                            float size4 = bytesToKB(size3);
                            System.out.println("压缩或者扩大的图片字节长度 = "+size4);
                        }
                        System.out.println("打印imageUrl=" + imageUrl);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                try {
                    File file = new File(jsonfilename);
                    String input = FileUtils.readFileToString(file, "UTF-8");
                        platformController.getJsonInfo(input, aByte,serialNumber);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
                break;
            case HCNetSDK.COMM_ITS_PLATE_RESULT:
                HCNetSDK.NET_ITS_PLATE_RESULT strItsPlateResult = new HCNetSDK.NET_ITS_PLATE_RESULT();
                strItsPlateResult.write();
                Pointer pItsPlateInfo = strItsPlateResult.getPointer();
                pItsPlateInfo.write(0, pAlarmInfo.getByteArray(0, strItsPlateResult.size()), 0, strItsPlateResult.size());
                strItsPlateResult.read();
                try {
                    String srt3 = new String(strItsPlateResult.struPlateInfo.sLicense, "GBK");
                    sAlarmType = sAlarmType + ",车辆类型：" + strItsPlateResult.byVehicleType + ",交通抓拍上传，车牌：" + srt3;
                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType;
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                for (int i = 0; i < strItsPlateResult.dwPicNum; i++) {
                    if (strItsPlateResult.struPicInfo[i].dwDataLen > 0) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                        String newName = sf.format(new Date());
                        FileOutputStream fout;
                        try {
                            String filename = ".\\pic\\" + new String(pAlarmer.sDeviceIP).trim() + "_"
                                    + newName + "_type[" + strItsPlateResult.struPicInfo[i].byType + "]_ItsPlate.jpg";
                            fout = new FileOutputStream(filename);
                            //将字节写入文件
                            long offset = 0;
                            ByteBuffer buffers = strItsPlateResult.struPicInfo[i].pBuffer.getByteBuffer(offset, strItsPlateResult.struPicInfo[i].dwDataLen);
                            byte[] bytes = new byte[strItsPlateResult.struPicInfo[i].dwDataLen];
                            buffers.rewind();
                            buffers.get(bytes);
                            fout.write(bytes);
                            fout.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case HCNetSDK.COMM_ALARMHOST_CID_ALARM:
                try {
                    /*NameValuePair[] data = {
                            new NameValuePair("serialNumber", serialNumber)
                    };
                    HttpClient httpClient = new HttpClient();
                    httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
                    httpClient.getParams().setContentCharset("utf-8");
                    PostMethod postMethod = new PostMethod(ConfigBean.ALARMURL);
                    postMethod.addRequestHeader("Connection", "close");
                    postMethod.setRequestBody(data);
                    httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
                    int status = httpClient.executeMethod(postMethod);
                    System.out.println("报警求助被触发"+serialNumber);*/
                    alarmDeviceController.getAlarmInfo(serialNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }

                break;
            default:
                newRow[0] = dateFormat.format(today);
                //报警类型
                newRow[1] = sAlarmType;
                //报警设备IP地址
                sIP = new String(pAlarmer.sDeviceIP).split("\0", 2);
                newRow[2] = sIP[0];
                break;
        }
    }
}
