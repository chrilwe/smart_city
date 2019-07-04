package com.iot.smart_city.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScAuthApi;
import com.iot.smart_city.auth.service.ScAuthService;
import com.iot.smart_city.model.ucenter.ScAuthToken;
import com.iot.smart_city.model.ucenter.ScUserInfo;
import com.iot.smart_city.util.mqtt_client.CookieUtil;
import com.smart_city.common.base.ScBaseController;
import com.smart_city.common.ucenter.request.ScLoginRequest;
import com.smart_city.common.ucenter.response.ScLoginResponse;
/**
 * 认证中心
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/")
public class ScAuthController extends ScBaseController implements ScAuthApi {
	
	@Autowired
	private ScAuthService scAuthService;
	
	@Value("${auth.cookieDomain}")
	private String cookieDomain;
	@Value("${auth.cookieMaxAge}")
	private int cookieMaxAge;
	@Value("${auth.cookiePath}")
	private String cookiePath;
	@Value("${auth.cookieName}")
	private String cookieName;
	
	/**
	 * 登录 认证
	 */
	@Override
	@PostMapping("/login")
	public ScLoginResponse scLogin(ScLoginRequest scLoginRequest) {
		ScLoginResponse loginResponse = scAuthService.login(scLoginRequest);
		ScAuthToken scAuthToken = loginResponse.getScAuthToken();
		String accessToken = scAuthToken.getAccessToken();
		String jwtToken = scAuthToken.getJwtToken();
		
		//设置请求头和cookie
		response.addHeader("Authorization", "Bearer "+jwtToken);
		CookieUtil.addCookie(response, cookieDomain, cookiePath, cookieName, accessToken, cookieMaxAge, false);
		return loginResponse;
	}
	
	/**
	 * 根据身份令牌查询用户信息
	 */
	@Override
	@GetMapping("/userinfo")
	public ScUserInfo getUserInfoByAccessToken(@RequestParam("accessToken")String accessToken) {
		ScUserInfo scUserInfo = scAuthService.getUserInfoByAccessToken(accessToken, request);
		return scUserInfo;
	}

}
