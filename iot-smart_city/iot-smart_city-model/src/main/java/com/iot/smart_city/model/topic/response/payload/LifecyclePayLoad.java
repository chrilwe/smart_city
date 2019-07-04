package com.iot.smart_city.model.topic.response.payload;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 设备生命周期变更:创建设备，删除设备，禁用设备,启用设备
 * @author Administrator
 *
 */
@Data
@ToString
public class LifecyclePayLoad extends PayLoad implements Serializable {
	private String action;
	private String iotId;
	private String productKey;
	private String deviceName;
	private String deviceSecret;
	private long messageCreateTime;
}
