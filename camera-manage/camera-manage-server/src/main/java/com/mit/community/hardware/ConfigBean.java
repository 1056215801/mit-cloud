package com.mit.community.hardware;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author qishengjun
 * @Date Created in 16:38 2019/11/27
 * @Company: mitesofor </p>
 * @Description:~
 */
@Component
public class ConfigBean implements InitializingBean {

    @Value("${communityCode:0}")
    private String communityCode;
    @Value("${dllPath:0}")
    private String dllpath;
    @Value("${url:0}")
    private String url;
    @Value("${jsonUrl:0}")
    private String jsonUrl;
    @Value("${alarmUrl:0}")
    private String alarmUrl;
    public static String COMMUNITY_CODE;
    public static String DLLPATH;
    public static String URL;
    public static String JSONURL;
    public static String ALARMURL;
    @Override
    public void afterPropertiesSet() throws Exception {
        COMMUNITY_CODE=communityCode;
        DLLPATH=dllpath;
        URL=url;
        JSONURL=jsonUrl;
        ALARMURL=alarmUrl;
    }
}
