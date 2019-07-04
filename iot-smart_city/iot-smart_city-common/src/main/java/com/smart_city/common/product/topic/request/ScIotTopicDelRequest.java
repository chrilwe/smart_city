package com.smart_city.common.product.topic.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScIotTopicDelRequest extends ScBaseRequest {
	private String topicId;
}
