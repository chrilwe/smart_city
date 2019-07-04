package com.iot.smart_city.model.topic.response.payload.changeaction;

import lombok.Data;
import lombok.ToString;

/**
 * 设备生命周期行为
 * @author chrilwe
 *
 */
public interface Action {
	public static final String CREATE = "create";
	public static final String DELETE = "delete";
	public static final String DISABLE = "disable";
	public static final String ENABLE = "enable";
}
