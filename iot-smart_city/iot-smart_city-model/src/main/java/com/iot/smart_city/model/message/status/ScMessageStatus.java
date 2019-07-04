package com.iot.smart_city.model.message.status;
/**
 * 消息系统的消息状态
 * @author chrilwe
 *
 */
public interface ScMessageStatus {
	public static final String WAITING_SEND = "等待发送";
	public static final String SENDING = "正在发送";
	public static final String SEND_SUCCESS = "完成发送";
	public static final String WAITING_PEOPLE_HANDLE = "等待人工干预处理";
	public static final String CANCEL = "消息已取消";
}
