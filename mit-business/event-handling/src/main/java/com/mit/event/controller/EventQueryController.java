package com.mit.event.controller;

import cn.hutool.core.util.EnumUtil;
import com.mit.common.web.Result;
import com.mit.event.dto.EventBaseInfoQueryDTO;
import com.mit.event.enums.EventClassificationEnum;
import com.mit.event.enums.EventEnum;
import com.mit.event.enums.EventSourceEnum;
import com.mit.event.enums.EventStatusEnum;
import com.mit.event.enums.EventTypeEnum;
import com.mit.event.service.IControlPersonnelAppearService;
import com.mit.event.service.IEventBaseInfoService;
import com.mit.event.service.IEventConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 事件查询
 */
@Slf4j
@Api(tags = "事件查询")
@RestController
@RequestMapping("/event/query")
public class EventQueryController {

    @Autowired
    private IEventConfigService eventConfigService;

    @Autowired
    private IEventBaseInfoService baseInfoService;

    @Autowired
    private IControlPersonnelAppearService controlPersonnelAppearService;

    @ApiOperation(value = "根据条件获取事件基本信息")
    @GetMapping(value = "/baseInfo/byCondition")
    public Result queryBaseInfoByCondition(@RequestBody @Valid EventBaseInfoQueryDTO baseInfoQueryDTO) {
        if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getEventCode())) {
            List<String> list = baseInfoQueryDTO.getEventCode().stream().filter(eventCode ->
                    null == EnumUtil.likeValueOf(EventEnum.class, eventCode.toUpperCase())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                return Result.failed("事件标识不匹配，请检查拼写是否正确");
            }
        }
        if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getClassification())) {
            List<String> list = baseInfoQueryDTO.getClassification().stream().filter(classification ->
                    null == EnumUtil.likeValueOf(EventClassificationEnum.class, classification.toUpperCase())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                return Result.failed("事件大分类标识不匹配，请检查拼写是否正确");
            }
        }
        if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getType())) {
            List<String> list = baseInfoQueryDTO.getType().stream().filter(type ->
                    null == EnumUtil.likeValueOf(EventTypeEnum.class, type.toUpperCase())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                return Result.failed("事件类型标识不匹配，请检查拼写是否正确");
            }
        }
        if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getSource())) {
            List<String> list = baseInfoQueryDTO.getSource().stream().filter(source ->
                    null == EnumUtil.likeValueOf(EventSourceEnum.class, source.toUpperCase())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                return Result.failed("事件来源标识不匹配，请检查拼写是否正确");
            }
        }
        if (!CollectionUtils.isEmpty(baseInfoQueryDTO.getStatus())) {
            List<Integer> list = baseInfoQueryDTO.getStatus().stream().filter(status ->
                    null == EnumUtil.likeValueOf(EventStatusEnum.class, status)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(list)) {
                return Result.failed("不存在的事件状态，请检查拼写是否正确");
            }
        }
        return Result.succeed(baseInfoService.getBaseInfoByCondition(baseInfoQueryDTO));
    }

/*
    @ApiOperation(value = "根据事件状态获取事件基本信息")
    @GetMapping(value = "/baseInfo/byStatus")
    public Result queryBaseInfoByStatus(@RequestParam(value = "status", required = false) Integer[] status,
                                        @RequestParam(value = "limit", required = false) Integer limit) {
        QueryWrapper<EventBaseInfo> wrapper = new QueryWrapper<>();
        if (null != status && status.length != 0) {
            wrapper.in("status", Arrays.asList(status));
        }
        wrapper.orderByDesc("happened_time");
        return Result.succeed(baseInfoService.list(wrapper));
    }

    @ApiOperation(value = "根据事件来源及状态获取所有事件基本信息")
    @GetMapping(value = "/baseInfo/bySourceAndStatus")
    public Result queryBaseInfoBySourceAndStatus(@RequestParam String eventSource,
            @RequestParam(value = "status", required = false) Integer[] status) {
        EventSourceEnum eventSourceEnum = EnumUtil.likeValueOf(EventSourceEnum.class, eventSource.toUpperCase());
        if (null == eventSourceEnum) {
            return Result.failed("来源标识不匹配，请检查拼写是否正确");
        }
        List<EventBaseInfo> baseInfos = baseInfoService.getBaseInfoByEventSourceAndStatusIn(eventSource,
                (null == status || status.length == 0) ? null : Arrays.asList(status));
        return Result.succeed(baseInfos);
    }
*/

    @ApiOperation(value = "根据事件标识及基本信息ID获取该事件附加信息")
    @GetMapping(value = "/additional")
    public Result queryAdditionalInfo(@RequestParam String eventCode, @RequestParam String baseInfoId) {
        if (StringUtils.isBlank(eventCode) || StringUtils.isBlank(baseInfoId)) {
            return Result.failed("参数不能为空");
        }

        EventEnum eventEnum = EnumUtil.likeValueOf(EventEnum.class, eventCode.toUpperCase());
        if (null == eventEnum) {
            return Result.failed("事件标识不匹配，请检查拼写是否正确");
        }
        switch (eventEnum) {
            case ControlPersonnelAppear:
                return Result.succeed(controlPersonnelAppearService.getByBaseInfoId(baseInfoId));
            default:
                return Result.succeed();
        }
    }


}
