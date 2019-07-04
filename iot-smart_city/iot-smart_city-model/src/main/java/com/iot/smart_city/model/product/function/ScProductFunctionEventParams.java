package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 功能事件输出参数
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunctionEventParams implements Serializable {
	private static final long serialVersionUID = 4893619242105968219L;
	
	private String id;
	private String eventId;//事件id
	private String name;
	private String UDID;
	private String dataType;//数据类型
	
}
