package com.mit.community.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 小区信息表
 *
 * @author Mr.Deng
 * @date 2018/11/14 11:51
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: mitesofor </p>
 */
@Data
public class ClusterCommunity{

    @TableId(type = IdType.AUTO)
    protected Integer id;

    /**创建时间*/
    @TableField("gmt_create")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtCreate;

    /**修改时间*/
    @TableField("gmt_modified")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtModified;
    /**
     * 小区编码
     */
    private String communityCode;
    /**
     * 小区名称
     */

    private String communityName;
    /**
     * 小区id
     */

    private Integer communityId;
    /**
     * 地区名称
     */

    private String areaName;


    private String areaId;
    /**
     * 地址
     */

    private String address;
    /**
     * 城市名称
     */

    private String cityName;


    private String cityId;
    /**
     * 街道名称
     */

    private String streetName;


    private String streetId;
    /**
     * 省份名称
     */

    private String provinceName;


    private String provinceId;
    /**
     * 小区类型
     */

    private String communityType;

    private String areaBelong;


    private String remark;


    private String committee;

    @TableField("committee_id")
    private String committeeId;

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String adminName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String password;

    private String ip;

    private String property;//物业

    @TableField("property_phone")
    private String propertyPhone;

    @TableField("dixing_url")
    private String diXingUrl;

    @TableField("frontcover_url")
    private String frontCoverUrl;

    private String center;

    private String right;

    private String left;
}
