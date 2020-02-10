package com.mit.community.entity.hik;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mit.community.entity.AuthorizeAppHouseholdDeviceGroup;
import com.mit.community.entity.AuthorizeHouseholdDeviceGroup;
import com.mit.community.entity.BaseEntity;
import com.mit.community.entity.HouseholdRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 住户表
 *
 * @author Mr.Deng
 * @date 2018/11/14 19:00
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: mitesofor </p>
 */
@Data
@TableName("household")
@AllArgsConstructor
@NoArgsConstructor
public class HouseHold extends BaseEntity {
    /**
     * 小区Code
     */
    @TableField("community_code")
    private String communityCode;
    /**
     * 是否外籍
     */
    @TableField("is_foregin")
    private Integer isForegin;//0否；1是
    /**
     * 住户ID
     */
    @TableField("household_id")
    private Integer householdId;

    /**
     * 业主名称
     */
    @TableField("household_name")
    private String householdName;

    /**
     * 业主状态（0：注销；1：启用；2：停用）
     */
    @TableField("household_status")
    private Integer householdStatus;
    /**
     * 值转成二进制授权状态（未授权：0;卡：1;roomNumapp：10;人脸：100）；例:人脸和卡授权：101
     */
    @TableField("authorize_status")
    private Integer authorizeStatus;
    /**
     * 业主性别(0：男；1：女)
     */
    private Integer gender;
    /**
     * 居住期限
     */
    @TableField("residence_time")
    private LocalDate residenceTime;
    /**
     * 业主手机号
     */
    private String mobile;

    /**
     * SIP账号
     */
    @TableField("sip_account")
    private String sipAccount;
    /**
     * SIP密码
     */
    @TableField("sip_password")
    private String sipPassword;

    /**
     * 身份证号码
     */
    @TableField("credential_num")
    private String credentialNum;

    /**
     * 工作职能
     */
    private String function;

    /**
     * 入职时间
     */
    @TableField("job_time")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date jobTime;

    /**
     * 合同到期时间
     */
    @TableField("contract_time")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contractTime;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 身份类型：1、群众、2、境外人员、3、孤寡老人、4、信教人员、5、留守儿童、6、上方人员、99、其他
     */
    @TableField("identity_type")
    private Short identityType;

    /**
     * 授权设备组
     */
    @TableField(exist = false)
    private List<AuthorizeHouseholdDeviceGroup> authorizeHouseholdDeviceGroups;
    /**
     * 授权门禁
     */
    @TableField(exist = false)
    private List<AuthorizeAppHouseholdDeviceGroup> authorizeAppHouseholdDeviceGroups;
    /**
     * 房屋
     */
    @TableField(exist = false)
    private List<HouseholdRoom> householdRoomList;

    /**
     * 群众
     */
    @TableField(exist = false)
    public static final Short NORMAL = 1;

    /**
     * 境外人员
     */
    @TableField(exist = false)
    public static final Short OVERSEAS = 2;

    /**
     * 孤寡老人
     */
    @TableField(exist = false)
    public static final Short LONELY = 3;

    /**
     * 信教人员
     */
    @TableField(exist = false)
    public static final Short RELIGION = 4;

    /**
     * 留守儿童
     */
    @TableField(exist = false)
    public static final Short STAY_AT_HOME = 5;

    /**
     * 上访人员
     */
    @TableField(exist = false)
    public static final Short VISITOR = 6;

    /**
     * 其他
     */
    @TableField(exist = false)
    public static final Short OTHER = 7;

    /**
     * 有效期限
     */
    @TableField(exist = false)
    private Long diffDay;

    /**
     * 与户主关系
     */
    @TableField(exist = false)
    private String householdType;

    /**
     * 房屋信息
     */
    @TableField(exist = false)
    private String housing;

    /**
     * 权限有效期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @TableField("validity_time")
    private Date validityTime;

    /**
     * 与户主关系
     */
    private Integer housetype;

    @TableField(exist = false)
    private String rkcf;

    @TableField(exist = false)
    private String labels;

    @TableField("mobile_belong")
    private Integer mobileBelong;//1本人；2紧急联系人

    @TableField("call_mobile")
    private String callMobile;

    @TableField("is_property")
    private Integer isProperty;//0否，1是

    private Integer resident;//常驻（0常驻，1非常驻）

    @TableField("people_type")
    private String peopleType;

    @TableField(exist = false)
    private String photoNetUrl;
}
