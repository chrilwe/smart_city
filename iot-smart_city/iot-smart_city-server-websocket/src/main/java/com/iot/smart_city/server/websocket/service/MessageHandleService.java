package com.iot.smart_city.server.websocket.service;

import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.client.WsClient;
import com.iot.smart_city.server.websocket.model.ConnectionMessage;
import com.iot.smart_city.server.websocket.model.MessageType;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;
import com.iot.smart_city.server.websocket.server.ScWebSocketHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/**
 * 处理接收的消息
 * @author chrilwe
 *
 */
@Service 
public class MessageHandleService {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${websocket.server_port}")
	private int serverPort;
	@Value("${websocket.server_host}")
	private String serverHost;
	@Value("${websocket.servers}")
	private String servers;
	@Value("${websocket.webSocketPath}")
	private String webSocketPath;
	@Value("${encrypt.key-store.secret}")
	private String secret;
	@Value("${encrypt.key-store.rsaFileName}")
	private String rsaFileName;
	@Value("${encrypt.key-store.alias}")
	private String alias;
	@Value("${encrypt.key-store.password}")
	private String password;
	
	public void handleMessage(WebSocketMessage webSocketMessage,ChannelHandlerContext ctx) {
		if(webSocketMessage == null) {
			return;
		}
		String messageBody = webSocketMessage.getMessageBody();
		String receiverId = webSocketMessage.getReceiverId();
		String messageType = webSocketMessage.getMessageType();
		String clientId = webSocketMessage.getClientId();
		if(StringUtils.isEmpty(messageBody) || StringUtils.isEmpty(messageType)) {
			return;
		}
		//点对点
		if(messageType.equals(MessageType.POINT_TO_POINT)) {
			if(StringUtils.isEmpty(receiverId)) {
				return;
			}
			this.pointToPoint(clientId,receiverId, ctx, messageBody);
		} else if(messageType.equals(MessageType.POINT_TO_ALL)) {
		//广播
			this.pointToAll(clientId, messageBody,ctx);
		}
	}
	
	//点对点发送消息
	public void pointToPoint(String clientId,String receiverId,ChannelHandlerContext ctx,String messageBody) {
		//查询发送消息到目标客户端的IP地址和端口号是否为当前服务器的地址
		String connectionMessageJson = stringRedisTemplate.boundValueOps(receiverId).get();
		if(StringUtils.isEmpty(connectionMessageJson)) {
			//目标客户端已经下线，将消息保存 
		} else {
			//目标客户端在线,判断是否连接的当前服务器
			ConnectionMessage connectionMessage = JSON.parseObject(connectionMessageJson, ConnectionMessage.class);
			String connectHost = connectionMessage.getConnectHost();
			int connectPort = connectionMessage.getConnectPort();
			ChannelId channelId = connectionMessage.getChannelId();
			if(connectHost.equals(serverHost) && connectPort == serverPort) {
				ChannelGroup channels = ScWebSocketHandler.channels;
				Channel channel = channels.find(channelId);
				WebSocketMessage webSocketMessage = new WebSocketMessage();
				webSocketMessage.setClientId(clientId);
				webSocketMessage.setMessageBody(messageBody);
				webSocketMessage.setMessageType(MessageType.POINT_TO_POINT);
				webSocketMessage.setReceiverId(receiverId);
				Object msg = JSON.toJSONString(webSocketMessage);
				channel.writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame) msg).text()));
			} else {
				WebSocketMessage webSocketMessage = new WebSocketMessage();
				webSocketMessage.setClientId(clientId);
				webSocketMessage.setMessageBody(messageBody);
				webSocketMessage.setMessageType(MessageType.POINT_TO_POINT);
				webSocketMessage.setReceiverId(receiverId);
				String uri = "ws://"+ connectHost + ":" + connectPort;
				try {
					String fromHost = serverHost + ":" +serverPort;
					String targetHost = connectHost + ":" + connectPort;
					WsClient.sendMessage(rsaFileName, secret, alias, password, 
							fromHost, targetHost, uri, webSocketMessage);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//广播方式
	public void pointToAll(String clientId,String messageBody,ChannelHandlerContext ctx) {
		//将消息发送到所有服务器
		WebSocketMessage webSocketMessage = new WebSocketMessage();
		webSocketMessage.setClientId(clientId);
		webSocketMessage.setMessageBody(messageBody);
		webSocketMessage.setMessageType(MessageType.POINT_TO_ALL);
		Object msg = JSON.toJSONString(webSocketMessage);
		ScWebSocketHandler.channels.writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame) msg).text()));
		if(StringUtils.isNotEmpty(servers)) {
			String[] split = servers.split(",");
			for (String string : split) {
				try {
					String uri  = "ws://"+ string + webSocketPath;
					String fromHost = serverHost + ":" +serverPort; 
					WsClient.sendMessage(rsaFileName, secret, alias, password, 
							fromHost, uri, uri, webSocketMessage);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
