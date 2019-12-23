package com.mit.iot.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Description 前端新增或修改设备基本信息转换类
 */
@Data
public class BaseDeviceInfoDTO {

    @ApiModelProperty(value = "ID，修改时传递")
    private Long id;

    @ApiModelProperty(value = "所属小区code", required = true)
    @NotBlank
    private String communityCode;

    @ApiModelProperty(value = "所属小区名称")
    private String communityName;

    @ApiModelProperty(value = "所属分区ID")
    private String zoneId;

    @ApiModelProperty(value = "所属分区名称")
    private String zoneName;

    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank
    @Length(max = 64)
    private String deviceName;

    @ApiModelProperty(value = "设备类型，大小写均可\nWIFI - wifi探针\nMANHOLE_COVER - 智能井盖传感器\n" +
            "FIRE_HYDRANT - 消防栓传感器\nCIRCUIT_MONITOR - 电路监测传感器", allowableValues = "WIFI, " +
            "MANHOLE_COVER, FIRE_HYDRANT, CIRCUIT_MONITOR", required = true)
    @NotBlank
    private String deviceType;

    @ApiModelProperty(value = "设备编号")
    @Length(max = 64)
    private String deviceNo;

    @ApiModelProperty(value = "设备序列号")
    @Length(max = 64)
    private String deviceSerialNo;

    @ApiModelProperty(value = "设备位置")
    @Length(max = 128)
    private String deviceLocation;

    @ApiModelProperty(value = "经度")
    @Length(max = 32)
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @Length(max = 32)
    private String latitude;

}
