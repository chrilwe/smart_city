package com.iot.smart_city.model.device.shadow;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ScDeviceShadowMetadata implements Serializable {
	private static final long serialVersionUID = 7361197323579345467L;
	
	@Data
	@ToString
	private class Desired {
		Color color;
		Sequence sequence;
	}
	
	@Data
	@ToString
	private class Color {
		long timestamp;
	}
	
	@Data
	@ToString
	private class Sequence {
		long timestamp;
	}
	
	@Data
	@ToString
	private class Reported {
		Color color;
		
	}
	
	private Desired desired;
	private Reported reported;
}
