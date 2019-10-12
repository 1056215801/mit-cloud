package com.mit.auth.server.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mit.common.model.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 认证的客户端
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "oauth_client_details")
public class Client extends SuperEntity {
    private static final long serialVersionUID = -8185413579135897885L;
    private String clientId;
    /**
     * 应用名称
     */
    private String clientName;
    private String resourceIds = "";
    private String clientSecret;
    private String clientSecretStr;
    private String scope = "all";
    private String authorizedGrantTypes = "authorization_code,password,refresh_token,client_credentials";
    private String webServerRedirectUri;
    private String authorities = "";
    private Integer accessTokenValidity = 18000;
    private Integer refreshTokenValidity = 28800;
    private String additionalInformation = "{}";
    private String autoapprove = "true";
}
