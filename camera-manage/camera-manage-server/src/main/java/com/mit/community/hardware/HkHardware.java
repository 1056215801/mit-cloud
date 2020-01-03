package com.mit.community.hardware;


import com.sun.jna.Pointer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author qishengjun
 * @Date Created in 16:56 2019/11/22
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "海康摄像头")
@RequestMapping(value = "/hkHardware")
public class HkHardware {
    static HCNetSDK hCNetSDK = HCNetSDK.INSTANCE;
    HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
    public HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
    public int lUserID=-1;//用户句柄
    public int lAlarmHandle=-1;//报警布防句柄
    public int lListenHandle=-1;//报警监听句柄
    public FMSGCallBack_V31 fMSFCallBack_V31=null;
    @PostMapping(value = "/login")
    @ApiOperation("登录注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "m_sDeviceIP",value = "ip地址",paramType = "query",dataType ="String" ),
            @ApiImplicitParam(name = "m_sUsername",value = "用户名",paramType = "query",dataType ="String" ),
            @ApiImplicitParam(name = "m_sPassword",value = "密码",paramType = "query",dataType ="String" )
    })
    public String login(String m_sDeviceIP,String m_sUsername,String m_sPassword){
        Map<String,Integer> map=new HashMap<String,Integer>();

        boolean initSuc =hCNetSDK.NET_DVR_Init();
        if (initSuc != true)
        {
            return "初始化失败";
        }
                hCNetSDK.NET_DVR_SetLogToFile(3,"D:\\logs",false);
        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struGeneralCfg = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();
        struGeneralCfg.byAlarmJsonPictureSeparate =1; //控制JSON透传报警数据和图片是否分离，0-不分离，1-分离（分离后走COMM_ISAPI_ALARM回调返回）
        struGeneralCfg.write();
        if(!hCNetSDK.NET_DVR_SetSDKLocalCfg(17, struGeneralCfg.getPointer()))
        {
            return "NET_DVR_SetSDKLocalCfg失败";
        }
        hCNetSDK.NET_DVR_SetConnectTime(2000,1);
        hCNetSDK.NET_DVR_SetReconnect(100000,true);
//        if (lUserID > -1) {
//            //先注销
//            hCNetSDK.NET_DVR_Logout(lUserID);
//            lUserID = -1;
//        }

        //注册

        System.out.println(ConfigBean.COMMUNITY_CODE);
        System.out.println(System.getProperty("user.dir"));
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        m_sPassword = new String(m_sPassword);//设备密码
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = (short)Integer.parseInt("8000");
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.write();
        lUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        map.put(m_sDeviceIP,lUserID);
        log.debug("打印输出"+ConfigBean.COMMUNITY_CODE);
        if (lUserID == -1) {
            hCNetSDK.NET_DVR_Cleanup();
            return "注册失败";
        } else {
            SetupAlarmChan(map,m_sDeviceIP);
           /* hCNetSDK.NET_DVR_Logout(lUserID);
            hCNetSDK.NET_DVR_Cleanup();*/
//            hCNetSDK.NET_DVR_Cleanup();
            return "注册成功";
        }
    }
    public String SetupAlarmChan(Map<String,Integer> map,String m_sDeviceIP) {

        if (map.get(m_sDeviceIP)== -1) {
            return "请先注册";
        }
  /*      if (lAlarmHandle < 0)//尚未布防,需要布防
        {*/
            if (fMSFCallBack_V31 == null) {
                fMSFCallBack_V31 = new FMSGCallBack_V31();
                Pointer pUser = null;
                if (!hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                    System.out.println("设置回调函数失败!");
                }
            }
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel = 1;//智能交通布防优先级：0- 一等级（高），1- 二等级（中），2- 三等级（低）
            m_strAlarmInfo.byAlarmInfoType = 1;//智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
            m_strAlarmInfo.byDeployType = 1; //布防类型(仅针对门禁主机、人证设备)：0-客户端布防(会断网续传)，1-实时布防(只上传实时数据)
            m_strAlarmInfo.byAlarmTypeURL=1;
            m_strAlarmInfo.write();
            lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(map.get(m_sDeviceIP), m_strAlarmInfo);
            map.put("lAlarmHandle"+m_sDeviceIP,lAlarmHandle);
            if (map.get("lAlarmHandle"+m_sDeviceIP) == -1) {
                System.out.println("布防失败,错误号:" + hCNetSDK.NET_DVR_GetLastError());
                hCNetSDK.NET_DVR_Logout(map.get(m_sDeviceIP));
                hCNetSDK.NET_DVR_Cleanup();
                return "布防失败";
            } else {
                return "布防成功";

            }
//        }
//        return "布防成功";
    }
    @GetMapping("/getImageUrl")
    public String getImageUrl(String imageUrl){
        System.out.println("获取到的图片路径"+imageUrl);
        return imageUrl;
    }
}
