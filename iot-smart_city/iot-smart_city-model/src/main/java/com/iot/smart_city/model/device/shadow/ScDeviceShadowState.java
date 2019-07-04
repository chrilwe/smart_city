package com.iot.smart_city.model.device.shadow;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class ScDeviceShadowState implements Serializable {
	private static final long serialVersionUID = -6727464462953193444L;
	
	private class Desired {
		String color;
		List<String> sequence;
	}
	
	private class Reported {
		String color;
	}
	
	private Desired desired;
	private Reported reported;
}
