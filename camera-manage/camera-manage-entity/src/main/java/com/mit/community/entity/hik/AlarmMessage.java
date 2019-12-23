package com.mit.community.entity.hik;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AlarmMessage对象", description="")
@TableName("alarm_message")
public class AlarmMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "设备序列号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "设备编号")
    @TableField("device_number")
    private String deviceNumber;

    @ApiModelProperty(value = "设备名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value = "报警位置")
    @TableField("alarm_location")
    private String alarmLocation;

    @ApiModelProperty(value = "处理状态:1处理中,2已处理")
    @TableField("process_state")
    private Integer processState;

    @ApiModelProperty(value = "处理说明")
    @TableField("process_instructions")
    private String processInstructions;

    @ApiModelProperty(value = "报警时间")
    @TableField("alarm_time")
    private Date alarmTime;
}
