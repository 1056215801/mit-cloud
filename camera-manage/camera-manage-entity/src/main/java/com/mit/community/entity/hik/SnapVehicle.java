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
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SnapVehicle对象", description="")
public class SnapVehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "车牌类型")
    @TableField("plateType")
    private String plateType;

    @ApiModelProperty(value = "车牌颜色")
    @TableField("plateColor")
    private String plateColor;

    @ApiModelProperty(value = "车辆类型")
    @TableField("vehicleType")
    private String vehicleType;

    @ApiModelProperty(value = "车辆颜色")
    @TableField("vehicleColor")
    private String vehicleColor;

    @ApiModelProperty(value = "车牌号")
    @TableField("plateNo")
    private String plateNo;

    @ApiModelProperty(value = "小区编码")
    @TableField("community_code")
    private String communityCode;

    @ApiModelProperty(value = "序列号编码")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty("抓拍时间")
    @TableField("shoot_time")
    private Date shootTime;

    @ApiModelProperty("图片路径")
    @TableField("imageUrl")
    private String imageUrl;
}
