package com.smart_city.common.product.topic.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScIotTopicQueryRequest extends ScBaseRequest {
	private int page;
	private int rows;
	private String keyword;
	private String type;
}
