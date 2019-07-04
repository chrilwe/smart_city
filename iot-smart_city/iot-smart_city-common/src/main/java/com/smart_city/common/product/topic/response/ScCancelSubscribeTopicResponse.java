package com.smart_city.common.product.topic.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;

/**
 * 取消订阅
 * @author chrilwe
 *
 */
@ToString
public class ScCancelSubscribeTopicResponse extends ScBaseResponse {
	public ScCancelSubscribeTopicResponse(int code, String message, boolean isSuccess) {
		super(code, message, isSuccess);
	}
}
