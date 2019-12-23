package com.mit.event.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 布控人员出现事件请求类
 */
@ApiModel
@Data
public class ControlPersonnelDTO {
    /**
     * 事件紧急程度
     */
    @ApiModelProperty(value = "事件紧急程度")
    @Range(min = 1, max = 3, message = "事件紧急程度为1-3之间")
    private Integer emergencyLevel;
    /**
     * 事件严重程度
     */
    @Range(min = 1, max = 3, message = "事件严重程度为1-3之间")
    private Integer severity;
    /**
     * 事件触发时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date happenedTime;
    /**
     * 事件发生所在小区code
     */
    @ApiModelProperty(required = true)
    @NotBlank
    private String communityCode;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 抓拍图片地址
     */
    @NotBlank
    private String captureImageUrl;
    /**
     * 抓拍时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date captureTime;
    /**
     * 姓名
     */
    private String username;
    /**
     * 性别
     */
    private String sex;
    /**
     * 证件类型
     */
    private String certificateType;
    /**
     * 证件号码
     */
    private String certificateNumber;
    /**
     * 人员类型
     */
    private String personType;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceCode;
}
