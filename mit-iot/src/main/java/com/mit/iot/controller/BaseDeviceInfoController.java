package com.mit.iot.controller;

import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mit.common.web.Result;
import com.mit.iot.dto.DeviceInfoQueryDTO;
import com.mit.iot.enums.DeviceTypeEnum;
import com.mit.iot.model.BaseDeviceInfo;
import com.mit.iot.service.IBaseDeviceInfoService;
import com.mit.iot.service.IWiFiProbeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description 设备基本信息控制类
 */
@Slf4j
@Api(tags = "设备基本信息接口")
@RestController
@RequestMapping("/device")
public class BaseDeviceInfoController {

    @Autowired
    private IBaseDeviceInfoService baseDeviceInfoService;

    @Autowired
    private IWiFiProbeService wiFiProbeService;

    @ApiOperation(value = "分页查询设备信息列表")
    @GetMapping(value = "/pageQuery")
    public Result<IPage<BaseDeviceInfo>> queryBaseInfoByCondition(@Valid DeviceInfoQueryDTO deviceInfoQueryDTO) {
        IPage<BaseDeviceInfo> pageList = baseDeviceInfoService.listByCondition(deviceInfoQueryDTO);
        pageList.getRecords().forEach(baseDeviceInfo -> {
            DeviceTypeEnum deviceTypeEnum = EnumUtil.likeValueOf(DeviceTypeEnum.class, baseDeviceInfo.getDeviceType());
            switch (deviceTypeEnum) {
                case WIFI:
                    baseDeviceInfo.setExtension(wiFiProbeService.getByBaseInfoId(baseDeviceInfo.getId()));
                    break;
                default:
                    break;
            }
        });
        return Result.succeed(pageList);
    }

    @ApiOperation(value = "删除设备信息")
    @DeleteMapping(value = "/deleteByIds")
    public Result deleteByIds(@RequestParam(value = "idList") List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Result.failed("参数为空");
        }
        baseDeviceInfoService.removeCascadeByIds(idList);
        return Result.succeed();
    }

    /**
     * 统计设备数量
     * @param communityCodeList 所属小区
     * @param deviceTypeList 设备类型
     * @param status 设备状态
     * @return
     */
    @ApiOperation(value = "统计设备数量")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "communityCodeList", value = "所属小区", allowMultiple = true),
            @ApiImplicitParam(name = "deviceTypeList", value = "设备类型，大小写均可\nWIFI - wifi探针\n" +
                    "MANHOLE_COVER - 智能井盖传感器\nFIRE_HYDRANT - 消防栓传感器\nCIRCUIT_MONITOR - 电路监测传感器",
                    allowableValues = "WIFI, MANHOLE_COVER, FIRE_HYDRANT, CIRCUIT_MONITOR",
                    allowMultiple = true),
            @ApiImplicitParam(name = "status", value = "设备状态，0 - 异常，1 - 正常", dataType = "int",
                    allowableValues = "range[0,1]", allowMultiple = true)
    })
    @GetMapping(value = "/metricsDeviceNum")
    public Result metricsByCondition(@RequestParam(value = "communityCodeList", required = false) List<String> communityCodeList,
                                    @RequestParam(value = "deviceTypeList", required = false) List<String> deviceTypeList,
                                    @RequestParam(value = "status", required = false) List<Integer> status) {
        return Result.succeed(baseDeviceInfoService.metricsDeviceNum(communityCodeList, deviceTypeList, status));
    }

}
