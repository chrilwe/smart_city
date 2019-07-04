package com.smart_city.common.product.topic.request;

import com.iot.smart_city.model.topic.ScIotTopic;
import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScIotTopicUpdateRequest extends ScBaseRequest {
	private ScIotTopic scIotTopic;
}
