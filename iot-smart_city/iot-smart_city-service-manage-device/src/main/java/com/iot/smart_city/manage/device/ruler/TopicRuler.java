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

import lombok.Data;
import lombok.ToString;

/**
 * 主题规则校验
 * @author chrilwe
 *
 */
public class TopicRuler {
	
	//校验topic并且返回校验主题类型
	public TopicCheckResult checkAndReturnTopicType(String topic) {
		TopicCheckResult result = this.checkOnlineAndOffline(topic);
		if(result.success) {
			return result;
		}
		TopicCheckResult checkOnlineAndOffline = this.checkOnlineAndOffline(topic);
		if(checkOnlineAndOffline.success) {
			return checkOnlineAndOffline;
		}
		TopicCheckResult checkEvent = this.checkEvent(topic);
		if(checkEvent.success) {
			return checkEvent;
		}
		TopicCheckResult checkLifecycle = this.checkLifecycle(topic);
		if(checkLifecycle.success) {
			return checkLifecycle;
		}
		TopicCheckResult checkProperty = this.checkProperty(topic);
		if(checkProperty.success) {
			return checkProperty;
		}
		return new TopicCheckResult(false,null);
	}
	
	private TopicCheckResult checkOnlineAndOffline(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 5) {
			return new TopicCheckResult(false,TopicType.ON_OFF);
		}
		//校验前三个参数
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("as") || !twoParam.equals("mqtt") 
				|| !thirdParam.equals("status")) {
			return new TopicCheckResult(false,TopicType.ON_OFF);
		}
		
		String productKey = split[3];
		String deviceName = split[4];
		//校验后面两个参数，判断当前是否存在
		return this.checkProductKeyAndDeviceName(productKey, deviceName);
	}
	
	private TopicCheckResult checkProperty(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new TopicCheckResult(false,TopicType.PROPERTY);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new TopicCheckResult(false,TopicType.PROPERTY);
		}
		TopicCheckResult result = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!result.success) {
			return result;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("property") || !sixParam.equals("post")) {
			return new TopicCheckResult(false,TopicType.PROPERTY);
		}
		return new TopicCheckResult(true,TopicType.PROPERTY);
	}
	
	private TopicCheckResult checkEvent(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new TopicCheckResult(false,TopicType.EVENT);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new TopicCheckResult(false,TopicType.EVENT);
		}
		TopicCheckResult result = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!result.success) {
			return result;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("event") || !sixParam.equals("post")) {
			return new TopicCheckResult(false,TopicType.EVENT);
		}
		return new TopicCheckResult(true,TopicType.EVENT);
	}
	
	private TopicCheckResult checkLifecycle(String topic) {
		String substring = topic.substring(1);
		String[] split = substring.split("/");
		if(split.length != 6) {
			return new TopicCheckResult(false,TopicType.LIFE_CYCLE);
		}
		String firstParam = split[0];
		String twoParam = split[1];
		String thirdParam = split[2];
		if(!firstParam.equals("sys")) {
			return new TopicCheckResult(false,TopicType.LIFE_CYCLE);
		}
		TopicCheckResult result = this.checkProductKeyAndDeviceName(firstParam, twoParam);
		if(!result.success) {
			return result;
		}
		String fourParam = split[3];
		String fiveParam = split[4];
		String sixParam = split[5];
		if(!fourParam.equals("thing") || !fiveParam.equals("lifecycle")) {
			return new TopicCheckResult(false,TopicType.LIFE_CYCLE);
		}
		return new TopicCheckResult(true,TopicType.LIFE_CYCLE);
	}
	
	private TopicCheckResult checkProductKeyAndDeviceName(String productKey,String deviceName) {
		SpringBeanContains bean = new SpringBeanContains();
		ScProductMapper scProductMapper = bean.getBean(ScProductMapper.class);
		ScProduct findById = scProductMapper.findById(productKey);
		if(findById == null) {
			return new TopicCheckResult(false,null);
		}
		ScDeviceMapper scDeviceMapper = bean.getBean(ScDeviceMapper.class);
		ScDevice findByName = scDeviceMapper.findByName(deviceName);
		if(findByName == null) {
			return new TopicCheckResult(false,null);
		}
		return new TopicCheckResult(true,null);
	}

	
	@Data
	@ToString
	public class TopicCheckResult {
		public boolean success;
		public String type;
		public TopicCheckResult(boolean success, String type) {
			this.success = success;
			this.type = type;
		}
	}
}
