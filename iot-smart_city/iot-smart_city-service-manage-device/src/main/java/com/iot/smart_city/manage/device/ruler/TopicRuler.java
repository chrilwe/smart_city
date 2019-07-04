package com.iot.smart_city.manage.device.ruler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.iot.smart_city.manage.device.bean.SpringBeanContains;
import com.iot.smart_city.manage.device.mapper.ScDeviceMapper;
import com.iot.smart_city.manage.device.mapper.ScProductMapper;
import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.iot.smart_city.model.topic.type.TopicType;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.product.topic.response.code.ScIotTopicCode;
import com.smart_city.common.product.topic.response.msg.ScIotTopicMsg;

/**
 * 主题规则校验
 * @author chrilwe
 *
 */
public class TopicRuler {
	
	/**
	 * @param type 主题类型
	 * @param topic 主题
	 * @return
	 */
	public ScBaseResponse checkTopic(String type,String topic) {
		if(StringUtils.isEmpty(topic) || StringUtils.isEmpty(type)) {
			throw new RuntimeException("主题类型和主题都不能为空");
		}
		
		if(!type.equals(TopicType.EVENT)
				&&!type.equals(TopicType.LIFE_CYCLE)
				&&!type.equals(TopicType.ON_OFF)&&!type.equals(TopicType.PROPERTY)) {
			throw new RuntimeException("主题类型错误");
		}
		
		//校验上下线通知：/as/mqtt/status/{productKey}/{deviceName}
		
		//校验设备属性上报通知：/sys/{productKey}/{deviceName}/thing/property/post
		
		//校验设备事件上报通知：/sys/{productKey}/{deviceName}/thing/event/post
		
		//校验设备生命周期变更通知：/sys/{productKey}/{deviceName}/thing/lifecycle
		if(type.equals(TopicType.ON_OFF)) {
			return this.checkOnlineAndOffline(topic);
		} else if(type.equals(TopicType.PROPERTY)) {
			return this.checkProperty(topic);
		} else if(type.equals(TopicType.EVENT)) {
			return this.checkEvent(topic);
		} else if(type.equals(TopicType.LIFE_CYCLE)) {
			return this.checkLifecycle(topic);
		}
		return new ScBaseResponse(ScIotTopicCode.SYSTEM_ERROR,ScIotTopicMsg.SYSTEM_ERROR,false);
	}
	
	private ScBaseResponse checkOnlineAndOffline(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 5) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		//校验前三个参数
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("as") || !twoParam.equals("mqtt") 
				|| !thirdParam.equals("status")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		
		String productKey = split[3];
		String deviceName = split[4];
		//校验后面两个参数，判断当前是否存在
		return this.checkProductKeyAndDeviceName(productKey, deviceName);
	}
	
	private ScBaseResponse checkProperty(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		ScBaseResponse response = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!response.isSuccess()) {
			return response;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("property") || !sixParam.equals("post")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		return new ScBaseResponse(ScIotTopicCode.SUCCESS,ScIotTopicMsg.SUCCESS,true);
	}
	
	private ScBaseResponse checkEvent(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		ScBaseResponse response = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!response.isSuccess()) {
			return response;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("event") || !sixParam.equals("post")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		return new ScBaseResponse(ScIotTopicCode.SUCCESS,ScIotTopicMsg.SUCCESS,false);
	}
	
	private ScBaseResponse checkLifecycle(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		ScBaseResponse response = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!response.isSuccess()) {
			return response;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("lifecycle")) {
			return new ScBaseResponse(ScIotTopicCode.ERROR_PARAM,ScIotTopicMsg.ERROR_PARAM,false);
		}
		return new ScBaseResponse(ScIotTopicCode.SUCCESS,ScIotTopicMsg.SUCCESS,false);
	}
	
	private ScBaseResponse checkProductKeyAndDeviceName(String productKey,String deviceName) {
		SpringBeanContains bean = new SpringBeanContains();
		ScProductMapper scProductMapper = bean.getBean(ScProductMapper.class);
		ScProduct findById = scProductMapper.findById(productKey);
		if(findById == null) {
			return new ScBaseResponse(ScIotTopicCode.PRODUCT_NULL,ScIotTopicMsg.PRODUCT_NULL,false);
		}
		ScDeviceMapper scDeviceMapper = bean.getBean(ScDeviceMapper.class);
		ScDevice findByName = scDeviceMapper.findByName(deviceName);
		if(findByName == null) {
			return new ScBaseResponse(ScIotTopicCode.DEVICE_NULL,ScIotTopicMsg.DEVICE_NULL,false);
		}
		return new ScBaseResponse(ScIotTopicCode.SUCCESS,ScIotTopicMsg.SUCCESS,true);
	}
	
	//从topic中获取deviceName和productKey
	public Map<String, String> getProductKeyAndDeviceNameFromTopic(String topic,String type) {
		//校验topic
		ScBaseResponse checkTopic = this.checkTopic(type, topic);
		if(checkTopic.isSuccess()) {
			Map<String,String> map = new HashMap<>();
			if(type.equals(TopicType.ON_OFF)) {
				String substring = topic.substring(1);
				String[] split = substring.split("/");
				String productKey = split[3];
				String deviceName = split[4];
				map.put("productKey", productKey);
				map.put("deviceName", deviceName);
			} else if(type.equals(TopicType.PROPERTY)) {
				String substring = topic.substring(1);
				String[] split = substring.split("/");
				String productKey = split[1];
				String deviceName = split[2];
				map.put("productKey", productKey);
				map.put("deviceName", deviceName);
			} else if(type.equals(TopicType.EVENT)) {
				String substring = topic.substring(1);
				String[] split = substring.split("/");
				String productKey = split[1];
				String deviceName = split[2];
				map.put("productKey", productKey);
				map.put("deviceName", deviceName);
			} else if(type.equals(TopicType.LIFE_CYCLE)) {
				String substring = topic.substring(1);
				String[] split = substring.split("/");
				String productKey = split[1];
				String deviceName = split[2];
				map.put("productKey", productKey);
				map.put("deviceName", deviceName);
			}
			return map;
		}
		return null;
	}
}
