package com.smart_city.common.product.topic.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScIotTopicAddResponse extends ScBaseResponse {
	
	public ScIotTopicAddResponse(int code, String message, boolean isSuccess) {
		super(code, message, isSuccess);
		
	}

}
