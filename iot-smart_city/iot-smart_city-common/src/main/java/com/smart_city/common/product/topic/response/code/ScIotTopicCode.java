package com.smart_city.common.product.topic.response.code;

public interface ScIotTopicCode {
	public static final int SUCCESS = 200;
	public static final int PARAM_NULL = 201;//请求参数为空
	public static final int ERROR_PARAM = 202;//非法参数
	public static final int SYSTEM_ERROR = 203;//系统异常
	public static final int TOPIC_NOEXISTS = 204;//主题不存在
	public static final int TOPIC_NOALLOW_SUB = 205;//主题不允许订阅
	public static final int TOPIC_NOALLOW_PUB = 206;//主题不允许发布
	public static final int TOPIC_SUBSCRIBE_NO = 207;//主题没有订阅
	public static final int TOPIC_SUBSCRIBE_YES =208;//主题已经订阅
	public static final int DEVICE_NULL = 209;//请添加设备
	public static final int PRODUCT_NULL =210;//产品不存在
	public static final int DEVICE_START_NO = 211;//产品未激活或者已经下线
}
