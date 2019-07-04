package com.iot.smart_city.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CenterApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(CenterApplication.class, args);
	}

}
