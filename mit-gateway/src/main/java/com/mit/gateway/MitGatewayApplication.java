package com.mit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MitGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitGatewayApplication.class, args);
    }

}
