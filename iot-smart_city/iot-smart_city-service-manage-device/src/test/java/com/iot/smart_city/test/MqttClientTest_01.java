package com.iot.smart_city.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.SpringApplication;
/**
 * mqtt客户端订阅
 * @author chrilwe
 *
 */
public class MqttClientTest_01 {
	public static void main(String[] args) throws MqttException {
		String topic = "testTopic";
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setConnectionTimeout(100);
		options.setAutomaticReconnect(true);
		options.setKeepAliveInterval(20);
		options.setUserName("test");
		options.setPassword("test".toCharArray());
		options.setWill(topic, "closed".getBytes(), 1, true);
		
		String serverUri = "tcp://127.0.0.1:1883";
		String clientId = "test01";
		MqttClient mqttClient = new MqttClient(serverUri,clientId);
		mqttClient.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println("topic:"+topic+"------message:"+new String(message.getPayload()));
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectionLost(Throwable arg0) {
				System.out.println("connect lost");
			}
		});
		mqttClient.connect(options);
		mqttClient.subscribe(topic);
	}
}
