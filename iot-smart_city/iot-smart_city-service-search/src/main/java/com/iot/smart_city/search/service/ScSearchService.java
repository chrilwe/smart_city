package com.iot.smart_city.search.service;

import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.request.ScSearchRequest;
import com.smart_city.common.search.response.ScSearchResponse;

public interface ScSearchService {
	public ScBaseResponse addProduct(ScSearchProductAddRequest addProductRequest);
	public ScBaseResponse addDevice(ScSearchDeviceAddRequest addDeviceRequest);
	public ScSearchResponse search(ScSearchRequest searchRequest);
}
