package com.iot.smart_city.server.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class WebsocketApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebsocketApplication.class, args);
	}

}
