package com.iot.smart_city.api;
/**
 * 搜索服务
 * @author chrilwe
 *
 */

import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.search.request.ScSearchDeviceAddRequest;
import com.smart_city.common.search.request.ScSearchProductAddRequest;
import com.smart_city.common.search.request.ScSearchRequest;
import com.smart_city.common.search.response.ScSearchResponse;

public interface ScSearchApi {
	//根据关键词搜索
	public ScSearchResponse search(ScSearchRequest searchRequest);
	//插入数据
	public ScBaseResponse addProduct(ScSearchProductAddRequest addProductRequest);
	public ScBaseResponse addDevice(ScSearchDeviceAddRequest addDeviceRequest);
}
