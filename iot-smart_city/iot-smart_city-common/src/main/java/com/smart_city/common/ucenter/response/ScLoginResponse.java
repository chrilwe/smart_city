package com.smart_city.common.ucenter.response;

import com.iot.smart_city.model.ucenter.ScAuthToken;
import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;

@Data
public class ScLoginResponse extends ScBaseResponse {
	
	private ScAuthToken scAuthToken;

	public ScLoginResponse(int code, String message, boolean isSuccess,ScAuthToken scAuthToken) {
		super(code, message, isSuccess);
		this.scAuthToken = scAuthToken;
		
	}

}
