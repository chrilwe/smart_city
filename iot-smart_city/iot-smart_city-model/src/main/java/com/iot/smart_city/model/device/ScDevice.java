package com.iot.smart_city.model.device;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ScDevice implements Serializable {
	private static final long serialVersionUID = -7408189938656405206L;
	
	private String deviceId;
	private String productId;
	private String deviceName;
	private String status;
	private String lastTime;//设备最后上线时间
	private String remarks;
}
