package com.iot.smart_city.model.topic.response.payload;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 设备事件上报
 * @author Administrator
 *
 */
@Data
@ToString
public class EventPayLoad extends PayLoad implements Serializable {
	private String iotId;//设备在平台内的唯一标识
	private String productKey;
	private String deviceName;
	private String type;//事件类型
	private String value;//事件参数json
	private long time;//事件产生时间
}
