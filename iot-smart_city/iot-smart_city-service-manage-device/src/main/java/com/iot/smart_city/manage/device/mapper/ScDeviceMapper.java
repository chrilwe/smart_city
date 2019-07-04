package com.iot.smart_city.manage.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iot.smart_city.model.device.ScDevice;

public interface ScDeviceMapper {
	public void scDeviceAdd(ScDevice scDevice);
	public void scDeviceDel(String deviceId);
	public void scDeviceUpdate(ScDevice scDevice);
	public List<ScDevice> scDeviceQuery(int start,int size);
	public List<ScDevice> findByProductIdAndStatus(@Param("productId")String productId,
			@Param("status")String status);
	public ScDevice findByName(String deviceName);
}
