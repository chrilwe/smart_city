package com.smart_city.common.device.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScDeviceQueryRequest extends ScBaseRequest {
	int page;//当前页码
	int rows;//当前页大小
	String keyword;//关键词
	String filter;
}
