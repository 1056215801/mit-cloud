package com.mit.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mit.common.web.Result;
import com.mit.iot.dto.hkwifi.WiFiQueryDTO;
import com.mit.iot.model.WiFiProbeAp;
import com.mit.iot.model.WiFiProbeTerminal;
import com.mit.iot.service.IWiFiProbeTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description wifi终端信息
 */
@Slf4j
@Api(tags = "WIFI探针终端信息接口")
@RestController
@RequestMapping("/device/wifi/terminal")
public class WiFiProbeTerminalController {

    @Autowired
    private IWiFiProbeTerminalService wiFiProbeTerminalService;

    @ApiOperation(value = "分页查询设备信息列表")
    @GetMapping(value = "/pageQuery")
    public Result<IPage<WiFiProbeTerminal>> queryApInfoByCondition(@Valid WiFiQueryDTO wiFiQueryDTO) {
        IPage<WiFiProbeTerminal> pageList = wiFiProbeTerminalService.listByCondition(wiFiQueryDTO);
        return Result.succeed(pageList);
    }
}
