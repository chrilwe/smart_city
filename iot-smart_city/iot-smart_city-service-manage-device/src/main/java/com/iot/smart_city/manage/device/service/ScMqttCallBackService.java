package com.iot.smart_city.manage.device.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.smart_city.manage.device.bean.SpringBeanContains;
import com.iot.smart_city.manage.device.ruler.TopicRuler;
import com.iot.smart_city.manage.device.ruler.TopicRuler.TopicCheckResult;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.device.response.MqttResponse;

/**
 * 订阅消息监听
 * @author chrilwe
 *
 */
public class ScMqttCallBackService implements MqttCallback {
	
	private static final int reTryTimes = 10;//尝试重新连接次数
	
	/**
	 * 连接失败
	 */
	@Override
	public void connectionLost(Throwable arg0) {
		String clientId = "";
		//尝试重新连接一定次数，如果没有连上，删除连接
		SpringBeanContains bean = new SpringBeanContains();
		ScMqttService mqttService = bean.getBean(ScMqttService.class);
		for(int i = 0;i < reTryTimes;i++) {
			try {
				MqttResponse connect = mqttService.connect(clientId);
				if(connect.isSuccess()) {
					break;
				}
			} catch (MqttException e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 接收消息
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//根据消息类型，执行相应的业务
		TopicRuler ruler = new TopicRuler();
		TopicCheckResult checkAndReturnTopicType = ruler.checkAndReturnTopicType(topic);
		String type = checkAndReturnTopicType.getType();
		
		byte[] payload = message.getPayload();
		
	}
	
	//TODO 接收上线下线消息类型
	
}
