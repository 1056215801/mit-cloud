package com.mit.event.controller;

import com.mit.common.web.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 事件统计
 */
@Slf4j
@Api(tags = "事件统计")
@RestController
@RequestMapping("/event/metrics")
public class MetricsController {

    //public Result
}
