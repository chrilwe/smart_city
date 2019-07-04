package com.smart_city.common.product.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScProductDelRequest extends ScBaseRequest {
	String productId;
}
