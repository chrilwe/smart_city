package com.iot.smart_city.api;
/**
 * 会员中心
 * @author chrilwe
 *
 */

import com.iot.smart_city.model.ucenter.ScUserInfo;
import com.smart_city.common.ucenter.request.ScLoginRequest;
import com.smart_city.common.ucenter.response.ScLoginResponse;

public interface ScAuthApi {
	//用户登录
	public ScLoginResponse scLogin(ScLoginRequest scLoginRequest);
	//根据身份令牌获取用户信息
	public ScUserInfo getUserInfoByAccessToken(String accessToken);
}
