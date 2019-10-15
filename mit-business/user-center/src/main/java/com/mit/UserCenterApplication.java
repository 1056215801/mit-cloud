package com.mit;

import com.mit.log.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.mit.common.feign", "com.mit.user"})
@EnableEurekaClient
@EnableResourceServer
@EnableFeignClients
@EnableLogging
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
