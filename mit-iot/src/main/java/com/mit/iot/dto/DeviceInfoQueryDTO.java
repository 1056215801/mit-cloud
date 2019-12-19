package com.mit.iot.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description 设备查询条件
 */
@Data
@ApiModel(description = "设备信息查询体")
public class DeviceInfoQueryDTO {

    @ApiModelProperty(value = "所属小区code")
    private List<String> communityCodeList;

    @ApiModelProperty(value = "所属分区ID")
    private String zoneId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备类型，大小写均可", allowableValues = "WIFI(wifi探针), " +
            "MANHOLE_COVER(智能井盖传感器), FIRE_HYDRANT(消防栓传感器), CIRCUIT_MONITOR(电路监测传感器)")
    private String deviceType;

    @ApiModelProperty(value = "设备编号")
    private String deviceNo;

    @ApiModelProperty(value = "设备序列号")
    private String deviceSerialNo;

    @ApiModelProperty(value = "设备状态，0 - 异常，1 - 正常", example = "1", allowableValues = "range[0,1]")
    private Integer status;

    @ApiModelProperty(value = "当前页", required = true, example = "1")
    @NotNull
    private Integer pageNum;

    @ApiModelProperty(value = "页大小", required = true, example = "10")
    @NotNull
    private Integer pageSize;

}
