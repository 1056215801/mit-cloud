package com.mit.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mit.common.model.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class ClusterCommunity {

    public ClusterCommunity(SysUser sysUser) {
        this.communityName = sysUser.getCommunityName();
        this.communityCode = sysUser.getCommunityCode();
    }

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

    private String committeeId;

    private String username;

    private String adminName;

    private String phone;

    private String password;

    protected Integer id;

    /**创建时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtCreate;

    /**修改时间*/
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtModified;

}
