package com.iot.smart_city.model.topic.type;

public interface TopicType {
	//  /as/mqtt/status/{productKey}/{deviceName}
	public static final String ON_OFF = "online_offline";//上下线通知
	//  /sys/{productKey}/{deviceName}/thing/property/post
	public static final String PROPERTY = "property";//设备属性上报通知
	//  /sys/{productKey}/{deviceName}/thing/event/post
	public static final String EVENT = "event";//设备事件上报
	//  /sys/{productKey}/{deviceName}/thing/lifecycle
	public static final String LIFE_CYCLE = "lifecycle";//设备生命周期变更
}
