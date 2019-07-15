package com.smart_city.common.device.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class MqttResponse extends ScBaseResponse {
	String clientId;
	String topic;
	
	public MqttResponse(int code, String message, boolean isSuccess,
			String clientId, String topic) {
		super(code, message, isSuccess);
		this.clientId = clientId;
		this.topic = topic;
	}
	
}
