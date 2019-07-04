package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 功能服务
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunctionService implements Serializable {
	private static final long serialVersionUID = -6801909465059934404L;
	
	private String serviceId;
	private String productId;//产品id
	private String type;//服务类型
	private String serviceName;//服务名称
	private String serviceUDID;//服务标识符
	private String isSync;//调用方式：同步或者异步
	private String description;//描述
	
}
