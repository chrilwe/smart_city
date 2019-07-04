package com.smart_city.common.ucenter.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScLoginRequest extends ScBaseRequest {
	private String username;
	private String password;
	private String code1;
	private String code2;
}
