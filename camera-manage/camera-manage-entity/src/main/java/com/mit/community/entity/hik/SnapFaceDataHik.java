package com.mit.community.entity.hik;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 人脸记录信息表
 * </p>
 *
 * @author qsj
 * @since 2019-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SnapFaceDataHik对象", description="人脸记录信息表")
public class SnapFaceDataHik implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "抓拍时间")
    @TableField("shoot_time")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shootTime;

    @ApiModelProperty(value = "照片地址")
    @TableField("image_url")
    private String imageUrl;

    @ApiModelProperty(value = "年龄")
    @TableField("age")
    private Integer age;

    @ApiModelProperty(value = "性别:0未知,1男性,2女性")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "眼镜")
    @TableField("glass")
    private Integer glass;

    @ApiModelProperty(value = "设备序列号")
    @TableField("serial_number")
    private String serialNumber;

    @ApiModelProperty(value = "候选图片唯一标识")
    private String uncode;

    @ApiModelProperty(value = "人脸库id")
    @TableField("face_database_id")
    private Integer faceDatabaseId;

    @ApiModelProperty(value = "进出方向:0无,1进,2出")
    @TableField("direction")
    private Integer direction;

    @ApiModelProperty(value = "小区编码")
    @TableField("communityCode")
    private String communityCode;

    @ApiModelProperty(value = "小区名称")
    @TableField("community_name")
    private String communityName;

    @ApiModelProperty(value = "种族")
    @TableField("emRace")
    private Integer emRace;

    @ApiModelProperty(value = "眼睛状态, 0:未知,1:未识别,2闭眼,3睁眼")
    @TableField("emEye")
    private Integer emEye;

    @ApiModelProperty(value = "嘴巴状态, 0:未知,1:未识别,2闭嘴,3张嘴")
    @TableField("emMouth")
    private Integer emMouth;


    @ApiModelProperty(value = "胡子状态, 0:未知,1:未识别,2没有胡子,3有胡子")
    @TableField("emBeard")
    private Integer emBeard;

    @ApiModelProperty(value = "上衣颜色")
    @TableField("jacketColor")
    private String jacketColor;

    @ApiModelProperty(value = "下衣颜色")
    @TableField("pantsColor")
    private String pantsColor;

    @ApiModelProperty(value = "袖子:1长袖,2短袖")
    @TableField("jacket")
    private Integer jacket;

    @ApiModelProperty(value = "裤裙:1裤子,2裙子")
    @TableField("pants")
    private Integer pants;

    @ApiModelProperty(value = "背包:0未知,1是,2否")
    @TableField("bag")
    private Integer bag;

    @ApiModelProperty(value = "领东西:1是,2否")
    @TableField("things")
    private Integer things;

    @ApiModelProperty(value = "帽子:1是,2否")
    @TableField("hat")
    private Integer hat;

    @ApiModelProperty(value = "口罩:1是,2否")
    @TableField("mask")
    private Integer mask;

    @ApiModelProperty(value = "发型:1长发,2短发")
    @TableField("hairstyle")
    private Integer hairstyle;

    @ApiModelProperty(value = "骑车:0未知,1是,2否")
    @TableField("ride")
    private Integer ride;

    @ApiModelProperty(value = "renyuanfenlei")
    @TableField("face_classification")
    private String faceClassification;

    @TableField("face_database_name")
    private String faceDatabaseName;

    @TableField("face_name")
    private String faceName;

    @TableField("snapshot_site")
    private String snapshotSite;

    @TableField(exist = false)
    private String place;
    @TableField(exist = false)
    private String deviceName;
}
