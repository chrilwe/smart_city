package com.iot.smart_city.gateway.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.iot.smart_city.util.mqtt_client.CookieUtil;
import com.iot.smart_city.util.mqtt_client.Oauth2Util;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.smart_city.common.base.ScBaseResponse;
/**
 * 登录认证前置过滤
 * @author chrilwe
 *
 */
@Component
public class AuthPreFilter extends ZuulFilter {
	
	private static final String PRE = "pre";
	private static final String POST = "post";
	private static final int CODE = 401;
	private static final String UNAUTHENTICATED = "UNAUTHENTICATED";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Value("${sc.cookieName}")
	private String cookieName;

	@Override
	public Object run() throws ZuulException {
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();
		HttpServletResponse response = currentContext.getResponse();
		
		//从请求头中取出jwt令牌
		String jwt = request.getHeader("Authorization");
		if(StringUtils.isEmpty(jwt)) {
			//拒绝访问
			this.unauthtnticated(currentContext);
			return null;
		}
		if(jwt.startsWith("Bearer ")) {
			//拒绝访问
			this.unauthtnticated(currentContext);
			return null;
		}
		
		//从cookie中取出accessToken
		Map<String, String> readCookie = CookieUtil.readCookie(request, cookieName);
		if(readCookie == null) {
			//拒绝访问
			this.unauthtnticated(currentContext);
			return null;
		}
		String accessToken = readCookie.get(cookieName);
		if(StringUtils.isEmpty(accessToken)) {
			//拒绝访问
			this.unauthtnticated(currentContext);
			return null;
		}
		
		//判断登录是否过期
		String authTokenJson = stringRedisTemplate.boundValueOps(accessToken).get();
		if(StringUtils.isEmpty(authTokenJson)) {
			//拒绝访问
			this.unauthtnticated(currentContext);
			return null;
		}
		return null;
	}

	@Override
	public boolean shouldFilter() {
		
		return true;
	}

	@Override
	public int filterOrder() {
		
		return 0;
	}

	@Override
	public String filterType() {
	
		return PRE;
	}
	
	private void unauthtnticated(RequestContext currentContext) {
		currentContext.setSendZuulResponse(false);
		ScBaseResponse res = new ScBaseResponse(CODE,UNAUTHENTICATED,false);
		currentContext.setResponseBody(JSON.toJSONString(res));
		currentContext.getResponse().setContentType("application/json;charset=utf-8");
	}
}
