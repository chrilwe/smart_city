package com.smart_city.common.product.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScProductQueryRequest extends ScBaseRequest {
	int page;
	int rows;
	String keyword;
	String filter;
}
