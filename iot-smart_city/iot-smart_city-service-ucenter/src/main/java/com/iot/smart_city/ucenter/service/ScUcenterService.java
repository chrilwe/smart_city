package com.iot.smart_city.ucenter.service;

import com.iot.smart_city.model.ucenter.ScUser;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.ucenter.request.ScRegisterRequest;
import com.smart_city.common.ucenter.response.ScRegisterResponse;

public interface ScUcenterService {
	public ScUser findByUsername(String username);
	public ScRegisterResponse register(ScRegisterRequest registerRequest);
	//检查手机号是否已经被注册
	public ScBaseResponse checkPhone(String phone);
	//检查邮箱是否已经被注册
	public ScBaseResponse checkEmail(String email);
}
