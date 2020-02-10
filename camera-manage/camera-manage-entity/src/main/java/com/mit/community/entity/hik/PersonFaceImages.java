package com.mit.community.entity.hik;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 登记/陌生人抓拍人脸图片信息
 * </p>
 *
 * @author qsj
 * @since 2019-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PersonFaceImages对象", description="登记/陌生人抓拍人脸图片信息")
@TableName("person_face_images")
public class PersonFaceImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "图片地址")
    @TableField("img_url")
    private String imgUrl;

    @ApiModelProperty(value = "人脸唯一标识:抓拍记录返回")
    @TableField("uncode")
    private String uncode;

    @ApiModelProperty(value = "人脸库唯一标识")
    @TableField("faceGroupIndexCode")
    private String faceGroupIndexCode;
    @ApiModelProperty(value = "人脸分类")
    @TableField("face_classification")
    private String faceClassification;

    @ApiModelProperty(value = "摄像头厂家:1大华,2海康")
    @TableField("camera_company")
    private Integer cameraCompany;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "性别:0未知.1男性,2女性")
    @TableField("sex")
    private String sex;

    @ApiModelProperty(value = "证件类型:0未知,1身份证,2护照")
    @TableField("iDType")
    private String iDType;

    @ApiModelProperty(value = "证件号")
    @TableField("idNo")
    private String idNo;

    @ApiModelProperty(value = "小区编码")
    @TableField("communityCode")
    private String communityCode;

    @ApiModelProperty(value = "住户编码")
    @TableField("household_id")
    private String householdId;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("modified_time")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;

    @TableField("face_database_name")
    private String faceDatabaseName;
    @TableField("controlInstructions")
    private String controlInstructions;
    @TableField("phone")
    private String phone;

    @TableField("number_type")
    private Integer numberType;
}
