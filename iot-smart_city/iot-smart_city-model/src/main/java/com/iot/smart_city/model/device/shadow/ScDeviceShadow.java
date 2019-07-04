package com.iot.smart_city.model.device.shadow;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
/**
 * 设备影子
 * @author chrilwe
 *
 */
@Data
@ToString
public class ScDeviceShadow implements Serializable {
	private static final long serialVersionUID = -504194965806583185L;
	
	private ScDeviceShadowState state;
	private ScDeviceShadowMetadata metadata;
	private long timestamp;
	private long version;
}
