package com.iot.smart_city.util.mqtt_client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class Client {
	
	/**
	 * 连接服务器
	 * @throws MqttException 
	 */
	public void connect() throws MqttException {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setConnectionTimeout(100);
		options.setAutomaticReconnect(true);
		options.setKeepAliveInterval(20);
		options.setUserName("test");
		options.setPassword("test".toCharArray());
		
		//报文
		MqttMessage message = new MqttMessage();
		message.setPayload("hello mqtt".getBytes());
		message.setId(1);
		message.setQos(1);
		message.setRetained(true);
		
		String serverUri = "tcp://127.0.0.1:1883";
		String clientId = "test";
		MqttClient mqttClient = new MqttClient(serverUri,clientId);
		MqttTopic topic = mqttClient.getTopic("test");
		mqttClient.setCallback(new MqttCallback() {

			@Override
			public void connectionLost(Throwable arg0) {
				System.out.println("连接断开");
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
				System.out.println("topic:"+topic);
				System.out.println("message:"+mqttMessage.getPayload());
				mqttClient.subscribe("test1");
			}
			
		});//回调函数
		mqttClient.connect(options);
		//setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
		options.setWill(topic, "closed".getBytes(), 2, true);
		//MqttDeliveryToken publish = topic.publish(message);
		mqttClient.subscribe("test",1);
		//publish.waitForCompletion();
		//System.out.println("发布消息成功");
		//mqttClient.disconnect();
	}
	
	//回调函数
	public class ClientCallback implements MqttCallback {

		@Override
		public void connectionLost(Throwable arg0) {
			System.out.println("连接断开");
		}
		
		//接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用,由 MqttClient.connect 激活此回调。
		@Override
		public void deliveryComplete(IMqttDeliveryToken arg0) {
			
		}
		
		//接收订阅后的消息
		@Override
		public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
			System.out.println("topic:"+topic);
			System.out.println("message:"+mqttMessage.getPayload());
		}
		
	}
	
	public static void main(String[] args) throws MqttException {
		Client client = new Client();
		client.connect();
	}
}
