package com.iot.smart_city.manage.device.service.impl;

import org.springframework.stereotype.Service;

import com.iot.smart_city.manage.device.service.ScDeviceService;
import com.iot.smart_city.model.device.ScDevice;
import com.smart_city.common.device.request.ScDeviceAddRequest;
import com.smart_city.common.device.request.ScDeviceDelRequest;
import com.smart_city.common.device.request.ScDeviceQueryRequest;
import com.smart_city.common.device.request.ScDeviceUpdateRequest;
import com.smart_city.common.device.response.ScDeviceAddResponse;
import com.smart_city.common.device.response.ScDeviceDelResponse;
import com.smart_city.common.device.response.ScDeviceQueryResponse;
import com.smart_city.common.device.response.ScDeviceUpdateResponse;
/**
 * 设备管理
 * @author chrilwe
 *
 */
@Service
public class ScDeviceServiceImpl implements ScDeviceService {

	@Override
	public ScDeviceAddResponse scDeviceAdd(ScDeviceAddRequest scDeviceRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceDelResponse scDeviceDel(ScDeviceDelRequest scDeviceDelRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceUpdateResponse scDeviceUpdate(ScDeviceUpdateRequest scDeviceUpdateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDeviceQueryResponse scDeviceQuery(ScDeviceQueryRequest scDeviceQueryRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScDevice findDeviceById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
