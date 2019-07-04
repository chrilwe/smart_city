package com.iot.smart_city.model.topic.response;

import com.iot.smart_city.model.topic.response.payload.PayLoad;

import lombok.Data;
import lombok.ToString;

/**
 * 订阅topic返回消息体
 * @author chrilwe
 *
 */
@Data
@ToString
public class SubscribeResponse {
	private String messageId;
	private String topic;
	private PayLoad payload;
	private long generateTime;//毫秒
	private int qos;
	private boolean success;
	private int code;
	private String msg;
}
