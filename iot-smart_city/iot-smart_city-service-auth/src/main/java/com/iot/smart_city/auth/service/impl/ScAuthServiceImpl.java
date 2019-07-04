package com.iot.smart_city.auth.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.auth.service.ScAuthService;
import com.iot.smart_city.model.ucenter.ScAuthToken;
import com.iot.smart_city.model.ucenter.ScUserInfo;
import com.iot.smart_city.util.mqtt_client.Oauth2Util;
import com.smart_city.common.services.ScServiceList;
import com.smart_city.common.ucenter.request.ScLoginRequest;
import com.smart_city.common.ucenter.response.ScLoginResponse;
import com.smart_city.common.ucenter.response.code.ScUcenterCode;
import com.smart_city.common.ucenter.response.msg.ScUcenterMsg;

@Service
public class ScAuthServiceImpl implements ScAuthService {

	@Autowired
	private LoadBalancerClient loadBalancerClient;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${auth.clientId}")
	private String clientId;
	@Value("${auth.clientSecret}")
	private String clientSecret;
	@Value("${auth.tokenValiditySeconds}")
	private long tokenValiditySeconds;

	@Override
	public ScLoginResponse login(ScLoginRequest loginRequest) {
		if(loginRequest == null) {
			return new ScLoginResponse(ScUcenterCode.PARAM_NULL,ScUcenterMsg.PARAM_NULL,false,null);
		}
		String code1 = loginRequest.getCode1();
		String code2 = loginRequest.getCode2();
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		if(StringUtils.isEmpty(code1) || StringUtils.isEmpty(code2) || StringUtils.isEmpty(username)
				|| StringUtils.isEmpty(password)) {
			return new ScLoginResponse(ScUcenterCode.ERROR_PARAMS,ScUcenterMsg.ERROR_PARAMS,false,null);
		}
		if(!code1.equals(code2)) {
			return new ScLoginResponse(ScUcenterCode.CODE_ERROR,ScUcenterMsg.CODE_ERROR,false,null);
		}
		
		//认证请求地址
		String authUrl = this.authUrl();
		
		//向security发起令牌申请
		HttpEntity httpEntity = httpEntity(clientId, clientSecret, username, password);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
			public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
		});
		ResponseEntity<Map> authResponse = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
		if(authResponse.getStatusCodeValue() == 401) {
			return new ScLoginResponse(ScUcenterCode.AUTH_USERNAME_OR_PASSWORD_ERROR,ScUcenterMsg.AUTH_USERNAME_OR_PASSWORD_ERROR,false,null);
		}
		Map body = authResponse.getBody();
		//{access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VycGljIjpudWxsLCJ1c2VyX25hbWUiOiJpdGNhc3QiLCJzY29wZSI6WyJhcHAiXSwibmFtZSI6InRlc3QwMiIsInV0eXBlIjoiMTAxMDAyIiwiaWQiOiI0OSIsImV4cCI6MTU2MTg1MTcwNCwiYXV0aG9yaXRpZXMiOlsiZmluZCJdLCJqdGkiOiI5ODEzMjhkMi05NGZhLTRiZGEtYWI4Yi0xYjA5ODI3ZWQ2YmMiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCIsInVzZXJuYW1lIjoiaXRjYXN0In0.U9uY9OmoDz05_ahcmV6BSXLGXb1FaO5KcctAQLUg-CzEXpqbDf8E-jCoDJ1lbjigv2upgz0M8f5yJPk9Umv6QsAayDOiSK5DxalCL-dYQC7TFP-Li5MQ4zzF-YO8GUzCL20BgqTSmHorlMX4CBlfPjU4R05r4UBq-6FLeZX7MOfccxlRv8bbBEzuaAyHfITurEY0dXZn3wXjWnTHuheJizbOxkp0I9if4JuULN5kDsq7QB7T6uJOpia0kr-dShdOaTiBK7IbhPWoEeCDhLBUE-17ID6UgCgRA7mxAsrQLOrNz2PVW-UOsqPpM8z5hdaBsIe8pCF5mI5ZcWrOzRcsww, token_type=bearer, refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VycGljIjpudWxsLCJ1c2VyX25hbWUiOiJpdGNhc3QiLCJzY29wZSI6WyJhcHAiXSwiYXRpIjoiOTgxMzI4ZDItOTRmYS00YmRhLWFiOGItMWIwOTgyN2VkNmJjIiwibmFtZSI6InRlc3QwMiIsInV0eXBlIjoiMTAxMDAyIiwiaWQiOiI0OSIsImV4cCI6MTU2MTg1MTcwNCwiYXV0aG9yaXRpZXMiOlsiZmluZCJdLCJqdGkiOiIyYzBiMTcyMC1kYWNkLTQyNjQtODVkMi1mZTk1ODA4ZWUwM2IiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCIsInVzZXJuYW1lIjoiaXRjYXN0In0.ZSyWNaPoy05zr3S-zs56dqGhowTfdFYYWNZ9RUqs_AocCB0EcFRAs1tb6FmHyF-3jt0emh5hW26rENYAmZBakw3xJWSEQWM-DtDS1_d-JpqXQFgiwvlhyZh9YN6t_mj0thTixnkWrhfDG65voiGtbWL3iJl3SsMRA2Fakat59EHJu1gFYfmFWR0D7LvghvxbCZm9crCjHUuXf3aKA2cU2WdujjIKjO8X-BlnCoN6wkxIexbAbmxBgQVo4KY_GLg2rkIoqX2ajFdgiltlf9lvVgnWwDfybMLsyPssdxlmYRrtQm2wpYFtm-WaZxnsRFRH2vt2v1Gs5Sn_Ilq-oxbZaw, expires_in=43199, scope=app, jti=981328d2-94fa-4bda-ab8b-1b09827ed6bc}
		String jwtToken = (String) body.get("access_token");
		String accessToken = (String) body.get("jti");
		String refreshToken = (String) body.get("refresh_token");
		if(StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken)
				|| StringUtils.isEmpty(jwtToken)) {
			return new ScLoginResponse(ScUcenterCode.AUTH_FAIL,ScUcenterMsg.AUTH_FAIL,false,null);
		}
		ScAuthToken authToken = new ScAuthToken();
		authToken.setAccessToken(accessToken);
		authToken.setJwtToken(jwtToken);
		authToken.setRefreshToken(refreshToken);
		
		//将AuthToken 放入redis
		saveAuthToken2Redis(authToken);
		return new ScLoginResponse(ScUcenterCode.SUCCESS,ScUcenterMsg.SUCCESS,true,authToken);
	}

	// HttpEntity
	private HttpEntity httpEntity(String clientId,String clientSecret,String username,String password) {
		//对clientId:clientSecret进行base64编码
		String string = clientId + ":" + clientSecret;
		byte[] encode = Base64Utils.encode(string.getBytes());
		String httpBasic = "Basic " + new String(encode);
		
		// 设置请求头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Authorization", httpBasic);

		// 设置请求体
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "password");
		body.add("username", username);
		body.add("password", password);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(body,
				headers);
		return httpEntity;
	}

	// 认证请求地址
	private String authUrl() {
		ServiceInstance choose = loadBalancerClient.choose(ScServiceList.IOT_SMART_CITY_SERVICE_AUTH);
		URI uri = choose.getUri();
		String authUrl = uri + "/auth/oauth/token";
		return authUrl;
	}
	
	//将AuthToken存入redis
	private boolean saveAuthToken2Redis(ScAuthToken scAuthToken) {
		stringRedisTemplate.boundValueOps(scAuthToken.getAccessToken())
							.set(JSON.toJSONString(scAuthToken),tokenValiditySeconds,TimeUnit.SECONDS);
		return true;
	}

	@Override
	public ScUserInfo getUserInfoByAccessToken(String accessToken, HttpServletRequest request) {
		if(StringUtils.isEmpty(accessToken)) {
			return null;
		}
		//根据acessToken在redis中查询authToken
		String authTokenJson = stringRedisTemplate.boundValueOps("accessToken").get();
		if(StringUtils.isEmpty(authTokenJson)) {
			return null;
		}
		ScAuthToken authToken = JSON.parseObject(authTokenJson, ScAuthToken.class);
		
		//从请求头中获取jwt令牌并且解析令牌
		Map<String, String> userMap = Oauth2Util.getJwtClaimsFromHeader(request);
		ScUserInfo userinfo = new ScUserInfo();
		if(userMap == null) {
			return null;
		}
		
		//重新设置过期时间
		stringRedisTemplate.boundValueOps(accessToken).expire(tokenValiditySeconds,TimeUnit.SECONDS);
		
		userinfo.setId(userMap.get("userId"));
		userinfo.setAccessToken(accessToken);
		userinfo.setJwtToken(authToken.getJwtToken());
		userinfo.setName(userMap.get("name"));
		userinfo.setUsername(userMap.get("username"));
		userinfo.setUserpic(userMap.get("userpic"));
		userinfo.setUtype(userMap.get("utype"));
		return userinfo;
	}
}
