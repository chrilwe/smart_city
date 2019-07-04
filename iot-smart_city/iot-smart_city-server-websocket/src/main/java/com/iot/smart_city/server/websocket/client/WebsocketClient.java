package com.iot.smart_city.server.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;

import javax.ws.rs.core.UriBuilder;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.TextFrame;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.server.websocket.model.WebSocketMessage;
/**
 * 该客户端用来桥接其他服务器通讯
 * @author chrilwe
 *
 */
public class WebsocketClient {
	private static WebSocketClient client;
	
	public static void main(String[] args) throws URISyntaxException {
		client = new WebSocketClient(new URI("ws://localhost:1314/ws"), new Draft_6455()) {
			
			@Override
			public void onOpen(ServerHandshake handshakedata) {
				System.out.println("client connect to server,status="+handshakedata.getHttpStatus());
			}
			
			@Override
			public void onMessage(String message) {
				System.out.println(message);
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
		while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
			System.out.println("connecting.....");
		}
		WebSocketMessage msg = new WebSocketMessage();
		msg.setAccessToken("access");
		msg.setClientId("1");
		msg.setJwtToken("jwt");
		msg.setMessageBody("hello");
		msg.setMessageType("point_to_point");
		msg.setReceiverId("2");
		client.send(JSON.toJSONString(msg));
	}
	
}
