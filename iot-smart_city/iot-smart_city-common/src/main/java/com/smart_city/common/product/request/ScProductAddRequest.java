package com.smart_city.common.product.request;

import com.iot.smart_city.model.product.ScProduct;
import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;
@Data
public class ScProductAddRequest extends ScBaseRequest {
	ScProduct scProduct;
	
}
