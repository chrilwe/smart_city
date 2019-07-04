package com.iot.smart_city.manage.device.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.iot.smart_city.manage.device.mapper.ScDeviceMapper;
import com.iot.smart_city.manage.device.mapper.ScIotTopicMapper;
import com.iot.smart_city.manage.device.ruler.TopicRuler;
import com.iot.smart_city.manage.device.service.ScIotTopicService;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.device.status.DeviceStatus;
import com.iot.smart_city.model.topic.ScIotTopic;
import com.iot.smart_city.model.topic.response.payload.LifecyclePayLoad;
import com.iot.smart_city.model.topic.response.payload.PayLoad;
import com.iot.smart_city.model.topic.status.TopicStatus;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.product.topic.request.ScIotTopicAddRequest;
import com.smart_city.common.product.topic.request.ScIotTopicDelRequest;
import com.smart_city.common.product.topic.request.ScIotTopicQueryRequest;
import com.smart_city.common.product.topic.request.ScIotTopicSubscribeRequest;
import com.smart_city.common.product.topic.request.ScIotTopicUpdateRequest;
import com.smart_city.common.product.topic.response.ScCancelSubscribeTopicResponse;
import com.smart_city.common.product.topic.response.ScIotTopicAddResponse;
import com.smart_city.common.product.topic.response.ScIotTopicDelResponse;
import com.smart_city.common.product.topic.response.ScIotTopicQueryResponse;
import com.smart_city.common.product.topic.response.ScIotTopicUpdateResponse;
import com.smart_city.common.product.topic.response.code.ScIotTopicCode;
import com.smart_city.common.product.topic.response.msg.ScIotTopicMsg;

@Service
public class ScIotTopicServiceImpl implements ScIotTopicService {

	@Value("${mqtt.server_uri}")
	private String serverUri;
	@Value("${mqtt.clear_session}")
	private boolean clearSession;
	@Value("${mqtt.connection_timeout}")
	private int connectionTimeOut;
	@Value("${mqtt.keep_alive}")
	private int keepAlive;
	@Value("${mqtt.auto_reconnect}")
	private boolean autoReconnect;
	
	@Autowired
	private ScIotTopicMapper scIotTopicMapper;
	@Autowired
	private ScDeviceMapper scDeviceMapper;

	@Override
	public ScIotTopicQueryResponse scIotTopicQuery(ScIotTopicQueryRequest scIotTopicQueryRequest) {
		if (scIotTopicQueryRequest == null) {
			return new ScIotTopicQueryResponse(ScIotTopicCode.PARAM_NULL, ScIotTopicMsg.PARAM_NULL, false, null);
		}

		// TODO

		return null;
	}

	@Override
	@Transactional
	public ScIotTopicAddResponse scIotTopicAdd(ScIotTopicAddRequest scIotTopicAddRequest) {
		if (scIotTopicAddRequest == null) {
			return new ScIotTopicAddResponse(ScIotTopicCode.PARAM_NULL, ScIotTopicMsg.PARAM_NULL, false);
		}

		ScIotTopic scIotTopic = scIotTopicAddRequest.getScIotTopic();
		String productId = scIotTopic.getProductId();
		String topic = scIotTopic.getTopic();
		String type = scIotTopic.getType();
		if (StringUtils.isEmpty(productId) || StringUtils.isEmpty(topic) || StringUtils.isEmpty(type)) {
			return new ScIotTopicAddResponse(ScIotTopicCode.ERROR_PARAM, ScIotTopicMsg.ERROR_PARAM, false);
		}

		String topicId = UUID.randomUUID().toString();
		scIotTopic.setId(topicId);
		scIotTopicMapper.addTopic(scIotTopic);
		return new ScIotTopicAddResponse(ScIotTopicCode.SUCCESS, ScIotTopicMsg.SUCCESS, true);
	}

	@Override
	@Transactional
	public ScIotTopicDelResponse scIotTopicDel(ScIotTopicDelRequest scIotTopicDelRequest) {
		if (scIotTopicDelRequest == null) {
			return new ScIotTopicDelResponse(ScIotTopicCode.PARAM_NULL, ScIotTopicMsg.PARAM_NULL, false);
		}

		String topicId = scIotTopicDelRequest.getTopicId();
		if (StringUtils.isEmpty(topicId)) {
			return new ScIotTopicDelResponse(ScIotTopicCode.ERROR_PARAM, ScIotTopicMsg.ERROR_PARAM, false);
		}

		scIotTopicMapper.deleteTopic(topicId);
		return new ScIotTopicDelResponse(ScIotTopicCode.SUCCESS, ScIotTopicMsg.SUCCESS, true);
	}

