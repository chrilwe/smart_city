package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 产品功能
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunction implements Serializable {
	private static final long serialVersionUID = -3666138856783220547L;
	
	private String schema;//物的TSL描述schem
	private String link;//云端系统级uri,用来调用服务/订阅事件
	private Profile profile;
	private ScProductFuctionProperty productFunctionProperty;//功能属性
	private ScProductFunctionService productFunctionService;//功能服务
	private ScProductFunctionEvent productFunctionEvent;//功能事件
	
	@Data
	@ToString
	private class Profile {
		String productKey;
	}
}
