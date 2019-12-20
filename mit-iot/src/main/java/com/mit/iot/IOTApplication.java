package com.mit.iot;

import com.mit.common.config.MybatisPlusConfig;
import com.mit.datasource.handler.MyMetaObjectHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.mit")
@MapperScan("com.mit.iot.mapper")
@ServletComponentScan
@EnableEurekaClient
@EnableResourceServer
@EnableSwagger2
@Import({MyMetaObjectHandler.class, MybatisPlusConfig.class})
public class IOTApplication {

    public static void main(String[] args) {
        SpringApplication.run(IOTApplication.class, args);
    }

}
