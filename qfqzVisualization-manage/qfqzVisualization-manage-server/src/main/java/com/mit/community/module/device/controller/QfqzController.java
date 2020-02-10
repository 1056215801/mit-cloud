package com.mit.community.module.device.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.mit.common.dto.LoginAppUser;
import com.mit.community.entity.*;
import com.mit.community.entity.vo.GroupVo;
import com.mit.community.feign.SysUserFeign;
import com.mit.community.service.*;


import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.sf.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.mit.common.web.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Thread.sleep;

/**
 * 烟感感知
 *
 * @author shuyy
 * @date 2019-01-04
 * @company mitesofor
 */
@RestController
@RequestMapping(value = "/qfqzVisualization")
@Slf4j
@Api(value = "群防群治可视化系统", tags = {"群防群治可视化系统"})
public class QfqzController {

    @Autowired
    private SmokeDetectorStatusService smokeDetectorStatusService;

    @Autowired
    private QfqzUrlService qfqzUrlService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private SysUserFeign sysUserFeign;

    @Autowired
    private ActiveService activeTimeService;

    @Autowired
    private EquipmentService equipmentService;

    @Value("callBackUrl")
    private String callBackUrl;
 /*   *//**
     * 分页
     *
     * @param deviceNum
     * @param devicePalce
     * @param pageNum
     * @param pageSize
     * @return com.mit.community.rest.util.Result
     * @throws
     * @author shuyy
     * @date 2019-01-04 14:53
     * @company mitesofor
     *//*
    @RequestMapping(value = "/listPage", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询", notes = "status 水压情况（压力不足、缺水、压力过大、水压正常）" +
            "device_status 设备状态（正常，故障，掉线）")
    public Result listPage(String communityCode,
                           String deviceNum, String devicePalce, Short status,
                           Short deviceStatus,
                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate gmtUploadStart,
                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate gmtUploadEnd,
                           Integer pageNum,
                           Integer pageSize) {
     *//*   Page<SmokeDetectorStatus> page = smokeDetectorStatusService.listPage(communityCode, deviceNum,
                devicePalce, status, deviceStatus, gmtUploadStart, gmtUploadEnd, pageNum, pageSize);*//*
      *//*  return Result.success(page);*//*
      return null;
    }

    @RequestMapping(value = "/insertData", method = RequestMethod.POST)
    @ApiOperation(value = "插入数据", notes = "插入数据")
    public Result insertData() {
//        smokeDetectorStatusService.delete(null);
     *//*   smokeDetectorStatusService.insertDataKXWT();
        smokeDetectorStatusService.insertDataXJB();
        smokeDetectorStatusService.insertDataYWHDHY();*//*
        smokeDetectorStatusService.insertDataNY();
        return Result.success("成功");
    }
*/

   @RequestMapping(value = "/queryUserInfos", method = RequestMethod.POST)
    @ApiOperation(value = "查询设备列表", notes = "查询设备列表")
    public Result queryUserInfos(String departmentId) {
      String url="http://120.76.189.28:1241/queryUserInfos";
      String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
      String seq=UUID.randomUUID().toString();
      String sign=getSHA256Str(serviceKey+seq);
      //NameValuePair[] data =null;
      NameValuePair[] data = {new NameValuePair("seq", seq),
               new NameValuePair("sign", sign),
               new NameValuePair("departmentId", "6014"),
               new NameValuePair("currPageIndex", "1"),
               new NameValuePair("pageSize", "1000")

      };
       HttpClient httpClient = new HttpClient();
       httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
       httpClient.getParams().setContentCharset("utf-8");
       PostMethod postMethod = new PostMethod(url);
       postMethod.addRequestHeader("Connection", "close");
       postMethod.setRequestBody(data);
       httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
       List<Map> userInfoList=new ArrayList<>();
       String result = StringUtils.EMPTY;
       try {
           int status = httpClient.executeMethod(postMethod);
           int healthStatus = 200;
           if (status != healthStatus) {
               return null;
           }
           BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
           StringBuilder stringBuffer = new StringBuilder();
           String str;
           while ((str = reader.readLine()) != null) {
               stringBuffer.append(str);
           }
           result = stringBuffer.toString();
           JSONObject jsonObject=JSONObject.fromObject(result);
           if("0".equals(jsonObject.get("code").toString())){
               userInfoList= (List<Map>) jsonObject.get("userInfos");
           }
       } catch (Exception e) {
           log.error("发送post请求错误", e);
           return Result.failed("发送post请求错误");
       } finally {
           postMethod.releaseConnection();
       }
       Result result1 = QfqzController.queryOnlinePosInfos("");
       List<Map> posInfoList = (List<Map>)result1.getDatas();
       for (Map map : userInfoList) {
           for (Map map1 : posInfoList) {
               if (map.get("id").toString().equals(map1.get("userId").toString())){
                   map.put("isOnline",1);
                   map.put("lon",map1.get("lon"));
                   map.put("lat",map1.get("lat"));
                   map.put("type",map1.get("type"));
                   map.put("gpsTime",map1.get("gpsTime"));
                   map.put("power",map1.get("power"));

               }
           }
       }
       return Result.succeed(userInfoList);
    }



