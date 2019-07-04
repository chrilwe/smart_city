package com.iot.smart_city.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class FileSystemApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(FileSystemApplication.class, args);
	}

}
