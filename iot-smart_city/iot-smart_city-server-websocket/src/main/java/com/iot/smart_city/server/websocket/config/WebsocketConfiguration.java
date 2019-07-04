package com.iot.smart_city.server.websocket.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iot.smart_city.server.websocket.server.ScWebSocketServer;

@Configuration
public class WebsocketConfiguration {
	
	@Value("${websocket.server_port}")
	private int serverPort;
	@Value("${websocket.server_host}")
	private String serverHost;
	@Value("${websocket.poolSize}")
	private int poolSize;
	@Value("${websocket.webSocketPath}")
	private String webSocketPath;
	@Value("${websocket.SO_KEEPALIVE}")
	private boolean SO_KEEPALIVE;
	@Value("${websocket.contentLength}")
	private int contentLength;
	@Value("${websocket.servers}")
	private String servers;
	
	@Bean
	public SpringListener springListener() {
		return new SpringListener();
	}
	
	private class SpringListener implements ServletContextListener {

		@Override
		public void contextDestroyed(ServletContextEvent event) {
			
		}
		
		@Override
		public void contextInitialized(ServletContextEvent event) {
			//容器启动后开启websocket服务器
			ScWebSocketServer.start(serverHost, serverPort, poolSize, contentLength, webSocketPath, SO_KEEPALIVE);
		}
		
	}

}
