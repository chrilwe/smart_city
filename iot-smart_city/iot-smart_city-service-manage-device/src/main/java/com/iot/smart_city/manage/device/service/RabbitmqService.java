package com.iot.smart_city.manage.device.service;

import com.smart_city.common.base.ScBaseResponse;

public interface RabbitmqService {
	public ScBaseResponse subscribeProductAdd(String exchange, String queue, String routKey);//消息订阅
	public ScBaseResponse subscribeDeviceAdd(String exchange, String queue, String routKey);
}
