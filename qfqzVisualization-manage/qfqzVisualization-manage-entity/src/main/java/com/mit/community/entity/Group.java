package com.mit.community.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Group对象", description="")
public class Group implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private Integer id;

    @ApiModelProperty(value = "群组编号")
    @TableField("group_number")
    private String groupNumber;

    @ApiModelProperty(value = "群组名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "群组描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "设备编号")
    @TableField("serialNumbers")
    private String serialNumbers;

    @ApiModelProperty(value = "辖区编号")
    @TableField("jurisdiction_number")
    private String jurisdictionNumber;

    @ApiModelProperty(value = "群组类型 1:预设群组2：临时群组")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "活跃度")
    @TableField("activity")
    private Integer activity;

    @ApiModelProperty(value = "创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;
}
