package com.smart_city.common.device.request;

import com.iot.smart_city.model.device.ScDevice;
import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class ScDeviceUpdateRequest extends ScBaseRequest {
	ScDevice scDevice;
}
