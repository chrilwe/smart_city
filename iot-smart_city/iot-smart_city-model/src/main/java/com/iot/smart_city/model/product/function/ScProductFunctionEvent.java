package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 产品功能事件
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunctionEvent implements Serializable {
	private static final long serialVersionUID = -1506004367566292445L;
	
	private String id;
	private String name;
	private String type;
	private String UDID;
	private String eventType;
	private String description;
}
