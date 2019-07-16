package com.iot.smart_city.manage.device.service.impl;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.smart_city.manage.device.builder.MqttPayloadBuilder;
import com.iot.smart_city.manage.device.group.MqttClientGroup;
import com.iot.smart_city.manage.device.mapper.ScDeviceMapper;
import com.iot.smart_city.manage.device.mapper.ScProductMapper;
import com.iot.smart_city.manage.device.ruler.TopicRuler;
import com.iot.smart_city.manage.device.ruler.TopicRuler.TopicCheckResult;
import com.iot.smart_city.manage.device.service.ScDeviceService;
import com.iot.smart_city.manage.device.service.ScMqttService;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.device.status.DeviceStatus;
import com.iot.smart_city.model.product.ScProduct;
import com.iot.smart_city.model.topic.response.payload.LifecyclePayLoad;
import com.iot.smart_city.model.topic.response.payload.changeaction.Action;
import com.iot.smart_city.model.topic.type.TopicType;
import com.iot.smart_city.util.mqtt_client.GenerateMessageNum;
import com.rabbitmq.client.Channel;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.device.request.ScDeviceAddRequest;
import com.smart_city.common.device.request.ScDeviceDelRequest;
import com.smart_city.common.device.request.ScDeviceQueryRequest;
import com.smart_city.common.device.request.ScDeviceUpdateRequest;
import com.smart_city.common.device.response.MqttResponse;
import com.smart_city.common.device.response.ScDeviceAddResponse;
import com.smart_city.common.device.response.ScDeviceDelResponse;
import com.smart_city.common.device.response.ScDeviceQueryResponse;
import com.smart_city.common.device.response.ScDeviceUpdateResponse;
import com.smart_city.common.device.response.code.ScDeviceCode;
import com.smart_city.common.device.response.msg.ScDeviceMsg;
/**
 * 设备管理
 * @author chrilwe
 *
 */
@Service
public class ScDeviceServiceImpl implements ScDeviceService {
	
	@Autowired
	private ScDeviceMapper scDeviceMapper;
	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private ScProductMapper scProductMapper;
	@Autowired
	private ScMqttService scMqttService;
	@Autowired
	private Channel channel;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	private static final int reTryTimes = 10;//尝试重新连接次数

