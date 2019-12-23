package com.mit.iot.dto.hkwifi;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description wifi探针AP热点/终端信息查询体
 */
@Data
@ApiModel(description = "WIFI探针AP热点/终端信息查询体")
public class WiFiQueryDTO {

    @ApiModelProperty(value = "平台索引", required = true)
    @NotBlank
    private String indexCode;

    @ApiModelProperty(value = "MAC地址")
    private String mac;

    @ApiModelProperty(value = "第一次被采集到的时间范围起始值, yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date firstAcquisitionTimeStart;

    @ApiModelProperty(value = "第一次被采集到的时间范围终值, yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date firstAcquisitionTimeEnd;

    @ApiModelProperty(value = "最后一次被采集到的时间范围起始值, yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date lastAcquisitionTimeStart;

    @ApiModelProperty(value = "最后一次被采集到的时间范围终值, yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date lastAcquisitionTimeEnd;

    @ApiModelProperty(value = "当前页", required = true, example = "1")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty(value = "页大小", required = true, example = "10")
    @NotNull
    private Integer pageSize;
}
