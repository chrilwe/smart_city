package com.iot.smart_city.model.product.function;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 事件枚举
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScProductFunctionEventEmun implements Serializable {
	private static final long serialVersionUID = -5830096497216274146L;
	
	private String id;
	private String eventId;//事件id
	private String paramsDesc;//参数描述
	private int data;//事件触发值
}
