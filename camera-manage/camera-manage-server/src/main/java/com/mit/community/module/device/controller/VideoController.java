package com.mit.community.module.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mit.common.web.Result;
import com.mit.community.config.UrlConfig;
import com.mit.community.entity.hik.DeviceInfo;
import com.mit.community.entity.hik.IntranetPenetration;
import com.mit.community.entity.hik.Transcode;
import com.mit.community.service.com.mit.community.service.hik.DeviceInfoService;
import com.mit.community.service.com.mit.community.service.hik.IntranetPenetrationService;
import com.mit.community.service.com.mit.community.service.hik.TranscodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author qishengjun
 * @Date Created in 15:50 2019/12/5
 * @Company: mitesofor </p>
 * @Description:~
 */
@RestController
@Slf4j
@Api(tags = "视频播放")
@RequestMapping(value = "/video")
public class VideoController {
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private TranscodeService transcodeService;
    @Autowired
    private IntranetPenetrationService intranetPenetrationService;
    @PostMapping("/startVideo")
    @ApiOperation("开启视频")
    public Result startVideo(String serialNumber,String communityCode,String ip){

        StringBuilder stringBuffer = new StringBuilder();
        QueryWrapper<IntranetPenetration> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("community_code",communityCode);
        IntranetPenetration intranetPenetration = intranetPenetrationService.getOne(queryWrapper);
        String localIpAdress="";
        if (intranetPenetration!=null) {
            localIpAdress=intranetPenetration.getLocalIpAdress();
        }
        showDevices(localIpAdress);
        QueryWrapper<Transcode> wrapper=new QueryWrapper<>();
        wrapper.eq("community_code",communityCode);
        wrapper.eq("ipAdress",ip);
        Transcode transcode = transcodeService.getOne(wrapper);
        String transcodeChannel="";
        if (transcode!=null){
            transcodeChannel = transcode.getChannel();
        }

        try {
            NameValuePair[] data = {new NameValuePair("serialNumber", serialNumber),
                    new NameValuePair("channel", transcodeChannel)
            };
//            String url="http://192.168.1.59:8011/cloud-service/hkVideoDevice/startVideo";
//             String url="http://23zh498270.wicp.vip/cloud-service/hkVideoDevice/startVideo";
           String url=localIpAdress+"/cloud-service/hkVideoDevice/startVideo";
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpClient.getParams().setContentCharset("utf-8");
            PostMethod postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Connection", "close");
            postMethod.setRequestBody(data);
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
           return Result.succeed(stringBuffer.toString(),"开启视频成功");
    }
      @PostMapping("/stopVideo")
      @ApiOperation("关闭视频")
      public Result stopVideo(String videoUrl){
          String [] arrs={};
          if (!StringUtils.isEmpty(videoUrl)){
              arrs=videoUrl.split("/");
          }
          IntranetPenetration penetration=deviceInfoService.getInfo(arrs[2]);
          String localIpAdress="";
          if (penetration!=null) {
              localIpAdress=penetration.getLocalIpAdress();
          }
          StringBuilder stringBuffer = new StringBuilder();
          try {
              NameValuePair[] data = {
                      new NameValuePair("videoUrl", videoUrl)
              };
//              String url="http://192.168.1.59:8011/cloud-service/hkVideoDevice/stopVideo2";
//                String url="http://23zh498270.wicp.vip/cloud-service/hkVideoDevice/stopVideo2";
              String url=localIpAdress+"/cloud-service/hkVideoDevice/stopVideo2";
              HttpClient httpClient = new HttpClient();
              httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
              httpClient.getParams().setContentCharset("utf-8");
              PostMethod postMethod = new PostMethod(url);
              postMethod.addRequestHeader("Connection", "close");
              postMethod.setRequestBody(data);
              httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
              int status = httpClient.executeMethod(postMethod);
              int healthStatus = 200;
              if (status != healthStatus) {
                  return Result.failed("关闭失败");
              }else {
                  return Result.succeed("关闭成功");
              }
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
          }
          return Result.succeed("关闭成功");
      }
      public void showDevices(String localIpAdress){
          StringBuilder stringBuffer = new StringBuilder();
          try {
              NameValuePair[] data = {
//                      new NameValuePair("videoUrl", UrlConfig.SHOWDEVICEURL)
              };
//              String url="http://192.168.1.59:8011/cloud-service/hkVideoDevice/stopVideo2";
//              String url="http://23zh498270.wicp.vip/cloud-service/hkVideoDevice/stopVideo2";
              String url=localIpAdress+"/cloud-service/hkVideoDevice/showDevices";
              HttpClient httpClient = new HttpClient();
              httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
              httpClient.getParams().setContentCharset("utf-8");
              PostMethod postMethod = new PostMethod(url);
              postMethod.addRequestHeader("Connection", "close");
              postMethod.setRequestBody(data);
              httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
              int status = httpClient.executeMethod(postMethod);
              int healthStatus = 200;
          } catch (IOException e) {
              e.printStackTrace();
          } finally {
          }
      }
}
