package com.iot.smart_city.manage.device.builder;
/**
 * 消息载荷构建
 * @author chrilwe
 *
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import com.iot.smart_city.model.topic.response.payload.EventPayLoad;
import com.iot.smart_city.model.topic.response.payload.IsOnlinePayLoad;
import com.iot.smart_city.model.topic.response.payload.LifecyclePayLoad;

public class MqttPayloadBuilder {
	
	/**
	 * 设备生命周期载荷 
	 * @param action
	 * @param deviceName
	 * @param deviceSecret
	 * @param iotId
	 * @param productKey
	 * @return
	 */
	public static LifecyclePayLoad buildLifecyclePayload(String action,String deviceName,
			String deviceSecret,String iotId,String productKey,String type) {
		LifecyclePayLoad payload = new LifecyclePayLoad();
		payload.setAction(action);
		payload.setDeviceName(deviceName);
		payload.setDeviceSecret(deviceSecret);
		payload.setIotId(iotId);
		payload.setMessageCreateTime(System.currentTimeMillis());
		payload.setProductKey(productKey);
		payload.setType(type);
		return payload;
	}
	
	/**
	 * 设备上下线载荷
	 * @param clientIp
	 * @param deviceName
	 * @param lastTime
	 * @param productKey
	 * @param status
	 * @return
	 */
	public static IsOnlinePayLoad buildIsOnlinePayLoad(String clientIp, String deviceName,
			String lastTime, String productKey, String status) {
		IsOnlinePayLoad payload = new IsOnlinePayLoad();
		payload.setClientIp(clientIp);
		payload.setDeviceName(deviceName);
		payload.setLastTime(lastTime);
		payload.setProductKey(productKey);
		payload.setStatus(status);
		payload.setTime(dateFormater("yyyy-MM-dd HH:mm:ss", new Date()));
		
		return payload;
	}
	
	
	/**
	 * 事件通知载荷
	 * @param deviceName
	 * @param iotId
	 * @param productKey
	 * @param type
	 * @param value
	 * @return
	 */
	public static EventPayLoad buildEventPayLoad(String deviceName,String iotId,
			String productKey,String type,String value) {
		EventPayLoad payload = new EventPayLoad();
		payload.setDeviceName(deviceName);
		payload.setIotId(iotId);
		payload.setProductKey(productKey);
		payload.setTime(System.currentTimeMillis());
		payload.setType(type);
		payload.setValue(value);
		return payload;
	}
	
	
	private static String dateFormater(String formatString, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateFormat = sdf.format(new Date());
		return dateFormat;
	}
}
