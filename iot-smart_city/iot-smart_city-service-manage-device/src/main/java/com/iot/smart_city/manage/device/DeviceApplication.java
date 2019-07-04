package com.iot.smart_city.manage.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan(basePackages="com.iot.smart_city.manage.device.mapper")
@EnableFeignClients(basePackages="com.iot.smart_city.manage.device.client")
public class DeviceApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(DeviceApplication.class, args);
	}

}
