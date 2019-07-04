package com.iot.smart_city.message.service;

import com.smart_city.common.base.ScBaseResponse;

public interface RabbitmqService {
	public ScBaseResponse sendMessage(String message,String exchange,String queue,String routingKey);//发送消息
}
