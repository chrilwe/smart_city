package com.iot.smart_city.server.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * websocket服务器
 * @author chrilwe
 *
 */
public class ScWebSocketServer {
	
	public static void start(String host,int port,int pool,int contentLength,String webPath, boolean keepAlive) {
		// boossGroups处理连接事件
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// workGroup处理已连接的channelHandler事件
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class) // 用的是nio的方式来处理连接
					.childHandler(new ScWebSocketChannelInitializer(contentLength,webPath,host,port))
					.option(ChannelOption.SO_BACKLOG, pool)
					.childOption(ChannelOption.SO_KEEPALIVE, keepAlive);

			// 绑定端口
			ChannelFuture future = serverBootstrap.bind(host,port).sync();
			// 事件是异步处理的，要想知道事件完成,监听事件返回future判断是否事件处理成功
			Channel channel = future.channel();
			// 等待关闭
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();

		}
	}
}
