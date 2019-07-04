package com.smart_city.common.product.response;

import com.smart_city.common.base.ScBaseResponse;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ScProductUpdateResponse extends ScBaseResponse {

	public ScProductUpdateResponse(int code, String message, boolean isSuccess) {
		super(code, message, isSuccess);
	}

}
