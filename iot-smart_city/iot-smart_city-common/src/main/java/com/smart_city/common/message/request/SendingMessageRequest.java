package com.smart_city.common.message.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class SendingMessageRequest extends ScBaseRequest {
	private String messageId;
}
