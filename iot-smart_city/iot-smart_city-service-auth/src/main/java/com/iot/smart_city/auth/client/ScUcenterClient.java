package com.iot.smart_city.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.smart_city.model.ucenter.ScUser;
import com.smart_city.common.services.ScServiceList;

@FeignClient(ScServiceList.IOT_SMART_CITY_SERVICE_UCENTER)
public interface ScUcenterClient {
	/**
	 * 根据账号查询
	 */
	@GetMapping("/ucenter/findByUsername")
	public ScUser findUserByUsername(@RequestParam("username")String username);
}
