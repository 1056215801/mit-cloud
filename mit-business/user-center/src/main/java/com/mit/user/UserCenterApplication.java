package com.mit.user;

import com.mit.log.annotation.EnableLogging;
import com.mit.datasource.handler.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 */
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.mit.common.feign", "com.mit.user.*"})
@MapperScan({"com.mit.user.mapper", "com.mit.log.dao"})
@EnableEurekaClient
@EnableResourceServer
@EnableFeignClients
@EnableLogging
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
@Import(MyMetaObjectHandler.class)
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
