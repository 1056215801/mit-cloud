package com.mit.iot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mit.common.web.Result;
import com.mit.iot.mapper.PowerMapper;
import com.mit.iot.model.ConfigInfo;
import com.mit.iot.model.PublicParams;
import com.mit.iot.service.PowerService;
import com.mit.iot.util.AccessTokenUtil;
import com.mit.iot.util.PowerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, ConfigInfo> implements PowerService {
    @Autowired
    private PowerMapper powerMapper;

    @Override
    public ConfigInfo getConfigInfo() {
        ConfigInfo configInfo = powerMapper.selectById(1);
        long currentTime = new Date().getTime();//当前时间
        long getAccessTokenTime = Long.valueOf(configInfo.getGmtModified());
        int timeAccessLag = (int)((currentTime - getAccessTokenTime)/1000);
        if (timeAccessLag >= 3000) {//过期了,重新获取
            //先获取code
            Map<String,Object> params = new HashedMap();
            params.put("response_type", "code");
            params.put("client_id", configInfo.getAppKey());
            params.put("redirect_uri", configInfo.getRedirectUri());
            params.put("uname", configInfo.getUname());
            params.put("passwd", configInfo.getPasswd());
            try {
                String result = AccessTokenUtil.sendPost("https://"+ configInfo.getIpAndPort() +"/oauth/authverify2.as",params);
                JSONObject json = JSON.parseObject(result);
                if ("true".equals(json.getString("success"))) {
                    String code = json.getString("code");
                    Map<String,Object> paramsToken = new HashedMap();
                    paramsToken.put("client_id", configInfo.getAppKey());
                    paramsToken.put("client_secret", AccessTokenUtil.getClientSecret(configInfo.getAppKey(),"authorization_code",configInfo.getRedirectUri(), code, configInfo.getAppSecret()));
                    paramsToken.put("grant_type", "authorization_code");
                    paramsToken.put("redirect_uri", configInfo.getRedirectUri());
                    paramsToken.put("code", code);
                    String resultToken = AccessTokenUtil.sendPost("https://"+ configInfo.getIpAndPort() +"/oauth/token.as",paramsToken);
                    JSONObject jsonToken = JSON.parseObject(resultToken);
                    if ("0".equals(jsonToken.getString("code"))) {
                        JSONObject data = jsonToken.getJSONObject("data");
                        String accessToken = data.getString("accessToken");
                        configInfo.setAccessToken(accessToken);
                        configInfo.setGmtModified(String.valueOf(System.currentTimeMillis()));
                        powerMapper.updateById(configInfo);
                    } else {
                        log.error("=====================获取token失败"+resultToken);
                    }
                } else {
                    log.error("=====================获取code失败"+result);
                }
            } catch (Exception e){
                e.printStackTrace();
                log.error("=====================刷新token出现异常"+e);
            }
        } else {//没过期，返回成功
            return configInfo;
        }
        return configInfo;
    }

    /**
     * 获取项目信息
     * @param projectCode
     * @return
     */
    public Result getProjectInfo (String projectCode) {
        ConfigInfo configInfo = getConfigInfo();
        PublicParams publicParams = new PublicParams(configInfo.getAppKey(),null,configInfo.getAccessToken(),null,null,configInfo.getIpAndPort());
        publicParams.setMethod("GET_PROJECT_INFO");
        Map<String,Object> params = PowerUtil.getParamsMap(publicParams);
        params.put("projectCode", configInfo.getProjectCode());
        try {
            params.put("sign", PowerUtil.getSign(params, configInfo.getAppSecret()));
            String result = AccessTokenUtil.sendPost("https://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
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
     * 获取项目内的所有设备(需要)
     * @param projectCode
     * @return
     */
    public Result getBoxes (String projectCode) {
        ConfigInfo configInfo = getConfigInfo();
        PublicParams publicParams = new PublicParams(configInfo.getAppKey(),null,configInfo.getAccessToken(),null,null,configInfo.getIpAndPort());
        publicParams.setMethod("GET_BOXES");
        Map<String,Object> params = PowerUtil.getParamsMap(publicParams);
        params.put("projectCode", configInfo.getProjectCode());
        try {
            params.put("sign", PowerUtil.getSign(params, configInfo.getAppSecret()));//https://open.snd02.com/oauth/authorize.as
            String result = AccessTokenUtil.sendPost("https://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
            JSONObject json = JSON.parseObject(result);
            if ("0".equals(json.getString("code"))) {
                JSONArray data = json.getJSONArray("data");
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
     * 获取设备实时状态数据(需要)
     * @param projectCode
     * @param mac
     * @return
     */
    public Result getBoxChannelsRealtime (String mac, String start, String end) {
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sim1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<JSONObject> returnData = new ArrayList<>();
        ConfigInfo configInfo = getConfigInfo();
        PublicParams publicParams = new PublicParams(configInfo.getAppKey(),null,configInfo.getAccessToken(),null,null,configInfo.getIpAndPort());
        publicParams.setMethod("GET_BOX_CHANNELS_REALTIME");
        Map<String,Object> params = PowerUtil.getParamsMap(publicParams);
        params.put("projectCode", configInfo.getProjectCode());
        params.put("mac", mac);
        try {
            params.put("sign", PowerUtil.getSign(params, configInfo.getAppSecret()));
            String result = AccessTokenUtil.sendPost("https://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
            JSONObject json = JSON.parseObject(result);
            if ("0".equals(json.getString("code"))) {
                JSONArray data = json.getJSONArray("data");
                if (data.size() > 0) {
                    for (int i=0; i<data.size(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        long time = sim1.parse(object.getString("updateTime")).getTime();
                        if (StringUtils.isNotBlank(start)&&StringUtils.isBlank(end)) {
                            long startTime = sim.parse(start).getTime();
                            if (time >= startTime) {
                                returnData.add(object);
                            }
                        } else if (StringUtils.isBlank(start)&&StringUtils.isNotBlank(end)) {
                            long endTime = sim.parse(end).getTime();
                            if (time <= endTime) {
                                returnData.add(object);
                            }
                        } else if (StringUtils.isNotBlank(start)&&StringUtils.isNotBlank(end)) {
                            long startTime = sim.parse(start).getTime();
                            long endTime = sim.parse(end).getTime();
                            if (time >= startTime && time <= endTime) {
                                returnData.add(object);
                            }
                        } else {
                            returnData.add(object);
                        }
                    }
                }
                return Result.succeed(returnData);
            } else {
                return Result.failed("获取失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.failed("获取失败,出现错误");
        }
    }


    /**
     * 获取设备的告警数据(需要)
     * @param projectCode
     * @param mac
     * @param start
     * @param end
     * @param pageSize
     * @param page
     * @param type
     * @param includeCalm
     * @return
     */
    public Result getBoxAlarm (String mac, String start, String end, Integer pageSize, Integer page, String type, boolean includeCalm) {
        ConfigInfo configInfo = getConfigInfo();
        PublicParams publicParams = new PublicParams(configInfo.getAppKey(),null,configInfo.getAccessToken(),null,null,configInfo.getIpAndPort());
        publicParams.setMethod("GET_BOX_ALARM");
        Map<String,Object> params = PowerUtil.getParamsMap(publicParams);
        params.put("projectCode", configInfo.getProjectCode());
        params.put("mac", mac);
        params.put("start", start);
        params.put("end", end);
        if (pageSize != null) {
            params.put("pageSize", pageSize);
        }
        if (page != null) {
            params.put("page", page);
        }
        if (StringUtils.isNotBlank(type)) {
            params.put("type", type);
        }
        if (includeCalm) {
            params.put("includeCalm", includeCalm);
        }
        try {
            params.put("sign", PowerUtil.getSign(params, configInfo.getAppSecret()));
            String result = AccessTokenUtil.sendPost("https://"+ publicParams.getIpAndPort() +"/invoke/router.as",params);
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


}
