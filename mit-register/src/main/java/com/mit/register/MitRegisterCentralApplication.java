package com.mit.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 服务注册中心 eureka-server
 */
@SpringBootApplication
@EnableEurekaServer
public class MitRegisterCentralApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitRegisterCentralApplication.class, args);
    }

    /**
     * 默认情况下，当Spring Security在类路径上时，它将要求将有效的CSRF令牌与每个请求一起发送到应用程序。
     * Eureka客户通常不会拥有有效的跨站点请求伪造（CSRF）令牌，需要为/eureka/**端点禁用此要求
     */
    @EnableWebSecurity
    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().ignoringAntMatchers("/eureka/**");
            super.configure(http);
        }
    }

}
