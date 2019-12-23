package com.mit.workflow;

import com.mit.workflow.config.AppDispatcherServletConfiguration;
import com.mit.workflow.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import({ApplicationConfiguration.class, AppDispatcherServletConfiguration.class})
@SpringBootApplication(
        exclude = SecurityAutoConfiguration.class
)
@EnableFeignClients(basePackages = "com.mit.common.feign")
@EnableSwagger2
public class WorkflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }
}
