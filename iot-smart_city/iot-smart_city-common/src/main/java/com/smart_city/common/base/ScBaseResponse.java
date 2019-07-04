package com.smart_city.common.base;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScBaseResponse {
	int code;//返回状态码
	String message;//返回信息
	boolean isSuccess;//请求成功与否
	
	public ScBaseResponse(int code, String message, boolean isSuccess) {
		this.code = code;
		this.message = message;
		this.isSuccess = isSuccess;
	}
	
	
}