	@Override
	@Transactional
	public ScIotTopicUpdateResponse scIotTopicUpdate(ScIotTopicUpdateRequest scIotTopicUpdateRequest) {
		if (scIotTopicUpdateRequest == null) {
			return new ScIotTopicUpdateResponse(ScIotTopicCode.PARAM_NULL, ScIotTopicMsg.PARAM_NULL, false);
		}

		ScIotTopic scIotTopic = scIotTopicUpdateRequest.getScIotTopic();
		scIotTopicMapper.updateTopic(scIotTopic);
		return new ScIotTopicUpdateResponse(ScIotTopicCode.SUCCESS, ScIotTopicMsg.SUCCESS, true);
	}

	/**
	 * 订阅主题
	 */
	@Override
	public ScBaseResponse subscribe(ScIotTopicSubscribeRequest subscribeRequest) {
		String username = "";
		String pw = "";
		// 主题id不能为空
		if (subscribeRequest == null) {
			return new ScBaseResponse(ScIotTopicCode.PARAM_NULL,ScIotTopicMsg.PARAM_NULL,false);
		}
		
		String productId = subscribeRequest.getProductId();
		String topicIds = subscribeRequest.getTopicIds();
		if(StringUtils.isEmpty(productId) || StringUtils.isEmpty(topicIds)) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		
		//校验订阅 和 设备状态
		List<ScDevice> startNoDevices = new ArrayList<>();//封装未激活或者下线的设备信息
		List<ScIotTopic> startYesDevices = new ArrayList<>();//封装已经激活的主题信息
		List<String> topicIdList = this.parase(topicIds);
		for (String topicId : topicIdList) {
			ScIotTopic findByIdAndProductId = scIotTopicMapper.findByIdAndProductId(topicId, productId);
			if(findByIdAndProductId.getStatus().equals(ScIotTopicCode.TOPIC_SUBSCRIBE_YES)) {
				continue;
			}
			
			//获取topic中的产品设备信息
			String topic = findByIdAndProductId.getTopic();
			String type = findByIdAndProductId.getType();
			TopicRuler ruler = new TopicRuler();
			Map<String, String> map = ruler.getProductKeyAndDeviceNameFromTopic(topic, type);
			String deviceName = map.get("deviceName");
			ScDevice findByName = scDeviceMapper.findByName(deviceName);
			String status = findByName.getStatus();
			if(status.equals(DeviceStatus.START_NO) || status.equals(DeviceStatus.OFFLINE)) {
				startNoDevices.add(findByName);
			} if(status.equals(DeviceStatus.START_YES)) {
				startYesDevices.add(findByIdAndProductId);
			}
		}
		
		//设备未激活或者已下线
		if(startNoDevices != null) {
			String deviceNames = "";
			for(ScDevice device : startNoDevices) {
				deviceNames += device.getDeviceName();
			}
			return new ScBaseResponse(ScIotTopicCode.DEVICE_START_NO,deviceNames + ScIotTopicMsg.DEVICE_START_NO,false);
		}
		
		//设备激活状态，订阅
		String clientId = UUID.randomUUID().toString();
		LifecyclePayLoad payload = new LifecyclePayLoad();
		payload.setIotId(clientId);
		payload.setAction(TopicStatus.SUBSCRIBE_YES);
		payload.setDeviceName("#");
		payload.setDeviceSecret("");
		payload.setMessageCreateTime(System.currentTimeMillis());
		payload.setProductKey("#");
		//this.subscribeMsg(username, pw, clientId, startYesDevices, payload, 1, true);
		return null;
	}

