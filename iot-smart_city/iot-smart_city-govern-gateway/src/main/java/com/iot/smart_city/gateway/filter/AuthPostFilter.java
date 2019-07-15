package com.iot.smart_city.gateway.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.ucenter.response.code.ScUcenterCode;
import com.smart_city.common.ucenter.response.msg.ScUcenterMsg;

/**
 * 网关登录验证后置过滤
 * @author chrilwe
 *
 */
public class AuthPostFilter extends ZuulFilter {
	
	private static final String POST = "post";
	private static final int UNAUTHENTICATED = 401;

	@Override
	public Object run() throws ZuulException {
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletResponse response = currentContext.getResponse();
		HttpServletRequest request = currentContext.getRequest();
		
		//验证失败,返回验证失败的response
		int status = response.getStatus();
		if(UNAUTHENTICATED == status) {
			this.unauthtnticated(currentContext);
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
		
		return POST;
	}
	
	private void unauthtnticated(RequestContext currentContext) {
		ScBaseResponse res = new ScBaseResponse(UNAUTHENTICATED,ScUcenterMsg.AUTH_FAIL,false);
		currentContext.setResponseBody(JSON.toJSONString(res));
		currentContext.getResponse().setContentType("application/json;charset=utf-8");
	}
}
