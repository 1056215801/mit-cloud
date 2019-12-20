package com.mit.iot.controller;

import com.mit.common.web.Result;
import com.mit.iot.model.ConfigInfo;
import com.mit.iot.service.PowerService;
import com.mit.iot.util.PowerUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "电量监控")
@RestController
@RequestMapping("/power")
public class PowerControlController {
    @Autowired
    private PowerService powerService;

    /*@ApiOperation(value = "获取code")
    @RequestMapping(value = "/getCode")
    public Result getCode(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        ConfigInfo configInfo = powerService.getConfigInfo();
        try {
            if (StringUtils.isNotBlank(code)) {
                long currentTime = new Date().getTime();//当前时间
                long getAccessTokenTime = getTimestampOfDateTime(configInfo.getGmtModified());//上次获取accessToken的时间
                int timeAccessLag = (int)((currentTime - getAccessTokenTime)/1000);
                if (timeAccessLag >= 3000) {//过期了

                } else {//没过期，返回成功
                    return Result.succeed("成功");
                }
            } else {
                String redirectURL = URLEncoder.encode("http://"
                        + "120.24.163.79:8888" + "/power/getCode", "UTF-8");

                String OauthURL = "http://ip:port/oauth/authorize.as?client_id="
                        + configInfo.getAppKey()
                        + "&response_type=code"
                        + "&redirect_uri="
                        + redirectURL;
                response.sendRedirect(OauthURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("获取code失败");
        }
        return Result.succeed("成功");
    }*/

    @ApiOperation(value = "获取项目信息")
    @GetMapping(value = "/getProjectInfo")
    public Result getProjectInfo(@RequestParam("communityCodeList") List<String> communityCodeList, String projectCode){
        return powerService.getProjectInfo(projectCode);
    }


    @ApiOperation(value = "获取所有设备信息")
    @GetMapping(value = "/getBoxes")
    public Result getBoxes(@RequestParam("communityCodeList") List<String> communityCodeList,String projectCode){
        return powerService.getBoxes(projectCode);
    }

    @ApiOperation(value = "获取设备实时状态数据",notes = "传参：start 格式：yyyy-MM-dd HH:mm")
    @GetMapping(value = "/getBoxChannelsRealtime")
    public Result getBoxChannelsRealtime(@RequestParam("communityCodeList") List<String> communityCodeList, String mac, String start, String end){
        return powerService.getBoxChannelsRealtime(mac, start, end);
    }

    @ApiOperation(value = "获取设备的告警数据",notes = "传参：start 格式：yyyy-MM-dd HH:mm, type W表示告警/I表示信息")
    @GetMapping(value = "/getBoxAlarm")
    public Result getBoxAlarm(@RequestParam("communityCodeList") List<String> communityCodeList, String mac, String start, String end, Integer pageSize, Integer page, String type, boolean includeCalm){
        return powerService.getBoxAlarm(mac, start, end, pageSize, page, type, includeCalm);
    }

}
