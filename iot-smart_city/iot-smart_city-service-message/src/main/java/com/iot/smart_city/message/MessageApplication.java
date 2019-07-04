package com.iot.smart_city.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(basePackages="com.iot.smart_city.message.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
public class MessageApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MessageApplication.class, args);
	}

}
