package com.smart_city.common.device.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScDeviceDelResponse extends ScBaseResponse {

	public ScDeviceDelResponse(int code, String message, boolean isSuccess) {
		super(code, message, isSuccess);
	}
	
}
