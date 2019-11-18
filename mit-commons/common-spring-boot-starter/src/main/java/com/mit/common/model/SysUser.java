package com.mit.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class SysUser extends SuperEntity {
	private static final long serialVersionUID = -5886012896705137070L;

	/**
	 * 用户名（登录账号）
	 */
	@NotBlank(message = "用户名不能为空")
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 姓名
	 */
	private String nickname;
	/**
	 * 头像地址
	 */
	private String headImgUrl;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 性别，0男 1女
	 */
	private Integer sex;
	/**
	 * 是否系统管理用户，true表示系统管理用户
	 */
	private Boolean isSystemUser;
	/**
	 * 用户类型，1 政府人员， 2 物业人员
	 */
	private Integer userType;
	/**
	 * 部门id
	 */
	private Long deptId;
	/**
	 * 职务
	 */
	private String position;
	/**
	 * 用户状态，0 禁用，1 正常
	 */
	private Integer status;
	/**
	 * 账号级别
	 */
	private Integer level;
	/**
	 * 最后一次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 省编码
	 */
	private String provinceCode;
	/**
	 * 省名称
	 */
	private String provinceName;
	/**
	 * 市编码
	 */
	private String cityCode;
	/**
	 * 市名称
	 */
	private String cityName;
	/**
	 * 区/县编码
	 */
	private String areaCode;
	/**
	 * 区/县名称
	 */
	private String areaName;
	/**
	 * 镇/街道办编码
	 */
	private String streetCode;
	/**
	 * 镇/街道办名称
	 */
	private String streetName;
	/**
	 * 居委会编码
	 */
	private String committeeCode;
	/**
	 * 居委会名称
	 */
	private String committeeName;
	/**
	 * 小区编码
	 */
	private String communityCode;
	/**
	 * 小区名称
	 */
	private String communityName;
}
