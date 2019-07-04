package com.iot.smart_city.api;
/**
 * 消息系统(处理分布式事务)
 * @author chrilwe
 *
 */

import com.iot.smart_city.model.message.ScMessage;
import com.smart_city.common.base.ScBaseResponse;
import com.smart_city.common.message.request.SendingMessageRequest;
import com.smart_city.common.message.request.SendingSuccessRequest;
import com.smart_city.common.message.request.WaitingSendMessageRequest;

public interface ScMessageApi {
	public ScBaseResponse waitingSendMessage(WaitingSendMessageRequest waitingSendMessageRequest);//预发送消息
	public ScBaseResponse sendingMessage(SendingMessageRequest sendingMessageRequest);//发送消息中
	public ScBaseResponse sendingSuccess(SendingSuccessRequest sendingSuccessRequest);//确定完成消息发送
	public ScMessage findMessageById(String messageId);
}
