package com.smart_city.common.search.request;

import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScSearchRequest extends ScBaseRequest {
	private String keyword;//关键词
	private String queryType;//搜索类型
	private int currentPage;//当前页
	private int pageSize;//页码
}
