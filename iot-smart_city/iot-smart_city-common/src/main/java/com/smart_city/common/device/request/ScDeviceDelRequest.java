package com.smart_city.common.device.request;

import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScDeviceDelRequest extends ScBaseRequest {
	String deviceId;
}
