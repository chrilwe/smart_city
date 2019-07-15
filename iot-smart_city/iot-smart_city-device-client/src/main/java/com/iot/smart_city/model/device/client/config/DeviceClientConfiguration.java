package com.iot.smart_city.model.device.client.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContextListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iot.smart_city.model.topic.response.payload.IsOnlinePayLoad;
import com.iot.smart_city.model.topic.response.payload.status.PayLoadStatus;

@Configuration
public class DeviceClientConfiguration {
	
	@Value("${mqtt.clear_session}")
	private boolean clearSession;
	@Value("${mqtt.connection_timeout}")
	private int timeout;
	@Value("${mqtt.auto_reconnect}")
	private boolean autoReconnect;
	@Value("${mqtt.keep_alive}")
	private int keepAlive;
	@Value("${mqtt.topic}")
	private String topic;
	@Value("${mqtt.username}")
	private String username;
	@Value("${mqtt.password}")
	private String password;
	@Value("${mqtt.ipAddress}")
	private String ipAddress;
	@Value("${mqtt.deviceName}")
	private String deviceName;
	@Value("${mqtt.productKey}")
	private String productKey;
	
	@Bean
	public MqttConnectOptions options() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(clearSession);
		options.setConnectionTimeout(timeout);
		options.setAutomaticReconnect(autoReconnect);
		options.setKeepAliveInterval(keepAlive);
		options.setUserName(username);
		options.setPassword(password.toCharArray());
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-dd HH:mm:ss");
		String outlineTime = sdf.format(new Date());
		
		//下线通知载荷
		IsOnlinePayLoad payload = new IsOnlinePayLoad();
		payload.setClientIp(ipAddress);
		payload.setDeviceName(deviceName);
		payload.setProductKey(productKey);
		payload.setStatus(PayLoadStatus.NO);
		payload.setTime(outlineTime);
		options.setWill(topic, "closed".getBytes(), 1, true);
		return options;
	}
}
