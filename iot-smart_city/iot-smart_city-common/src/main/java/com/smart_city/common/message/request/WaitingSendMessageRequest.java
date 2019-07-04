package com.smart_city.common.message.request;

import java.util.Date;

import com.iot.smart_city.model.message.ScMessage;
import com.smart_city.common.base.ScBaseRequest;

import lombok.Data;

@Data
public class WaitingSendMessageRequest extends ScBaseRequest {
	private String messageId;
	private String messageBody;//消息内容
	private String dataType;//消息内容数据类型
	private int timeout;//消息消费过期时间
	private String exchange;//交换机
	private String queue;//消息队列
	private String routKey;//路由key
	
}
