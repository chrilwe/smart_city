package com.iot.smart_city.manage.device.service;
/**
 * 产品管理
 * @author chrilwe
 *
 */

import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.product.request.ScProductAddRequest;
import com.smart_city.common.product.request.ScProductDelRequest;
import com.smart_city.common.product.request.ScProductQueryRequest;
import com.smart_city.common.product.request.ScProductUpdateRequest;
import com.smart_city.common.product.response.ScProductAddResponse;
import com.smart_city.common.product.response.ScProductDelResponse;
import com.smart_city.common.product.response.ScProductQueryResponse;
import com.smart_city.common.product.response.ScProductUpdateResponse;

public interface ScProductService {
	//添加产品
	public ScProductAddResponse scProductAdd(ScProductAddRequest scProductAddRequest);
	
	//删除产品
	public ScProductDelResponse scProductDel(ScProductDelRequest scProductDelRequest);
	
	//更新产品
	public ScProductUpdateResponse scProductUpdate(ScProductUpdateRequest scProductUpdateRequest);
	
	//查询产品
	public ScProductQueryResponse scProductQuery(ScProductQueryRequest scProductQueryRequest);
	
	//根据id查询
	public ScProduct findById(String productId);
}
