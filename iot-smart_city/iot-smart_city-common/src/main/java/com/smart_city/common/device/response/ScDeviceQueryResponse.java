package com.smart_city.common.device.response;

import java.util.List;

import com.iot.smart_city.model.device.ScDevice;
import com.smart_city.common.base.ScBaseResponse;

public class ScDeviceQueryResponse extends ScBaseResponse {
	
	List<ScDevice> scDevices;

	public ScDeviceQueryResponse(int code, String message, 
			boolean isSuccess, List<ScDevice> scDevices) {
		super(code, message, isSuccess);
		this.scDevices = scDevices;
	}

}
