package com.iot.smart_city.manage.device.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.iot.smart_city.manage.device.group.MqttClientGroup;
import com.iot.smart_city.manage.device.service.ScMqttCallBackService;
import com.iot.smart_city.manage.device.service.ScMqttService;
import com.iot.smart_city.model.topic.response.payload.PayLoad;
import com.iot.smart_city.util.mqtt_client.GenerateMessageNum;
import com.smart_city.common.device.response.MqttResponse;
import com.smart_city.common.device.response.code.ScDeviceCode;
import com.smart_city.common.device.response.msg.ScDeviceMsg;
/**
 * mqtt服务
 * @author chrilwe
 *
 */
@Service
public class ScMqttServiceImpl implements ScMqttService {
	
	@Value("${mqtt.server_uri}")
	private String mqttServerUri;
	@Value("${mqtt.clear_session}")
	private boolean cleanSession;
	@Value("${mqtt.connect_timeout}")
	private int connectTimeout;
	@Value("${mqtt.auto_reconnect}")
	private boolean autoReconnect;
	@Value("${mqtt.keep_alive}")
	private int keepAlive;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 发布内容
	 * @throws MqttPersistenceException 
	 * @throws MqttException 
	 * @throws UnsupportedEncodingException 
	 */
	@Override
	public MqttResponse publish(PayLoad payLoad, String clientId, String topic, int qos) throws MqttPersistenceException, MqttException, UnsupportedEncodingException {
		//消息报文
		//生成唯一消息id
		GenerateMessageNum gmn = new GenerateMessageNum();
		String messageId = gmn.generate();
		MqttMessage message = new MqttMessage();
		message.setId(Integer.parseInt(messageId));
		message.setPayload(payLoad.toString().getBytes("UTF-8"));
		message.setQos(qos);
		message.setRetained(true);
		
		MqttClient mqttClient = MqttClientGroup.getInstance().get(clientId);
		mqttClient.publish(topic, message);
		return new MqttResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true,clientId,topic);
	}

	/**
	 * 订阅消息
	 * @throws MqttException 
	 */
	@Override
	public MqttResponse subscribe(String clientId,String topic) throws MqttException {
		MqttClient mqttClient = MqttClientGroup.getInstance().get(clientId);
		IMqttToken subscribeWithResponse = mqttClient.subscribeWithResponse(topic);
		subscribeWithResponse.setActionCallback(new IMqttActionListener(){

			@Override
			public void onFailure(IMqttToken arg0, Throwable arg1) {
				//TODO 订阅失败
				throw new RuntimeException("订阅失败");
			}

			@Override
			public void onSuccess(IMqttToken arg0) {
				//TODO 订阅成功，添加订阅的消息记录
			}
			
		});
		return new MqttResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true,clientId,topic);
	}

	// 创建MqttConnectOptions
	private MqttConnectOptions options() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(cleanSession);
		options.setConnectionTimeout(connectTimeout);
		options.setAutomaticReconnect(autoReconnect);
		options.setKeepAliveInterval(keepAlive);

		return options;
	}
	
	/**
	 * 连接到mqtt服务器
	 * 对于分布式多服务部署情况，采用rabbitmq通知其中一个已连接的客户端实例执行相应的业务
	 */
	@Override
	public MqttResponse connect(String clientId,HttpServletRequest request) throws MqttException {
		MqttConnectOptions options = this.options();
		MqttClient mqttClient = new MqttClient(mqttServerUri, clientId);
		mqttClient.setCallback(new ScMqttCallBackService(request));
		
		IMqttToken connectWithResult = mqttClient.connectWithResult(options);
		boolean complete = connectWithResult.isComplete();
		//连接成功，将连接存入内存，并且将当前服务器的地址存入redis，作为连接标记
		if(complete) {
			MqttClientGroup.getInstance().put(clientId, mqttClient);
			stringRedisTemplate.boundValueOps(clientId).set(request.getRequestURI());;
			return new MqttResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true,clientId,null);
		}
		return new MqttResponse(ScDeviceCode.SYSTEM_ERROR,ScDeviceMsg.SYSTEM_ERROR,false,clientId,null);
	}
}
