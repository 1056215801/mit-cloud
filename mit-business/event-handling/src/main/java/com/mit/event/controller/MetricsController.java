package com.mit.event.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.mit.common.web.Result;
import com.mit.event.service.IEventBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 事件统计
 */
@Slf4j
@Api(tags = "事件统计")
@RestController
@RequestMapping("/event/metrics")
public class MetricsController {

    @Autowired
    private IEventBaseInfoService baseInfoService;

    @ApiOperation(value = "按事件状态统计")
    @GetMapping(value = "/byStatus")
    public Result metricsByStatus() {
        return Result.succeed(baseInfoService.metricsByStatus());
    }

    @ApiOperation(value = "按事件类型统计")
    @GetMapping(value = "/byType")
    public Result metricsByType() {
        return Result.succeed(baseInfoService.metricsByType());
    }

    @ApiOperation(value = "按事件来源和紧急程度统计")
    @GetMapping(value = "/bySourceAndEmergency")
    public Result metricsBySourceAndEmergencyLevel() {
        return Result.succeed(baseInfoService.metricsBySourceAndEmergencyLevel());
    }

    @ApiOperation(value = "按事件来源和状态及时间统计")
    @GetMapping(value = "/bySourceAndStatusBetweenTime")
    public Result metricsBySourceAndStatusBetweenTime(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            DateUtil.parse(startTime, DatePattern.NORM_DATE_PATTERN);
            DateUtil.parse(endTime, DatePattern.NORM_DATE_PATTERN);
        } catch (Exception e) {
            return Result.failed("时间格式不正确，格式为yyyy-MM-dd, " + e.getMessage());
        }
        return Result.succeed(baseInfoService.metricsBySourceAndStatusBetweenTime(startTime, endTime));
    }

    @ApiOperation(value = "按事件类型及时间统计")
    @GetMapping(value = "/byTypeBetweenTime")
    public Result metricsByTypeBetweenTime(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            DateUtil.parse(startTime, "yyyy-MM");
            DateUtil.parse(endTime, "yyyy-MM");
        } catch (Exception e) {
            return Result.failed("时间格式不正确，格式为yyyy-MM, " + e.getMessage());
        }
        return Result.succeed(baseInfoService.metricsByTypeBetweenTime(startTime, endTime));
    }
}
