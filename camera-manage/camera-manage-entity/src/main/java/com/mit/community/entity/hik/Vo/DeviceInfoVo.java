package com.mit.community.entity.hik.Vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author qishengjun
 * @Date Created in 17:16 2019/11/28
 * @Company: mitesofor </p>
 * @Description:~
 */
@Data
public class DeviceInfoVo {

    @ApiModelProperty(value = "设备名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value = "设备序列号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "设备编号")
    @TableField("device_number")
    private String deviceNumber;

    @ApiModelProperty(value = "安装位置")
    @TableField("installation_location")
    private String installationLocation;

    @ApiModelProperty(value = "方向:0无,1进,2出")
    @TableField("direction")
    private Integer direction;

    @ApiModelProperty(value = "地理位置")
    @TableField("geographic_coordinates")
    private String geographicCoordinates;

    @ApiModelProperty(value = "小区编号")
    @TableField("communityCode")
    private String communityCode;

    @ApiModelProperty(value = "小区名称")
    @TableField("communityName")
    private String communityName;

    @ApiModelProperty(value = "设备类型:1普通摄像机,2人脸摄像机,3全结构摄像机")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "ip地址")
    @TableField("ipAdress")
    private String ipAdress;

    @ApiModelProperty(value = "视频分辨率")
    @TableField("video_resolution")
    private String videoResolution;

    @ApiModelProperty(value = "视频播放地址")
    @TableField("playback_address")
    private String playbackAddress;

    @ApiModelProperty(value = "分区编号")
    @TableField("zone_id")
    private Integer zoneId;

    @ApiModelProperty(value = "分区名称")
    @TableField("zone_name")
    private String zoneName;

    @ApiModelProperty("是否支持wifi探针")
    @TableField("wifi_probe")
    private Integer wifiProbe;

    @ApiModelProperty("街道名")
    private String streetName;

    @ApiModelProperty("nvr序列号编码")
    @TableField("nvr_serial_number")
    private String nvrSerialNumber;

    @ApiModelProperty("摄像头账号")
    @TableField("username")
    private String username;

    @ApiModelProperty("摄像头密码")
    @TableField("password")
    private String password;
}
