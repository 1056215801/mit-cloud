package com.mit.iot.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 对接电力系统所需的参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("config_info")
public class ConfigInfo extends BaseEntity{
    @TableField("app_key")
    private String appKey;

    @TableField("app_secret")
    private String appSecret;

    @TableField("project_code")
    private String projectCode;

    @TableField("ip_port")
    private String ipAndPort;

    @TableField("redirect_uri")
    private String redirectUri;

    @TableField("access_token")
    private String accessToken;

    private String uname;

    private String passwd;
}
