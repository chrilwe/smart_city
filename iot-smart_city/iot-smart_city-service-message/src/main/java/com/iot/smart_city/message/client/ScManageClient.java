package com.iot.smart_city.message.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iot.smart_city.model.device.ScDevice;
import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.services.ScServiceList;

@FeignClient(ScServiceList.IOT_SMART_CITY_SERVICE_MANAGE)
public interface ScManageClient {
	
	@GetMapping("/manage/product/find")
	public ScProduct findProductById(@RequestParam("productId")String productId);
	
	@GetMapping("/manage/device/find")
	public ScDevice findDeviceById(@RequestParam("id")String id);
}
