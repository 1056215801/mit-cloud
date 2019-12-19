package com.mit.iot.controller;

import com.mit.common.web.Result;
import com.mit.iot.dto.hkwifi.WiFiProbeDTO;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IWiFiProbeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description wifi探针controller
 */
@Slf4j
@Api(tags = "WIFI探针接口")
@RestController
@RequestMapping("/device/wifi")
public class WiFiProbeController {

    @Autowired
    private IWiFiProbeService wiFiProbeService;

    @Autowired
    private IBaseDeviceInfoService baseDeviceInfoService;

    @ApiOperation(value = "新增WIFI探针设备")
    @PostMapping(value = "/save")
    public Result saveInfo(@RequestBody @Valid WiFiProbeDTO wiFiProbeDTO) {
        try {
            baseDeviceInfoService.checkBaseInfo(wiFiProbeDTO);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
        wiFiProbeService.saveWithBaseInfo(wiFiProbeDTO);
        return Result.succeed();
    }

    @ApiOperation(value = "修改WIFI探针设备信息")
    @PutMapping(value = "/update")
    public Result updateInfo(@RequestBody @Valid WiFiProbeDTO wiFiProbeDTO) {
        if (null == wiFiProbeDTO.getId()) {
            return Result.failed("id不能为空");
        }
        try {
            baseDeviceInfoService.checkBaseInfo(wiFiProbeDTO);
            wiFiProbeService.updateWithBaseInfo(wiFiProbeDTO);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
        return Result.succeed();
    }
}
