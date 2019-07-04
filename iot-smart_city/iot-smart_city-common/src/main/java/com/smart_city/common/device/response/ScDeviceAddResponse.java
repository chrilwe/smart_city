package com.smart_city.common.device.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;

@Data
public class ScDeviceAddResponse extends ScBaseResponse {
	String devicekey;//设备登录key
	String devicePassword;//设备登录密码
	public ScDeviceAddResponse(int code, String message, boolean isSuccess, 
			String devicekey, String devicePassword) {
		super(code,message,isSuccess);
		this.devicekey = devicekey;
		this.devicePassword = devicePassword;
	}
	
}
