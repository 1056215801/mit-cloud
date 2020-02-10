package com.mit.community.entity.hik;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author qsj
 * @since 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeviceInfo对象", description="")
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value = "安装位置")
    @TableField("installation_location")
    private String installationLocation;

    @ApiModelProperty(value = "方向:0无,1进,2出")
    @TableField("direction")
    private Integer direction;

    @ApiModelProperty(value = "地理位置")
    @TableField("geographic_coordinates")
    private String geographicCoordinates;

    @ApiModelProperty(value = "摄像头厂家:1大华,2海康")
    @TableField("camera_company")
    private Integer cameraCompany;

    @ApiModelProperty(value = "小区编号")
    @TableField("communityCode")
    private String communityCode;

    @ApiModelProperty(value = "设备编号")
    @TableField("device_number")
    private String deviceNumber;

    @ApiModelProperty(value = "小区名称")
    @TableField("communityName")
    private String communityName;

    @ApiModelProperty(value = "设备类型:1普通摄像机,2人脸摄像机,3全结构摄像机")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "负责人电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "ip地址")
    @TableField("ipAdress")
    private String ipAdress;

    @ApiModelProperty(value = "设备序列号")
    @TableField("serial_number")
    private String serialNumber;

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

    @ApiModelProperty(value = "设备状态")
    @TableField("device_state")
    private Integer deviceState;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("modified_time")
    private Date modifiedTime;

    @TableField("street_name")
    private String streetName;

    @ApiModelProperty("是否支持wifi探针")
    @TableField("wifi_probe")
    private Integer wifiProbe;

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
