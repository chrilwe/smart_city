package com.smart_city.common.device.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScDeviceUpdateResponse extends ScBaseResponse{
	private String deviceKey;
	private String devicePassword;
	public ScDeviceUpdateResponse(int code, String message, 
			boolean isSuccess, String deviceKey, String devicePassword) {
		super(code, message, isSuccess);
		this.deviceKey = deviceKey;
		this.devicePassword = devicePassword;
	}

}