	/**
	 * 创建设备，并且返回设备的登录账号和密码
	 * deviceName,devicePass
	 */
	@Transactional
	public ScDeviceAddResponse scDeviceAdd(ScDeviceAddRequest scDeviceRequest) {
		if(scDeviceRequest == null) {
			return new ScDeviceAddResponse(ScDeviceCode.PARAM_NULL,ScDeviceMsg.PARAM_NULL,false,null,null);
		}
		ScDevice scDevice = scDeviceRequest.getScDevice();
		scDevice.setStatus(DeviceStatus.START_NO);
		
		//生成设备登录密码
		GenerateMessageNum num = new GenerateMessageNum();
		String generate = num.generate();
		if(StringUtils.isEmpty(generate)) {
			return new ScDeviceAddResponse(ScDeviceCode.SYSTEM_ERROR,ScDeviceMsg.SYSTEM_ERROR,false,null,null);
		}
		scDevice.setDevicePassword(generate);
		scDeviceMapper.scDeviceAdd(scDevice);
		return new ScDeviceAddResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true,scDevice.getDeviceName(),generate);
	}
	
	/**
	 * 删除设备:设备发布成功不允许删除
	 */
	@Transactional
	public ScDeviceDelResponse scDeviceDel(ScDeviceDelRequest scDeviceDelRequest) {
		if(scDeviceDelRequest == null) {
			return new ScDeviceDelResponse(ScDeviceCode.PARAM_NULL,ScDeviceMsg.PARAM_NULL,false);
		}
		String deviceId = scDeviceDelRequest.getDeviceId();
		ScDevice findById = scDeviceMapper.findById(deviceId);
		if(findById.getStatus().equals(DeviceStatus.ONLINE)) {
			return new ScDeviceDelResponse(ScDeviceCode.DEVICE_ONLINE,ScDeviceMsg.DEVICE_ONLINE,false);
		}
		scDeviceMapper.scDeviceDel(deviceId);
		return new ScDeviceDelResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true);
	}
	
	/**
	 * 更新设备:设备已经发布成功不允许修改
	 */
	@Transactional
	public ScDeviceUpdateResponse scDeviceUpdate(ScDeviceUpdateRequest scDeviceUpdateRequest) {
		if(scDeviceUpdateRequest == null) {
			return new ScDeviceUpdateResponse(ScDeviceCode.PARAM_NULL,ScDeviceMsg.PARAM_NULL,false,null,null);
		}
		ScDevice scDevice = scDeviceUpdateRequest.getScDevice();
		//判断当前设备是否已经下线
		String deviceId = scDevice.getDeviceId();
		ScDevice findById = scDeviceMapper.findById(deviceId);
		String status = findById.getStatus();
		if(status.equals(DeviceStatus.ONLINE)) {
			return new ScDeviceUpdateResponse(ScDeviceCode.DEVICE_ONLINE,ScDeviceMsg.DEVICE_ONLINE,false,null,null);
		}
		
		//重新生成设备登录密码
		GenerateMessageNum num = new GenerateMessageNum();
		String generate = num.generate();
		if(StringUtils.isEmpty(generate)) {
			return new ScDeviceUpdateResponse(ScDeviceCode.SYSTEM_ERROR,ScDeviceMsg.SYSTEM_ERROR,false,null,null);
		}
		scDevice.setDevicePassword(generate);
		scDevice.setStatus(DeviceStatus.START_NO);
		scDeviceMapper.scDeviceUpdate(scDevice);
		return new ScDeviceUpdateResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true,scDevice.getDeviceName(),generate);
	}

	
	public ScDeviceQueryResponse scDeviceQuery(ScDeviceQueryRequest scDeviceQueryRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ScDevice findDeviceById(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		return scDeviceMapper.findById(id);
	}
	
	/**
	 * 设备激活
	 * @throws MqttException 
	 */
	@Transactional
	public ScBaseResponse wakeUpDevice(String deviceId, HttpServletRequest request) throws MqttException {
		if(StringUtils.isEmpty(deviceId)) {
			return new ScBaseResponse(ScDeviceCode.PARAM_NULL,ScDeviceMsg.PARAM_NULL,false);
		}
		
		//查询设备产品信息
		ScDevice findById = scDeviceMapper.findById(deviceId);
		if(findById == null) {
			return new ScBaseResponse(ScDeviceCode.DEVICE_NO_EXISTS,ScDeviceMsg.DEVICE_NO_EXISTS,false);
		}
		if(findById.getStatus().equals(DeviceStatus.START_YES)) {
			return new ScBaseResponse(ScDeviceCode.DEVICE_STARTED,ScDeviceMsg.DEVICE_STARTED,false);
		}
		String productId = findById.getProductId();
		ScProduct scProduct = scProductMapper.findById(productId);
		String productName = scProduct.getProductName();
		String deviceName = findById.getDeviceName();
		
		//拼接激活topic  /sys/{productKey}/{deviceName}/thing/lifecycle
		String topic = "/sys/" + productName +"/" + deviceName +"/thing/lifecycle";
		
		//客户端连接mqtt服务器，发送激活信息给设备端,设备端返回应答
		String clientId = "";
		int qos = 1;
		String deviceSecret = "";
		String iotId = "";
		boolean flag = false;
		//设备连接失败，尝试重新连接
		for(int i = 0;i < reTryTimes;i++) {
			try {
				MqttResponse connect = scMqttService.connect(clientId,request);
				if(connect.isSuccess()) {
					flag = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		if(flag) {
			try {
				LifecyclePayLoad payload = MqttPayloadBuilder.buildLifecyclePayload(Action.ENABLE, deviceName, 
						deviceSecret, iotId, productName, TopicType.LIFE_CYCLE);
				MqttResponse mqttResponse = scMqttService.publish(payload , clientId, topic, qos);
				findById.setStatus(DeviceStatus.START_YES);
				scDeviceMapper.scDeviceUpdate(findById);
				return new ScBaseResponse(ScDeviceCode.SUCCESS,ScDeviceMsg.SUCCESS,true);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new ScBaseResponse(ScDeviceCode.SYSTEM_ERROR,ScDeviceMsg.SYSTEM_ERROR,false);
	}

	/**
	 * 订阅设备消息
	 */
	public ScBaseResponse sub(String topic,String deviceName) {
		if(StringUtils.isEmpty(topic)) {
			return new ScBaseResponse(ScDeviceCode.PARAM_NULL,ScDeviceMsg.PARAM_NULL,false);
		}
		//判断设备是否已经激活
		ScDevice findByName = scDeviceMapper.findByName(deviceName);
		if(findByName == null) {
			return new ScBaseResponse(ScDeviceCode.DEVICE_NO_EXISTS,ScDeviceMsg.DEVICE_NO_EXISTS,false);
		}
		String status = findByName.getStatus();
		if(!status.equals(DeviceStatus.ONLINE)) {
			return new ScBaseResponse(ScDeviceCode.PLEASE_WAKEUP_DEVICE,ScDeviceMsg.PLEASE_WAKEUP_DEVICE,false);
		}
		
		String clientId = "";
		TopicRuler ruler = new TopicRuler();
		TopicCheckResult checkAndReturnTopicType = ruler.checkAndReturnTopicType(topic);
		if(checkAndReturnTopicType.success) {
			//判断连接mqtt服务器连接实例是否在本服务中
			String clientIp = stringRedisTemplate.boundValueOps(clientId).get();
			MqttClient mqttClient = MqttClientGroup.getInstance().get(clientId);
		}
		return null;
	}
	
}
