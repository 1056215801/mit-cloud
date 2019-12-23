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
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author qsj
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AlarmDevice对象", description="")
public class AlarmDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value = "设备序列号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "设备编号")
    @TableField("device_number")
    private String deviceNumber;

    @ApiModelProperty(value = "设备类型:1紧急报警箱,2紧急报警按钮,3紧急报警立杆")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "设备位置")
    @TableField("device_location")
    private String deviceLocation;

    @ApiModelProperty(value = "ip地址")
    @TableField("ipAdress")
    private String ipAdress;

    @ApiModelProperty(value = "小区编码")
    @TableField("community_code")
    private String communityCode;

    @ApiModelProperty(value = "小区名称")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "分区id")
    @TableField("zone_id")
    private Integer zoneId;

    @ApiModelProperty(value = "分区名称")
    @TableField("zone_name")
    private String zoneName;

    @ApiModelProperty(value = "经纬度")
    @TableField("geographic_coordinates")
    private String geographicCoordinates;

    @ApiModelProperty(value = "设备状态:1正常,2故障,3掉线")
    @TableField("device_status")
    private Integer deviceStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("modified_time")
    private Date modifiedTime;


}
