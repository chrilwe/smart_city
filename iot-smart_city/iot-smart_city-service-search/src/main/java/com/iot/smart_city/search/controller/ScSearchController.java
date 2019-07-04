package com.iot.smart_city.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iot.smart_city.api.ScSearchApi;
import com.iot.smart_city.search.service.ScSearchService;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.request.ScSearchRequest;
import com.smart_city.common.search.response.ScSearchResponse;
/**
 * 搜索服务
 * @author chrilwe
 *
 */
@RestController
@RequestMapping("/search")
public class ScSearchController implements ScSearchApi {
	
	@Autowired
	private ScSearchService scSearchService;
	
	/**
	 * 关键词搜索
	 */
	@Override
	@GetMapping("/item")
	public ScSearchResponse search(ScSearchRequest searchRequest) {
		
		return scSearchService.search(searchRequest);
	}
	

	@Override
	@PostMapping("/add/product")
	public ScBaseResponse addProduct(ScSearchProductAddRequest addProductRequest) {
		
		return scSearchService.addProduct(addProductRequest);
	}

	@Override
	@PostMapping("/add/device")
	public ScBaseResponse addDevice(ScSearchDeviceAddRequest addDeviceRequest) {
		
		return scSearchService.addDevice(addDeviceRequest);
	}

}
