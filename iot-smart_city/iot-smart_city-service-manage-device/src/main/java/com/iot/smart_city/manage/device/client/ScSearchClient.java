package com.iot.smart_city.manage.device.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.request.ScSearchRequest;
import com.smart_city.common.search.response.ScSearchResponse;
import com.smart_city.common.services.ScServiceList;

@FeignClient(ScServiceList.IOT_SMART_CITY_SERVICE_SEARCH)
@RequestMapping("/search")
public interface ScSearchClient {
	
	/**
	 * 关键词搜索
	 */
	@GetMapping("/item")
	public ScSearchResponse search(ScSearchRequest searchRequest);
	

	@PostMapping("/add/product")
	public ScBaseResponse addProduct(ScSearchProductAddRequest addProductRequest);

	@PostMapping("/add/device")
	public ScBaseResponse addDevice(ScSearchDeviceAddRequest addDeviceRequest);
}
