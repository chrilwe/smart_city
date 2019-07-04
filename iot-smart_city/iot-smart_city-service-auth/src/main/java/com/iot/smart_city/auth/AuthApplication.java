package com.iot.smart_city.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages="com.iot.smart_city.auth.client")
@EnableEurekaClient
@EnableDiscoveryClient
public class AuthApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AuthApplication.class, args);
	}

}
