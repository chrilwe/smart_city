package com.smart_city.common.product.topic.response;

import com.iot.smart_city.model.topic.ScIotTopic;
import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;

@Data
public class ScIotTopicQueryResponse extends ScBaseResponse {
	
	ScIotTopic scIotTopic;

	public ScIotTopicQueryResponse(int code, String message, boolean isSuccess, ScIotTopic scIotTopic) {
		super(code, message, isSuccess);
		this.scIotTopic = scIotTopic;
		
	}

}
