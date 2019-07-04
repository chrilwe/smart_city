package com.smart_city.common.product.topic.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

/**
 * 订阅主题请求
 * @author chrilwe
 *
 */
@Data
public class ScIotTopicSubscribeRequest extends ScBaseRequest {
	private String productId;//订阅的产品id
	private String topicIds;//订阅的主题
}
