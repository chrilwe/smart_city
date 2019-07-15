package com.iot.smart_city.manage.device.service;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.iot.smart_city.model.topic.response.payload.PayLoad;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.device.response.MqttResponse;

public interface ScMqttService {
	//发布
	public MqttResponse publish(PayLoad payLoad,String clientId, String topic, int qos) throws MqttPersistenceException, MqttException, UnsupportedEncodingException;
	//订阅
	public MqttResponse subscribe(String clientId,String topic) throws MqttException;
	//连接到mqtt服务器
	public MqttResponse connect(String clientId) throws MqttException;
}
