package com.smart_city.common.device.response.msg;

public interface ScDeviceMsg {
	public static final String SUCCESS = "请求成功";
	public static final String SYSTEM_ERROR = "系统异常";
	public static final String PARAM_NULL = "参数不能为空";
	public static final String DEVICE_NO_EXISTS = "设备不存在";
	public static final String DEVICE_STARTED = "设备已经激活";
	public static final String DEVICE_ONLINE = "设备在线不能修改删除";
	public static final String PLEASE_WAKEUP_DEVICE = "请先激活设备";
}
