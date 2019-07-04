package com.smart_city.common.message.response.msg;

public interface ScMessageMsg {
	public static final String SUCCESS = "成功";
	public static final String PARAM_NULL = "请求参数为空";
	public static final String MESSAGE_EXIST = "消息已存在";
	public static final String MESSAGE_NO_EXIST = "消息不存在";
	public static final String MESSAGE_STATUS_ISNOT_SENDING = "消息状态不是发送中";
	public static final String MESSAGE_SENDTIMES_LIMIT = "消息重发次数超过限定值";
	public static final String SYSTEM_ERROR = "系统异常";
	public static final String PARAM_ISNOTALLOW_NULL = "参数都不能为空";
}
