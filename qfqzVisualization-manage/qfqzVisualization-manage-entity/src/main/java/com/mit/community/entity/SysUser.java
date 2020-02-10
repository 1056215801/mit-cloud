package com.mit.community.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author shuyy
 * @date 2018/11/14
 * @company mitesofor
 */
@TableName("sys_user")
@Data
public class SysUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 小区code
     */
    @TableField("community_code")
    private String communityCode;
    /**
     * 省份名称
     */
    @TableField("province_name")
    private String provinceName;
    /**
     * 城市名称
     */
    @TableField("city_name")
    private String cityName;
    /**
     * 区/县名称
     */
    @TableField("area_name")
    private String areaName;
    /**
     * 镇/街道
     */
    @TableField("street_name")
    private String streetName;
    /**
     * 地址
     */
    private String address;
    /**
     * 管理员名称
     */
    @TableField("admin_name")
    private String adminName;
    /**
     * 账号角色
     */
    private String role;
    /**
     * logo
     */
    private String logo;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 所管理小区
     */
    @TableField("management_community")
    private String managementCommunity;
    /**
     * 管理单位
     */
    @TableField("management_unit")
    private String managementUnit;
    /**
     * 管辖范围
     */
    @TableField("managerment_scope")
    private String managementScope;
    /**
     * 账号类型
     */
    @TableField("account_type")
    private String accountType;

    /**
     * 社区名称
     */
    @TableField("community_name")
    private String communityName;

    @TableField("gmt_modified")
    private LocalDateTime alterTime;

    @TableField("gmt_create")
    private LocalDateTime createtime;

    public SysUser() {
        super();
    }

    public SysUser(String username, String password, String communityCode, String provinceName, String cityName, String areaName, String streetName,
                   String address, String adminName, String role, String logo, String phone, String remark, String managementCommunity, String managementUnit,
                   String managementScope, String accountType, String communityName, LocalDateTime alterTime, LocalDateTime createtime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.communityCode = communityCode;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.areaName = areaName;
        this.streetName = streetName;
        this.address = address;
        this.adminName = adminName;
        this.role = role;
        this.logo = logo;
        this.phone = phone;
        this.remark = remark;
        this.managementCommunity = managementCommunity;
        this.managementUnit = managementUnit;
        this.managementScope = managementScope;
        this.accountType = accountType;
        this.communityName = communityName;
        this.alterTime = alterTime;
        this.createtime = createtime;
    }
}
