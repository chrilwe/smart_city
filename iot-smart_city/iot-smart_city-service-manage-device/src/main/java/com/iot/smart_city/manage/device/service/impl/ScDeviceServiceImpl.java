package com.iot.smart_city.manage.device.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iot.smart_city.manage.device.builder.MqttPayloadBuilder;
import com.iot.smart_city.manage.device.group.MqttClientGroup;
import com.iot.smart_city.manage.device.mapper.ScDeviceMapper;
import com.iot.smart_city.manage.device.mapper.ScProductMapper;

import com.iot.smart_city.manage.device.service.ScDeviceService;
import com.iot.smart_city.manage.device.service.ScMqttService;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.device.status.DeviceStatus;
import com.iot.smart_city.model.product.ScProduct;
import com.iot.smart_city.model.topic.response.payload.LifecyclePayLoad;
import com.iot.smart_city.model.topic.response.payload.PayLoad;
import com.iot.smart_city.model.topic.response.payload.changeaction.Action;
import com.iot.smart_city.model.topic.type.TopicType;
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
	private static final int reTryTimes = 10;//尝试重新连接次数

	@Override
	public ScDeviceAddResponse scDeviceAdd(ScDeviceAddRequest scDeviceRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceDelResponse scDeviceDel(ScDeviceDelRequest scDeviceDelRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceUpdateResponse scDeviceUpdate(ScDeviceUpdateRequest scDeviceUpdateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceQueryResponse scDeviceQuery(ScDeviceQueryRequest scDeviceQueryRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDevice findDeviceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 设备激活
	 * @throws MqttException 
	 */
	@Override
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
		for(int i = 0;i < reTryTimes;i++) {
			try {
				MqttResponse connect = scMqttService.connect(clientId);
				//设备连接成功，发送激活消息
				if(connect.isSuccess()) {
					LifecyclePayLoad payload = MqttPayloadBuilder.buildLifecyclePayload(Action.ENABLE, deviceName, 
							deviceSecret, iotId, productName, TopicType.LIFE_CYCLE);
					MqttResponse mqttResponse = scMqttService.publish(payload , clientId, topic, qos);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		return null;
	}
	
}
