package com.iot.smart_city.model.device.client.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import com.iot.smart_city.model.topic.response.payload.IsOnlinePayLoad;
import com.iot.smart_city.model.topic.response.payload.status.PayLoadStatus;
import com.iot.smart_city.util.mqtt_client.GenerateMessageNum;
/**
 * spring容器加载完成后，连接到mqtt服务器
 * @author chrilwe
 *
 */
@Component
public class ContextListener implements ServletContextListener {
	
	@Value("${mqtt.server_uri}")
	private String serverUri;
	@Value("${mqtt.topic}")
	private String topic;
	@Value("${mqtt.ipAddress}")
	private String ipAddress;
	@Value("${mqtt.deviceName}")
	private String deviceName;
	@Value("${mqtt.productKey}")
	private String productKey;
	
	@Autowired
	private MqttConnectOptions mqttConnectOptions;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		MqttClient mqttClient = null;
		try {
			mqttClient = new MqttClient(serverUri,UUID.randomUUID().toString());
		} catch (MqttException e) {
			e.printStackTrace();
			return;
		}
		mqttClient.setCallback(new MqttCallback(){

			@Override
			public void connectionLost(Throwable arg0) {
				System.out.println("连接失败");
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println("-----topic:"+topic + " -----message:"+ new String(message.getPayload()));
			}
			
		});
		try {
			IMqttToken result = mqttClient.connectWithResult(mqttConnectOptions);
			//连接成功，向订阅该设备的客户端发送上线通知
			if(result.isComplete()) {
				GenerateMessageNum gmn = new GenerateMessageNum();
				String messageId = gmn.generate();
				
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss");
				String onlineTime = sdf.format(new Date());
				
				//上线通知载荷
				IsOnlinePayLoad payload = new IsOnlinePayLoad();
				payload.setClientIp(ipAddress);
				payload.setDeviceName(deviceName);
				payload.setStatus(PayLoadStatus.ON);
				payload.setTime(onlineTime);
				payload.setProductKey(productKey);
				
				MqttMessage message = new MqttMessage();
				message.setId(Integer.parseInt(messageId));
				message.setPayload(payload.toString().getBytes());
				message.setQos(1);
				message.setRetained(true);
			
				mqttClient.publish(topic, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} 
	}

}
