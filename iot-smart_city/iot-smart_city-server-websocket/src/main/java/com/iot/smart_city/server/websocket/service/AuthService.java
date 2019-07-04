package com.iot.smart_city.server.websocket.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.model.ConnectionMessage;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;
import com.iot.smart_city.server.websocket.util.JwtTokenUtil;

import io.netty.channel.ChannelId;

@Service
public class AuthService {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${websocket.server_port}")
	private int serverPort;
	@Value("${websocket.server_host}")
	private String serverHost;
	
	//当前客户端身份验证
	public boolean auth(WebSocketMessage message,ChannelId channelId) {
		if(message == null) {
			return false;
		}
		String jwtToken = message.getJwtToken();
		String clientId = message.getClientId();
		String messageBody = message.getMessageBody();
		String receiverId = message.getReceiverId();
		if(StringUtils.isEmpty(jwtToken) || StringUtils.isEmpty(clientId)) {
			//没有认证，关闭连接
			return false;
		} else {
			//解析令牌
			Map paraseJwtToken = null;
			try {
				paraseJwtToken = JwtTokenUtil.decodeAndVerify(jwtToken);
			} catch (Exception e) {
				//令牌错误
				e.printStackTrace();
				return false;
			}
			if(paraseJwtToken == null) {
				return false;
			} else {
				//认证成功，将客户端连接信息放到redis中
				String connectMsg = stringRedisTemplate.boundValueOps(clientId).get();
				if(StringUtils.isEmpty(connectMsg)) {
					ConnectionMessage connectionMessage = new ConnectionMessage();
					connectionMessage.setChannelId(channelId);
					connectionMessage.setClientId(clientId);
					connectionMessage.setConnectHost(serverHost);
					connectionMessage.setConnectPort(serverPort);
					stringRedisTemplate.boundValueOps(clientId).set(JSON.toJSONString(connectionMessage));
				} 
			}
		}
		return true;
	}
}
