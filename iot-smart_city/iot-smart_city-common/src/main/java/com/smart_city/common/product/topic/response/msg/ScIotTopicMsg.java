package com.smart_city.common.product.topic.response.msg;

public interface ScIotTopicMsg {
	public static final String SUCCESS = "请求成功";
	public static final String PARAM_NULL = "请求参数不能为空";
	public static final String ERROR_PARAM = "非法请求参数";
	public static final String SYSTEM_ERROR = "系统异常";
	public static final String TOPIC_NOEXISTS = "主题不存在";
	public static final String TOPIC_NOALLOW_SUB = "主题不允许订阅";
	public static final String TOPIC_NOALLOW_PUB = "主题不允许发布";
	public static final String TOPIC_SUBSCRIBE_NO = "主题没有订阅";
	public static final String TOPIC_SUBSCRIBE_YES = "主题已经订阅";
	public static final String DEVICE_NULL = "请添加或者激活设备";
	public static final String PRODUCT_NULL = "产品不存在";
	public static final String DEVICE_START_NO = "未激活或者已下线";
}
