package com.iot.smart_city.server.websocket.server;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.model.ConnectionMessage;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;
import com.iot.smart_city.server.websocket.service.AuthService;
import com.iot.smart_city.server.websocket.service.MessageHandleService;
import com.iot.smart_city.server.websocket.util.JwtTokenUtil;
import com.iot.smart_city.server.websocket.util.SpringBeanUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ScWebSocketHandler extends SimpleChannelInboundHandler<Object> {
	String host;
	int port;
	public ScWebSocketHandler(String host,int port) {
		this.host = host;
		this.port = port;
	}

	// 存放channel的集合
	public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 关闭连接
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端：" + ctx.channel().id() + "关闭连接");
		// 将客户端连接删除
		channels.remove(ctx.channel());
	}

	/**
	 * 读完成
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// 刷新数据
		ctx.flush();
	}

	/**
	 * 捕获到异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println(cause);
		// 捕获到异常，关闭连接
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(((TextWebSocketFrame) msg).text());
		//channels.writeAndFlush(new TextWebSocketFrame(((TextWebSocketFrame) msg).text()));
		//认证方式jwt认证
		SpringBeanUtil beanUtil = new SpringBeanUtil();
		String messageJson = ((TextWebSocketFrame) msg).text();
		if(StringUtils.isEmpty(messageJson)) {
			//没有认证，关闭客户端连接
			ctx.close();
		} else {
			WebSocketMessage message = null;
			try {
				message = JSON.parseObject(messageJson, WebSocketMessage.class);
			} catch (Exception e) {
				e.printStackTrace();
				message = null;
			}
			if(message == null) {
				//消息格式错误，关闭连接
				ctx.close();
			} else {
				AuthService authService = beanUtil.getBean(AuthService.class);
				boolean auth = authService.auth(message, ctx.channel().id());
				if(!auth) {
					ctx.close();
				} else {
					//执行相关业务
					MessageHandleService messageHandleService = beanUtil.getBean(MessageHandleService.class);
					messageHandleService.handleMessage(message,ctx);
				}
			}
		}
	}

}