    @RequestMapping(value = "/queryDepartmentInfos", method = RequestMethod.POST)
    @ApiOperation(value = "查询部门列表", notes = "查询部门列表")
    public Result queryDepartmentInfos() {
        String url="http://120.76.189.28:1241/queryDepartmentInfos";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String seq=UUID.randomUUID().toString();
        String sign=getSHA256Str(serviceKey+seq);
        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign)

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<DepartmentInfo> list=new ArrayList<>();
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                list= (List<DepartmentInfo>) jsonObject.get("departmentInfos");
            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed(list);
    }


    @RequestMapping(value = "/queryOnlinePosInfos", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有在线设备最新位置信息", notes = "查询所有在线设备最新位置信息")
    public static Result queryOnlinePosInfos(String userId ) {
        String url="http://120.76.189.28:1241/queryOnlinePosInfos";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String seq=UUID.randomUUID().toString();
        String sign=getSHA256Str(serviceKey+seq);
        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("departmentId", "6014"),
                new NameValuePair("currPageIndex", "1"),
                new NameValuePair("pageSize", "1000")

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<Map> list=new ArrayList<>();
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                list= (List<Map>) jsonObject.get("posInfos");
            }
            for (Map map : list) {

            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed(list);
    }

    @RequestMapping(value = "/queryPosInfos", method = RequestMethod.POST)
    @ApiOperation(value = "查询设备轨迹信息", notes = "查询设备轨迹信息")
    public Result queryPosInfos(String userId,String date) {
        String url="http://120.76.189.28:1241/queryPosInfos";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String seq=UUID.randomUUID().toString();
        String sign=getSHA256Str(serviceKey+seq);

        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("userId", userId),
                new NameValuePair("date", date),
                new NameValuePair("currPageIndex", "1"),
                new NameValuePair("pageSize", "1000")

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<PosInfo> list=new ArrayList<>();
        String result = StringUtils.EMPTY;
        HashMap<String,Object> map=new HashMap<>();
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            int total=0;
            if("0".equals(jsonObject.get("code").toString())){
                list= (List<PosInfo>) jsonObject.get("posInfos");
               total = (int)jsonObject.get("total");
            }

            map.put("total",total);
            map.put("list",list);
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed(map);
    }
    @RequestMapping(value = "/videoCallBack", method = RequestMethod.GET)
    @ApiOperation(value = "开启终端视频", notes = "开启终端视频")
    public Result videoCallBack(@RequestParam(value="url") String url,@RequestParam(value="userId") String userId) {
        System.out.println("-----------------回调函数被调用--------------------");
        QfqzUrl qfqzUrl=new QfqzUrl();
        qfqzUrl.setUrl(url);
        qfqzUrl.setUserId(userId);

        EntityWrapper<QfqzUrl> wrapper=new EntityWrapper<>();
        wrapper.eq("userId",userId);

        QfqzUrl qfqzUrl1Two=qfqzUrlService.selectOne(wrapper);
        if(qfqzUrl1Two==null){

            qfqzUrlService.insert(qfqzUrl);
        }else{
            qfqzUrlService.update(qfqzUrl,wrapper);
            //qfqzUrlService.updateById(qfqzUrl);
        }
        System.out.print("++++++++++++++++++++"+url+"++++++++++++++"+userId);
        return Result.succeed(url);
    }
    @RequestMapping(value = "/startVideo", method = RequestMethod.POST)
    @ApiOperation(value = "开启终端视频", notes = "开启终端视频")
    public Result startVideo(String userId ,String seq,String groupNumber) {

      //  userId="21451";
        seq=UUID.randomUUID().toString();
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        // seq="12345678";
        String sign=getSHA256Str(serviceKey+seq);
        String callBackUrl="http://120.79.67.123:8240/qfqzVisualization/videoCallBack";
        String url="http://120.76.189.28:1241/startVideo";
       // String url="http://120.76.189.28:1241/startVideo?seq="+seq+"&sign="+sign+"&userId="+userId+"&callBackUrl="+callBackUrl;
//        String urlcall="http://192.168.1.138:8011/cloud-service/qfqzVisualization/videoCallBack";
        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("userId", userId),
                new NameValuePair("callBackUrl", callBackUrl)
//                new NameValuePair("callBackUrl", urlcall)


        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
       /* String charSetName = "utf-8";
        // 创建一个方法实例.
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader( "Connection", "close");
        // 提供定制的重试处理程序是必要的
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));*/
       /* postMethod.setRequestBody(data);*/

        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<PosInfo> list=new ArrayList<>();
        String result = StringUtils.EMPTY;
        QfqzUrl qfqzUrl=null;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
/*                EntityWrapper<Group> wrapper=new EntityWrapper<>();
                wrapper.eq("group_number",groupNumber);
                Group group = groupService.selectOne(wrapper);
                if (group.getActivity()==null){
                    group.setActivity(1);
                    Active activeTime=new Active();
                    activeTime.setActiveTime(LocalDateTime.now());
                    activeTime.setGroupNumber(groupNumber);
                    activeTimeService.insert(activeTime);
                }else {
                    EntityWrapper<Active> entityWrapper=new EntityWrapper<>();
                    entityWrapper.eq("group_number",groupNumber);
                    entityWrapper.orderBy("active_time",false);
                    entityWrapper.last("limit 1");
                    Active activeTime = activeTimeService.selectOne(entityWrapper);
                    LocalDateTime time = activeTime.getActiveTime();
                    Duration duration=Duration.between(time,LocalDateTime.now());
                    long days = duration.toDays();
                    if (days<=1){
                        group.setActivity(group.getActivity()+1);
                    }else {
                        group.setActivity(1);
                    }

                }*/
                sleep(4000);

                EntityWrapper<QfqzUrl> qfqzWrapper=new EntityWrapper<>();
                qfqzWrapper.eq("userId",userId);

                qfqzUrl=qfqzUrlService.selectOne(qfqzWrapper);
            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed(qfqzUrl);
    }

    @RequestMapping(value = "/stopVideo", method = RequestMethod.POST)
    @ApiOperation(value = "关闭终端视频", notes = "关闭终端视频")
    public Result stopVideo(String userId) {
        String  seq=UUID.randomUUID().toString();
      //  userId="21451";
        String url="http://120.76.189.28:1241/stopVideo";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
       // String seq="123456";
        String sign=getSHA256Str(serviceKey+seq);

        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("userId", userId)
        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){

            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed("成功");
    }


    @RequestMapping(value = "/changeMaxTime", method = RequestMethod.POST)
    @ApiOperation(value = "延长视频时间", notes = "延长视频时间")
    public Result changeMaxTime(String userId,String time) {
        String  seq=UUID.randomUUID().toString();
        userId="21451";
        String url="http://120.76.189.28:1241/changeMaxTime";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        // String seq="123456";
        String sign=getSHA256Str(serviceKey+seq);

        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("userId", userId),
                new NameValuePair("time", time)
        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){

            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed("成功");
    }

    @RequestMapping(value = "/changeDefinition", method = RequestMethod.POST)
    @ApiOperation(value = "调整视频清晰度", notes = "调整视频清晰度")
    public Result changeDefinition(String userId,String defintion  ) {
        String  seq=UUID.randomUUID().toString();
       // userId="21451";
        String url="http://120.76.189.28:1241/changeDefinition";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        // String seq="123456";
        String sign=getSHA256Str(serviceKey+seq);

        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("userId", userId),
                new NameValuePair("defintion", defintion)

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<PosInfo> list=new ArrayList<>();
        String result = StringUtils.EMPTY;

        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){

            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
        return Result.succeed("成功");
    }


    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

  /*  public static void main(String[] args) {

       String ss= getSHA256Str("9df75b2c58a4457b91857371a978e916"+"123456");
       System.out.print(ss);
    }
*/

    @ApiOperation(value = "查询设备轨道信息-根据日期范围查询（日期范围最大七天）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="startDate",value = "查询开始日期",dataType = "String",paramType = "query",required = true),
//            @ApiImplicitParam(name = "endDate",value = "查询结束日期",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(name = "userId",value = "设备Id",dataType = "String",paramType = "query")

    })
    @PostMapping("queryPosInfosDateRange")
    public Result queryPosInfosDateRange(String startDate,String endDate,String userId){
    PostMethod postMethod=null;
    List<PosInfo> list=new ArrayList<>();
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(startDate);
            Date date2 = format.parse(endDate);
            long daysBetween=(date2.getTime()-date1.getTime()+1000000)/(60*60*24*1000);
            if (daysBetween>7){
                return Result.failed("最多只能查询一周之内的信息");
            }
            String seq = UUID.randomUUID().toString();
            String url="http://120.76.189.28:1241/queryPosInfosDateRange";
            String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
            String sign=getSHA256Str(serviceKey+seq);
            NameValuePair[] data= {new NameValuePair("seq",seq),new NameValuePair("sign",sign),
                    new NameValuePair("userId",userId),
                    new NameValuePair("startDate",startDate),
                    new NameValuePair("endDate",endDate),
                    new NameValuePair("currPageIndex","1"),new NameValuePair("pageSize","1000")};
            HttpClient httpClient=new HttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpClient.getParams().setContentCharset("utf-8");
            postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Connection", "close");
            postMethod.setRequestBody(data);
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

            String result = StringUtils.EMPTY;
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                list= (List<PosInfo>) jsonObject.get("posInfos");
            }else {
                return Result.failed(jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (postMethod != null) {
            postMethod.releaseConnection();
            }
        }
        return Result.succeed(list);

    }
    @ApiOperation(value = "发送文字广播")
    @PostMapping("/facadeSendBroadcast")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "content",value = "发送内容",dataType = "String",paramType = "query",required = true),
    @ApiImplicitParam(name = "userIds",value = "设备Id",dataType = "String",paramType = "query",required = true)
    })
    public Result facadeSendBroadcast(String content,String userIds){
        PostMethod postMethod=null;
        try {
            String seq = UUID.randomUUID().toString();
            String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
            String sign=getSHA256Str(serviceKey+seq);
            String url="http://120.76.189.28:1241/facadeSendBroadcast";
            String encode = URLEncoder.encode(content, "UTF-8");
            NameValuePair[] data={
                    new NameValuePair("userIds",userIds),
                    new NameValuePair("content",content),
                    new NameValuePair("seq",seq),
                    new NameValuePair("sign",sign)
            };
            HttpClient httpClient= new HttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpClient.getParams().setContentCharset("utf-8");
            postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Connection", "close");
            postMethod.setRequestBody(data);
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            String result = StringUtils.EMPTY;
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            if ("0".equals(result)){
                return Result.succeed("成功");
            }else{
                return Result.failed("发送失败");
            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        }finally {
            postMethod.releaseConnection();
        }
    }
    @ApiOperation(value = "删除群组")
    @ApiImplicitParam(name = "name",value = "群组名称",dataType = "String",required = true,paramType = "query")
    @PostMapping("/deleteGroup")
    public Result deleteGroup(String name){
        PostMethod postMethod=null;
        try {
            String url="https://120.76.189.28:18281/possecu_cs/thirdSystem/delete_group.htm";
            String  seq=UUID.randomUUID().toString();
            String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
            String sign=getSHA256Str(serviceKey+seq);
            NameValuePair[] data = {new NameValuePair("seq", seq),
                    new NameValuePair("sign", sign),
                    new NameValuePair("name", name),
            };
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpClient.getParams().setContentCharset("utf-8");
            postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Connection", "close");
            postMethod.setRequestBody(data);
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            String result=StringUtils.EMPTY;
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                EntityWrapper<Group> wrapper=new EntityWrapper<>();
                wrapper.eq("name",name);
                groupService.delete(wrapper);
                return Result.succeed("成功");
            }else {
                return Result.failed(jsonObject.get("msg").toString());
            }
        } catch (IOException e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }

    }
    @ApiOperation(value = "创建群组/添加成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "群组名称",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(name = "serialNumbers",value = "设备编码",dataType = "String",paramType = "query")
    })
    @PostMapping("/addGroupMember")
    public Result addGroupMember(String name,String serialNumbers,String description,String jurisdictionNumber){

        PostMethod postMethod=null;
        try {
            Group group=new Group();
//            String url="https://120.76.189.28:18281/thirdSystem/add_group_member.htm";
            String url="https://120.76.189.28:18281/possecu_cs/thirdSystem/add_group_member.htm";
            String  seq=UUID.randomUUID().toString();
            String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
            String sign=getSHA256Str(serviceKey+seq);
            NameValuePair[] data = {new NameValuePair("seq", seq),
                    new NameValuePair("sign", sign),
                    new NameValuePair("name", name),
                    new NameValuePair("serialNumbers", serialNumbers)

            };
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
            httpClient.getParams().setContentCharset("utf-8");
            postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Connection", "close");
            postMethod.setRequestBody(data);
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            String result = StringUtils.EMPTY;
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                group.setGroupNumber(uuid);
                group.setDescription(description);
                group.setName(name);
                group.setGmtCreate(LocalDateTime.now());
                group.setGmtModified(LocalDateTime.now());
                group.setType(1);
                group.setSerialNumbers(serialNumbers);
                Result<LoginAppUser> info = sysUserFeign.info();
                LoginAppUser loginAppUser = info.getDatas();
                Integer level = loginAppUser.getLevel();
                if (StringUtils.isEmpty(jurisdictionNumber)){
                    if (level==1){
                        group.setJurisdictionNumber("");
                    }
                    if (level==3) {
                        group.setJurisdictionNumber(loginAppUser.getProvinceCode());
                    }
                    if (level==4) {
                        group.setJurisdictionNumber(loginAppUser.getCityCode());
                    }
                    if (level==5) {
                        group.setJurisdictionNumber(loginAppUser.getAreaCode());
                    }
                    if (level==6) {
                        group.setJurisdictionNumber(loginAppUser.getStreetCode());
                    }
                    if (level==7) {
                        group.setJurisdictionNumber(loginAppUser.getCommitteeCode());
                    }
                    if (level==8) {
                        group.setJurisdictionNumber(loginAppUser.getCommunityCode());
                    }
                }else {
                    group.setSerialNumbers(jurisdictionNumber);
                }
                groupService.insert(group);
                   return Result.succeed(uuid);
            }else {
                return Result.failed(jsonObject.get("msg").toString());
            }


        } catch (IOException e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }
    }
    @ApiOperation(value = "删除成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "群组名称",dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(name = "serialNumbers",value = "设备编号",paramType = "query",dataType = "String")
    })
    @PostMapping("/deleteMember")
    public Result deleteMember(String name,String serialNumbers){
        String  seq=UUID.randomUUID().toString();
        String url="https://120.76.189.28:18281/possecu_cs/thirdSystem/delete_member.htm";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String sign=getSHA256Str(serviceKey+seq);
        //NameValuePair[] data =null;
        NameValuePair[] data = {new NameValuePair("seq", seq),
                new NameValuePair("sign", sign),
                new NameValuePair("name", name),
                new NameValuePair("serialNumbers", serialNumbers)

        };
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Connection", "close");
        postMethod.setRequestBody(data);

        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        List<PosInfo> list=new ArrayList<>();
        String result = StringUtils.EMPTY;

        try {
            int status = httpClient.executeMethod(postMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
            StringBuilder stringBuffer = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            JSONObject jsonObject=JSONObject.fromObject(result);
            if("0".equals(jsonObject.get("code").toString())){
                EntityWrapper<Group> wrapper=new EntityWrapper<>();
                wrapper.eq("name",name);
                Group group = groupService.selectOne(wrapper);
                String groupSerialNumbers = group.getSerialNumbers();
                String[] groupSplit = groupSerialNumbers.split(",");
                String[] numberSplit = serialNumbers.split(",");
                String[] newArray=groupSplit;
                for (int i = 0; i < groupSplit.length; i++) {
                    for (int j = 0; j < numberSplit.length; j++) {
                        if (groupSplit[i]==numberSplit[j]){
                            newArray=ArrayUtils.remove(groupSplit,i);
                        }
                    }
                }
                groupSerialNumbers = StringUtils.join(newArray, ",");
                group.setSerialNumbers(groupSerialNumbers);
                groupService.updateById(group);
                return Result.succeed("成功");
            }else {
                return Result.failed(jsonObject.get("msg").toString());
            }
        } catch (Exception e) {
            log.error("发送post请求错误", e);
            return Result.failed("发送post请求错误");
        } finally {
            postMethod.releaseConnection();
        }

    }

    @GetMapping("/readTime")
    public Result readTime(String serialNumbers,String groupNumber){
        String  seq=UUID.randomUUID().toString();
        String url="https://120.76.189.28:18281/possecu_cs/thirdSystem/real_time_voice.htm";
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String sign=getSHA256Str(serviceKey+seq);
        HttpClient httpClient = new HttpClient();
        String param="?seq="+seq+"&sign="+sign+"&serialNumbers="+serialNumbers+"&closeCallback=voiceCloseCallback";
        url+=param;
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Content-Type", "text/html; charset=UTF-8");
        String result = StringUtils.EMPTY;
        try {
            int status = httpClient.executeMethod(getMethod);
            int healthStatus = 200;
            if (status != healthStatus) {
                return null;
            }
            EntityWrapper<Group> wrapper=new EntityWrapper<>();
                wrapper.eq("group_number",groupNumber);
                Group group = groupService.selectOne(wrapper);
                if (group.getActivity()==null){
                    group.setActivity(1);
                    Active activeTime=new Active();
                    activeTime.setActiveTime(LocalDateTime.now());
                    activeTime.setGroupNumber(groupNumber);
                    activeTimeService.insert(activeTime);
                }else {
                    EntityWrapper<Active> entityWrapper=new EntityWrapper<>();
                    entityWrapper.eq("group_number",groupNumber);
                    entityWrapper.orderBy("active_time",false);
                    entityWrapper.last("limit 1");
                    Active activeTime = activeTimeService.selectOne(entityWrapper);
                    LocalDateTime time = activeTime.getActiveTime();
                    Duration duration=Duration.between(time,LocalDateTime.now());
                    long days = duration.toDays();
                    if (days<=1){
                        group.setActivity(group.getActivity()+1);
                    }else {
                        group.setActivity(1);
                    }

            }
            result = getMethod.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("发送get请求错误", e);
            return Result.failed("发送get请求错误");
        } finally {
            getMethod.releaseConnection();
        }
        return Result.succeed(result);
    }
    @ApiOperation(value = "获取序列号和校验码")
    @PostMapping("/getSeq")
    public Result getSeq(){
        String  seq=UUID.randomUUID().toString();
        String serviceKey="27bf1c196a1f4b99bab5eff3d7e9bc45";
        String sign=getSHA256Str(serviceKey+seq);
        Map<String,String> map=new HashMap<>();
        map.put("seq",seq);
        map.put("sign",sign);
        return Result.succeed(map);
    }
    @ApiOperation(value = "获取群组列表")
    @PostMapping("/getGroupList")
     public Result getGroupList(){
        List<Group> groupList = groupService.selectList(null);
        List<GroupVo> arrayList=new ArrayList();
        for (Group group : groupList) {
            GroupVo groupVo=new GroupVo();
            BeanUtils.copyProperties(group,groupVo);
            String serialNumbers = group.getSerialNumbers();
            String[] numbersArray = serialNumbers.split(",");
            List<String> list = Arrays.asList(numbersArray);
            List<Equipment> equipmentList = equipmentService.selectBatchIds(list);
            groupVo.setEquipmentList(equipmentList);
            arrayList.add(groupVo);
        }
        return Result.succeed(groupList);
    }
}