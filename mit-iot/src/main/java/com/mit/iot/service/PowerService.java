package com.mit.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mit.common.web.Result;
import com.mit.iot.model.ConfigInfo;

public interface PowerService extends IService<ConfigInfo> {
    ConfigInfo getConfigInfo();
    Result getProjectInfo(String projectCode);
    Result getBoxes(String projectCode);
    Result getBoxChannelsRealtime(String mac, String start, String end);
    Result getBoxAlarm (String mac, String start, String end, Integer pageSize, Integer page, String type, boolean includeCalm);
}
