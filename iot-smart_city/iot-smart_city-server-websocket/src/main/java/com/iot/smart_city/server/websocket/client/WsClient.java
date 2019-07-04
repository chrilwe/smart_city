package com.iot.smart_city.server.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;
import com.iot.smart_city.server.websocket.util.JwtTokenUtil;
import com.iot.smart_city.server.websocket.util.SpringBeanUtil;

/**
 * 客户端
 * 
 * @author chrilwe
 *
 */
public class WsClient {

	public static void sendMessage(String rsaFileName, String secret, String alias, String password,
			String fromHost, String targetHost,String uri, 
			WebSocketMessage webSocketMessage) throws URISyntaxException {
		WebSocketClient client = new WebSocketClient(new URI(uri), new Draft_6455()) {

			@Override
			public void onOpen(ServerHandshake handshakedata) {
				System.out.println("client connect to server,status=" + handshakedata.getHttpStatus());
			}

			@Override
			public void onMessage(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception ex) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
				System.out.println("close client");
			}
		};
		client.connect();
		while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
			System.out.println("connecting.....");
		}
		
		//先从redis中拿jwt令牌，没有则需要生成令牌
		SpringBeanUtil beanUtil = new SpringBeanUtil();
		StringRedisTemplate redisTemplate = beanUtil.getBean(StringRedisTemplate.class);
		String jwt = redisTemplate.boundValueOps(fromHost).get();
		if(StringUtils.isEmpty(jwt)) {
			//为客户端生成jwt令牌
			jwt = JwtTokenUtil.createJwtToken(rsaFileName, secret, alias, password, fromHost, targetHost);	
			//将令牌放入redis
			redisTemplate.boundValueOps(fromHost).set(jwt);
		}
		webSocketMessage.setJwtToken(jwt);
		client.send(JSON.toJSONString(webSocketMessage));
		try {
			Thread.sleep(2 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.close();
	}
}
