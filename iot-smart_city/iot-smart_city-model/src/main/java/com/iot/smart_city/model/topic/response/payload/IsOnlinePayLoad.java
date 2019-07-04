package com.iot.smart_city.model.topic.response.payload;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 设备上下线状态数据
 * @author chrilwe
 *
 */
@Data
@ToString
public class IsOnlinePayLoad extends PayLoad implements Serializable {
	private String status;
	private String productKey;
	private String deviceName;
	private String time;//发送通知的时间点
	private String lastTime;//状态变更前最后一次通信时间
	private String clientIp;//设备公网出口IP
}
