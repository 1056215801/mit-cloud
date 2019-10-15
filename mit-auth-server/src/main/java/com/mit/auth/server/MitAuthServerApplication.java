package com.mit.auth.server;

import com.mit.log.annotation.EnableLogging;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@ComponentScan(basePackages = {"com.mit.common.feign", "com.mit.auth.server"})
@MapperScan({"com.mit.auth.server.dao", "com.mit.log.dao"})
@EnableEurekaClient
@EnableResourceServer
@EnableFeignClients(basePackages = "com.mit.common.feign")
@EnableHystrix
@EnableLogging
@SpringBootApplication
public class MitAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitAuthServerApplication.class, args);
    }

}
