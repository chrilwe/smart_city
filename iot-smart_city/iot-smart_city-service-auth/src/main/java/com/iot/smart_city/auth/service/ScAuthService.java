package com.iot.smart_city.auth.service;

import javax.servlet.http.HttpServletRequest;

import com.iot.smart_city.model.ucenter.ScUserInfo;
import com.smart_city.common.ucenter.request.ScLoginRequest;
import com.smart_city.common.ucenter.response.ScLoginResponse;

public interface ScAuthService {
	public ScLoginResponse login(ScLoginRequest loginRequest);
	public ScUserInfo getUserInfoByAccessToken(String accessToken, HttpServletRequest request);
}
