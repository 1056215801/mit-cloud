package com.mit.iot.controller;

import com.mit.common.web.Result;
import com.mit.iot.dto.BaseDeviceInfoDTO;
import com.mit.iot.model.BaseDeviceInfo;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IFireHydrantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description 消防栓controller
 */
@Slf4j
@Api(tags = "消防栓")
@RestController
@RequestMapping("/device/fireHydrant")
public class FireHydrantController {

    @Autowired
    private IFireHydrantService fireHydrantService;

    @Autowired
    private IBaseDeviceInfoService baseDeviceInfoService;

    @ApiOperation(value = "新增消防栓设备")
    @PostMapping(value = "/save")
    public Result saveInfo(@RequestBody @Valid BaseDeviceInfoDTO baseDeviceInfoDTO) {
        if (StringUtils.isEmpty(baseDeviceInfoDTO.getDeviceNo())) {
            return Result.failed("设备编号不能为空");
        }
        try {
            baseDeviceInfoService.checkBaseInfo(baseDeviceInfoDTO);
        } catch (Exception e) {
            return Result.failed(e.getMessage());
        }
        fireHydrantService.saveWithBaseInfo(baseDeviceInfoDTO);
        return Result.succeed();
    }

    @ApiOperation(value = "修改消防栓设备信息")
    @PutMapping(value = "/update")
    public Result updateInfo(@RequestBody @Valid BaseDeviceInfoDTO baseDeviceInfoDTO) {
        if (null == baseDeviceInfoDTO.getId()) {
            return Result.failed("id不能为空");
        }
        BaseDeviceInfo dbBaseDeviceInfo = baseDeviceInfoService.getById(baseDeviceInfoDTO.getId());
        if (dbBaseDeviceInfo == null) {
            return Result.failed("不能更新不存在的设备信息");
        }
        try {
            baseDeviceInfoService.checkBaseInfo(baseDeviceInfoDTO);
        } catch (Exception e) {
            if ((long) dbBaseDeviceInfo.getId() != baseDeviceInfoDTO.getId()) {
                return Result.failed(e.getMessage());
            }
        }

        fireHydrantService.updateWithBaseInfo(baseDeviceInfoDTO);
        return Result.succeed();
    }
}
