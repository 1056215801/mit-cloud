package com.mit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 */
//@EnableSearchClient
@EnableTransactionManagement
//@EnableFeignInterceptor
@SpringBootApplication(scanBasePackages = {"com.mit.common.feign", "com.mit.user"})
@EnableEurekaClient
@EnableResourceServer
@EnableFeignClients
public class UserCenterApp {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApp.class, args);
    }
}
