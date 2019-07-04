package com.iot.smart_city.server.websocket.model;

import io.netty.channel.ChannelId;
import lombok.Data;
import lombok.ToString;

/**
 * 记录客户端连接服务端的信息
 * @author chrilwe
 *
 */
@Data
@ToString
public class ConnectionMessage {
	private String clientId;//连接服务端的客户端id
	private String connectHost;//连接的服务端ip地址
	private int connectPort;//连接的服务端端口
	private ChannelId channelId;//服务端存放channel的id
}
