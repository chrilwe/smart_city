package com.iot.smart_city.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(basePackages="com.iot.smart_city.ucenter.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
public class UcenterApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(UcenterApplication.class, args);
	}

}
