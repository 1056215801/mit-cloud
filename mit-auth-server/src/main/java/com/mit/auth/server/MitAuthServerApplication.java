package com.mit.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication(scanBasePackages = {"com.mit.common.feign", "com.mit.auth.server"})
@EnableEurekaClient
@EnableResourceServer
@EnableFeignClients("com.mit.common.*")
@EnableHystrix
public class MitAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitAuthServerApplication.class, args);
    }

}
