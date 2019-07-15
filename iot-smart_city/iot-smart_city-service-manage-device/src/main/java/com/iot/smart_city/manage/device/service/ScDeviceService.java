package com.iot.smart_city.manage.device.service;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.iot.smart_city.model.device.ScDevice;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.device.request.ScDeviceAddRequest;
import com.smart_city.common.device.request.ScDeviceDelRequest;
import com.smart_city.common.device.request.ScDeviceQueryRequest;
import com.smart_city.common.device.request.ScDeviceUpdateRequest;
import com.smart_city.common.device.response.ScDeviceAddResponse;
import com.smart_city.common.device.response.ScDeviceDelResponse;
import com.smart_city.common.device.response.ScDeviceQueryResponse;
import com.smart_city.common.device.response.ScDeviceUpdateResponse;

public interface ScDeviceService {
	//添加设备信息
	public ScDeviceAddResponse scDeviceAdd(ScDeviceAddRequest scDeviceRequest);
		
	//删除设备
	public ScDeviceDelResponse scDeviceDel(ScDeviceDelRequest scDeviceDelRequest);
		
	//修改设备信息
	public ScDeviceUpdateResponse scDeviceUpdate(ScDeviceUpdateRequest scDeviceUpdateRequest);

	//查询设备信息
	public ScDeviceQueryResponse scDeviceQuery(ScDeviceQueryRequest scDeviceQueryRequest);
	
	//根据id查询
	public ScDevice findDeviceById(String id);
	
	//设备激活
	public ScBaseResponse wakeUpDevice(String deviceId,HttpServletRequest request) throws MqttException;
}
