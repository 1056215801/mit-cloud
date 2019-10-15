package com.mit.log.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
* 日志实体
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log")
public class SysLog implements Serializable {
	private static final long serialVersionUID = -5398795297842978376L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 请求路径
	 */
	private String url;

	/**
	 * 请求方式
	 */
	private String httpMethod;

	/**
	 * 操作用户
	 */
	private String username;

	/**
	 * 客户端IP
	 */
	private String ip;

	/**
	 * 归属模块
	 */
	private String module;

	/**
	 * 执行方法的参数值
	 */
	private String params;

	/**
	 * 日志错误信息
	 */
	private String remark;

	/**
	 * 是否执行成功
	 */
	private Boolean flag;

	/**
	 * 响应时间，毫秒为单位
	 */
	private Long time;

}