	// 订阅消息
	private void subscribeMsg(String username,String pw,String clientId, List<ScIotTopic> scIotTopics, PayLoad payload, int qos, boolean retained)
			throws MqttException {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(clearSession);
		options.setConnectionTimeout(connectionTimeOut);
		options.setAutomaticReconnect(autoReconnect);
		options.setKeepAliveInterval(keepAlive);
		options.setUserName(username);
		options.setPassword(pw.toCharArray());
		//options.setWill(topic, "closed".getBytes(), qos, retained);

		MqttClient mqttClient = new MqttClient(serverUri, clientId);
		mqttClient.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable arg0) {
				System.out.println("客户端连接下线");
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println("接收到mqttmessage:" + JSON.toJSONString(message));
				//判断当前订阅是否需要取消
				String string = new String(message.getPayload());
				LifecyclePayLoad payload = null;
				try {
					payload = JSON.parseObject(string, LifecyclePayLoad.class);
				} catch (Exception e) {
					//TODO
				}
				if(payload.getAction().equals(TopicStatus.SUBSCRIBE_CANCEL)) {
					//取消订阅
					
				}
			}
		});
		mqttClient.connect(options);
		for(ScIotTopic topic : scIotTopics) {
			mqttClient.subscribe(topic.getTopic());
		}
	}

	/**
	 * 取消订阅
	 */
	@Override
	public ScCancelSubscribeTopicResponse cancelSubscribe(String topicIds) {
		String username = "";
		String pw = "";
		// 主题id不能为空
		if (StringUtils.isEmpty(topicIds)) {
			return new ScCancelSubscribeTopicResponse(ScIotTopicCode.PARAM_NULL,ScIotTopicMsg.PARAM_NULL,false);
		}
		
		List<String> idList = parase(topicIds);
		List<ScIotTopic> topics = new ArrayList<ScIotTopic>(); 
		//检查是否已经订阅
		for (String topicId : idList) {
			ScIotTopic findById = scIotTopicMapper.findById(topicId);
			String status = findById.getStatus();
			if(!status.equals(TopicStatus.SUBSCRIBE_YES)) {
				return new ScCancelSubscribeTopicResponse(ScIotTopicCode.TOPIC_SUBSCRIBE_NO,ScIotTopicMsg.TOPIC_SUBSCRIBE_NO,false);
			}
			topics.add(findById);
		}
		
		//将取消订阅的消息发布到topic中,已订阅的客户端收到取消订阅消息，关闭该订阅的客户端
		for (ScIotTopic scIotTopic : topics) {
			String topic = scIotTopic.getTopic();
			MqttMessage message = new MqttMessage();
			int messageId = Integer.parseInt(System.currentTimeMillis()+"");
			LifecyclePayLoad payload = new LifecyclePayLoad();
			payload.setAction(TopicStatus.SUBSCRIBE_CANCEL);
			payload.setDeviceName("#");
			payload.setDeviceSecret("");
			payload.setIotId(scIotTopic.getProductId());
			payload.setMessageCreateTime(System.currentTimeMillis());
			payload.setProductKey("#");
			message.setId(messageId);
			message.setPayload(JSON.toJSONString(payload).getBytes());
			message.setQos(1);
			message.setRetained(true);
			try {
				this.publishMsg(username, pw, 1, true, topic, UUID.randomUUID().toString(), message);
			} catch (MqttException e) {
				return new ScCancelSubscribeTopicResponse(ScIotTopicCode.SYSTEM_ERROR,ScIotTopicMsg.SYSTEM_ERROR,false);
			}
		}
		return  new ScCancelSubscribeTopicResponse(ScIotTopicCode.SUCCESS,ScIotTopicMsg.SUCCESS,true);
	}
	
	//发布消息
	private boolean publishMsg(String username,String pw,int qos,boolean retained,String topic,String clientId,MqttMessage message) throws MqttException {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setConnectionTimeout(100);
		options.setAutomaticReconnect(true);
		options.setKeepAliveInterval(20);
		options.setUserName(username);
		options.setPassword(pw.toCharArray());
		
		MqttClient mqttClient = new MqttClient(serverUri, clientId);
		mqttClient.connect(options);
		MqttTopic mqttTopic = mqttClient.getTopic(topic);
		MqttDeliveryToken publish = mqttTopic.publish(message);
		publish.waitForCompletion();
		//发布成功，关闭客户端连接
		mqttClient.disconnect();
		return true;
	}
	
	//解析字符串
	private List<String> parase(String ids) {
		List<String> list = new ArrayList<String>();
		String[] split = ids.split(",");
		for (String string : split) {
			list.add(string);
		}
		return list;
	}
}
