package com.mit.community.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author qishengjun
 * @Date Created in 11:04 2019/12/12
 * @Company: mitesofor </p>
 * @Description:~
 */
@Component
public class UrlConfig implements InitializingBean {

    @Value("${showDeviceUrl}")
    private String showDeviceUrl;
    @Value("${localIpAdress}")
    private String localIpAdress;
    public static String SHOWDEVICEURL;
    public static String LOCALIPADRESS;
    @Override
    public void afterPropertiesSet() throws Exception {
        SHOWDEVICEURL=showDeviceUrl;
        LOCALIPADRESS=localIpAdress;
    }
}
