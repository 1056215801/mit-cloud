package com.mit.iot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mit.common.web.Result;
import com.mit.iot.model.ConfigInfo;
import com.mit.iot.model.PublicParams;
import com.mit.iot.service.PowerService;
import com.mit.iot.service.impl.PowerServiceImpl;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class PowerUtil {
    @Autowired
    private PowerServiceImpl powerService;


    /**
     * 获取单个设备信息
     * @param mac
     * @return
     */
    public Result getBox (String mac) {
        ConfigInfo configInfo = powerService.getConfigInfo();
        PublicParams publicParams = new PublicParams(configInfo.getAppKey(),null,configInfo.getAccessToken(),null,null,configInfo.getIpAndPort());
        publicParams.setMethod("GET_BOX");
        Map<String,Object> params = getParamsMap(publicParams);
        params.put("mac", mac);
        try {
            params.put("sign", getSign(params, configInfo.getAppSecret()));
            String result = AccessTokenUtil.sendPost("http://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
            JSONObject json = JSON.parseObject(result);
            if ("0".equals(json.getString("code"))) {
                JSONObject data = json.getJSONObject("data");
                return Result.succeed(data);
            } else {
                return Result.failed("获取失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.failed("获取失败,出现错误");
        }
    }







    /**
     * 获取设备的开关状态
     * @param projectCode
     * @param mac
     * @param addr
     * @return
     */
    /*public Result getBoxChannelsOc (String projectCode, String mac, String addr) {
        PublicParams publicParams = new PublicParams();
        publicParams.setMethod("GET_BOX_CHANNELS_OC");
        Map<String,Object> params = getParamsMap(publicParams);
        params.put("projectCode", projectCode);
        params.put("mac", mac);
        if (StringUtils.isNotBlank(addr)) {
            params.put("addr", addr);
        }
        try {
            String result = AccessTokenUtil.sendPost("http://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
            JSONObject json = JSON.parseObject(result);
            if ("0".equals(json.getString("code"))) {
                JSONObject data = json.getJSONObject("data");
                return Result.succeed(data);
            } else {
                return Result.failed("获取失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.failed("获取失败,出现错误");
        }
    }*/

    public static String getSign(Map<String,Object> params, String appSecret) throws Exception{
        List<String> keyList = new ArrayList<>();
        java.util.Iterator it = params.entrySet().iterator();
        while(it.hasNext()){
            java.util.Map.Entry entry = (java.util.Map.Entry)it.next();
            keyList.add(entry.getKey().toString());    //返回对应的键
        }
        List<String> res = getSort(keyList);//升序排序后的
        StringBuffer sb = new StringBuffer("");
        for (String str : res ) {
            System.out.println(str);
            sb.append(params.get(str));
        }
        return AccessTokenUtil.MD5(sb + appSecret);

    }

    public static List<String> getSort(List<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        return list;
    }


    public static Map<String,Object> getParamsMap(PublicParams publicParams) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());

        Map<String,Object> params = new HashedMap();
        params.put("client_id", publicParams.getClient_id());
        params.put("method", publicParams.getMethod());
        params.put("access_token", publicParams.getAccess_token());
        params.put("timestamp", date);
        return params;
    }

    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }
}
