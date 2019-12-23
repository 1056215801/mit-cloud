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
 * @since 2019-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Stranger对象", description="")
public class Stranger implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "人脸图片唯一标识")
    @TableField("uncode")
    private String uncode;

    @ApiModelProperty(value = "小区编号")
    @TableField("communityCode")
    private String communityCode;

    @ApiModelProperty(value = "设备唯一标识")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty("抓拍时间")
    @TableField("shoot_time")
    private LocalDateTime shootTime;

}
