package com.mit.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mit.common.web.Result;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.model.WiFiProbeAp;
import com.mit.iot.service.IWiFiProbeApService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description wifi热点AP信息
 */
@Slf4j
@Api(tags = "WIFI探针AP热点信息接口")
@RestController
@RequestMapping("/device/wifi/ap")
public class WiFiProbeApController {

    @Autowired
    private IWiFiProbeApService wiFiProbeApService;

    @ApiOperation(value = "分页查询AP热点信息列表")
    @GetMapping(value = "/pageQuery")
    public Result<IPage<WiFiProbeAp>> queryApInfoByCondition(@Valid WiFiQueryDTO wiFiQueryDTO) {
        IPage<WiFiProbeAp> pageList = wiFiProbeApService.listByCondition(wiFiQueryDTO);
        return Result.succeed(pageList);
    }
}
