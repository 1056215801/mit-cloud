package com.mit;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动类
 * EnableFeignClients({"com.mit.auth.client.feign"})：uth-client模块，会定时发送查询哪些服务可以访问这个服务，所以需要把client的feign路径包含进来
 * @author shuyy
 * @date 2018年11月9日
 * @company mitesofor
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableTransactionManagement
@EnableSwagger2Doc
@ServletComponentScan
@EnableResourceServer
@MapperScan(basePackages = {"com.mit.community.mapper", "com.mit.community.*.*.mapper"})
@ComponentScan({"com.mit.community"})
//如果web监听器 ServletContextListener没有和启动类下一个包下@ServletComponentScan(value= "com.smp.listener")这样才能扫到对应包下类，如果是多个包就和@ComponentScan一样处理加{}
public class QfqzVisualizationManageBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(QfqzVisualizationManageBootstrap.class).web(WebApplicationType.SERVLET).run(args);
//        SpringApplication.run(QfqzVisualizationManageBootstrap.class, args);
    }
}
