package com.mit.common.constant;

/**
 * @Description oauth常量
 */
public class SecurityConstants {
    /**
     * 角色前缀
     */
    public static final String ROLE = "ROLE_";
    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";

    public static final String TOKEN_PARAM = "access_token";

    public static final String TOKEN_HEADER = "accessToken";

    public static final String AUTH = "auth";

    public static final String TOKEN = "token";

    public static final String Authorization = "Authorization";

    /**
     * 授权码模式
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";

    /**
     * 密码模式
     */
    public static final String PASSWORD = "password";

    /**
     * 刷新token
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * OAUTH模式登录处理地址
     */
    public static final String OAUTH_LOGIN_PRO_URL = "/user/login";

    /**
     * PASSWORD模式登录处理地址
     */
    public static final String PASSWORD_LOGIN_PRO_URL = "/oauth/user/token";

    /**
     * 获取授权码地址
     */
    public static final String AUTH_CODE_URL = "/oauth/authorize";

    /**
     * 登录页面
     */
    public static final String LOGIN_PAGE = "/login.html";

    /**
     * 登出URL
     */
    public static final String LOGOUT_URL = "/oauth/remove/token";

    /**
     * 默认生成图形验证码过期时间
     */
    public static final int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 默认保存code的前缀
     */
    public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

    /**
     * 用户信息头
     */
    public static final String USER_HEADER = "x-user-header";

    /**
     * 用户id信息头
     */
    public static final String USER_ID_HEADER = "x-userid-header";

    /**
     * 角色信息头
     */
    public static final String ROLE_HEADER = "x-role-header";
}
