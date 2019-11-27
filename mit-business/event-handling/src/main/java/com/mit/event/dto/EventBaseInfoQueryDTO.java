package com.mit.event.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 事件基本信息查询条件
 */
@ApiModel(description = "事件基本信息查询条件体")
@Data
public class EventBaseInfoQueryDTO {

    @ApiModelProperty(value = "事件标识", allowableValues = "CONTROL_PERSONNEL(布控人员出现事件)")
    private List<String> eventCode;

    @ApiModelProperty(value = "事件大分类", allowableValues = "PUBLIC_MANAGEMENT(公共管理), PUBLIC_SECURITY(公共安全), " +
            "PUBLIC_SERVICE(公共服务)")
    private List<String> classification;

    @ApiModelProperty(value = "事件类型", allowableValues = "FACILITY_MANAGEMENT(设施管理), PEACE_MANAGEMENT(平安治理), " +
            "ROAD_TRAFFIC(道路交通), LIVELIHOOD_SERVICES(民生服务), EMERGENCY_EVENT(突发事件), OTHERS(其它事件)")
    private List<String> type;

    @ApiModelProperty(value = "事件来源", allowableValues = "ACTIVE_DISCOVERY(主动发现), MASS_REPORT(群众上报), " +
            "DEVICE_AWARENESS(设备感知), SUPERVISORY_CO_ORGANIZER(督办协办)")
    private List<String> source;

    @ApiModelProperty(value = "事件状态\n1 - 未受理, 2 - 处置中, 3 - 已完成, 4 - 超时, 5 - 催单", allowableValues = "range[1,5]")
    private List<Integer> status;

    @ApiModelProperty(value = "触发时间的查询开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date happenedTimeStart;
    @ApiModelProperty(value = "触发时间的查询结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date happenedTimeEnd;

    @ApiModelProperty(value = "当前页")
    private Integer pageNumber;
    @ApiModelProperty(value = "页大小")
    private Integer pageSize;

    @ApiModelProperty(hidden = true)
    private Integer offset;
    @ApiModelProperty(hidden = true)
    private Integer limit;

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
    public Integer getLimit() {
        return pageSize;
    }
}
