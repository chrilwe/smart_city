package com.iot.smart_city.server.websocket.model;

import lombok.Data;
import lombok.ToString;

/**
 * websocket数据格式
 * @author chrilwe
 *
 */
@Data
@ToString
public class WebSocketMessage {
	private String clientId;//客户端id
	private String messageBody;//消息内容
	private String receiverId;//接收消息端id
	private String jwtToken;//jwt令牌
	private String accessToken;//身份令牌 
	private String messageType;//消息类型：点对点发送，广播形式
}
